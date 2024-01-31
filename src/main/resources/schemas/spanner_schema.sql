-- Copyright 2024 Google LLC
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     https://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

CREATE SEQUENCE singer_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;
CREATE SEQUENCE album_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;
CREATE SEQUENCE song_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;

CREATE TABLE singers (
  singer_id bigint DEFAULT nextval('singer_id_seq') NOT NULL,
  first_name character varying(1024),
  last_name character varying(1024),
  PRIMARY KEY(singer_id)
);

CREATE TABLE albums (
  singer_id bigint NOT NULL,
  album_id bigint DEFAULT nextval('album_id_seq') NOT NULL,
  album_title character varying,
  PRIMARY KEY(singer_id, album_id)
) INTERLEAVE IN PARENT singers;

CREATE UNIQUE INDEX albums_album_title_key ON albums (album_title) WHERE (album_title IS NOT NULL);

CREATE TABLE songs (
  singer_id bigint NOT NULL,
  album_id bigint NOT NULL,
  song_id bigint DEFAULT nextval('song_id_seq') NOT NULL,
  song_name character varying,
  song_data jsonb,
  PRIMARY KEY(singer_id, album_id, song_id)
) INTERLEAVE IN PARENT albums;
