alter table person modify column role varchar(12);
alter table knowledge_room add users varchar(8000);
alter table knowledge_room add room_owner varchar(255);
alter table knowledge_room modify column users varchar(20000);

alter table knowledge_room_message add knowledge_room varchar(255);

drop table knowledge_room_message;