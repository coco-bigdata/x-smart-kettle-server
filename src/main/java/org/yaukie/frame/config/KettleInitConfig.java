package org.yaukie.frame.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.yaukie.base.system.ASyncManager;
import org.yaukie.base.util.SpringContextUtil;
import org.yaukie.frame.monitor.ThreadPoolExcutorMonitor;
import org.yaukie.frame.pool.StandardPoolExecutor;
import org.yaukie.frame.pool.StandardThreadFactory;
import org.yaukie.xtl.config.KettleInit;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Author: yuenbin
 * @Date :2020/10/27
 * @Time :18:31
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: kettle环境初始化入口工具
 **/
@Configuration
 @Slf4j
public class KettleInitConfig {


    @Value("${kettle.log.file.path}")
    private String logFilePath ;

    @Value("${kettle.log.file.size}")
    private String logFileSize;


    @Value("${kettle.scheduler.enabled}")
    private String enabled;


    @PostConstruct
    private void init(){
        try {
            log.info("===kettle 环境准备初始化===\r\n");
            log.info("===kettle日志文件最大可支持--{}MB==\r\n",logFileSize );
            log.info("===kettle日志默认存放路径为{}===\r\n",new File(logFilePath).getAbsoluteFile());
            KettleInit.init();
            KettleEnvironment.init();
            /**默认开启日志监听*/
//            KettleLogStore.getAppender().addLoggingEventListener(new XLogListener());
            log.info("===kettle 环境初始化完成===\r\n");
        } catch (KettleException e) {
            log.error("===kettle初始化出现异常--{}===",e);
        }

    }


    /**
     * 配置kettle任务运行线程池
     * 为了提高转换或作业执行效率
      * @return StandardThreadExecutor
     */
    @Bean(name="executor")
    public StandardPoolExecutor executor(KettleThreadPoolProperties kettleThreadPoolProperties) {
        log.info("===Kettle任务线程池准备初始化===");
        long start = System.currentTimeMillis() ;
        Set<Thread> threadSet = new HashSet<>();
        StandardPoolExecutor standardPoolExecutor  = new StandardPoolExecutor(
                kettleThreadPoolProperties.getCoreThreads(),
                kettleThreadPoolProperties.getMaxThreads(),
                kettleThreadPoolProperties.getKeepAliveTimeMin(),
                TimeUnit.SECONDS,
                kettleThreadPoolProperties.getQueueCapacity(),
                new StandardThreadFactory(kettleThreadPoolProperties.getNamePrefix(),threadSet),
                new ThreadPoolExecutor.AbortPolicy()
        );

        long end
                = System.currentTimeMillis() ;
        log.info("===Kettle任务线程池初始化完毕,总耗时--{} 毫秒===",(end-start));
        return  standardPoolExecutor ;
    }


    @DependsOn("executor")
    @Bean
    public ThreadPoolExcutorMonitor threadPoolExcutorMonitor() {
        log.info("===Kettle任务线程池监控任务开启===");
        long start
                = System.currentTimeMillis() ;

        ThreadPoolExcutorMonitor monitor = new ThreadPoolExcutorMonitor();
        monitor.setExecutorService(SpringContextUtil.getBean("executor"));
        ExecutorService service = Executors.newSingleThreadScheduledExecutor() ;
        // 十秒之后执行线程任务
         ((ScheduledExecutorService) service).schedule(monitor,10,TimeUnit.SECONDS) ;

                long end
                = System.currentTimeMillis() ;
        log.info("===Kettle任务线程池监控任务开启,总耗时--{} 毫秒===",(end-start));
        return  monitor ;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "executorService")
    protected ScheduledExecutorService scheduledExecutorService(KettleThreadPoolProperties kettleThreadPoolProperties)
    {
        log.info("===系统日志采集任务线程池准备初始化===");
        long start
                = System.currentTimeMillis() ;
        ScheduledExecutorService  scheduledExecutorService  =  new ScheduledThreadPoolExecutor(kettleThreadPoolProperties.getCoreThreads(),
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                ASyncManager.ThreadsManager.printException(r, t);
            }
        };
        long end
                = System.currentTimeMillis() ;
        log.info("===系统日志采集任务线程池初始化完毕,总耗时--{}毫秒===",(end-start));

        return  scheduledExecutorService  ;
    }




//    @Bean
//    public Carte carte(){
//        log.info("准备启动 carte 子服务器,,,");
//        KettleClientEnvironment.getInstance().setClient(KettleClientEnvironment.ClientType.CARTE);
//        SlaveServerConfig config = new SlaveServerConfig();
//        SlaveServer slaveServer = new SlaveServer("localhost:8010","localhost","8010","admin","admin");
//        slaveServer.setMaster(false);
//        KettleLogStore.init(config.getMaxLogLines(), config.getMaxLogTimeoutMinutes());
//        config.setJoining(true);
//        config.setSlaveServer(slaveServer);
//        Carte carte = null;
//        try {
//            carte = new Carte(config, false);
//            CarteSingleton.setCarte(carte);
//            carte.getWebServer().join();
//        } catch (Exception e) {
//            log.info("carte 子服务器启动出现异常,{},,,",e);
//        }
//         return carte;
//    }

}
