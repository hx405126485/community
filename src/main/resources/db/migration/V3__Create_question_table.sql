create table question
(
	id int auto_increment,
	title varchar(50),
	description text,
	gmt_create BIGINT,
	GMT_MODIFIED BIGINT,
	CREATOR int,
	comment_count int default 0,
	iew_count int default 0,
	like_count int default 0,
	tag varchar(256),
	constraint question_pk
		primary key (id)
);
