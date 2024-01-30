-- WARNING:
-- Ideally you should also set the SKIP RANGE of the following sequences not to
-- conflict with existing SERIAL values from the PostgreSQL database.

CREATE SEQUENCE singer_id_seq BIT_REVERSED_POSITIVE;
ALTER TABLE singers ALTER COLUMN singer_id SET DEFAULT nextval('singer_id_seq');

CREATE SEQUENCE album_id_seq BIT_REVERSED_POSITIVE;
ALTER TABLE albums ALTER COLUMN album_id SET DEFAULT nextval('album_id_seq');

CREATE SEQUENCE song_id_seq BIT_REVERSED_POSITIVE;
ALTER TABLE songs ALTER COLUMN song_id SET DEFAULT nextval('song_id_seq');
