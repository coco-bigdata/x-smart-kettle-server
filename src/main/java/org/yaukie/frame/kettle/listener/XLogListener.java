package org.yaukie.frame.kettle.listener;

import com.alibaba.fastjson.JSON;
import org.pentaho.di.core.logging.*;
import org.yaukie.base.util.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.trans.Trans;
import org.yaukie.base.util.GenCodeUtil;
import org.yaukie.base.util.SpringContextUtil;
import org.yaukie.frame.kettle.service.LogService;
import org.yaukie.xtl.KettleUtil;
import org.yaukie.xtl.exceptions.XtlExceptions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yuenbin
 * @Date :2020/11/26
 * @Time :15:32
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 增加kettle执行日志完整监听
 **/
@Slf4j
public class XLogListener implements KettleLoggingEventListener {

    public    static    Map<Object, File> jobFileMap = new ConcurrentHashMap<>();

    public KettleLogLayout layout =new KettleLogLayout(true);


    public Pattern pattern ;


    public   OutputStream outputStream ;
    /**
     * 记录一下当前监听的线程
     */
    private static Map<String, Object> activeThreadMap = new ConcurrentHashMap<>();

    /**
     * 记录开始时间
     */
    private static Map<String, String> startTimeMap = new ConcurrentHashMap<>();

    private String logFilePath = "";

    private String logFileSize = "20";

    private String logChannelId;

    private String threadId;

    private String logType;

    /***定义全局的日志ID*/
    private String logId;

    private StringBuilder logStr = new StringBuilder();

    private Object obj = null;


    public XLogListener() {

    }

    public XLogListener(String logFilePath, Object obj) throws KettleException {
        this.logFilePath = logFilePath;
        this.obj = obj;
        setOutputStream(addLogListener(logFilePath, obj));
        this.setLogId(GenCodeUtil.nextId());
        if (obj instanceof Job) {
            Job job = (Job) obj;
            this.setLogType("job");
            this.setLogChannelId(job.getLogChannelId());
            this.setThreadId(job.getObjectId().getId());
        } else {
            Trans trans = (Trans) obj;
            this.setLogType("trans");
            this.setLogChannelId(trans.getLogChannelId());
            this.setThreadId(trans.getObjectId().getId());
        }


    }

    public boolean writeFileLog(KettleLoggingEvent event) {
        try {
            Object messageObject = event.getMessage();
            if (messageObject instanceof LogMessage) {
                boolean logToFile = false;
                if (this.getLogChannelId() == null) {
                    logToFile = true;
                } else {
                    LogMessage message = (LogMessage) messageObject;
                    List<String> logChannelChildren = LoggingRegistry.getInstance().getLogChannelChildren(this.getLogChannelId());
                    logToFile = Const.indexOfString(message.getLogChannelId(), logChannelChildren) >= 0;
                }

                if (logToFile) {
                    String logText = this.layout.format(event);
                    this.getOutputStream().write(logText.getBytes());
                    this.getOutputStream().write(Const.CR.getBytes());
                    return true;
                }
            }
        } catch (IOException e) {
            log.error("写入日志出现异常,原因为:", e);
        }
        return false;
    }


