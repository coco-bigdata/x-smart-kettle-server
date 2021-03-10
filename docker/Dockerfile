#基础镜像
FROM centos:centos7.6.1810
#作者信息
MAINTAINER yuenbin <yaukie@163.com>
ADD jodconverter-web/target/kkFileView-*.tar.gz /opt/
RUN mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup &&\
    curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo &&\
    sed -i -e '/mirrors.cloud.aliyuncs.com/d' -e '/mirrors.aliyuncs.com/d' /etc/yum.repos.d/CentOS-Base.repo &&\
    yum makecache &&\
    yum install -y java-1.8.0-openjdk.x86_64 &&\
ENV LANG zh_CN.UTF-8
ENV KKFILEVIEW_BIN_FOLDER /opt/kkFileView-2.2.0/bin
ENTRYPOINT [
"java",
"-Dfile.encoding=UTF-8",
"-Dsun.java2d.cmm=sun.java2d.cmm.kcms.KcmsServiceProvider",
"-Dspring.config.location=/opt/kkFileView-2.2.0/config/application.properties",
"-jar",
"/opt/kkFileView-2.2.0/bin/kkFileView-2.2.0.jar"
]