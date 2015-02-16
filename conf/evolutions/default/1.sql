# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table area (
  area_name                 varchar(255),
  tags                      varchar(255))
;

create table environment (
  name                      varchar(255),
  tags                      varchar(255))
;

create table user_profile (
  id                        varchar(255) not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password1                 varchar(255),
  password2                 varchar(255),
  constraint pk_user_profile primary key (id))
;

create sequence user_profile_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists area;

drop table if exists environment;

drop table if exists user_profile;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_profile_seq;

