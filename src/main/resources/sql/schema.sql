--DROP TABLE IF EXISTS userdata;
CREATE TABLE IF NOT EXISTS userdata (
  id int AUTO_INCREMENT primary key,
  name VARCHAR(200),
  email VARCHAR(254),
  address VARCHAR(200) NOT NULL,
  is_active BOOLEAN,
  creation_date TIMESTAMP WITH TIME ZONE,
  deletion_date TIMESTAMP WITH TIME ZONE
);