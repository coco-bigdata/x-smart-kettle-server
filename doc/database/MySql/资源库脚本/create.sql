create table r_cluster
(
	ID_CLUSTER bigint not null
		primary key,
	NAME varchar(255) null,
	BASE_PORT varchar(255) null,
	SOCKETS_BUFFER_SIZE varchar(255) null,
	SOCKETS_FLUSH_INTERVAL varchar(255) null,
	SOCKETS_COMPRESSED tinyint(1) null,
	DYNAMIC_CLUSTER tinyint(1) null
)
;

create table r_cluster_slave
(
	ID_CLUSTER_SLAVE bigint not null
		primary key,
	ID_CLUSTER int null,
	ID_SLAVE int null
)
;

create table r_condition
(
	ID_CONDITION bigint not null
		primary key,
	ID_CONDITION_PARENT int null,
	NEGATED tinyint(1) null,
	OPERATOR varchar(255) null,
	LEFT_NAME varchar(255) null,
	CONDITION_FUNCTION varchar(255) null,
	RIGHT_NAME varchar(255) null,
	ID_VALUE_RIGHT int null
)
;

create table r_database
(
	ID_DATABASE bigint not null
		primary key,
	NAME varchar(255) null,
	ID_DATABASE_TYPE int null,
	ID_DATABASE_CONTYPE int null,
	HOST_NAME varchar(255) null,
	DATABASE_NAME mediumtext null,
	PORT int null,
	USERNAME varchar(255) null,
	PASSWORD varchar(255) null,
	SERVERNAME varchar(255) null,
	DATA_TBS varchar(255) null,
	INDEX_TBS varchar(255) null
)
;

create table r_database_attribute
(
	ID_DATABASE_ATTRIBUTE bigint not null
		primary key,
	ID_DATABASE int null,
	CODE varchar(255) null,
	VALUE_STR mediumtext null,
	constraint IDX_RDAT
		unique (ID_DATABASE, CODE)
)
;

create table r_database_contype
(
	ID_DATABASE_CONTYPE bigint not null
		primary key,
	CODE varchar(255) null,
	DESCRIPTION varchar(255) null
)
;

create table r_database_type
(
	ID_DATABASE_TYPE bigint not null
		primary key,
	CODE varchar(255) null,
	DESCRIPTION varchar(255) null
)
;

create table r_dependency
(
	ID_DEPENDENCY bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_DATABASE int null,
	TABLE_NAME varchar(255) null,
	FIELD_NAME varchar(255) null
)
;

create table r_directory
(
	ID_DIRECTORY bigint not null
		primary key,
	ID_DIRECTORY_PARENT int null,
	DIRECTORY_NAME varchar(255) null,
	constraint IDX_RDIR
		unique (ID_DIRECTORY_PARENT, DIRECTORY_NAME)
)
;

create table r_element
(
	ID_ELEMENT bigint not null
		primary key,
	ID_ELEMENT_TYPE int null,
	NAME mediumtext null
)
;

create table r_element_attribute
(
	ID_ELEMENT_ATTRIBUTE bigint not null
		primary key,
	ID_ELEMENT int null,
	ID_ELEMENT_ATTRIBUTE_PARENT int null,
	ATTR_KEY varchar(255) null,
	ATTR_VALUE mediumtext null
)
;

create table r_element_type
(
	ID_ELEMENT_TYPE bigint not null
		primary key,
	ID_NAMESPACE int null,
	NAME mediumtext null,
	DESCRIPTION mediumtext null
)
;

create table r_job
(
	ID_JOB bigint not null
		primary key,
	ID_DIRECTORY int null,
	NAME varchar(255) null,
	DESCRIPTION mediumtext null,
	EXTENDED_DESCRIPTION mediumtext null,
	JOB_VERSION varchar(255) null,
	JOB_STATUS int null,
	ID_DATABASE_LOG int null,
	TABLE_NAME_LOG varchar(255) null,
	CREATED_USER varchar(255) null,
	CREATED_DATE datetime null,
	MODIFIED_USER varchar(255) null,
	MODIFIED_DATE datetime null,
	USE_BATCH_ID tinyint(1) null,
	PASS_BATCH_ID tinyint(1) null,
	USE_LOGFIELD tinyint(1) null,
	SHARED_FILE varchar(255) null
)
;

create table r_job_attribute
(
	ID_JOB_ATTRIBUTE bigint not null
		primary key,
	ID_JOB int null,
	NR int null,
	CODE varchar(255) null,
	VALUE_NUM bigint null,
	VALUE_STR mediumtext null,
	constraint IDX_JATT
		unique (ID_JOB, CODE, NR)
)
;

create table r_job_hop
(
	ID_JOB_HOP bigint not null
		primary key,
	ID_JOB int null,
	ID_JOBENTRY_COPY_FROM int null,
	ID_JOBENTRY_COPY_TO int null,
	ENABLED tinyint(1) null,
	EVALUATION tinyint(1) null,
	UNCONDITIONAL tinyint(1) null
)
;

