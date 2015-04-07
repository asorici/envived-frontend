# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table area (
  name                      varchar(255),
  tags                      varchar(255))
;

create table booth_description (
  product_id                varchar(255) not null,
  product_name              varchar(255),
  product_description       varchar(255),
  constraint pk_booth_description primary key (product_id))
;

create table environment (
  id                        varchar(255) not null,
  name                      varchar(255),
  tags                      varchar(255),
  description               varchar(255),
  width                     varchar(255),
  height                    varchar(255),
  img_thumbnail_url         varchar(255),
  constraint pk_environment primary key (id))
;

create table user_profile (
  id                        varchar(255) not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password1                 varchar(255),
  password2                 varchar(255),
  password                  varchar(255),
  constraint pk_user_profile primary key (id))
;

create sequence booth_description_seq;

create sequence environment_seq;

create sequence user_profile_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists area;

drop table if exists booth_description;

drop table if exists environment;

drop table if exists user_profile;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists booth_description_seq;

drop sequence if exists environment_seq;

drop sequence if exists user_profile_seq;

