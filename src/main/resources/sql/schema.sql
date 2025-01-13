--DROP TABLE IF EXISTS userdata;
CREATE TABLE IF NOT EXISTS userdata (
  id bigserial primary key,
  name VARCHAR(200) NOT NULL,
  birth_date date NOT NULL,
  gender VARCHAR(10) NOT NULL,
  email VARCHAR(254) NOT NULL,
  address VARCHAR(200) NOT NULL,
  city VARCHAR(200) NOT NULL,
  country VARCHAR(200) NOT NULL,
  is_active BOOLEAN NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  deletion_date TIMESTAMP WITH TIME ZONE
);