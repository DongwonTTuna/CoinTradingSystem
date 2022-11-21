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
INSERT INTO APIKEYS (exchange,api_key,secret_key) VALUES ('UPBIT',null,null);

DROP TABLE IF EXISTS TotalTradesNPnL;
CREATE TABLE TotalTradesNPnL(
    exchange TEXT NOT NULL PRIMARY KEY,
    num      INTEGER NOT NULL,
    Pbal     NUMERIC NOT NULL,
    Tbal     NUMERIC NOT NULL
);


DROP SCHEMA IF EXISTS B CASCADE;
CREATE SCHEMA IF NOT EXISTS B;

DO $$
DECLARE
  exc text;
BEGIN
  FOR exc IN
  SELECT
    exchange
  FROM
    APIKEYS LOOP
      EXECUTE 'CREATE TABLE IF NOT EXISTS B.' || exc || 'BAL(symbol text NOT NULL, amount numeric not null, freeAmount numeric NOT NULL, freezeAmount numeric NOT NULL, withdrawable boolean NOT NULL, walletAddress text NOT NULL)';
      EXECUTE 'INSERT INTO TotalTradesNPnL (exchange, num, Pbal, Tbal) VALUES ('''|| exc ||''',0,0,0)';
    END LOOP;
END
$$;

