package org.yaukie.frame.monitor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

/**
 * @Author: yuenbin
 * @Date : 2021/6/7
 * @Time : 17:11
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:  编写一个线程池监控线程
*/
public abstract class IThreadPoolExcutorMonitor extends TimerTask {

    public abstract void monitor() ;

    }
