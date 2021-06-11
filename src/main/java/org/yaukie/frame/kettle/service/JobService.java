package org.yaukie.frame.kettle.service;

import ch.qos.logback.core.util.FileUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.KettleLoggingEventListener;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yaukie.base.core.entity.SftpBean;
import org.yaukie.base.util.DateHelper;
import org.yaukie.base.util.Sftp;
import org.yaukie.base.util.StringTools;
import org.yaukie.frame.autocode.model.*;
import org.yaukie.frame.autocode.service.api.XLogService;
import org.yaukie.frame.autocode.service.api.XParamsService;
import org.yaukie.frame.autocode.service.api.XRepositoryService;
import org.yaukie.frame.kettle.listener.DefaultListener;
import org.yaukie.frame.kettle.listener.XLogListener;
import org.yaukie.frame.pool.StandardPoolExecutor;
import org.yaukie.xtl.KettleUtil;
import org.yaukie.xtl.cons.Constant;
import org.yaukie.xtl.cons.XJobStatus;
import org.yaukie.xtl.exceptions.XtlExceptions;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: yuenbin
 * @Date :2020/11/3
 * @Time :19:59
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 作业执行工具类
 * 作业开启
 * 作业停止
 **/
@Component
@Slf4j
public class JobService  {
    /**
     * 保存正在运行的作业
     */
    private static Map<String, Job> jobMap = new ConcurrentHashMap();

    private static Repository repository;

    @Autowired
    private XRepositoryService xRepositoryService;

    @Autowired
    private DefaultListener defaultListener;

    @Autowired
    private XLogService xLogService;

    @Autowired
    private XParamsService xParamsService ;

    @Autowired
    private StandardPoolExecutor executor;

    @Value("${kettle.log.file.path}")
    private String logFilePath;

    /**
     * @Author: yuenbin
     * @Date :2020/11/10
     * @Time :18:09
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 启动一个作业
     **/
    public void startJob(XJob xJob) throws XtlExceptions, KettleException {
        String jobType = xJob.getJobType();
        if (StringUtils.isEmpty(jobType)) {
            try {
                throw new XtlExceptions("请指定作业类型!");
            } catch (XtlExceptions e) {
            }
        }

        /**作业路径,文件绝对路径,或 资源库存储路径*/
        String jobPath = xJob.getJobPath();
        /**日志级别**/
        String logLevel = xJob.getJobLogLevel();
        /**如果是文件库中的作业*/
        if (jobType.equalsIgnoreCase(Constant.JOB_FILE_TYPE)) {
            doRunFileJob(xJob);
            /**如果是资源库中的作业*/
        } else if (jobType.equalsIgnoreCase(Constant.JOB_REPO_TYPE)) {
            doRunRepoJob(xJob);
        }else if(jobType.equalsIgnoreCase("sftp")){
            doRunFileJobFromFtp(xJob);
        }

    }