create table r_job_lock
(
	ID_JOB_LOCK bigint not null
		primary key,
	ID_JOB int null,
	ID_USER int null,
	LOCK_MESSAGE mediumtext null,
	LOCK_DATE datetime null
)
;

create table r_job_note
(
	ID_JOB int null,
	ID_NOTE int null
)
;

create table r_jobentry
(
	ID_JOBENTRY bigint not null
		primary key,
	ID_JOB int null,
	ID_JOBENTRY_TYPE int null,
	NAME varchar(255) null,
	DESCRIPTION mediumtext null
)
;

create table r_jobentry_attribute
(
	ID_JOBENTRY_ATTRIBUTE bigint not null
		primary key,
	ID_JOB int null,
	ID_JOBENTRY int null,
	NR int null,
	CODE varchar(255) null,
	VALUE_NUM double null,
	VALUE_STR mediumtext null,
	constraint IDX_RJEA
		unique (ID_JOBENTRY_ATTRIBUTE, CODE, NR)
)
;

create table r_jobentry_copy
(
	ID_JOBENTRY_COPY bigint not null
		primary key,
	ID_JOBENTRY int null,
	ID_JOB int null,
	ID_JOBENTRY_TYPE int null,
	NR int null,
	GUI_LOCATION_X int null,
	GUI_LOCATION_Y int null,
	GUI_DRAW tinyint(1) null,
	PARALLEL tinyint(1) null
)
;

create table r_jobentry_database
(
	ID_JOB int null,
	ID_JOBENTRY int null,
	ID_DATABASE int null
)
;

create index IDX_RJD1
	on r_jobentry_database (ID_JOB)
;

create index IDX_RJD2
	on r_jobentry_database (ID_DATABASE)
;

create table r_jobentry_type
(
	ID_JOBENTRY_TYPE bigint not null
		primary key,
	CODE varchar(255) null,
	DESCRIPTION varchar(255) null
)
;

create table r_log
(
	ID_LOG bigint not null
		primary key,
	NAME varchar(255) null,
	ID_LOGLEVEL int null,
	LOGTYPE varchar(255) null,
	FILENAME varchar(255) null,
	FILEEXTENTION varchar(255) null,
	ADD_DATE tinyint(1) null,
	ADD_TIME tinyint(1) null,
	ID_DATABASE_LOG int null,
	TABLE_NAME_LOG varchar(255) null
)
;

create table r_loglevel
(
	ID_LOGLEVEL bigint not null
		primary key,
	CODE varchar(255) null,
	DESCRIPTION varchar(255) null
)
;

create table r_namespace
(
	ID_NAMESPACE bigint not null
		primary key,
	NAME mediumtext null
)
;

create table r_note
(
	ID_NOTE bigint not null
		primary key,
	VALUE_STR mediumtext null,
	GUI_LOCATION_X int null,
	GUI_LOCATION_Y int null,
	GUI_LOCATION_WIDTH int null,
	GUI_LOCATION_HEIGHT int null,
	FONT_NAME mediumtext null,
	FONT_SIZE int null,
	FONT_BOLD tinyint(1) null,
	FONT_ITALIC tinyint(1) null,
	FONT_COLOR_RED int null,
	FONT_COLOR_GREEN int null,
	FONT_COLOR_BLUE int null,
	FONT_BACK_GROUND_COLOR_RED int null,
	FONT_BACK_GROUND_COLOR_GREEN int null,
	FONT_BACK_GROUND_COLOR_BLUE int null,
	FONT_BORDER_COLOR_RED int null,
	FONT_BORDER_COLOR_GREEN int null,
	FONT_BORDER_COLOR_BLUE int null,
	DRAW_SHADOW tinyint(1) null
)
;

create table r_partition
(
	ID_PARTITION bigint not null
		primary key,
	ID_PARTITION_SCHEMA int null,
	PARTITION_ID varchar(255) null
)
;

create table r_partition_schema
(
	ID_PARTITION_SCHEMA bigint not null
		primary key,
	NAME varchar(255) null,
	DYNAMIC_DEFINITION tinyint(1) null,
	PARTITIONS_PER_SLAVE varchar(255) null
)
;

create table r_repository_log
(
	ID_REPOSITORY_LOG bigint not null
		primary key,
	REP_VERSION varchar(255) null,
	LOG_DATE datetime null,
	LOG_USER varchar(255) null,
	OPERATION_DESC mediumtext null
)
;

create table r_slave
(
	ID_SLAVE bigint not null
		primary key,
	NAME varchar(255) null,
	HOST_NAME varchar(255) null,
	PORT varchar(255) null,
	WEB_APP_NAME varchar(255) null,
	USERNAME varchar(255) null,
	PASSWORD varchar(255) null,
	PROXY_HOST_NAME varchar(255) null,
	PROXY_PORT varchar(255) null,
	NON_PROXY_HOSTS varchar(255) null,
	MASTER tinyint(1) null
)
;

