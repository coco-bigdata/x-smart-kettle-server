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
@Configuration(value = "kettleThreadPoolProperties")
public class KettleThreadPoolProperties {

    /**
     * 线程池前缀
     */
    @Value("${kettle.pool.namePrefix}")
    private String namePrefix ;

    /**
     * 核心线程数
     */
    @Value("${kettle.pool.coreThreads}")
    private int coreThreads;// = 20;

    /**
     * 最大的线程数
     */
    @Value("${kettle.pool.maxThreads}")
    private int maxThreads;// = 50;

    /**
     * 队列容量
     */

    @Value("${kettle.pool.queueCapacity}")
    private int queueCapacity;// = 100;

    /**
     * 空闲x分钟则释放线程
     */
    @Value("${kettle.pool.keepAliveTimeMin}")
    private long keepAliveTimeMin;// = 5;


}
