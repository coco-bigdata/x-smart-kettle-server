package org.yaukie.frame.kettle.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaukie.base.core.entity.SftpBean;
import org.yaukie.base.util.DateHelper;
import org.yaukie.base.util.Sftp;
import org.yaukie.base.util.StringTools;
import org.yaukie.frame.autocode.model.*;
import org.yaukie.frame.autocode.service.api.XLogService;
import org.yaukie.frame.autocode.service.api.XQuartzService;
import org.yaukie.frame.autocode.service.api.XRepositoryService;
import org.yaukie.frame.kettle.listener.DefaultListener;
import org.yaukie.frame.kettle.listener.XLogListener;
import org.yaukie.frame.pool.StandardPoolExecutor;
import org.yaukie.xtl.KettleUtil;
import org.yaukie.xtl.cons.Constant;
import org.yaukie.xtl.cons.XTransStatus;
import org.yaukie.xtl.exceptions.XtlExceptions;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: yuenbin
 * @Date :2020/11/3
 * @Time :19:59
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 转换的操作工具
 * 转换的开启
 * 转换的停止
 **/
@Service
@Slf4j
public class TransService  {

    private static Map<String,Trans> transMap = new ConcurrentHashMap<>();

    private static Repository repository ;

    @Value("${kettle.log.file.path}")
    private String logFilePath ;

    @Autowired
    private XRepositoryService xRepositoryService ;

    @Autowired
    private DefaultListener  defaultListener ;

    @Autowired
    private XQuartzService xQuartzService ;

    @Autowired
    private XLogService xLogService;

    @Autowired
    private StandardPoolExecutor executor ;


    /**
     *  @Author: yuenbin
     *  @Date :2020/11/10
     * @Time :18:09
     * @Motto: It is better to be clear than to be clever !
     * @Destrib:  启动一个转换
     **/
    public void startTrans(XTrans xTrans ) throws XtlExceptions, KettleException {
        String transType = xTrans.getTransType();
        if(StringUtils.isEmpty(transType)){
            try {
                throw new XtlExceptions("请指定转换类型!") ;
            } catch (XtlExceptions e) {
            }
        }

        /**作业路径,文件绝对路径,或 资源库存储路径*/
        String transPath = xTrans.getTransPath() ;
        /**日志级别**/
        String logLevel = xTrans.getTransLogLevel() ;
        /**如果是文件库中的作业*/
        if(transType.equalsIgnoreCase(Constant.TRANS_FILE_TYPE)){
            doRunFileTrans(xTrans);
            /**如果是资源库中的作业*/
        }else if(transType.equalsIgnoreCase(Constant.TRANS_REPO_TYPE)){
            doRunRepoTrans(xTrans);
        }else if(transType.equalsIgnoreCase("sftp")){
            doRunFileTransFromFtp(xTrans);
        }
    }


