-- ADD departments
INSERT INTO PUBLIC.DEPARTMENT(NAME) VALUES ('none'), ('Exploration'), ('Business'), ('Security'), ('Resources'), ('Social'), ('Support'), ('Public Relations');
-- ADD division
INSERT INTO PUBLIC.DIVISION(NAME, DEPARTMENT_ID) VALUES ('none', 1), ('none', 2), ('none', 3), ('none', 4), ('none', 5), ('none', 6), ('none', 7), ('none', 8), ('Cartography', 2), ('Prospecting', 2), ('Research', 2), ('Contracts', 3), ('Finance', 3), ('Trade', 3), ('CSOC', 4), ('Ground', 4), ('Repossesion', 4), ('Space', 4), ('Development', 5), ('Extraction', 5), ('Transport', 5), ('Diplomacy', 6), ('HR', 6), ('Training', 6), ('CSAR', 7), ('Engineering', 7), ('IT', 7), ('e-Sports', 8), ('Media', 8);      
-- ADD influence and demerits
INSERT INTO PUBLIC.INFLUENCE_TYPE(NAME) VALUES ('INFLUENCE'), ('DEMERIT');
-- ADD ranks
INSERT INTO PUBLIC.RANK(NAME, TRIBUTES_PER_WEEK) VALUES ('Junior Associate', 50), ('Associate', 100), ('Senior Associate', 200), ('Manager', 400), ('Director', 800), ('Board Member', 1600), ('CEO', 1600);
-- TESTING ADD sample corporateers
INSERT INTO PUBLIC.CORPORATEER(NAME, LIFETIME_INFLUENCE, TOTAL_INFLUENCE, TRIBUTES, MAIN_DIVISION_ID, RANK_ID) VALUES ('Weyland', 0, 0, 0, 1, 7), ('Zollak', 0, 0, 0, 1, 6), ('Draxian', 0, 0, 0, 1, 3), ('Braden12', 0, 0, 0, 1, 6), ('Nikmyth', 0, 0, 0, 1, 6);

