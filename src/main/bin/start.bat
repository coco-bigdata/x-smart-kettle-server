@echo off
SetLocal EnableDelayedExpansion    
set "SMART_KETTLE_HOME=%cd%"
cd %SMART_KETTLE_HOME%/front/nginx
echo first step ready starting Smart Kettle Front ...
echo Using NGINX_DIR %SMART_KETTLE_HOME%/front/nginx
set hasNginx=no
for /F "tokens=1*" %%a in ('tasklist /nh /fi "imagename eq nginx.exe"') do if %%a == nginx.exe set hasNginx=yes
if %hasNginx% == yes goto end
start nginx.exe
:end
echo if nginx has started: %hasNginx%

set hasNginx=

nginx.exe -s reload

echo nginx starting 

echo ready to redirect http://localhost/kettle-admin 

start iexplore "http://localhost/kettle-admin"

tasklist|findstr -i "nginx.exe"
if ERRORLEVEL 1 (
	echo nginx is off in %Date:~0,4%-%Date:~5,2%-%Date:~8,2% %Time:~0,2%:%Time:~3,2%
	echo please check it by yourself...
)

cd %SMART_KETTLE_HOME%/server
echo Using SMART_KETTLE_HOME %SMART_KETTLE_HOME%
echo second step Starting Smart Kettle Server...
echo Please check log file in ../log/smart-kettle.log for more information
echo You can get help in our official homesite: https://my.oschina.net/yaukie
echo If this project is helpful to you, please star it on https://my.oschina.net/yaukie
java -Djava.ext.dirs=./lib -Xms500m -Xmx1g -XX:MaxDirectMemorySize=1g -Dsun.net.client.defaultConnectTimeout=60000  -Dspring.config.location=.\config\application-dev.yml -jar smart-kettle-2021.4.jar -> ..\log\smart-kettle.log
@pause