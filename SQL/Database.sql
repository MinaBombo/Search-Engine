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
  ID   SERIAL UNIQUE,
  Path CHARACTER VARYING(255) NOT NULL,
  URL  CHARACTER VARYING(255)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.Document
  OWNER TO "SearchEngine";

CREATE TABLE public.Word
(
  ID         SERIAL,
  Text       CHARACTER VARYING(80) NOT NULL,
  DocumentID INTEGER               NOT NULL REFERENCES Document (ID)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.Word
  OWNER TO "SearchEngine";