-- ADD influence
INSERT INTO PUBLIC.INFLUENCE(AMOUNT, CORPORATEER_ID, DIVISION_ID, TYPE_ID) VALUES (0, 1, 1, 1), (0, 1, 2, 1), (0, 1, 3, 1), (0, 1, 4, 1), (0, 1, 5, 1), (0, 1, 6, 1), (0, 1, 7, 1), (0, 1, 8, 1), (0, 1, 9, 1), (100, 1, 10, 1), (0, 1, 11, 1), (0, 1, 12, 1), (0, 1, 13, 1), (0, 1, 14, 1), (0, 1, 15, 1), (0, 1, 16, 1), (0, 1, 17, 1), (0, 1, 18, 1), (0, 1, 19, 1), (0, 1, 20, 1), (0, 1, 21, 1), (0, 1, 22, 1), (0, 1, 23, 1), (0, 1, 24, 1), (0, 1, 25, 1), (0, 1, 26, 1), (0, 1, 27, 1), (0, 1, 28, 1), (0, 1, 29, 1),(0, 1, 1, 2), (0, 1, 2, 2), (0, 1, 3, 2), (0, 1, 4, 2), (0, 1, 5, 2), (0, 1, 6, 2), (0, 1, 7, 2), (0, 1, 8, 2), (0, 1, 9, 2), (0, 1, 10, 2), (0, 1, 11, 2), (0, 1, 12, 2), (0, 1, 13, 2), (0, 1, 14, 2), (0, 1, 15, 2), (0, 1, 16, 2), (0, 1, 17, 2), (0, 1, 18, 2), (0, 1, 19, 2), (0, 1, 20, 2), (0, 1, 21, 2), (0, 1, 22, 2), (0, 1, 23, 2), (0, 1, 24, 2), (0, 1, 25, 2), (0, 1, 26, 2), (0, 1, 27, 2), (0, 1, 28, 2), (0, 1, 29, 2),(0, 2, 1, 1), (0, 2, 2, 1), (0, 2, 3, 1), (0, 2, 4, 1), (0, 2, 5, 1), (0, 2, 6, 1), (0, 2, 7, 1), (0, 2, 8, 1), (0, 2, 9, 1), (0, 2, 10, 1), (0, 2, 11, 1), (0, 2, 12, 1), (0, 2, 13, 1), (0, 2, 14, 1), (0, 2, 15, 1), (0, 2, 16, 1), (0, 2, 17, 1), (0, 2, 18, 1), (0, 2, 19, 1), (0, 2, 20, 1), (0, 2, 21, 1), (0, 2, 22, 1), (0, 2, 23, 1), (0, 2, 24, 1), (0, 2, 25, 1), (0, 2, 26, 1), (0, 2, 27, 1), (0, 2, 28, 1), (0, 2, 29, 1),(0, 2, 1, 2), (0, 2, 2, 2), (0, 2, 3, 2), (0, 2, 4, 2), (0, 2, 5, 2), (0, 2, 6, 2), (0, 2, 7, 2), (0, 2, 8, 2), (0, 2, 9, 2), (0, 2, 10, 2), (0, 2, 11, 2), (0, 2, 12, 2), (0, 2, 13, 2), (0, 2, 14, 2), (0, 2, 15, 2), (0, 2, 16, 2), (0, 2, 17, 2), (0, 2, 18, 2), (0, 2, 19, 2), (0, 2, 20, 2), (0, 2, 21, 2), (0, 2, 22, 2), (0, 2, 23, 2), (0, 2, 24, 2), (0, 2, 25, 2), (0, 2, 26, 2), (0, 2, 27, 2), (0, 2, 28, 2), (0, 2, 29, 2),(0, 3, 1, 1), (0, 3, 2, 1), (0, 3, 3, 1), (0, 3, 4, 1), (0, 3, 5, 1), (0, 3, 6, 1), (0, 3, 7, 1), (0, 3, 8, 1), (0, 3, 9, 1), (0, 3, 10, 1), (0, 3, 11, 1), (0, 3, 12, 1), (0, 3, 13, 1), (0, 3, 14, 1), (0, 3, 15, 1), (0, 3, 16, 1), (0, 3, 17, 1), (0, 3, 18, 1), (0, 3, 19, 1), (0, 3, 20, 1), (0, 3, 21, 1), (0, 3, 22, 1), (0, 3, 23, 1), (0, 3, 24, 1), (0, 3, 25, 1), (0, 3, 26, 1), (0, 3, 27, 1), (0, 3, 28, 1), (0, 3, 29, 1),(0, 3, 1, 2), (0, 3, 2, 2), (0, 3, 3, 2), (0, 3, 4, 2), (0, 3, 5, 2), (0, 3, 6, 2), (0, 3, 7, 2), (0, 3, 8, 2), (0, 3, 9, 2), (0, 3, 10, 2), (0, 3, 11, 2), (0, 3, 12, 2), (0, 3, 13, 2), (0, 3, 14, 2), (0, 3, 15, 2), (0, 3, 16, 2), (0, 3, 17, 2), (0, 3, 18, 2), (0, 3, 19, 2), (0, 3, 20, 2), (0, 3, 21, 2), (0, 3, 22, 2), (0, 3, 23, 2), (0, 3, 24, 2), (0, 3, 25, 2), (0, 3, 26, 2), (0, 3, 27, 2), (0, 3, 28, 2), (0, 3, 29, 2),(0, 4, 1, 1), (0, 4, 2, 1), (0, 4, 3, 1), (0, 4, 4, 1), (0, 4, 5, 1), (0, 4, 6, 1), (0, 4, 7, 1), (0, 4, 8, 1), (0, 4, 9, 1), (0, 4, 10, 1), (0, 4, 11, 1), (0, 4, 12, 1), (0, 4, 13, 1), (0, 4, 14, 1), (0, 4, 15, 1), (0, 4, 16, 1), (0, 4, 17, 1), (0, 4, 18, 1), (0, 4, 19, 1), (0, 4, 20, 1), (0, 4, 21, 1), (0, 4, 22, 1), (0, 4, 23, 1), (0, 4, 24, 1), (0, 4, 25, 1), (0, 4, 26, 1), (0, 4, 27, 1), (0, 4, 28, 1), (0, 4, 29, 1),(0, 4, 1, 2), (0, 4, 2, 2), (0, 4, 3, 2), (0, 4, 4, 2), (0, 4, 5, 2), (0, 4, 6, 2), (0, 4, 7, 2), (0, 4, 8, 2), (0, 4, 9, 2), (0, 4, 10, 2), (0, 4, 11, 2), (0, 4, 12, 2), (0, 4, 13, 2), (0, 4, 14, 2), (0, 4, 15, 2), (0, 4, 16, 2), (0, 4, 17, 2), (0, 4, 18, 2), (0, 4, 19, 2), (0, 4, 20, 2), (0, 4, 21, 2), (0, 4, 22, 2), (0, 4, 23, 2), (0, 4, 24, 2), (0, 4, 25, 2), (0, 4, 26, 2), (0, 4, 27, 2), (0, 4, 28, 2), (0, 4, 29, 2),(0, 5, 1, 1), (0, 5, 2, 1), (0, 5, 3, 1), (0, 5, 4, 1), (0, 5, 5, 1), (0, 5, 6, 1), (0, 5, 7, 1), (0, 5, 8, 1), (0, 5, 9, 1), (0, 5, 10, 1), (0, 5, 11, 1), (0, 5, 12, 1), (0, 5, 13, 1), (0, 5, 14, 1), (0, 5, 15, 1), (0, 5, 16, 1), (0, 5, 17, 1), (0, 5, 18, 1), (0, 5, 19, 1), (0, 5, 20, 1), (0, 5, 21, 1), (0, 5, 22, 1), (0, 5, 23, 1), (0, 5, 24, 1), (0, 5, 25, 1), (0, 5, 26, 1), (0, 5, 27, 1), (0, 5, 28, 1), (0, 5, 29, 1),(0, 5, 1, 2), (0, 5, 2, 2), (0, 5, 3, 2), (0, 5, 4, 2), (0, 5, 5, 2), (0, 5, 6, 2), (0, 5, 7, 2), (0, 5, 8, 2), (0, 5, 9, 2), (0, 5, 10, 2), (0, 5, 11, 2), (0, 5, 12, 2), (0, 5, 13, 2), (0, 5, 14, 2), (0, 5, 15, 2), (0, 5, 16, 2), (0, 5, 17, 2), (0, 5, 18, 2), (0, 5, 19, 2), (0, 5, 20, 2), (0, 5, 21, 2), (0, 5, 22, 2), (0, 5, 23, 2), (0, 5, 24, 2), (0, 5, 25, 2), (0, 5, 26, 2), (0, 5, 27, 2), (0, 5, 28, 2), (0, 5, 29, 2);

-- ADD users
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'weyland@thecorporateer.com', 1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'), 1);
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (2, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'zollak@thecorporateer.com', 1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), 2);
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (3, 'draxian', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'draxian@thecorporateer.com', 1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), 3);
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (4, 'braden12', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'braden12@thecorporateer.com', 1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), 4);
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (5, 'nikmyth', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'nikmyth@thecorporateer.com', 1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), 5);
 
INSERT INTO AUTHORITY (ID, NAME) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (4, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (5, 1);