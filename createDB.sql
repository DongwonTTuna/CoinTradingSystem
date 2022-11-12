DROP DATABASE "TRADINGBOT";
CREATE DATABASE "TRADINGBOT" WITH OWNER = postgres ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C' IS_TEMPLATE = FALSE;


DROP TABLE IF EXISTS ORDERS;

CREATE TABLE ORDERS (
    uuid TEXT NOT NULL PRIMARY KEY,
    exchange TEXT NOT NULL,
    ordertype SMALLINT NOT NULL,
    symbol TEXT NOT NULL,
    checkprice NUMERIC NOT NULL,
    targetprice NUMERIC NOT NULL,
    amount NUMERIC NOT NULL
);
DROP TABLE IF EXISTS APIKEYS;

CREATE TABLE APIKEYS(
    exchange TEXT NOT NULL PRIMARY KEY,
    api_key TEXT,
    secret_key TEXT
);

INSERT INTO APIKEYS (exchange,api_key,secret_key) VALUES ('BINANCE',null,null);
INSERT INTO APIKEYS (exchange,api_key,secret_key) VALUES ('GATEIO',null,null);
INSERT INTO APIKEYS (exchange,api_key,secret_key) VALUES ('MEXC',null,null);
INSERT INTO APIKEYS (exchange,api_key,secret_key) VALUES ('HUOBI',null,null);