    public boolean writeDbLog(KettleLoggingEvent event) {
        LogService logService = (LogService) SpringContextUtil.getBean("logService", LogService.class);
        try {
            Object messageObject = event.getMessage();
            if (messageObject instanceof LogMessage) {
                boolean logToDb = false;
                if (this.getLogChannelId() == null) {
                    logToDb = true;
                } else {
                    LogMessage message = (LogMessage) messageObject;
                    List<String> logChannelChildren = LoggingRegistry.getInstance().getLogChannelChildren(this.getLogChannelId());
                    logToDb = Const.indexOfString(message.getLogChannelId(), logChannelChildren) >= 0;
                }

                if (logToDb) {
                    String logText = this.layout.format(event);
                    String type = "";
                    //取默认值 正在运行中

                    String recordStatus = "";
                    String startTime = "";
                    String endTime = "";
                    String logFile = "";
                    if (obj instanceof Job) {
                        Job job = (Job) obj;
                        type = "job";
                        startTime = startTimeMap.get(this.getThreadId());
                        logFile = this.jobFileMap.get(job).getAbsolutePath();
                        recordStatus = KettleUtil.getJobStatus(job).value();
                    } else if (obj instanceof Trans) {
                        Trans trans = (Trans) obj;
                        type = "trans";
                        startTime = startTimeMap.get(this.getThreadId());
                        logFile = this.jobFileMap.get(trans).getAbsolutePath();
                        recordStatus = KettleUtil.getTransStatus(trans).value();
                    }
                    /**保存日志执行记录到数据库*/
                    String logId = this.getLogId();
                    logService.addLog(logId, this.getThreadId(), type, recordStatus,
                            logFile,
                            this.logStr.append(logText).append((char) 13).append((char) 10).toString(),
                            startTime, endTime);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("写入日志出现异常,原因为:", e);
        }
        return false;
    }

    public void recordWarningLog(KettleLoggingEvent event) {
        LogService logService = (LogService) SpringContextUtil.getBean("logService", LogService.class);
        Object object = event.getMessage();
        LogMessage message = (LogMessage) object;
        String joblogStr = message.getMessage();
        pattern = Pattern.compile("(error)");
        Matcher m = pattern.matcher(joblogStr);
        if (m.find() || message.getLevel().isError()) {
            String msg = getExceptionMsg(joblogStr, m);
            String logLevel = message.getLevel().getLevel() + "";
            String error = String.valueOf(message.isError());
            String subject = message.getSubject();
            String logChannel = message.getLogChannelId();
            String logFile = this.logFilePath;
            String targetId = threadId;
            String type = "";
            String targetName = "未知线程任务：" + Thread.currentThread().getName();
            if (obj instanceof Job) {
                Job job = (Job) obj;
                logFile = jobFileMap.get(job).getAbsolutePath();
                targetId = job.getObjectId().getId();
                targetName = job.getJobMeta().getName();
                type = "job";
            } else if (obj instanceof Trans) {
                Trans trans = (Trans) obj;
                logFile = jobFileMap.get(trans).getAbsolutePath();
                targetId = trans.getObjectId().getId();
                targetName = trans.getTransMeta().getName();
                type = "trans";
            }

            logService.doAddLogWarning(logChannel, targetId, targetName, logFile, error, msg, subject, logLevel, type);
            log.info("异常日志已保存入库!");
        }
    }

    public void eventAdded(KettleLoggingEvent event) {
        boolean failed = true;
         try {
            synchronized (new Object()) {
                if (writeFileLog(event)) {
                    failed = false;
                    try {
                        getOutputStream().flush();
                        recordWarningLog(event);
                        /**同时写数据库*/
                        Thread.sleep(50);
                        writeDbLog(event);
                    } catch (XtlExceptions ex) {
                        failed = true;
                        log.error("日志文件写入出现异常,原因:{}", ex);
                    }
                }

            }
            if (failed) {
                log.debug("因为异常丢失的日志{}", JSON.toJSON(event));
                recordWarningLog(event);
            }
        } catch (Exception ex) {
            log.error("作业日志处理失败{},原因为,{}", JSON.toJSONString(event), ex);
        }

    }

    public OutputStream addLogListener(String logPath, Object obj) throws KettleException {
        log.info("任务{}日志监听启动了,日志路径{}...", obj, logPath);
        logFilePath = logPath;
        String target;
        String targetName;
        if (obj instanceof Job) {
            Job job = (Job) obj;
            target = "job";
            targetName = job.getJobMeta().getName();
            activeThreadMap.put(job.getObjectId().getId(), job);
            startTimeMap.put(job.getObjectId().getId(), DateHelper.format(new Date()));
        } else {
            Trans trans = (Trans) obj;
            target = "trans";
            targetName = trans.getTransMeta().getName();
            activeThreadMap.put(trans.getObjectId().getId(), trans);
            startTimeMap.put(trans.getObjectId().getId(), DateHelper.format(new Date()));
        }
        try {
            File file = getLogFile(target, targetName);
            if (file == null) {
                throw new KettleException("必须指定日志文件物理路径!");
            }
            jobFileMap.put(obj, file);
            return new FileOutputStream(file, true);
        } catch (KettleException e) {
            throw new KettleException("出现异常,原因:" + e);
        } catch (FileNotFoundException e) {
            throw new KettleException("出现异常,原因:" + e);
        }
    }


    private File getLogFile(String target, String targetName) {
        File file = null;
        synchronized (new Object()) {
            /**如果定义了日志存储的物理路径,则将日志写入到磁盘一份*/
            if (StringUtils.isNotBlank(logFilePath)) {
                logFilePath = logFilePath.replaceAll("\\\\", "\\/");
                file = new File(logFilePath + "/" + target + "/" + targetName + "/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                StringBuilder logFilePathString = new StringBuilder();
                logFilePathString
                        .append(file.getAbsolutePath()).append("/")
                        .append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))
                        .append(".")
                        .append("txt");
                file = new File(logFilePathString.toString());

                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        log.error("创建文件出现异常,原因为:", e);
                    }
                }
            }
        }

        return file;
    }



    public String getLogChannelId() {
        return logChannelId;
    }

    public void setLogChannelId(String logChannelId) {
        this.logChannelId = logChannelId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }


    public void close() throws KettleException {
        if(outputStream != null){
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new KettleException(e);
            }
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public   String getExceptionMsg(String joblogStr, Matcher m) {
        if (joblogStr.length() <= 3000) {
            return joblogStr;
        } else if (!m.find()) {
            return joblogStr.substring(0, 3000);
        } else if (m.start() <= 100) {
            return joblogStr.substring(0, 3000);
        } else {
            return joblogStr.length() - m.start() + 100 <= 3000 ? joblogStr.substring(m.start() - 100) : joblogStr.substring(m.start() - 100, m.start() + 2900);
        }
    }



}
