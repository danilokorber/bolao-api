insert into tournament(id, name) values (1, 'World Cup');

insert into tournament_edition(id, name, tournament_id, open_date, close_date) values (1, 'Qatar 2022', 1, '2022-11-20', '2022-12-18');

insert into tournament_group(id, tournament_edition_id, name) values (1, 1, 'A');
insert into tournament_group(id, tournament_edition_id, name) values (2, 1, 'B');
insert into tournament_group(id, tournament_edition_id, name) values (3, 1, 'C');
insert into tournament_group(id, tournament_edition_id, name) values (4, 1, 'D');
insert into tournament_group(id, tournament_edition_id, name) values (5, 1, 'E');
insert into tournament_group(id, tournament_edition_id, name) values (6, 1, 'F');
insert into tournament_group(id, tournament_edition_id, name) values (7, 1, 'G');
insert into tournament_group(id, tournament_edition_id, name) values (8, 1, 'H');
insert into tournament_group(id, tournament_edition_id, name) values (9, 1, '1/8');
insert into tournament_group(id, tournament_edition_id, name) values (10, 1, '1/4');
insert into tournament_group(id, tournament_edition_id, name) values (11, 1, '1/2');
insert into tournament_group(id, tournament_edition_id, name) values (12, 1, 'Finals');

insert into team(id, en, de, pt, short_name) values (1, 'Qatar', 'Katar', 'Catar', 'QAT');
insert into team(id, en, de, pt, short_name) values (2, 'Ecuador', 'Ecuador', 'Equador', 'ECU');
insert into team(id, en, de, pt, short_name) values (3, 'Senegal', 'Senegal', 'Senegal', 'SEN');
insert into team(id, en, de, pt, short_name) values (4, 'Netherlands', 'Niederlande', 'Holanda', 'NED');
insert into team(id, en, de, pt, short_name) values (5,  'England', 'England', 'Inglaterra', 'ENG');
insert into team(id, en, de, pt, short_name) values (6,  'IR Iran', 'Iran', 'Irã', 'IRN');
insert into team(id, en, de, pt, short_name) values (7,  'USA', 'USA', 'EUA', 'USA');
insert into team(id, en, de, pt, short_name) values (8,  'Wales', 'Wales', 'Gales', 'WLS');
insert into team(id, en, de, pt, short_name) values (9,  'Argentina', 'Argentinien', 'Argentina', 'ARG');
insert into team(id, en, de, pt, short_name) values (10,  'Saudi Arabia', 'Saudi Arabien', 'Arábia Saudita', 'KSA');
insert into team(id, en, de, pt, short_name) values (11,  'Mexico', 'Mexico', 'México', 'MEX');
insert into team(id, en, de, pt, short_name) values (12,  'Poland', 'Polen', 'Polônia', 'POL');
insert into team(id, en, de, pt, short_name) values (13,  'France', 'Frankreich', 'França', 'FRA');
insert into team(id, en, de, pt, short_name) values (14,  'Australia', 'Australien', 'Austrália', 'AUS');
insert into team(id, en, de, pt, short_name) values (15,  'Denmark', 'Dänemark', 'Dinamarca', 'DEN');
insert into team(id, en, de, pt, short_name) values (16,  'Tunisia', 'Tunesien', 'Tunísia', 'TUN');
insert into team(id, en, de, pt, short_name) values (17,  'Spain', 'Spanien', 'Espanha', 'ESP');
insert into team(id, en, de, pt, short_name) values (18,  'Costa Rica', 'Costa Rica', 'Costa Rica', 'CRI');
insert into team(id, en, de, pt, short_name) values (19,  'Germany', 'Deutschland', 'Alemanha', 'GER');
insert into team(id, en, de, pt, short_name) values (20,  'Japan', 'Japan', 'Japão', 'JPN');
insert into team(id, en, de, pt, short_name) values (21,  'Belgium', 'Belgien', 'Bélgica', 'BEL');
insert into team(id, en, de, pt, short_name) values (22,  'Canada', 'Kanada', 'Canadá', 'CAN');
insert into team(id, en, de, pt, short_name) values (23,  'Morocco', 'Marokko', 'Marrocos', 'MAR');
insert into team(id, en, de, pt, short_name) values (24,  'Croatia', 'Kroatien', 'Croácia', 'CRO');
insert into team(id, en, de, pt, short_name) values (25,  'Brazil', 'Brasilien', 'Brasil', 'BRA');
insert into team(id, en, de, pt, short_name) values (26,  'Serbia', 'Serbien', 'Sérvia', 'SRB');
insert into team(id, en, de, pt, short_name) values (27,  'Switzerland', 'Schweiz', 'Suíça', 'SWI');
insert into team(id, en, de, pt, short_name) values (28,  'Cameroon', 'Kamerun', 'Camarões', 'CAM');
insert into team(id, en, de, pt, short_name) values (29,  'Portugal', 'Portugal', 'Portugal', 'POR');
insert into team(id, en, de, pt, short_name) values (30,  'Ghana', 'Ghana', 'Gana', 'GHA');
insert into team(id, en, de, pt, short_name) values (31,  'Uruguay', 'Uruguay', 'Uruguai', 'URU');
insert into team(id, en, de, pt, short_name) values (32,  'Korea Republic', 'Korea', 'Coreia do Sul', 'KOR');

