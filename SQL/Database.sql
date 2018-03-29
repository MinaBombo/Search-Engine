--DROP DATABASE IF EXISTS "SearchEngineDatabase";
-- CREATE DATABASE IF NOT EXISTS "SearchEngineDatabase"
-- WITH
-- OWNER = "SearchEngine"
-- ENCODING = 'UTF8'
-- LC_COLLATE = 'English_United States.1252'
-- LC_CTYPE = 'English_United States.1252'
-- TABLESPACE = pg_default
-- CONNECTION LIMIT = -1;

--\connect SearchEngineDatabase;

DROP SCHEMA IF EXISTS "SearchEngineSchema" CASCADE;
CREATE SCHEMA "SearchEngineSchema"
    AUTHORIZATION "SearchEngine";

SET SEARCH_PATH TO "SearchEngineSchema";
	
CREATE TABLE Document
(
  ID        SERIAL UNIQUE,
  Name      CHARACTER VARYING(255) NOT NULL,
  URL       CHARACTER VARYING(255),
  Processed BOOLEAN DEFAULT FALSE
)

WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE Document
  OWNER TO "SearchEngine";
-- INSERT INTO Document (Name, URL, Processed)
-- VALUES ('TestDoc1.html', 'TestURL', FALSE), ('TestDoc2.html', 'TestURL', FALSE), ('TestDoc3.html', 'TestURL', FALSE)
--   , ('TestDoc4.html', 'TestURL', FALSE), ('TestDoc5.html', 'TestURL', FALSE), ('TestDoc6.html', 'TestURL', FALSE),
--   ('TestDoc7.html', 'TestURL', FALSE)
--   , ('TestDoc8.html', 'TestURL', FALSE), ('TestDoc9.html', 'TestURL', FALSE), ('TestDoc10.html', 'TestURL', FALSE),
--   ('TestDoc11.html', 'TestURL', FALSE), ('TestDoc12.html', 'TestURL', FALSE);
--Edit the Text type, so it consumes less memory
CREATE TABLE Word
(
  ID         SERIAL,
  Text       TEXT    NOT NULL,
  DocumentID INTEGER NOT NULL REFERENCES Document (ID)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE Word
  OWNER TO "SearchEngine";

CREATE TABLE Link
(
  ID            SERIAL UNIQUE,
  MainLink      TEXT NOT NULL,
  RefrencedLink TEXT NOT NULL
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE Link
  OWNER TO "SearchEngine";

CREATE TABLE Seed
(
  ID  SERIAL UNIQUE,
  URL TEXT NOT NULL,
  Processed BOOLEAN DEFAULT  FALSE
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE Seed
  OWNER TO "SearchEngine";
INSERT INTO Seed (URL,Processed) VALUES ('https://en.wikipedia.org/wiki/Main_Page',FALSE );
ALTER USER "SearchEngine" SET search_path to 'SearchEngineSchema'