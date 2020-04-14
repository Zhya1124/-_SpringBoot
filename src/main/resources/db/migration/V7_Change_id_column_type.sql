alter table question modify id bigint auto_increment not null;
alter table `user` modify id bigint auto_increment not null;
alter table question modify creator bigint auto_increment not null;
alter table comment modify commentator bigint auto_increment not null;