INSERT INTO userlogins5_archive.users (USER_ID, EMAIL, PASSWD, IS_GUEST, SIGNATURE, REGISTER_TIME, LAST_ACTIVE, LAST_NAME, FIRST_NAME, MIDDLE_NAME, TITLE,
ORGANIZATION, DEPARTMENT, ADDRESS, CITY, STATE, ZIP_CODE, PHONE_NUMBER, COUNTRY, PREV_USER_ID)
SELECT USER_ID, EMAIL, PASSWD, IS_GUEST, SIGNATURE, REGISTER_TIME, LAST_ACTIVE, LAST_NAME, FIRST_NAME, MIDDLE_NAME, TITLE,
ORGANIZATION, DEPARTMENT, ADDRESS, CITY, STATE, ZIP_CODE, PHONE_NUMBER, COUNTRY, PREV_USER_ID
FROM userlogins3_archive.users;

INSERT INTO userlogins5_archive.users (USER_ID, EMAIL, PASSWD, IS_GUEST, SIGNATURE, REGISTER_TIME, LAST_ACTIVE, LAST_NAME, FIRST_NAME, MIDDLE_NAME, TITLE,
ORGANIZATION, DEPARTMENT, ADDRESS, CITY, STATE, ZIP_CODE, PHONE_NUMBER, COUNTRY, PREV_USER_ID)
SELECT USER_ID, EMAIL, PASSWD, IS_GUEST, SIGNATURE, REGISTER_TIME, LAST_ACTIVE, LAST_NAME, FIRST_NAME, MIDDLE_NAME, TITLE,
ORGANIZATION, DEPARTMENT, ADDRESS, CITY, STATE, ZIP_CODE, PHONE_NUMBER, COUNTRY, PREV_USER_ID
FROM userlogins3_archive_south.users WHERE user_id NOT IN (SELECT user_id FROM userlogins5_archive.users);

INSERT INTO userlogins5_archive.user_roles (USER_ID, USER_ROLE)
SELECT USER_ID, USER_ROLE
FROM userlogins3_archive.user_roles;

INSERT INTO userlogins5_archive.user_roles (USER_ID, USER_ROLE)
SELECT USER_ID, USER_ROLE
FROM userlogins3_archive.user_roles WHERE user_id NOT IN (SELECT user_id FROM userlogins5_archive.users);

INSERT INTO userlogins5_archive.user_baskets (BASKET_ID, USER_ID, BASKET_NAME, PROJECT_ID, RECORD_CLASS, IS_DEFAULT, PK_COLUMN_1, PK_COLUMN_2, PK_COLUMN_3, PREV_BASKET_ID)
SELECT BASKET_ID, USER_ID, BASKET_NAME, PROJECT_ID, RECORD_CLASS, IS_DEFAULT, PK_COLUMN_1, PK_COLUMN_2, PK_COLUMN_3, PREV_BASKET_ID
FROM userlogins3_archive.user_baskets WHERE 