    /**
     *  @Author: yuenbin
     *  @Date :2020/11/11
     * @Time :18:31
     * @Motto: It is better to be clear than to be clever !
     * @Destrib:  运行一个资源库转换
    **/
        private void doRunRepoTrans(XTrans xTrans)  {
        String repositoryId = xTrans.getTransRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repositoryId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        if (xRepository == null) {
            throw new XtlExceptions("请定义资源库!");
        }
            XLogListener xLogListener =null ;
            try {

             repository  =  KettleUtil.conByNative(xRepository.getRepoId(),xRepository.getRepoName(),
                    xRepository.getRepoName(),xRepository.getRepoType(),
                    xRepository.getDbHost(),xRepository.getDbPort(),xRepository.getDbName(),
                    xRepository.getDbUsername(),xRepository.getDbPassword(),xRepository.getRepoUsername(),
                    xRepository.getRepoPassword()) ;
            TransMeta transMeta = KettleUtil.loadTrans(xTrans.getTransName(),xTrans.getTransPath(),repository) ;
            Trans trans = new Trans(transMeta);
            trans.setLogLevel(LogLevel.DEBUG);
            if (StringUtils.isNotEmpty(xTrans.getTransLogLevel())) {
                trans.setLogLevel(Constant.logger(xTrans.getTransLogLevel()));
            }

            trans.setVariable(Constant.VARIABLE_TRANS_MONITOR_ID, xTrans.getTransId());
            transMeta.setCapturingStepPerformanceSnapShots(true);
            trans.setMonitored(true);
            trans.setInitializing(true);
            trans.setPreparing(true);
            trans.setRunning(true);
            trans.setSafeModeEnabled(true);
            trans.addTransListener(defaultListener);
             String recordStatus = XTransStatus.SUCCESS.value();;
             String stopTime = null;

                 /**添加日志监听*/
                 xLogListener = new XLogListener(logFilePath,trans);
             KettleLogStore.getAppender().addLoggingEventListener(xLogListener);
                  trans.execute(null);
                transMap.put(xTrans.getTransId(),trans );
                trans.waitUntilFinished();
                if(trans.isFinishedOrStopped()){
                    recordStatus = KettleUtil.getTransStatus(trans).value();
                    stopTime = DateHelper.format(new Date());
                    XLog xLog = new XLog();
                    xLog.setTargetResult(recordStatus);
                    xLog.setStopTime(stopTime);
                    XLogExample xLogExample = new XLogExample();
                    xLogExample.createCriteria().andLogIdEqualTo(xLogListener.getLogId());
                    xLogService.updateByExampleSelective(xLog,xLogExample ) ;
                    KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
                }
             } catch (Exception e) {
              KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
                StringWriter str = new StringWriter();
                e.printStackTrace(new PrintWriter(str));
                log.error("转换任务执行失败,原因为: {}",str.toString().substring(0,800) );
                //强行将运行日志更新到记录
             throw  new RuntimeException(e);
             }
    }

    /**
     *  @Author: yuenbin
     *  @Date :2020/11/11
     * @Time :18:31
     * @Motto: It is better to be clear than to be clever !
     * @Destrib:  运行一个文件库转换
    **/
    private void doRunFileTrans(XTrans xTrans) throws KettleException {
        String repositoryId = xTrans.getTransRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repositoryId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        if (xRepository == null) {
            throw new XtlExceptions("请定义资源库!");
        }

        repository = KettleUtil.conFileRep(xTrans.getTransRepositoryId(), xRepository.getRepoName(),
                xRepository.getBaseDir());
        TransMeta transMeta = KettleUtil.loadTrans(xTrans.getTransName(),xTrans.getTransPath() ,repository );
        Trans trans = new Trans(transMeta);
        trans.setLogLevel(LogLevel.DEBUG);
        if (StringUtils.isNotEmpty(xTrans.getTransLogLevel())) {
            trans.setLogLevel(Constant.logger(xTrans.getTransLogLevel()));
        }
        trans.setVariable(Constant.VARIABLE_TRANS_MONITOR_ID, xTrans.getTransId());
        transMeta.setCapturingStepPerformanceSnapShots(true);
        trans.setMonitored(true);
        trans.setInitializing(true);
        trans.setPreparing(true);
        trans.setRunning(true);
        trans.setSafeModeEnabled(true);
        trans.addTransListener(defaultListener);
         String recordStatus = XTransStatus.SUCCESS.value();
         String stopTime = "";
        XLogListener xLogListener =null ;
        try {
             trans.execute(null);
            /**添加日志监听*/
               xLogListener = new XLogListener(logFilePath,trans);
            KettleLogStore.getAppender().addLoggingEventListener(xLogListener);
             transMap.put(xTrans.getTransId(),trans );
            trans.waitUntilFinished();
            if(trans.isFinishedOrStopped()){
                recordStatus = KettleUtil.getTransStatus(trans).value();
                stopTime = DateHelper.format(new Date());
                XLog xLog = new XLog();
                xLog.setTargetResult(recordStatus);
                xLog.setStopTime(stopTime);
                XLogExample xLogExample = new XLogExample();
                xLogExample.createCriteria().andLogIdEqualTo(xLogListener.getLogId());
                xLogService.updateByExampleSelective(xLog,xLogExample ) ;
                KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
            }
         } catch (Exception e) {
            KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            log.error("转换任务执行失败,原因为: {}",str.toString().substring(0,800) );
         }
    }