create table r_step
(
	ID_STEP bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	NAME varchar(255) null,
	DESCRIPTION mediumtext null,
	ID_STEP_TYPE int null,
	DISTRIBUTE tinyint(1) null,
	COPIES int null,
	GUI_LOCATION_X int null,
	GUI_LOCATION_Y int null,
	GUI_DRAW tinyint(1) null,
	COPIES_STRING varchar(255) null
)
;

create table r_step_attribute
(
	ID_STEP_ATTRIBUTE bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_STEP int null,
	NR int null,
	CODE varchar(255) null,
	VALUE_NUM bigint null,
	VALUE_STR mediumtext null,
	constraint IDX_RSAT
		unique (ID_STEP, CODE, NR)
)
;

create table r_step_database
(
	ID_TRANSFORMATION int null,
	ID_STEP int null,
	ID_DATABASE int null
)
;

create index IDX_RSD1
	on r_step_database (ID_TRANSFORMATION)
;

create index IDX_RSD2
	on r_step_database (ID_DATABASE)
;

create table r_step_type
(
	ID_STEP_TYPE bigint not null
		primary key,
	CODE varchar(255) null,
	DESCRIPTION varchar(255) null,
	HELPTEXT varchar(255) null
)
;

create table r_trans_attribute
(
	ID_TRANS_ATTRIBUTE bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	NR int null,
	CODE varchar(255) null,
	VALUE_NUM bigint null,
	VALUE_STR mediumtext null,
	constraint IDX_TATT
		unique (ID_TRANSFORMATION, CODE, NR)
)
;

create table r_trans_cluster
(
	ID_TRANS_CLUSTER bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_CLUSTER int null
)
;

create table r_trans_hop
(
	ID_TRANS_HOP bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_STEP_FROM int null,
	ID_STEP_TO int null,
	ENABLED tinyint(1) null
)
;

create table r_trans_lock
(
	ID_TRANS_LOCK bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_USER int null,
	LOCK_MESSAGE mediumtext null,
	LOCK_DATE datetime null
)
;

create table r_trans_note
(
	ID_TRANSFORMATION int null,
	ID_NOTE int null
)
;

create table r_trans_partition_schema
(
	ID_TRANS_PARTITION_SCHEMA bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_PARTITION_SCHEMA int null
)
;

create table r_trans_slave
(
	ID_TRANS_SLAVE bigint not null
		primary key,
	ID_TRANSFORMATION int null,
	ID_SLAVE int null
)
;

create table r_trans_step_condition
(
	ID_TRANSFORMATION int null,
	ID_STEP int null,
	ID_CONDITION int null
)
;

create table r_transformation
(
	ID_TRANSFORMATION bigint not null
		primary key,
	ID_DIRECTORY int null,
	NAME varchar(255) null,
	DESCRIPTION mediumtext null,
	EXTENDED_DESCRIPTION mediumtext null,
	TRANS_VERSION varchar(255) null,
	TRANS_STATUS int null,
	ID_STEP_READ int null,
	ID_STEP_WRITE int null,
	ID_STEP_INPUT int null,
	ID_STEP_OUTPUT int null,
	ID_STEP_UPDATE int null,
	ID_DATABASE_LOG int null,
	TABLE_NAME_LOG varchar(255) null,
	USE_BATCHID tinyint(1) null,
	USE_LOGFIELD tinyint(1) null,
	ID_DATABASE_MAXDATE int null,
	TABLE_NAME_MAXDATE varchar(255) null,
	FIELD_NAME_MAXDATE varchar(255) null,
	OFFSET_MAXDATE double null,
	DIFF_MAXDATE double null,
	CREATED_USER varchar(255) null,
	CREATED_DATE datetime null,
	MODIFIED_USER varchar(255) null,
	MODIFIED_DATE datetime null,
	SIZE_ROWSET int null
)
;

create table r_user
(
	ID_USER bigint not null
		primary key,
	LOGIN varchar(255) null,
	PASSWORD varchar(255) null,
	NAME varchar(255) null,
	DESCRIPTION varchar(255) null,
	ENABLED tinyint(1) null
)
;

create table r_value
(
	ID_VALUE bigint not null
		primary key,
	NAME varchar(255) null,
	VALUE_TYPE varchar(255) null,
	VALUE_STR varchar(255) null,
	IS_NULL tinyint(1) null
)
;

create table r_version
(
	ID_VERSION bigint not null
		primary key,
	MAJOR_VERSION int null,
	MINOR_VERSION int null,
	UPGRADE_DATE datetime null,
	IS_UPGRADE tinyint(1) null
)
;
