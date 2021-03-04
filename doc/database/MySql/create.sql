create table x_database
(
	id int not null auto_increment
		primary key,
	name varchar(255) null,
	database_type varchar(10) null,
	database_contype varchar(10) null,
	host_name varchar(255) null,
	database_name varchar(50) null,
	port int null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	username varchar(255) null,
	password varchar(255) null,
	servername varchar(255) null
)
;

create table x_dict
(
	id int(10) unsigned not null auto_increment
		primary key,
	dict_name varchar(100) not null,
	dict_id varchar(100) null,
	dict_key varchar(100) null,
	dict_value varchar(500) null,
	order_num varchar(10) null,
	dict_desc varchar(255) null
)
;

create table x_job
(
	id int not null auto_increment
		primary key,
	job_id varchar(100) null,
	job_name varchar(200) not null,
	job_description varchar(500) null,
	job_type varchar(5) null,
	job_path varchar(200) null,
	job_repository_id varchar(30) null,
	job_log_level varchar(10) null,
	job_status varchar(10) null,
	is_del char(2) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	is_monitor_enabled char(2) null,
	tpl_key varchar(150) default '' null
)
;

create table x_log
(
	id int not null auto_increment
		primary key,
	log_id varchar(30) null,
	target_id varchar(30) null,
	log_type varchar(8) null,
	log_file_path varchar(200) null,
	start_time varchar(25) null,
	stop_time varchar(25) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	target_result char(2) null,
	log_text text null
)
;

comment on column x_log.target_id is '记录目标对象:作业或转换'
;

comment on column x_log.log_type is '日志类型:作业或转换'
;

comment on column x_log.log_file_path is '日志物理路径'
;

comment on column x_log.start_time is '执行开始时间'
;

comment on column x_log.stop_time is '执行结束时间'
;

comment on column x_log.target_result is '目标状态:成功或失败'
;

comment on column x_log.log_text is '日志文本内容'
;

create table x_log_warning
(
	id int not null auto_increment
		primary key,
	log_id varchar(50) null,
	target_id varchar(30) null,
	target_name varchar(200) null,
	log_type varchar(8) null,
	log_file varchar(200) null,
	log_msg text null,
	log_level varchar(10) null,
	log_error varchar(10) null,
	log_subject varchar(20) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null
)
;

comment on column x_log_warning.target_id is '作业或转换ID'
;

comment on column x_log_warning.log_type is '日志记录类型'
;

comment on column x_log_warning.log_file is '日志物理路径'
;

comment on column x_log_warning.log_level is '日志级别'
;

comment on column x_log_warning.log_error is '日志异常提示'
;

comment on column x_log_warning.log_subject is '日志主题'
;

create table x_monitor
(
	id int not null auto_increment
		primary key,
	monitor_id varchar(30) not null,
	target_id varchar(100) null,
	success_count varchar(5) null,
	fail_count varchar(5) null,
	target_status char(2) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	target_lines_read varchar(10) null,
	target_lines_written varchar(10) null,
	target_lines_updated varchar(10) null,
	target_lines_input varchar(10) null,
	target_lines_output varchar(10) null,
	target_lines_rejected varchar(10) null,
	description varchar(500) null,
	monitor_type varchar(8) null
)
;

create table x_params
(
	id int not null auto_increment
		primary key,
	obj_code varchar(30) null,
	obj_name varchar(30) null,
	obj_des varchar(30) null,
	target_id varchar(30) null,
	target_type varchar(8) null,
	is_use char(2) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	obj_val varchar(30) null
)
;

comment on column x_params.obj_val is '默认值'
;

create table x_permission
(
	id bigint not null auto_increment
		primary key,
	permission_id varchar(32) not null,
	app_id varchar(32) not null,
	system_id varchar(32) null,
	pid varchar(32) null,
	tag varchar(30) null,
	name varchar(20) null,
	label varchar(30) null,
	type tinyint null,
	permission_value varchar(50) null,
	uri varchar(100) null,
	icon varchar(50) null,
	status tinyint null,
	orders bigint null,
	create_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null
)
;

create index app_id
	on x_permission (app_id, system_id)
;

create index permission_id
	on x_permission (permission_id)
;

create index permission_value
	on x_permission (permission_value)
;

create table x_quartz
(
	id int not null auto_increment
		primary key,
	quartz_id varchar(30) not null,
	quartz_description varchar(500) null,
	quartz_cron varchar(100) null,
	is_del char(2) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	target_id varchar(30) not null,
	quartz_type varchar(10) not null
)
;