    /**
     * @Author: yuenbin
     * @Date :2020/11/10
     * @Time :18:08
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 从资源库获取作业, 并执行start
     **/
    private void doRunRepoJob(XJob xJob) throws XtlExceptions, KettleException {
        String repositoryId = xJob.getJobRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repositoryId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        if (xRepository == null) {
            throw new XtlExceptions("请定义资源库!");
        }

        repository = KettleUtil.conByNative(xRepository.getRepoId(), xRepository.getRepoName(),
                xRepository.getRepoName(), xRepository.getRepoType(),
                xRepository.getDbHost(), xRepository.getDbPort(), xRepository.getDbName(),
                xRepository.getDbUsername(), xRepository.getDbPassword(), xRepository.getRepoUsername(),
                xRepository.getRepoPassword());
        JobMeta jobMeta = KettleUtil.loadJob(xJob.getJobName(), xJob.getJobPath(), repository);
        XParamsExample xParamsExample = new XParamsExample() ;
        xParamsExample.createCriteria().andTargetTypeEqualTo("job")
                .andTargetIdEqualTo(xJob.getJobId());
        List<XParams> xParams = xParamsService.selectByExample(xParamsExample);
        if(!CollectionUtils.isEmpty(xParams)){
            xParams.forEach(item -> {
                try {
                    jobMeta.setParameterValue(item.getObjCode()+"", item.getObjVal()+"");
                } catch (UnknownParamException e) {
                    throw new RuntimeException(e) ;
                }
            });
        }

        Job job = new Job(repository, jobMeta);
        job.setDaemon(true);
        job.setVariable(Constant.VARIABLE_JOB_MONITOR_ID, xJob.getJobId());
        job.addJobListener(defaultListener);
        /**设置默认日志级别为DEBUG*/
        job.setLogLevel(LogLevel.DEBUG);
        if (StringUtils.isNotEmpty(xJob.getJobLogLevel())) {
            job.setLogLevel(Constant.logger(xJob.getJobLogLevel()));
        }
        String exception = null;
        /**默认运行成功*/
        String recordStatus = XJobStatus.SUCCESS.value();
        String stopTime = "";
        String logText = "";
        XLogListener xLogListener = null ;
        try {
            /**添加日志监听*/
            xLogListener =new XLogListener(logFilePath,job) ;
            KettleLogStore.getAppender().addLoggingEventListener(xLogListener);
            job.run();
             jobMap.put(xJob.getJobId(), job);
            job.waitUntilFinished();
            if (job.isFinished()) {
                recordStatus = KettleUtil.getJobStatus(job).value();
                stopTime = DateHelper.format(new Date());
                XLog xLog = new XLog();
                xLog.setTargetResult(recordStatus);
                xLog.setStopTime(stopTime);
                XLogExample xLogExample = new XLogExample();
                xLogExample.createCriteria().andLogIdEqualTo(xLogListener.getLogId());
                xLogService.updateByExampleSelective(xLog, xLogExample);
                KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
            }
        } catch (Exception e) {
            KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            log.error("任务执行失败,原因为: {}", str.toString().substring(0, 800));
        }

    }

    /**
     * @Author: yuenbin
     * @Date :2020/11/10
     * @Time :18:08
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 从文件获取作业, 并执行start
     **/
    private void doRunFileJob(XJob xJob) throws XtlExceptions, KettleException {
        String repositoryId = xJob.getJobRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repositoryId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        if (xRepository == null) {
            throw new XtlExceptions("请定义资源库!");
        }
        repository = KettleUtil.conFileRep(xRepository.getRepoId(), xRepository.getRepoName(),
                xRepository.getBaseDir());
        JobMeta jobMeta = KettleUtil.loadJob(xJob.getJobName(), xJob.getJobPath(), repository);
        org.pentaho.di.job.Job job = new org.pentaho.di.job.Job(null, jobMeta);
        job.setDaemon(true);
        job.setVariable(Constant.VARIABLE_JOB_MONITOR_ID, xJob.getJobId());
        job.addJobListener(defaultListener);
        job.setLogLevel(LogLevel.DEBUG);
        if (StringUtils.isNotEmpty(xJob.getJobLogLevel())) {
            job.setLogLevel(Constant.logger(xJob.getJobLogLevel()));
        }
        String recordStatus = XJobStatus.SUCCESS.value();
        String stopTime = null;
        XLogListener  xLogListener = null ;
         try {
             /**添加日志监听*/
             xLogListener =new XLogListener(logFilePath,job) ;
             KettleLogStore.getAppender().addLoggingEventListener(xLogListener);
            job.run();
             jobMap.put(xJob.getJobId(), job);
            job.waitUntilFinished();
            if (job.isFinished()) {
                recordStatus = KettleUtil.getJobStatus(job).value();
                stopTime = DateHelper.format(new Date());
                XLog xLog = new XLog();
                xLog.setTargetResult(recordStatus);
                xLog.setStopTime(stopTime);
                XLogExample xLogExample = new XLogExample();
                xLogExample.createCriteria().andLogIdEqualTo(xLogListener.getLogId());
                xLogService.updateByExampleSelective(xLog, xLogExample);
                KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
            }
        } catch (Exception e) {
            KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
            StringWriter str = new StringWriter();
            e.printStackTrace(new PrintWriter(str));
            log.error("任务执行失败,原因为: {}", str.toString().substring(0, 800));
        }
    }

