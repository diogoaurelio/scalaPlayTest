# --- !Ups

CREATE TABLE people (
  id bigserial not null,
  firstname varchar(255),
  lastname varchar(255),
  email varchar(255) not null,
  CONSTRAINT person_key PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE people;