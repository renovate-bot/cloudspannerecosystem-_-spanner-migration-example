CREATE SEQUENCE singer_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;

CREATE TABLE singers (
  singer_id bigint DEFAULT nextval('singer_id_seq'::text) NOT NULL,
  first_name character varying(1024),
  last_name character varying(1024),
  PRIMARY KEY(singer_id)
);

CREATE TABLE albums (
  singer_id bigint NOT NULL,
  album_id bigint NOT NULL,
  album_title character varying,
  PRIMARY KEY(singer_id, album_id)
) INTERLEAVE IN PARENT singers;

CREATE UNIQUE INDEX albums_album_title_key ON albums (album_title) WHERE (album_title IS NOT NULL);

CREATE TABLE songs (
  singer_id bigint NOT NULL,
  album_id bigint NOT NULL,
  song_id bigint NOT NULL,
  song_name character varying,
  song_data jsonb,
  PRIMARY KEY(singer_id, album_id, song_id)
) INTERLEAVE IN PARENT albums;
