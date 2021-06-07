package org.yaukie.frame.monitor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: yuenbin
 * @Date :2021/6/7
 * @Time : 17:14
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:
 */
@Slf4j
@Data
public class ThreadPoolExcutorMonitor extends IThreadPoolExcutorMonitor {

    private ThreadPoolExecutor executor ;

    @Override
    public void monitor() {
        String buffer = "CurrentPoolSize：" + executor.getPoolSize() +
                " \r\n CorePoolSize：" + executor.getCorePoolSize() +
                "\r\n MaximumPoolSize：" + executor.getMaximumPoolSize() +
                "\r\n ActiveTaskCount：" + executor.getActiveCount() +
                " \r\n CompletedTaskCount：" + executor.getCompletedTaskCount() +
                " \r\n TotalTaskCount：" + executor.getTaskCount() +
                " \r\n isTerminated：" + executor.isTerminated();
                    log.info(buffer);
    }

    @Override
    public void run() {
        while (true)
        {
            monitor();
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                log.error("线程池监控出行异常,{}",e);
            }
        }
    }

}
