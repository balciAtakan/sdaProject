create table person(
id varchar (16) not null,
firstname varchar(24),
lastname varchar(24),
username varchar(24),
pass varchar(24),
role varchar(12));

create table knowledge_room(
uuid varchar (255) not null primary key,
roomname varchar(255),
user_roles varchar(255),
users varchar(255));

CREATE TABLE knowledge_room_message(
    uuid varchar(255) NOT NULL,
    content varchar(8000) NOT NULL,
    knowledge_room varchar(255) NOT NULL,
    owner varchar(255),
    date date,
    PRIMARY KEY (uuid),
    CONSTRAINT FK_KR FOREIGN KEY (knowledge_room)
    REFERENCES knowledge_room(uuid)
);

CREATE TABLE knowledge(
	uuid varchar(255) not null primary key,
	word varchar(255) not null,
    category varchar(255) not null,
    knowledge_text varchar(8000),
	knowledge_data blob,

	CONSTRAINT FK_CATEGORY FOREIGN KEY (category)
    REFERENCES categories(uuid)


);

CREATE TABLE categories(
	uuid varchar(255) not null primary key,
	cat_name varchar(255) not null,
    sub_category varchar(255)

);