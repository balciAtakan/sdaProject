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