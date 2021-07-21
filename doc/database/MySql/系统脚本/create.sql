create table xtl.x_database
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

create table xtl.x_dict
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

create table xtl.x_job
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

create table xtl.x_log
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

create table xtl.x_log_warning
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

create table xtl.x_login_info
(
	user_name varchar(50) default '' null,
	ipaddr varchar(128) default '' null,
	login_location varchar(255) default '' null,
	browser varchar(50) default '' null,
	os varchar(50) default '' null,
	status char(4) default '0' null,
	msg varchar(255) default '' null,
	login_time datetime null,
	id bigint not null auto_increment
		primary key
)
;

create table xtl.x_menu
(
	menu_id bigint not null auto_increment
		primary key,
	menu_name varchar(50) not null,
	parent_id bigint default '0' null,
	order_num int(4) default '0' null,
	path varchar(200) default '' null,
	component varchar(255) null,
	is_frame int(1) default '1' null,
	is_cache int(1) default '0' null,
	menu_type char default '' null,
	visible char default '0' null,
	status char default '0' null,
	perms varchar(100) null,
	icon varchar(100) default '#' null,
	create_by varchar(64) default '' null,
	create_time datetime null,
	update_by varchar(64) default '' null,
	update_time datetime null,
	remark varchar(500) default '' null
)
;

create table xtl.x_monitor
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

create table xtl.x_oper_log
(
	method varchar(100) default '' null,
	oper_url varchar(255) default '' null,
	oper_ip varchar(128) default '' null,
	oper_location varchar(255) default '' null,
	oper_param varchar(2000) default '' null,
	json_result varchar(2000) default '' null,
	status char(7) default '0' null,
	error_msg varchar(2000) default '' null,
	oper_time datetime null,
	id bigint not null auto_increment
		primary key,
	operator_name varchar(50) default '' null,
	module_name varchar(50) default '' null,
	request_type varchar(10) default '' null,
	client_type char(6) default '0' null,
	oper_user_name varchar(50) default '0' null,
	oper_dept_name varchar(50) default '' null
)
;

create table xtl.x_params
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

create table xtl.x_post
(
	post_id bigint not null auto_increment
		primary key,
	post_code varchar(64) not null,
	post_name varchar(50) not null,
	post_sort int(4) not null,
	status char not null,
	create_by varchar(64) default '' null,
	create_time datetime null,
	update_by varchar(64) default '' null,
	update_time datetime null,
	remark varchar(500) null
)
;

create table xtl.x_quartz
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

create table xtl.x_repository
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

create table xtl.x_role
(
	role_id bigint not null auto_increment
		primary key,
	role_name varchar(30) not null,
	role_key varchar(100) not null,
	role_sort int(4) not null,
	data_scope char default '1' null,
	menu_check_strictly tinyint(1) default '1' null,
	dept_check_strictly tinyint(1) default '1' null,
	status char not null,
	del_flag char default '0' null,
	create_by varchar(64) default '' null,
	create_time datetime null,
	update_by varchar(64) default '' null,
	update_time datetime null,
	remark varchar(500) null
)
;

create table xtl.x_role_dept
(
	role_id bigint not null,
	dept_id bigint not null,
	primary key (role_id, dept_id)
)
;

create table xtl.x_role_menu
(
	role_id bigint not null,
	menu_id bigint not null,
	primary key (role_id, menu_id)
)
;

create table xtl.x_template
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

create table xtl.x_trans
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

create table xtl.x_user
(
	user_id bigint not null auto_increment
		primary key,
	dept_id bigint null,
	user_name varchar(30) not null,
	nick_name varchar(30) not null,
	user_type varchar(2) default '00' null,
	email varchar(50) default '' null,
	phonenumber varchar(11) default '' null,
	sex char default '0' null,
	avatar varchar(100) default '' null,
	password varchar(100) default '' null,
	status char default '0' null,
	del_flag char default '0' null,
	login_ip varchar(128) default '' null,
	login_date datetime null,
	create_by varchar(64) default '' null,
	create_time datetime null,
	update_by varchar(64) default '' null,
	update_time datetime null,
	remark varchar(500) null
)
;

create table xtl.x_user_post
(
	user_id bigint not null,
	post_id bigint not null,
	primary key (user_id, post_id)
)
;

create table xtl.x_user_role
(
	user_id bigint not null,
	role_id bigint not null,
	primary key (user_id, role_id)
)
;

