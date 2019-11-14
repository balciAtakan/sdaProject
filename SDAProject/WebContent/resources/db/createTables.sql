create table person(
id varchar (16) not null,
firstname varchar(24),
lastname varchar(24),
username varchar(24),
pass varchar(24),
modify_date datetime(3) not null default '2019-01-01 00:00:00',
primary key (id));

create table person_role (
	id varchar(16) not null ,
    role_code varchar(8) not null,
    role_description varchar(255),
    person_id varchar(16),
    primary key (id),
    FOREIGN KEY (person_id)
        REFERENCES person(id)
        ON DELETE CASCADE
);

create table knowledge_room(
uuid varchar (255) not null primary key,
roomname varchar(255) not null,
room_owner varchar(255) not null,
date_create datetime(3) not null,
 	CONSTRAINT fk_person FOREIGN KEY (room_owner)
    REFERENCES person(id));

create table knowledge_room_role (
	id varchar(16) not null ,
    role_code varchar(8) not null,
    knowledge_room_id varchar(16),
    primary key (id),
    FOREIGN KEY (knowledge_room_id)
        REFERENCES knowledge_room(uuid)
        ON DELETE CASCADE
);

create table knowledge_room_user(
	id varchar(16) not null ,
    username varchar(255) not null,
    knowledge_room_id varchar(16) not null,
    person_id varchar(16) not null,
    primary key (id),
    FOREIGN KEY (knowledge_room_id)
        REFERENCES knowledge_room(uuid)
        ON DELETE CASCADE,
	FOREIGN KEY (person_id)
        REFERENCES person(id)
        ON DELETE CASCADE
);

CREATE TABLE knowledge_room_message(
    uuid varchar(255) NOT NULL,
    content varchar(8000) NOT NULL,
    knowledge_room varchar(255) NOT NULL,
    ownerID varchar(255),
    modify_date datetime(3) not null default '2019-01-01 00:00:00',
    PRIMARY KEY (uuid),
    CONSTRAINT FK_KR FOREIGN KEY (knowledge_room)
    REFERENCES knowledge_room(uuid) ON DELETE CASCADE
);

CREATE TABLE categories(
	uuid varchar(255) not null primary key,
	cat_name varchar(255) not null,
    sub_category varchar(255)
);

CREATE TABLE knowledge(
	uuid varchar(255) not null primary key,
	word varchar(255) not null,
  category varchar(255) not null,
  knowledge_text varchar(8000),
  modify_date datetime(3) not null default '2019-01-01 00:00:00',
  room_owner varchar(255) not null,
	knowledge_data blob,
	CONSTRAINT FK_CATEGORY FOREIGN KEY (category)
    REFERENCES categories(uuid) ON DELETE CASCADE,
  CONSTRAINT fk_person FOREIGN KEY (room_owner)
    REFERENCES person(id)
);

create table knowledge_synonym(
                                  uuid varchar(16) not null primary key,
                                  word varchar(255) not null,
                                  score int not null,
                                  knowledge_id varchar(16) not null,
                                  FOREIGN KEY (knowledge_id)
                                      REFERENCES knowledge(uuid)
                                      ON DELETE CASCADE
);