comment on column x_quartz.quartz_id is '定时调度ID'
;

comment on column x_quartz.quartz_description is '调度描述'
;

comment on column x_quartz.quartz_cron is '调度正则表达式'
;

comment on column x_quartz.is_del is '调度状态是否有效:1有效:0无效'
;

comment on column x_quartz.target_id is '目标ID'
;

comment on column x_quartz.quartz_type is '调度类型:作业或者转换'
;

create table x_repository
(
	id int not null auto_increment
		primary key,
	repo_id varchar(30) not null,
	repo_name varchar(50) null,
	repo_username varchar(50) null,
	repo_password varchar(50) null,
	repo_type varchar(10) null,
	db_access varchar(10) null,
	db_host varchar(50) null,
	db_port varchar(10) null,
	db_name varchar(20) null,
	db_username varchar(50) null,
	db_password varchar(50) null,
	is_del char(2) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	type varchar(10) null,
	base_dir varchar(100) null
)
;

comment on column x_repository.repo_id is '资源库ID'
;

comment on column x_repository.repo_name is '资源库名称'
;

comment on column x_repository.repo_username is '资源库登陆用户名'
;

comment on column x_repository.repo_password is '资源库登录密码'
;

comment on column x_repository.repo_type is '资源库使用的数据库类型'
;

comment on column x_repository.db_access is '数据库访问方式'
;

comment on column x_repository.db_host is '数据库连接地址'
;

comment on column x_repository.db_port is '数据库端口'
;

comment on column x_repository.db_name is '数据库名称'
;

comment on column x_repository.db_username is '数据库登录用户名'
;

comment on column x_repository.db_password is '数据库登录密码'
;

comment on column x_repository.is_del is '是否启用1 弃用 0 使用'
;

comment on column x_repository.type is '资源库类型 file或db'
;

comment on column x_repository.base_dir is '资源库基础路径'
;

create table x_role
(
	id bigint not null auto_increment
		primary key,
	role_id varchar(32) not null,
	app_id varchar(32) null,
	system_id varchar(32) null,
	organ_id varchar(30) null,
	name varchar(20) null,
	title varchar(20) null,
	description varchar(200) null,
	orders bigint null,
	create_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null
)
;

create index app_id
	on x_role (app_id)
;

create index role_id
	on x_role (role_id)
;

create index system_id
	on x_role (system_id)
;

create table x_role_permission
(
	id bigint not null auto_increment
		primary key,
	role_id varchar(32) null,
	permission_id varchar(32) null,
	create_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null
)
;

create index permission_id
	on x_role_permission (permission_id)
;

create index role_id
	on x_role_permission (role_id)
;

create table x_template
(
	id int not null auto_increment
		primary key,
	template_name varchar(200) not null,
	template_description varchar(500) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	template_key varchar(150) null,
	template_path varchar(250) not null
)
;

comment on column x_template.template_name is '模板名称'
;

comment on column x_template.template_description is '模板描述'
;

comment on column x_template.template_path is '模板路径'
;

create table x_trans
(
	id int not null auto_increment
		primary key,
	trans_id varchar(30) not null,
	trans_name varchar(200) not null,
	trans_description varchar(500) null,
	trans_type varchar(5) null,
	trans_path varchar(200) null,
	trans_repository_id varchar(30) null,
	trans_log_level varchar(10) null,
	trans_status varchar(10) null,
	is_del char(2) null,
	created_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	is_monitor_enabled char(2) null,
	tpl_key varchar(150) default '' null
)
;

create table x_user
(
	id bigint not null auto_increment
		primary key,
	user_id varchar(30) not null,
	password varchar(100) null,
	user_name varchar(100) null,
	organ_id varchar(30) null,
	user_email varchar(100) null,
	user_phone varchar(13) null,
	user_avater varchar(100) null,
	status char(2) null,
	is_admin char default '0' null,
	create_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null,
	lock_time datetime null,
	expired_time datetime null,
	constraint uk_x_user
		unique (user_id)
)
;

create table x_user_role
(
	id bigint not null auto_increment
		primary key,
	user_id varchar(30) not null,
	role_id varchar(32) null,
	create_time datetime default CURRENT_TIMESTAMP null,
	update_time datetime default CURRENT_TIMESTAMP null
)
;

create index role_id
	on x_user_role (role_id)
;

create index user_id
	on x_user_role (user_id)
;

