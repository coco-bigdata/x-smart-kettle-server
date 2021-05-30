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

comment on table x_menu is '菜单权限表'
;

comment on column x_menu.menu_id is '菜单ID'
;

comment on column x_menu.menu_name is '菜单名称'
;

comment on column x_menu.parent_id is '父菜单ID'
;

comment on column x_menu.order_num is '显示顺序'
;

comment on column x_menu.path is '路由地址'
;

comment on column x_menu.component is '组件路径'
;

comment on column x_menu.is_frame is '是否为外链（0是 1否）'
;

comment on column x_menu.is_cache is '是否缓存（0缓存 1不缓存）'
;

comment on column x_menu.menu_type is '菜单类型（M目录 C菜单 F按钮）'
;

comment on column x_menu.visible is '菜单状态（0显示 1隐藏）'
;

comment on column x_menu.status is '菜单状态（0正常 1停用）'
;

comment on column x_menu.perms is '权限标识'
;

comment on column x_menu.icon is '菜单图标'
;

comment on column x_menu.create_by is '创建者'
;

comment on column x_menu.create_time is '创建时间'
;

comment on column x_menu.update_by is '更新者'
;

comment on column x_menu.update_time is '更新时间'
;

comment on column x_menu.remark is '备注'
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

comment on column x_oper_log.method is '调用方法'
;

comment on column x_oper_log.oper_url is '请求地址'
;

comment on column x_oper_log.oper_ip is 'IP地址'
;

comment on column x_oper_log.oper_location is '操作地点'
;

comment on column x_oper_log.oper_param is '请求入参'
;

comment on column x_oper_log.json_result is '返回结果'
;

comment on column x_oper_log.status is '操作状态 (0 失败 1 成功)'
;

comment on column x_oper_log.error_msg is '错误消息'
;

comment on column x_oper_log.oper_time is '操作时间'
;

comment on column x_oper_log.operator_name is '操作名称 （0其它 1新增 2修改 3删除 4 强退 5 清空数据）'
;

comment on column x_oper_log.module_name is '模块名称'
;

comment on column x_oper_log.request_type is '请求方式'
;

comment on column x_oper_log.client_type is '操作客户端 (0 PC 1 MOBILE 2 PAD)'
;

comment on column x_oper_log.oper_user_name is '操作人'
;

comment on column x_oper_log.oper_dept_name is '操作人所属部门'
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

comment on table x_post is '岗位信息表'
;

comment on column x_post.post_id is '岗位ID'
;

comment on column x_post.post_code is '岗位编码'
;

comment on column x_post.post_name is '岗位名称'
;

comment on column x_post.post_sort is '显示顺序'
;

comment on column x_post.status is '状态（0正常 1停用）'
;

comment on column x_post.create_by is '创建者'
;

comment on column x_post.create_time is '创建时间'
;

comment on column x_post.update_by is '更新者'
;

comment on column x_post.update_time is '更新时间'
;

comment on column x_post.remark is '备注'
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

comment on table x_role is '角色信息表'
;

comment on column x_role.role_id is '角色ID'
;

comment on column x_role.role_name is '角色名称'
;

comment on column x_role.role_key is '角色权限字符串'
;

comment on column x_role.role_sort is '显示顺序'
;

comment on column x_role.data_scope is '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）'
;

comment on column x_role.menu_check_strictly is '菜单树选择项是否关联显示'
;

comment on column x_role.dept_check_strictly is '部门树选择项是否关联显示'
;

comment on column x_role.status is '角色状态（0正常 1停用）'
;

comment on column x_role.del_flag is '删除标志（0代表存在 2代表删除）'
;

comment on column x_role.create_by is '创建者'
;

comment on column x_role.create_time is '创建时间'
;

comment on column x_role.update_by is '更新者'
;

comment on column x_role.update_time is '更新时间'
;

comment on column x_role.remark is '备注'
;

create table xtl.x_role_dept
(
	role_id bigint not null,
	dept_id bigint not null,
	primary key (role_id, dept_id)
)
;

comment on table x_role_dept is '角色和部门关联表'
;

comment on column x_role_dept.role_id is '角色ID'
;

comment on column x_role_dept.dept_id is '部门ID'
;

create table xtl.x_role_menu
(
	role_id bigint not null,
	menu_id bigint not null,
	primary key (role_id, menu_id)
)
;

comment on table x_role_menu is '角色和菜单关联表'
;

comment on column x_role_menu.role_id is '角色ID'
;

comment on column x_role_menu.menu_id is '菜单ID'
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

comment on table x_user is '用户信息表'
;

comment on column x_user.user_id is '用户ID'
;

comment on column x_user.dept_id is '部门ID'
;

comment on column x_user.user_name is '用户账号'
;

comment on column x_user.nick_name is '用户昵称'
;

comment on column x_user.user_type is '用户类型（00系统用户）'
;

comment on column x_user.email is '用户邮箱'
;

comment on column x_user.phonenumber is '手机号码'
;

comment on column x_user.sex is '用户性别（0男 1女 2未知）'
;

comment on column x_user.avatar is '头像地址'
;

comment on column x_user.password is '密码'
;

comment on column x_user.status is '帐号状态（0正常 1停用）'
;

comment on column x_user.del_flag is '删除标志（0代表存在 2代表删除）'
;

comment on column x_user.login_ip is '最后登录IP'
;

comment on column x_user.login_date is '最后登录时间'
;

comment on column x_user.create_by is '创建者'
;

comment on column x_user.create_time is '创建时间'
;

comment on column x_user.update_by is '更新者'
;

comment on column x_user.update_time is '更新时间'
;

comment on column x_user.remark is '备注'
;

create table xtl.x_user_post
(
	user_id bigint not null,
	post_id bigint not null,
	primary key (user_id, post_id)
)
;

comment on table x_user_post is '用户与岗位关联表'
;

comment on column x_user_post.user_id is '用户ID'
;

comment on column x_user_post.post_id is '岗位ID'
;

create table xtl.x_user_role
(
	user_id bigint not null,
	role_id bigint not null,
	primary key (user_id, role_id)
)
;

comment on table x_user_role is '用户和角色关联表'
;

comment on column x_user_role.user_id is '用户ID'
;

comment on column x_user_role.role_id is '角色ID'
;

