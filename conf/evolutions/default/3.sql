# --- Favourites table

# --- !Ups

create table FAVOURITES (
  EMAIL                     varchar(255) not null,
  STATION_ID                BIGINT not null
);

alter table FAVOURITES add constraint FK_FAV_USER foreign key(EMAIL) references USERS(EMAIL) on update NO ACTION on delete NO ACTION;

create unique index IDX_FAV_USER_STATION on FAVOURITES (EMAIL, STATION_ID);

# --- !Downs

drop table FAVOURITES;
