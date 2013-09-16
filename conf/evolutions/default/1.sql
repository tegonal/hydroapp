# --- Users table

# --- !Ups

create table USERS (
  EMAIL                     varchar(255) not null primary key,
  PASSWORD                  varchar(255) not null,
  FIRSTNAME                 varchar(255),
  LASTNAME                  varchar(255)
);

# --- !Downs

drop table USERS;
