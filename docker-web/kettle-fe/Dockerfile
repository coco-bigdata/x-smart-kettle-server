#基础镜像
FROM openjdk:8
#作者信息
LABEL maintainer="Peter Zhang <zhangchunsheng423@gmail.com>"

RUN mkdir -p /home/bigdata/kettle
RUN mkdir -p /home/bigdata/kettle/smart-kettle-2021.5/log
RUN mkdir -p /home/bigdata/kettle/xtl/kettle/logs

#解压压缩文件到容器指定目录
ADD smart-kettle-*.tar.gz /home/bigdata/kettle

RUN ls /home/bigdata/kettle/smart-kettle-2021.5/server

#挂载容器日志目录到虚拟机上
VOLUME /home/bigdata/kettle/aps/logs:/home/bigdata/kettle/smart-kettle-2021.5/log

# CentOS
RUN echo "Asia/shanghai" > /etc/timezone;

#设置环境变量
ENV JAVA_HOME /usr/java/jdk1.8.0_121
ENV PATH $JAVA_HOME/bin:$PATH

#容器启动时需要执行的命令
CMD ["java","-version"]
ENV XTL_APP_SERVER_PORT 8017
ENV XTL_APP_DATASOURCE_URL jdbc:mysql://172.21.0.5:3316/xtl?useUniCode=true&characterEncoding=UTF-8
ENV XTL_APP_DATASOURCE_USERNAME root
ENV XTL_APP_DATASOURCE_PASS root

ENV XTL_APP_MASTER_DATASOURCE_URL jdbc:mysql://172.21.0.5:3316/xtl?useUniCode=true&characterEncoding=UTF-8
ENV XTL_APP_MASTER_DATASOURCE_USERNAME root
ENV XTL_APP_MASTER_DATASOURCE_PASS root

ENV XTL_KETTLE_SCHEDULER false
ENV XTL_KETTLE_LOG_FILE_PATH /home/bigdata/kettle/xtl/kettle/logs
ENV XTL_KETTLE_REPO_NAME 临时资源库
ENV XTL_KETTLE_DB_HOST 172.21.0.5
ENV XTL_KETTLE_DB_PORT 3316
ENV XTL_KETTLE_DB_NAME etl
ENV XTL_KETTLE_DB_USERNAME root
ENV XTL_KETTLE_DB_PASS root

ENV XTL_APP_LOG_PATH /home/bigdata/kettle/smart-kettle-2021.5/log
ENV XTL_APP_LOG_LEVEL info

WORKDIR /home/bigdata/kettle

ENTRYPOINT ["java","-jar","-Xms200m","-Xmx1g","-XX:MaxDirectMemorySize=1g","-Dsun.net.client.defaultConnectTimeout=60000","-Dfile.encoding=UTF-8","smart-kettle-2021.5/server/smart-kettle-2021.5.jar"]