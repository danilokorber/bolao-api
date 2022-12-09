CREATE TABLE survey (
  user_id varchar(255) NOT NULL,
  question_id int NOT NULL,
  answer varchar(1000),
  comments varchar(1000),
  saved_at timestamp,
  PRIMARY KEY (user_id, question_id)
);