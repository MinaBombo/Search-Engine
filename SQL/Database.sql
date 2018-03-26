DROP DATABASE IF EXISTS "SearchEngineDatabase";
CREATE DATABASE "SearchEngineDatabase"
WITH
OWNER = "SearchEngine"
ENCODING = 'UTF8'
LC_COLLATE = 'English_United States.1252'
LC_CTYPE = 'English_United States.1252'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;

\connect SearchEngineDatabase

DROP TABLE IF EXISTS public.Word;
DROP TABLE IF EXISTS public.Document;
CREATE TABLE public.Document
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

ALTER TABLE public.Document
  OWNER TO "SearchEngine";
INSERT INTO Document (Name, URL, Processed)
VALUES ('TestDoc1.html', 'TestURL', FALSE), ('TestDoc2.html', 'TestURL', FALSE), ('TestDoc3.html', 'TestURL', FALSE)
  , ('TestDoc4.html', 'TestURL', FALSE), ('TestDoc5.html', 'TestURL', FALSE), ('TestDoc6.html', 'TestURL', FALSE),
  ('TestDoc7.html', 'TestURL', FALSE)
  , ('TestDoc8.html', 'TestURL', FALSE), ('TestDoc9.html', 'TestURL', FALSE), ('TestDoc10.html', 'TestURL', FALSE),
  ('TestDoc11.html', 'TestURL', FALSE),('TestDoc12.html', 'TestURL', FALSE);
--Edit the Text type, so it consumes less memory
CREATE TABLE public.Word
(
  ID         SERIAL,
  Text       TEXT    NOT NULL,
  DocumentID INTEGER NOT NULL REFERENCES Document (ID)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.Word
  OWNER TO "SearchEngine";