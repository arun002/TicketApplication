CREATE TABLE IF NOT EXISTS SEAT_HOLD(
   HOLD_ID SERIAL PRIMARY KEY NOT NULL,
   EMAIL VARCHAR(100) NOT NULL,
   HOLD_DATE TIMESTAMPTZ NOT NULL,
   NUM_SEATS INT NOT NULL,
   RESERVATION_ID INTEGER,
   PRICE NUMERIC
);

CREATE TABLE IF NOT EXISTS RESERVATION(
   RESERVE_ID SERIAL PRIMARY KEY NOT NULL,
   CONFIRM_CODE VARCHAR(100) NOT NULL,
   EMAIL VARCHAR(100) NOT NULL,
   CONFIRM_DATE TIMESTAMPTZ NOT NULL,
   NUM_SEATS INT NOT NULL,
   PRICE NUMERIC
);

CREATE TABLE IF NOT EXISTS VENUE(
   LEVEL_ID INT PRIMARY KEY NOT NULL,
   LEVEL_NAME VARCHAR(100)  NOT NULL,
   PRICE DECIMAL NOT NULL,
   NUM_ROWS INT NOT NULL,
   SEATS_ROW INT NOT NULL
);

CREATE TABLE IF NOT EXISTS SEATS(
   SEAT_ID SERIAL PRIMARY KEY NOT NULL,
   HOLD_ID INTEGER,
   LEVEL_ID INT NOT NULL,
   ROW_NUM INT NOT NULL,
   SEAT_NUM INT NOT NULL
);

ALTER TABLE SEATS ADD FOREIGN KEY(HOLD_ID) REFERENCES SEAT_HOLD(HOLD_ID);

ALTER TABLE SEATS ADD FOREIGN KEY(LEVEL_ID) REFERENCES VENUE(LEVEL_ID);

ALTER TABLE SEAT_HOLD ADD FOREIGN KEY(RESERVATION_ID) REFERENCES RESERVATION(RESERVE_ID);