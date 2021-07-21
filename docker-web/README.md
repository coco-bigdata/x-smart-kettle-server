```shell
https://repo.rdc.aliyun.com/repository/128991-release-EJH8o1/org/yaukie/xtl/x-kettle-core/2021.4/x-kettle-core-2021.4.jar

https://repo.rdc.aliyun.com/repository/128991-release-EJH8o1/org/yaukie/xtl/x-kettle-core/2021.4/x-kettle-core-2021.4-sources.jar
```

```shell
create database xtl default character set utf8 collate utf8_unicode_ci;
create database etl default character set utf8 collate utf8_unicode_ci;

mv ~/smart-kettle-2021.5-make-assembly.tar.gz kettle-fe/

sudo docker-compose build

sudo docker-compose up kettle-fe-mysql
sudo docker-compose down
sudo docker-compose up
sudo docker-compose up -d

mysql -h127.0.0.1 -uroot -p -P3316 xtl < doc/database/MySql/系统脚本/create.sql
```