insert into team(id, en, de, pt, short_name) values (33,  'Winner Group A', 'Sieger Gruppe A', '1º lugar A', '1A');
insert into team(id, en, de, pt, short_name) values (34,  'Runner-up Group A', 'Zweiter Gruppe A', '2º lugar A', '2A');
insert into team(id, en, de, pt, short_name) values (35,  'Winner Group B', 'Sieger Gruppe B', '1º lugar B', '1B');
insert into team(id, en, de, pt, short_name) values (36,  'Runner-up Group B', 'Zweiter Gruppe B', '2º lugar B', '2B');
insert into team(id, en, de, pt, short_name) values (37,  'Winner Group C', 'Sieger Gruppe C', '1º lugar C', '1C');
insert into team(id, en, de, pt, short_name) values (38,  'Runner-up Group C', 'Zweiter Gruppe C', '2º lugar C', '2C');
insert into team(id, en, de, pt, short_name) values (39,  'Winner Group D', 'Sieger Gruppe D', '1º lugar D', '1D');
insert into team(id, en, de, pt, short_name) values (40,  'Runner-up Group D', 'Zweiter Gruppe D', '2º lugar D', '2D');
insert into team(id, en, de, pt, short_name) values (41,  'Winner Group E', 'Sieger Gruppe E', '1º lugar E', '1E');
insert into team(id, en, de, pt, short_name) values (42,  'Runner-up Group E', 'Zweiter Gruppe E', '2º lugar E', '2E');
insert into team(id, en, de, pt, short_name) values (43,  'Winner Group F', 'Sieger Gruppe F', '1º lugar F', '1F');
insert into team(id, en, de, pt, short_name) values (44,  'Runner-up Group F', 'Zweiter Gruppe F', '2º lugar F', '2F');
insert into team(id, en, de, pt, short_name) values (45,  'Winner Group G', 'Sieger Gruppe G', '1º lugar G', '1G');
insert into team(id, en, de, pt, short_name) values (46,  'Runner-up Group G', 'Zweiter Gruppe G', '2º lugar G', '2G');
insert into team(id, en, de, pt, short_name) values (47,  'Winner Group H', 'Sieger Gruppe H', '1º lugar H', '1H');
insert into team(id, en, de, pt, short_name) values (48,  'Runner-up Group H', 'Zweiter Gruppe H', '2º lugar H', '2H');

