#!/bin/bash
echo "current shell does not check ,so do not know whether if it can run"
FLAG=false
SMART_KETTLE_HOME=$(cd "$(dirname "$0")";pwd)
export SMART_KETTLE_HOME=$SMART_KETTLE_HOME
cd $SMART_KETTLE_HOME/front/nginx
echo "Using SMART_KETTLE_HOME $SMART_KETTLE_HOME"
echo "Starting Smart Kettle Front..."
source ~/.bash_profile
count=`ps -ef | grep "nginx: master process" | grep -v grep | wc -l`
if [ $count -eq 0 ];then
        systemctl start nginx
		FLAG=true
fi

echo "Starting Nginx..."
cd $SMART_KETTLE_HOME/server
echo "Starting Smart Kettle Server..."
echo "You can get help in our official homesite:  https://my.oschina.net/yaukie"
echo "If this project is helpful to you, please star it on  https://gitee.com/yaukie/x-smart-kettle-front.git"
nohup -Djava.ext.dirs=./lib -Xms500m -Xmx1g -XX:MaxDirectMemorySize=1g -Dsun.net.client.defaultConnectTimeout=60000  -Dspring.config.location=.\config\application-dev.yml -jar smart-kettle-2021.3.jar -> ..\log\smart-kettle.log 2>&1 &

tail -fn 300 ../log/smart-kettle.log



