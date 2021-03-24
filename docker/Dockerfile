#基础镜像
FROM centos:centos7.6.1810
#作者信息
MAINTAINER yuenbin "yaukie@163.com"
#解压压缩文件到容器指定目录
ADD smart-kettle-*.tar.gz /opt/smart-kettle
#挂载容器日志目录到虚拟机上
VOLUME /opt/aps/logs:/opt/smart-kettle/log
RUN mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup &&\
    curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo &&\
    sed -i -e '/mirrors.cloud.aliyuncs.com/d' -e '/mirrors.aliyuncs.com/d' /etc/yum.repos.d/CentOS-Base.repo &&\
    yum makecache &&\
    yum install -y java-1.8.0-openjdk.x86_64 &&\
    fc-cache -fv
#容器启动时需要执行的命令
CMD ["java","-version"]
ENV XTL_APP_SERVER_PORT 9876
ENV XTL_APP_DATASOURCE_URL jdbc:mysql://localhost:3308/xtl?useUniCode=true&characterEncoding=UTF-8
ENV XTL_APP_DATASOURCE_USERNAME root 
ENV XTL_APP_DATASOURCE_PASS root 
ENV XTL_KETTLE_SCHEDULER false 
ENV XTL_KETTLE_LOG_FILE_PATH /opt/xtl/kettle/logs
ENV XTL_KETTLE_REPO_NAME 临时资源库 
ENV XTL_KETTLE_DB_HOST localhost
ENV XTL_KETTLE_DB_PORT 3308
ENV XTL_KETTLE_DB_NAME etl
ENV XTL_KETTLE_DB_USERNAME root
ENV XTL_KETTLE_DB_PASS root
ENV XTL_APP_LOG_PATH /opt/smart-kettle/log
ENV XTL_APP_LOG_LEVEL info
ENTRYPOINT ["java",
"-Xms500m","-Xmx1g","-XX:MaxDirectMemorySize=1g",
"-Dsun.net.client.defaultConnectTimeout=60000",
"-Dfile.encoding=UTF-8",
"-Dspring.config.location=/opt/smart-kettle/server/config/application-dev.yml",
"-jar","/opt/smart-kettle/server/smart-kettle-*.jar"]