    /**
     *  运行一个远程 转换
     * @param xTrans
     * @throws XtlExceptions
     * @throws KettleException
     */
    private void doRunFileTransFromFtp(XTrans xTrans) throws XtlExceptions, KettleException {
        String repositoryId = xTrans.getTransRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repositoryId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        if (xRepository == null) {
            throw new XtlExceptions("请定义资源库!");
        }

        String jobPath = xTrans.getTransPath() ;
        String type = xRepository.getType();
        String targetFileName = xTrans.getTransName() ;
        // 采取将文件下载到本地的方式,执行一个作业
        if(type.equalsIgnoreCase("sftp")){
            SftpBean sftpBean = new SftpBean();
            sftpBean.setHost(xRepository.getDbHost());
            sftpBean.setPort(Integer.parseInt(xRepository.getDbPort()));
            sftpBean.setUserName(xRepository.getDbUsername());
            sftpBean.setSecretKey(xRepository.getDbPassword());
            Sftp sftp = new Sftp(sftpBean);
            String downLoadPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            downLoadPath=downLoadPath.substring(1);
            downLoadPath=downLoadPath.replaceAll("[\\\\/]", "/");
            downLoadPath=downLoadPath.substring(0, downLoadPath.lastIndexOf("/"));
            String absoluteDownLoadPath = downLoadPath+"/"+"download" ;
            File file = new File(absoluteDownLoadPath +"/" + targetFileName);
            if(file.exists()){
                file.delete() ;
            }
            File outFile = new File(absoluteDownLoadPath);
            if(!outFile.exists()){
                outFile.mkdirs() ;
            }

            ChannelSftp channelSftp = null;
            try {
                channelSftp = sftp.open();
                if(channelSftp.isConnected()){
                    //准备下载从远程机器下载文件
                    File loadFile = sftp.download(StringTools.substring(xTrans.getTransPath(),0,StringTools.lastIndexOf(xTrans.getTransPath(),xTrans.getTransName())),
                            xTrans.getTransName(),file.getAbsolutePath(),channelSftp);
                    if(loadFile.exists()){
                        log.info("远程文件{}下载成功,位置:{}",xTrans.getTransName(),loadFile.getAbsoluteFile());
                    }
                    TransMeta transMeta = new TransMeta(loadFile.getAbsolutePath());
                     Trans trans = new Trans(transMeta);
                    trans.setLogLevel(LogLevel.DEBUG);
                    if (StringUtils.isNotEmpty(xTrans.getTransLogLevel())) {
                        trans.setLogLevel(Constant.logger(xTrans.getTransLogLevel()));
                    }
                    trans.setVariable(Constant.VARIABLE_TRANS_MONITOR_ID, xTrans.getTransId());
                    transMeta.setCapturingStepPerformanceSnapShots(true);
                    trans.setMonitored(true);
                    trans.setInitializing(true);
                    trans.setPreparing(true);
                    trans.setRunning(true);
                    trans.setSafeModeEnabled(true);
                    trans.addTransListener(defaultListener);
                    String recordStatus = XTransStatus.SUCCESS.value();
                    String stopTime = "";
                    XLogListener xLogListener = null ;
                    try {
                        trans.execute(null);
                        /**添加日志监听*/
                          xLogListener = new XLogListener(logFilePath,trans);
                          KettleLogStore.getAppender().addLoggingEventListener(xLogListener);
                         transMap.put(xTrans.getTransId(),trans );
                        trans.waitUntilFinished();
                        if(trans.isFinishedOrStopped()){
                            recordStatus = KettleUtil.getTransStatus(trans).value();
                            stopTime = DateHelper.format(new Date());
                            XLog xLog = new XLog();
                            xLog.setTargetResult(recordStatus);
                            xLog.setStopTime(stopTime);
                            XLogExample xLogExample = new XLogExample();
                            xLogExample.createCriteria().andLogIdEqualTo(xLogListener.getLogId());
                            xLogService.updateByExampleSelective(xLog,xLogExample ) ;
                            KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
                        }
                    } catch (Exception e) {
                        KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
                        StringWriter str = new StringWriter();
                        e.printStackTrace(new PrintWriter(str));
                        log.error("转换任务执行失败,原因为: {}",str.toString().substring(0,800) );
                    }
                }
            } catch (JSchException e) {
                StringWriter str = new StringWriter();
                e.printStackTrace(new PrintWriter(str));
                log.error("任务执行失败,原因为: {}", str.toString().substring(0, 800));
            }

        }

    }

