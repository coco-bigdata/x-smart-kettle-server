#基础镜像
FROM openjdk:8
#作者信息
MAINTAINER yuenbin "yaukie@163.com"
#解压压缩文件到容器指定目录
ADD smart-kettle-*.tar.gz /opt
#挂载容器日志目录到虚拟机上
VOLUME /opt/aps/logs:/opt/smart-kettle-2021.4/log
# CentOS
RUN echo "Asia/shanghai" > /etc/timezone;
#设置环境变量
ENV JAVA_HOME /usr/java/jdk1.8.0_121 
ENV PATH $JAVA_HOME/bin:$PATH 
#容器启动时需要执行的命令
CMD ["java","-version"]
ENV XTL_APP_SERVER_PORT 9876
ENV XTL_APP_DATASOURCE_URL jdbc:mysql://localhost:3306/xtl?useUniCode=true&characterEncoding=UTF-8
ENV XTL_APP_DATASOURCE_USERNAME root 
ENV XTL_APP_DATASOURCE_PASS root 
ENV XTL_KETTLE_SCHEDULER false 
ENV XTL_KETTLE_LOG_FILE_PATH /opt/xtl/kettle/logs
ENV XTL_KETTLE_REPO_NAME 临时资源库 
ENV XTL_KETTLE_DB_HOST localhost
ENV XTL_KETTLE_DB_PORT 3306
ENV XTL_KETTLE_DB_NAME etl
ENV XTL_KETTLE_DB_USERNAME root
ENV XTL_KETTLE_DB_PASS root
ENV XTL_APP_LOG_PATH /opt/smart-kettle-2021.4/log
ENV XTL_APP_LOG_LEVEL info
ENTRYPOINT ["java","-jar","-Xms500m","-Xmx1g","-XX:MaxDirectMemorySize=1g","-Dsun.net.client.defaultConnectTimeout=60000","-Dfile.encoding=UTF-8","-Dspring.config.location=/opt/smart-kettle-2021.4/server/config/application-dev.yml","/opt/smart-kettle-2021.4/server/smart-kettle-2021.4.jar"]