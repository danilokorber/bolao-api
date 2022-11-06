CREATE TABLE tournament (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  name varchar(255)
);

CREATE TABLE tournament_edition (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  name varchar(255),
  tournament_id bigint,
  open_date date,
  close_date date
);

CREATE TABLE tournament_group (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  tournament_edition_id bigint,
  name varchar(255)
);

CREATE TABLE team (
  id int AUTO_INCREMENT PRIMARY KEY,
  en varchar(255),
  de varchar(255),
  pt varchar(255),
  short_name varchar(255)
);

CREATE TABLE group_member (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  group_id bigint,
  group_order int,
  team_id int
);

CREATE TABLE match (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  tournament_edition_id bigint,
  tournament_match_id bigint,
  group_id bigint,
  venue_id bigint,
  kick_off datetime,
  team_a int,
  team_b int,
  score_a int,
  score_b int,
  pen_a int,
  pen_b int
);

CREATE TABLE venue (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  name varchar(100) NOT NULL,
  city varchar(50),
  country varchar(3),
  capacity int,
  google_maps varchar(255)
);

CREATE TABLE match_event (
  id uniqueidentifier PRIMARY KEY,
  match_id int,
  event_type_id int,
  minute int,
  team_id int,
  player_id int
);

CREATE TABLE event_type (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(255),
  emoji char,
  color varchar(255)
);

CREATE TABLE player (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(255),
  short_name varchar(255),
  shirt_number int
);

--CREATE TABLE user (
--  id int AUTO_INCREMENT PRIMARY KEY,
--  keycloak_uuid varchar(36),
--  name varchar(255),
--  mail varchar(255),
--  google_id varchar(255),
--  facebook_id varchar(255),
--  microsoft_id varchar(255),
--  twitter_id varchar(255),
--  github_id varchar(255),
--  photo_url varchar(255),
--  password varchar(255),
--  created_at timestamp
--);

--CREATE TABLE game (
--  id int AUTO_INCREMENT PRIMARY KEY,
--  tournament_edition_id int,
--  rule_id int
--);
--
--CREATE TABLE user_in_game (
--  id int AUTO_INCREMENT PRIMARY KEY,
--  user_id varchar,
--  game_id int,
--  created_at timestamp,
--  payment float,
--  payed_at datetime
--);

CREATE TABLE bet (
  id binary(16) PRIMARY KEY,
  match_id bigint,
  user_id varchar,
  points int,
  score_a bigint,
  score_b bigint,
  bet_made_at timestamp,
  active boolean
);

--CREATE TABLE rule (
--  id int AUTO_INCREMENT PRIMARY KEY
--);

CREATE TABLE admin (
  id varchar(255) PRIMARY KEY,
  user_id varchar,
  game_id int
);

CREATE TABLE logs (
    id int AUTO_INCREMENT PRIMARY KEY,
    logged_at timestamp DEFAULT CURRENT_TIMESTAMP,
    type varchar(10) NOT NULL,
    message varchar(1000) NOT NULL,
    user_id varchar(255)
);

create sequence hibernate_sequence start with 1 increment by 1;

ALTER TABLE tournament_edition ADD FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE group_member ADD FOREIGN KEY (group_id) REFERENCES tournament_group (id);

ALTER TABLE group_member ADD FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE tournament_group ADD FOREIGN KEY (tournament_edition_id) REFERENCES tournament_edition (id);

ALTER TABLE match ADD FOREIGN KEY (tournament_edition_id) REFERENCES tournament_edition (id);

ALTER TABLE match ADD FOREIGN KEY (group_id) REFERENCES tournament_group (id);

ALTER TABLE match ADD FOREIGN KEY (team_a) REFERENCES team (id);

ALTER TABLE match ADD FOREIGN KEY (team_b) REFERENCES team (id);

ALTER TABLE match ADD FOREIGN KEY (venue_id) REFERENCES venue (id);

ALTER TABLE match_event ADD FOREIGN KEY (match_id) REFERENCES match (id);

ALTER TABLE match_event ADD FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE match_event ADD FOREIGN KEY (event_type_id) REFERENCES event_type (id);

ALTER TABLE match_event ADD FOREIGN KEY (player_id) REFERENCES player (id);

--ALTER TABLE game ADD FOREIGN KEY (tournament_edition_id) REFERENCES tournament_edition (id);

--ALTER TABLE user_in_game ADD FOREIGN KEY (user_id) REFERENCES user (id);
--
--ALTER TABLE user_in_game ADD FOREIGN KEY (game_id) REFERENCES game (id);

--ALTER TABLE bet ADD FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE bet ADD FOREIGN KEY (match_id) REFERENCES match (id);

--ALTER TABLE game ADD FOREIGN KEY (rule_id) REFERENCES rule (id);

--ALTER TABLE admin ADD FOREIGN KEY (user_id) REFERENCES user (id);

--ALTER TABLE admin ADD FOREIGN KEY (game_id) REFERENCES game (id);
