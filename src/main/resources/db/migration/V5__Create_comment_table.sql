create table COMMENT
(
	ID BIGINT auto_increment,
	PARENT_ID BIGINT not null,
	TYPE int not null,
	commentator int not null,
	gmt_create bigint,
	gmt_modified bigint,
	like_count bigint default 0,
	constraint COMMENT_pk
		primary key (ID)
);

