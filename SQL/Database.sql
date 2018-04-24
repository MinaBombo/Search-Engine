-- DROP DATABASE IF EXISTS "SearchEngineDatabase";
-- CREATE USER "SearchEngine" PASSWORD 'root';
-- CREATE DATABASE "SearchEngineDatabase"
-- WITH
-- OWNER = "SearchEngine"
-- ENCODING = 'UTF8'
-- TABLESPACE = pg_default
-- CONNECTION LIMIT = -1;
--
-- \connect SearchEngineDatabase;

DROP SCHEMA IF EXISTS "SearchEngineSchema" CASCADE;
CREATE SCHEMA "SearchEngineSchema"
  AUTHORIZATION "SearchEngine";

SET SEARCH_PATH TO "SearchEngineSchema";

CREATE TABLE Document
(
  ID        SERIAL UNIQUE,
  Name      CHARACTER VARYING(255) NOT NULL,
  URL       TEXT,
  Processed BOOLEAN DEFAULT FALSE,
  Rank      FLOAT DEFAULT 1.0
)

WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE Document
  OWNER TO "SearchEngine";


CREATE TABLE Word
(
  ID         SERIAL,
  Text       TEXT    NOT NULL,
  Count      Integer DEFAULT 1,
  DocumentID INTEGER NOT NULL REFERENCES Document (ID) ON DELETE CASCADE,
  CONSTRAINT UniqueConstraint UNIQUE (DocumentID,Text)
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
  DocuemntID    INTEGER NOT NULL REFERENCES Document(ID) ON DELETE CASCADE ,
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
  ID        SERIAL UNIQUE,
  URL       TEXT NOT NULL UNIQUE,
  Processed BOOLEAN DEFAULT FALSE,
  InLinks   INTEGER DEFAULT 0
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE Seed
  OWNER TO "SearchEngine";

CREATE UNLOGGED TABLE TempSeedStorage (LIKE Seed
);
ALTER TABLE TempSeedStorage
  OWNER TO "SearchEngine";

ALTER USER "SearchEngine" SET search_path TO 'SearchEngineSchema';

INSERT INTO Seed (URL) VALUES ('https://en.wikipedia.org/wiki/Main_Page'),
  ('http://facebook.com'), ('http://digitaloccean.com'), ('http://gitlab.com'), ('http://amazon.com'),
  ('http://twitter.com'),
  ('http://google.com'),
  ('http://cnn.com'),
  ('http://bbc.com'),
  ('http://tutorialspoint.com'),
  ('http://stackoverflow.com'),
  ('https://stackoverflow.com'),
  ('https://insights.stackoverflow.com/survey/2018'),
  ('https://en.wikipedia.org'),
  ('http://javacodegeeks.com'),
  ('http://owasp.org'),
  ('http://mongodb.com'),
  ('http://github.com'),
  ('http://facebook.github.io'),
  ('https://angular.io/tutorial'),
  ('https://www.namecheap.com'),
  ('https://www.youtube.com');