insert into team(id, en, de, pt, short_name) values (49,  'Winner Match 49', 'Sieger Spiel 49', 'Vencedor jogo 49', '⚽️49');
insert into team(id, en, de, pt, short_name) values (50,  'Winner Match 50', 'Sieger Spiel 50', 'Vencedor jogo 50', '⚽50');
insert into team(id, en, de, pt, short_name) values (51,  'Winner Match 51', 'Sieger Spiel 51', 'Vencedor jogo 51', '⚽51');
insert into team(id, en, de, pt, short_name) values (52,  'Winner Match 52', 'Sieger Spiel 52', 'Vencedor jogo 52', '⚽52');
insert into team(id, en, de, pt, short_name) values (53,  'Winner Match 53', 'Sieger Spiel 53', 'Vencedor jogo 53', '⚽53');
insert into team(id, en, de, pt, short_name) values (54,  'Winner Match 54', 'Sieger Spiel 54', 'Vencedor jogo 54', '⚽54');
insert into team(id, en, de, pt, short_name) values (55,  'Winner Match 55', 'Sieger Spiel 55', 'Vencedor jogo 55', '⚽55');
insert into team(id, en, de, pt, short_name) values (56,  'Winner Match 56', 'Sieger Spiel 56', 'Vencedor jogo 56', '⚽56');

insert into team(id, en, de, pt, short_name) values (57,  'Winner Match 57', 'Sieger Spiel 57', 'Vencedor jogo 57', '⚽57');
insert into team(id, en, de, pt, short_name) values (58,  'Winner Match 58', 'Sieger Spiel 58', 'Vencedor jogo 58', '⚽58');
insert into team(id, en, de, pt, short_name) values (59,  'Winner Match 59', 'Sieger Spiel 59', 'Vencedor jogo 59', '⚽59');
insert into team(id, en, de, pt, short_name) values (60,  'Winner Match 60', 'Sieger Spiel 60', 'Vencedor jogo 60', '⚽60');

insert into team(id, en, de, pt, short_name) values (61,  'Losers Match 61', 'Verlierer Spiel 61', 'Perdedor jogo 61', '❌61');
insert into team(id, en, de, pt, short_name) values (62,  'Losers Match 62', 'Verlierer Spiel 62', 'Perdedor jogo 62', '❌62');
insert into team(id, en, de, pt, short_name) values (63,  'Winner Match 61', 'Sieger Spiel 61', 'Vencedor jogo 61', '⚽61');
insert into team(id, en, de, pt, short_name) values (64,  'Winner Match 62', 'Sieger Spiel 62', 'Vencedor jogo 62', '⚽62');

insert into group_member(group_id, group_order, team_id) values (1, 1, 1);
insert into group_member(group_id, group_order, team_id) values (1, 2, 2);
insert into group_member(group_id, group_order, team_id) values (1, 3, 3);
insert into group_member(group_id, group_order, team_id) values (1, 4, 4);
insert into group_member(group_id, group_order, team_id) values (2, 1, 5);
insert into group_member(group_id, group_order, team_id) values (2, 2, 6);
insert into group_member(group_id, group_order, team_id) values (2, 3, 7);
insert into group_member(group_id, group_order, team_id) values (2, 4, 8);
insert into group_member(group_id, group_order, team_id) values (3, 1, 9);
insert into group_member(group_id, group_order, team_id) values (3, 2, 10);
insert into group_member(group_id, group_order, team_id) values (3, 3, 11);
insert into group_member(group_id, group_order, team_id) values (3, 4, 12);
insert into group_member(group_id, group_order, team_id) values (4, 1, 13);
insert into group_member(group_id, group_order, team_id) values (4, 2, 14);
insert into group_member(group_id, group_order, team_id) values (4, 3, 15);
insert into group_member(group_id, group_order, team_id) values (4, 4, 16);
insert into group_member(group_id, group_order, team_id) values (5, 1, 17);
insert into group_member(group_id, group_order, team_id) values (5, 2, 18);
insert into group_member(group_id, group_order, team_id) values (5, 3, 19);
insert into group_member(group_id, group_order, team_id) values (5, 4, 20);
insert into group_member(group_id, group_order, team_id) values (6, 1, 21);
insert into group_member(group_id, group_order, team_id) values (6, 2, 22);
insert into group_member(group_id, group_order, team_id) values (6, 3, 23);
insert into group_member(group_id, group_order, team_id) values (6, 4, 24);
insert into group_member(group_id, group_order, team_id) values (7, 1, 25);
insert into group_member(group_id, group_order, team_id) values (7, 2, 26);
insert into group_member(group_id, group_order, team_id) values (7, 3, 27);
insert into group_member(group_id, group_order, team_id) values (7, 4, 28);
insert into group_member(group_id, group_order, team_id) values (8, 1, 29);
insert into group_member(group_id, group_order, team_id) values (8, 2, 30);
insert into group_member(group_id, group_order, team_id) values (8, 3, 31);
insert into group_member(group_id, group_order, team_id) values (8, 4, 32);

