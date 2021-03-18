package org.yaukie.frame.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *  @Author: yuenbin
 *  @Date :2020/10/27
 * @Time :18:41
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:  配置kettle 多任务异步线程池
 *  配置一下线程池的基础参数
**/
@Data
@ConfigurationProperties(prefix = "kettle.pool")
public class KettleThreadPoolProperties {

    /**
     * 线程池前缀
     */

    private String namePrefix ;

    /**
     * 核心线程数
     */

    private int coreThreads;// = 20;

    /**
     * 最大的线程数
     */

    private int maxThreads;// = 50;

    /**
     * 队列容量
     */

    private int queueCapacity;// = 100;

    /**
     * 空闲x分钟则释放线程
     */

    private long keepAliveTimeMin;// = 5;


}