    /**
     *  运行一个远程作业
     * @param xJob
     * @throws XtlExceptions
     * @throws KettleException
     */
    private void doRunFileJobFromFtp(XJob xJob) throws XtlExceptions, KettleException {
        String repositoryId = xJob.getJobRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repositoryId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        if (xRepository == null) {
            throw new XtlExceptions("请定义资源库!");
        }

        String jobPath = xJob.getJobPath() ;
        String type = xRepository.getType();
        String targetFileName = xJob.getJobName() ;
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
                    File loadFile = sftp.download(StringTools.substring(xJob.getJobPath(),0,StringTools.lastIndexOf(xJob.getJobPath(),xJob.getJobName())),
                            xJob.getJobName(),file.getAbsolutePath(),channelSftp);
                    if(loadFile.exists()){
                        log.info("远程文件{}下载成功,位置:{}",xJob.getJobPath(),loadFile.getAbsoluteFile());
                    }
                    JobMeta jobMeta = new JobMeta(loadFile.getAbsolutePath(), null) ;
                    Job job = new Job(null,jobMeta) ;
                    job.setDaemon(true);
                    job.setVariable(Constant.VARIABLE_JOB_MONITOR_ID, xJob.getJobId());
                    job.addJobListener(defaultListener);
                    job.setLogLevel(LogLevel.DEBUG);
                    if (StringUtils.isNotEmpty(xJob.getJobLogLevel())) {
                        job.setLogLevel(Constant.logger(xJob.getJobLogLevel()));
                    }
                    String recordStatus = XJobStatus.SUCCESS.value();
                    String stopTime = null;
                    XLogListener xLogListener = null ;
                    try {

                        /**添加日志监听*/
                        xLogListener =new XLogListener(logFilePath,job) ;
                        KettleLogStore.getAppender().addLoggingEventListener(xLogListener);
                        job.run();
                          jobMap.put(xJob.getJobId(), job);
                        job.waitUntilFinished();
                        if (job.isFinished()) {
                            recordStatus = KettleUtil.getJobStatus(job).value();
                            stopTime = DateHelper.format(new Date());
                            XLog xLog = new XLog();
                            xLog.setTargetResult(recordStatus);
                            xLog.setStopTime(stopTime);
                            XLogExample xLogExample = new XLogExample();
                            xLogExample.createCriteria().andLogIdEqualTo(xLogListener.getLogId());
                            xLogService.updateByExampleSelective(xLog, xLogExample);
                            KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
                        }
                    } catch (Exception e) {
                        KettleLogStore.getAppender().removeLoggingEventListener(xLogListener);
                        StringWriter str = new StringWriter();
                        e.printStackTrace(new PrintWriter(str));
                        log.error("任务执行失败,原因为: {}", str.toString().substring(0, 800));
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
     * @Author: yuenbin
     * @Date :2020/11/24
     * @Time :10:04
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 强行停止一个作业
     **/
    public XJobStatus doStopJob(XJob xJob) {
        String repoId = xJob.getJobRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repoId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        try {

            if (xRepository == null) {
                throw new XtlExceptions("请定义资源库!");
            }
            repository = KettleUtil.holder.get(repoId);
            if (null == repository) {
                throw new XtlExceptions("当前资源库尚未初始化,不能强行停止作业!");
            }
            Job job = jobMap.get(xJob.getJobId());
            if (job == null) {
                return XJobStatus.STOPPED;
            }
            KettleUtil.jobStopAll(job);
            log.debug("作业 {} 成功停止,作业状态{}!", xJob.getJobName(), job.getState());
            return KettleUtil.getJobStatus(job);
        } catch (XtlExceptions ex) {
            throw new XtlExceptions(ex.getMessage());
        }

    }


    /**
     * @Author: yuenbin
     * @Date :2020/11/24
     * @Time :10:04
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 强行退出一个作业
     **/
    public XJobStatus doKillJob(XJob xJob) throws XtlExceptions {
        String repoId = xJob.getJobRepositoryId();
        XRepositoryExample xRepositoryExample = new XRepositoryExample();
        xRepositoryExample.createCriteria().andRepoIdEqualTo(repoId + "");
        XRepository xRepository = xRepositoryService.selectFirstExample(xRepositoryExample);
        try {

            if (xRepository == null) {
                throw new XtlExceptions("请定义资源库!");
            }
            repository = KettleUtil.holder.get(repoId);
            if (null == repository) {
                throw new XtlExceptions("当前资源库尚未初始化,不能强行停止作业!");
            }
            Job job = jobMap.get(xJob.getJobId());
            if (job == null) {
                return XJobStatus.STOPPED;
            }
            KettleUtil.jobKillAll(job);
            log.debug("作业 {} 被强行终止,作业状态{}!", xJob.getJobName(), job.getState());
            return KettleUtil.getJobStatus(job);
        } catch (XtlExceptions e) {
            throw new XtlExceptions(e.getMessage());
        }

    }

}