insert into venue(id, name, city, country, capacity) values (1, 'Lusail Iconic', 'Lusail', 'QAT', 80000);
insert into venue(id, name, city, country, capacity) values (2, 'Al Bayt', 'Al Khor', 'QAT', 60000);
insert into venue(id, name, city, country, capacity) values (3, 'Stadium 974', 'Doha', 'QAT', 40000);
insert into venue(id, name, city, country, capacity) values (4, 'Al Thumama', 'Doha', 'QAT', 40000);
insert into venue(id, name, city, country, capacity) values (5, 'Education City', 'Al Rayyan', 'QAT', 45350);
insert into venue(id, name, city, country, capacity) values (6, 'Ahmad bin Ali', 'Al Rayyan', 'QAT', 44740);
insert into venue(id, name, city, country, capacity) values (7, 'Khalifa International', 'Al Rayyan', 'QAT', 40000);
insert into venue(id, name, city, country, capacity) values (8, 'Al Janoub', 'Al Wakrah', 'QAT', 40000);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (1, 1, 1, 1, 2, '2022-11-20 17:00:00', 1, 2);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (2, 1, 2, 1, 4, '2022-11-21 17:00:00', 3, 4);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (18, 1, 18, 1, 4, '2022-11-25 14:00:00', 1, 3);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (19, 1, 19, 1, 7, '2022-11-25 17:00:00', 4, 2);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (35, 1, 35, 1, 7, '2022-11-29 16:00:00', 2, 3);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (36, 1, 36, 1, 2, '2022-11-29 16:00:00', 4, 1);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (3, 1, 3, 2, 7, '2022-11-21 14:00:00', 5, 6);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (4, 1, 4, 2, 6, '2022-11-21 20:00:00', 7, 8);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (17, 1, 17, 2, 6, '2022-11-25 11:00:00', 8, 6);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (20, 1, 20, 2, 2, '2022-11-25 20:00:00', 5, 7);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (33, 1, 33, 2, 6, '2022-11-29 20:00:00', 8, 5);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (34, 1, 34, 2, 4, '2022-11-29 20:00:00', 6, 7);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (8, 1, 8, 3, 1, '2022-11-22 11:00:00', 9, 10);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (7, 1, 7, 3, 3, '2022-11-22 17:00:00', 11, 12);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (22, 1, 22, 3, 5, '2022-11-26 14:00:00', 12, 10);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (24, 1, 24, 3, 1, '2022-11-26 20:00:00', 9, 11);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (39, 1, 39, 3, 3, '2022-11-30 20:00:00', 12, 9);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (40, 1, 40, 3, 1, '2022-11-30 20:00:00', 10, 11);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (6, 1, 6, 4, 5, '2022-11-22 14:00:00', 15, 16);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (5, 1, 5, 4, 8, '2022-11-22 20:00:00', 13, 14);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (21, 1, 21, 4, 8, '2022-11-26 11:00:00', 16, 14);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (23, 1, 23, 4, 3, '2022-11-26 17:00:00', 13, 15);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (37, 1, 37, 4, 8, '2022-11-30 16:00:00', 14, 15);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (38, 1, 38, 4, 5, '2022-11-30 16:00:00', 16, 13);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (11, 1, 11, 5, 7, '2022-11-23 14:00:00', 19, 20);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (10, 1, 10, 5, 4, '2022-11-23 17:00:00', 17, 18);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (25, 1, 25, 5, 6, '2022-11-27 11:00:00', 20, 18);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (28, 1, 28, 5, 2, '2022-11-27 20:00:00', 17, 19);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (43, 1, 43, 5, 7, '2022-12-01 20:00:00', 18, 19);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (44, 1, 44, 5, 2, '2022-12-01 20:00:00', 20, 17);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (12, 1, 12, 6, 2, '2022-11-23 11:00:00', 23, 24);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (9, 1, 9, 6, 6, '2022-11-23 20:00:00', 21, 22);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (26, 1, 26, 6, 4, '2022-11-27 14:00:00', 21, 23);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (27, 1, 27, 6, 7, '2022-11-27 17:00:00', 24, 22);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (41, 1, 41, 6, 6, '2022-12-01 16:00:00', 24, 21);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (42, 1, 42, 6, 4, '2022-12-01 16:00:00', 22, 23);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (13, 1, 13, 7, 8, '2022-11-24 11:00:00', 27, 28);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (16, 1, 16, 7, 1, '2022-11-24 20:00:00', 25, 26);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (29, 1, 29, 7, 8, '2022-11-28 11:00:00', 28, 26);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (31, 1, 31, 7, 3, '2022-11-28 17:00:00', 25, 27);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (47, 1, 47, 7, 3, '2022-12-02 20:00:00', 26, 27);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (48, 1, 48, 7, 1, '2022-12-02 20:00:00', 28, 25);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (14, 1, 14, 8, 5, '2022-11-24 14:00:00', 31, 32);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (15, 1, 15, 8, 3, '2022-11-24 17:00:00', 29, 30);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (30, 1, 30, 8, 5, '2022-11-28 14:00:00', 32, 30);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (32, 1, 32, 8, 1, '2022-11-28 20:00:00', 29, 31);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (45, 1, 45, 8, 8, '2022-12-02 16:00:00', 30, 31);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (46, 1, 46, 8, 5, '2022-12-02 16:00:00', 32, 29);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (49, 1, 49, 9, 7, '2022-12-03 16:00:00', 33, 36);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (50, 1, 50, 9, 6, '2022-12-03 20:00:00', 37, 40);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (52, 1, 52, 9, 4, '2022-12-04 16:00:00', 39, 38);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (51, 1, 51, 9, 2, '2022-12-04 20:00:00', 35, 34);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (53, 1, 53, 9, 8, '2022-12-05 16:00:00', 41, 44);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (54, 1, 54, 9, 3, '2022-12-05 20:00:00', 45, 48);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (55, 1, 55, 9, 5, '2022-12-06 16:00:00', 43, 42);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (56, 1, 56, 9, 1, '2022-12-06 20:00:00', 47, 46);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (58, 1, 58, 10, 5, '2022-12-09 16:00:00', 53, 54);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (57, 1, 57, 10, 1, '2022-12-09 20:00:00', 49, 50);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (60, 1, 60, 10, 4, '2022-12-10 16:00:00', 55, 56);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (59, 1, 59, 10, 2, '2022-12-10 20:00:00', 51, 52);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (61, 1, 61, 11, 1, '2022-12-13 20:00:00', 57, 58);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (62, 1, 62, 11, 2, '2022-12-14 20:00:00', 59, 60);

insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (63, 1, 63, 12, 7, '2022-12-17 16:00:00', 61, 62);
insert into matches(id, tournament_edition_id, tournament_match_id, group_id, venue_id, kick_off, team_a, team_b) values (64, 1, 64, 12, 1, '2022-12-18 16:00:00', 63, 64);