    /**
     *  @Author: yuenbin
     *  @Date :2020/11/24
     * @Time :10:04
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 强行暂停一个转换
     **/
    public   XTransStatus doPauseTrans(XTrans xTrans )  {
        String repoId =xTrans.getTransRepositoryId() ;
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repoId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        try {

            if (xRepository == null) {
                throw new XtlExceptions("请定义资源库!");
            }
            repository = KettleUtil.holder.get(repoId) ;
            if(null == repository ){
                throw new XtlExceptions("当前资源库尚未初始化,不能强行停止转换");
            }
            Trans  trans = transMap.get(xTrans.getTransId()) ;
            if(trans == null ){
                return XTransStatus.STOPPED ;
            }
            if(!trans.isPaused()){
                trans.pauseRunning();
                log.debug("转换 {} 成功暂停,转换状态{}!",xTrans.getTransName(),xTrans.getTransStatus());
            }

            return KettleUtil.getTransStatus(trans) ;
        }catch (XtlExceptions ex ){
            throw new XtlExceptions(ex.getMessage());
        }

    }

    /**
     *  @Author: yuenbin
     *  @Date :2020/11/24
     * @Time :10:04
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 强行恢复一个转换
     **/
    public   XTransStatus doResumeTrans(XTrans xTrans )  {
        String repoId =xTrans.getTransRepositoryId() ;
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repoId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        try {

            if (xRepository == null) {
                throw new XtlExceptions("请定义资源库!");
            }
            repository = KettleUtil.holder.get(repoId) ;
            if(null == repository ){
                throw new XtlExceptions("当前资源库尚未初始化,不能强行停止转换");
            }
            Trans  trans = transMap.get(xTrans.getTransId()) ;
            if(trans == null ){
                return XTransStatus.STOPPED ;
            }
            if(trans.isPaused()){
                trans.resumeRunning();
            }

            if(!trans.isPaused()){
                log.debug("转换 {} 成功恢复运行,转换状态{}!",xTrans.getTransName(),xTrans.getTransStatus());
            }
            return KettleUtil.getTransStatus(trans) ;
        }catch (XtlExceptions ex ){
            throw new XtlExceptions(ex.getMessage());
        }

    }

    /**
     *  @Author: yuenbin
     *  @Date :2020/11/24
     * @Time :10:04
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 强行停止一个作业
     **/
    public   XTransStatus doStopTrans(XTrans xTrans )  {
        String repoId =xTrans.getTransRepositoryId() ;
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repoId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        try {

            if (xRepository == null) {
                throw new XtlExceptions("请定义资源库!");
            }
            repository = KettleUtil.holder.get(repoId) ;
            if(null == repository ){
                throw new XtlExceptions("当前资源库尚未初始化,不能强行停止转换");
            }
            Trans  trans = transMap.get(xTrans.getTransId()) ;
            if(trans == null ){
                return XTransStatus.STOPPED ;
            }
            trans.stopAll();
            log.debug("转换 {} 成功停止,转换状态{}!",xTrans.getTransName(),xTrans.getTransStatus());
            return KettleUtil.getTransStatus(trans) ;
        }catch (XtlExceptions ex ){
            throw new XtlExceptions(ex.getMessage());
        }

    }


    /**
     *  @Author: yuenbin
     *  @Date :2020/11/24
     * @Time :10:04
     * @Motto: It is better to be clear than to be clever !
     * @Destrib:  强行退出一个作业
     **/
    public XTransStatus  doKillTrans(XTrans xTrans) throws XtlExceptions{
        String repoId =xTrans.getTransRepositoryId() ;
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repoId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        try {

            if (xRepository == null) {
                throw new XtlExceptions("请定义资源库!");
            }
            repository = KettleUtil.holder.get(repoId) ;
            if(null == repository ){
                throw new XtlExceptions("当前资源库尚未初始化,不能强行终止转换!");
            }
            Trans  trans = transMap.get(xTrans.getTransId()) ;
            if(trans == null ){
                return XTransStatus.STOPPED ;
            }
            trans.killAll();
            log.debug("转换 {} 成功终止,转换状态{}!",xTrans.getTransName(),xTrans.getTransStatus());
            return KettleUtil.getTransStatus(trans);
        }catch (XtlExceptions ex ){
            throw new XtlExceptions(ex.getMessage());
        }
    }

}
