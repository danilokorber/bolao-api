CREATE TABLE tournament (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(255)
);

CREATE TABLE tournament_edition (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(255),
  tournament_id int,
  open_date date,
  close_date date
);

CREATE TABLE tournament_group (
  id int AUTO_INCREMENT PRIMARY KEY,
  tournament_edition_id int,
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
  id int AUTO_INCREMENT PRIMARY KEY,
  group_id int,
  group_order int,
  team_id int
);

CREATE TABLE matches (
  id int AUTO_INCREMENT PRIMARY KEY,
  tournament_edition_id int,
  tournament_match_id int,
  group_id int,
  venue_id int,
  kick_off TIMESTAMP,
  team_a int,
  team_b int,
  score_a int,
  score_b int,
  pen_a int,
  pen_b int
);

CREATE TABLE venue (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(100) NOT NULL,
  city varchar(50),
  country varchar(3),
  capacity int,
  google_maps varchar(255)
);

CREATE TABLE bet (
  id binary(16) PRIMARY KEY,
  match_id int,
  user_id varchar(255),
  points int,
  score_a int,
  score_b int,
  bet_made_at timestamp,
  active boolean
);

CREATE TABLE admin (
  id varchar(255) PRIMARY KEY,
  user_id varchar(255),
  game_id int
);

CREATE TABLE logs (
    id int AUTO_INCREMENT PRIMARY KEY,
    logged_at timestamp DEFAULT CURRENT_TIMESTAMP,
    type varchar(10) NOT NULL,
    message varchar(1000) NOT NULL,
    user_id varchar(255)
);

ALTER TABLE tournament_edition ADD FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE group_member ADD FOREIGN KEY (group_id) REFERENCES tournament_group (id);

ALTER TABLE group_member ADD FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE tournament_group ADD FOREIGN KEY (tournament_edition_id) REFERENCES tournament_edition (id);

ALTER TABLE matches ADD FOREIGN KEY (tournament_edition_id) REFERENCES tournament_edition (id);

ALTER TABLE matches ADD FOREIGN KEY (group_id) REFERENCES tournament_group (id);

ALTER TABLE matches ADD FOREIGN KEY (team_a) REFERENCES team (id);

ALTER TABLE matches ADD FOREIGN KEY (team_b) REFERENCES team (id);

ALTER TABLE matches ADD FOREIGN KEY (venue_id) REFERENCES venue (id);

ALTER TABLE bet ADD FOREIGN KEY (match_id) REFERENCES matches (id);