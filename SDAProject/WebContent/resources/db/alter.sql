alter table person modify column role varchar(12);
alter table knowledge_room add users varchar(8000);
alter table knowledge_room add room_owner varchar(255);
alter table knowledge_room modify column users varchar(20000);

alter table knowledge_room_message add knowledge_room varchar(255);

drop table knowledge_room_message;

alter table person add primary key(id);
alter table person drop column role;

ALTER TABLE knowledge_room
ADD COLUMN date_create datetime(3) AFTER room_owner;

alter table knowledge_room
add CONSTRAINT FK_person FOREIGN KEY (room_owner)
    REFERENCES person(id);