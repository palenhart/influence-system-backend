-- ADD departments
INSERT INTO PUBLIC.DEPARTMENT(NAME) VALUES ('none'), ('Exploration'), ('Business'), ('Security'), ('Resources'), ('Social'), ('Support'), ('Public Relations');
-- ADD division
INSERT INTO PUBLIC.DIVISION(NAME, DEPARTMENT_ID) VALUES ('none', 1), ('Cartograhy', 2), ('Prospecting', 2), ('Research', 2), ('Contracts', 3), ('Finance', 3), ('Trade', 3), ('CSOC', 4), ('Ground', 4), ('Repossesion', 4), ('Space', 4), ('Development', 5), ('Extraction', 5), ('Transport', 5), ('Diplomacy', 6), ('HR', 6), ('Training', 6), ('CSAR', 7), ('Engineering', 7), ('IT', 7), ('e-Sports', 8), ('Media', 8);      
-- ADD influence and demerits
INSERT INTO PUBLIC.INFLUENCE_TYPE(NAME) VALUES ('INFLUENCE'), ('DEMERIT');
-- ADD ranks
INSERT INTO PUBLIC.RANK(NAME, TRIBUTES_PER_WEEK) VALUES ('Junior Associate', 50), ('Associate', 100), ('Senior Associate', 200), ('Manager', 400), ('Director', 800), ('Board Member', 1600), ('CEO', 1600);
-- TESTING ADD sample corporateers
INSERT INTO PUBLIC.CORPORATEER(NAME, TOTAL_INFLUENCE, TRIBUTES, MAIN_DIVISION_ID, RANK_ID) VALUES ('Weyland', 0, 0, 1, 7), ('Zollak', 0, 0, 1, 6);

-- ADD influence
INSERT INTO PUBLIC.INFLUENCE(AMOUNT, CORPORATEER_ID, DEPARTMENT_ID, DIVISION_ID, TYPE_ID) VALUES (0, 1, 1, 1, 1), (0, 1, 2, 1, 1), (0, 1, 3, 1, 1), (0, 1, 4, 1, 1), (0, 1, 5, 1, 1), (0, 1, 6, 1, 1), (0, 1, 7, 1, 1), (0, 1, 8, 1, 1), (0, 1, 2, 2, 1), (0, 1, 2, 3, 1), (0, 1, 2, 4, 1), (0, 1, 3, 5, 1), (0, 1, 3, 6, 1), (0, 1, 3, 7, 1), (0, 1, 4, 8, 1), (0, 1, 4, 9, 1), (0, 1, 4, 10, 1), (0, 1, 4, 11, 1), (0, 1, 5, 12, 1), (0, 1, 5, 13, 1), (0, 1, 5, 14, 1), (0, 1, 6, 15, 1), (0, 1, 6, 16, 1), (0, 1, 6, 17, 1), (0, 1, 7, 18, 1), (0, 1, 7, 19, 1), (0, 1, 7, 20, 1), (0, 1, 8, 21, 1), (0, 1, 8, 22, 1), (0, 1, 1, 1, 2), (0, 1, 2, 1, 2), (0, 1, 3, 1, 2), (0, 1, 4, 1, 2), (0, 1, 5, 1, 2), (0, 1, 6, 1, 2), (0, 1, 7, 1, 2), (0, 1, 8, 1, 2), (0, 1, 2, 2, 2), (0, 1, 2, 3, 2), (0, 1, 2, 4, 2), (0, 1, 3, 5, 2), (0, 1, 3, 6, 2), (0, 1, 3, 7, 2), (0, 1, 4, 8, 2), (0, 1, 4, 9, 2), (0, 1, 4, 10, 2), (0, 1, 4, 11, 2), (0, 1, 5, 12, 2), (0, 1, 5, 13, 2), (0, 1, 5, 14, 2), (0, 1, 6, 15, 2), (0, 1, 6, 16, 2), (0, 1, 6, 17, 2), (0, 1, 7, 18, 2), (0, 1, 7, 19, 2), (0, 1, 7, 20, 2), (0, 1, 8, 21, 2), (0, 1, 8, 22, 2), (0, 2, 1, 1, 1), (0, 2, 2, 1, 1), (0, 2, 3, 1, 1), (0, 2, 4, 1, 1), (0, 2, 5, 1, 1), (0, 2, 6, 1, 1), (0, 2, 7, 1, 1), (0, 2, 8, 1, 1), (0, 2, 2, 2, 1), (0, 2, 2, 3, 1), (0, 2, 2, 4, 1), (0, 2, 3, 5, 1), (0, 2, 3, 6, 1), (0, 2, 3, 7, 1), (0, 2, 4, 8, 1), (0, 2, 4, 9, 1), (0, 2, 4, 10, 1), (0, 2, 4, 11, 1), (0, 2, 5, 12, 1), (0, 2, 5, 13, 1), (0, 2, 5, 14, 1), (0, 2, 6, 15, 1), (0, 2, 6, 16, 1), (0, 2, 6, 17, 1), (0, 2, 7, 18, 1), (0, 2, 7, 19, 1), (0, 2, 7, 20, 1), (0, 2, 8, 21, 1), (0, 2, 8, 22, 1), (0, 2, 1, 1, 2), (0, 2, 2, 1, 2), (0, 2, 3, 1, 2), (0, 2, 4, 1, 2), (0, 2, 5, 1, 2), (0, 2, 6, 1, 2), (0, 2, 7, 1, 2), (0, 2, 8, 1, 2), (0, 2, 2, 2, 2), (0, 2, 2, 3, 2), (0, 2, 2, 4, 2), (0, 2, 3, 5, 2), (0, 2, 3, 6, 2), (0, 2, 3, 7, 2), (0, 2, 4, 8, 2), (0, 2, 4, 9, 2), (0, 2, 4, 10, 2), (0, 2, 4, 11, 2), (0, 2, 5, 12, 2), (0, 2, 5, 13, 2), (0, 2, 5, 14, 2), (0, 2, 6, 15, 2), (0, 2, 6, 16, 2), (0, 2, 6, 17, 2), (0, 2, 7, 18, 2), (0, 2, 7, 19, 2), (0, 2, 7, 20, 2), (0, 2, 8, 21, 2), (0, 2, 8, 22, 2);

-- ADD users
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin@admin.com', 1, PARSEDATETIME('01-01-2016', 'dd-MM-yyyy'), 1);
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE, CORPORATEER_ID) VALUES (2, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'enabled@user.com', 1, PARSEDATETIME('01-01-2016','dd-MM-yyyy'), 2);
INSERT INTO USER (ID, USERNAME, PASSWORD, EMAIL, ENABLED, LAST_PASSWORD_RESET_DATE) VALUES (3, 'disabled', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'disabled@user.com', 0, PARSEDATETIME('01-01-2016','dd-MM-yyyy'));
 
INSERT INTO AUTHORITY (ID, NAME) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (ID, NAME) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_ID) VALUES (3, 1);