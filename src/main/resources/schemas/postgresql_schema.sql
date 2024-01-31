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

-- When converting to Spanner:
--   * Table does not contain a primary key column, one must be added
--   * SERIAL type does not exist in Spanner
CREATE TABLE IF NOT EXISTS singers (
  singer_id   SERIAL UNIQUE,
  first_name  VARCHAR(1024),
  last_name   VARCHAR(1024)
);

-- When converting to Spanner:
--   * singer_id has int4 type, which must be converted to int8 in Spanner
--   * SERIAL type does not exist in Spanner
--   * album_title UNIQUE must be converted to an UNIQUE index in Spanner
--   * FOREIGN KEY relation can be converted to Spanner INTERLEAVED table
CREATE TABLE IF NOT EXISTS albums (
  singer_id     int,
  album_id      SERIAL,
  album_title   VARCHAR UNIQUE,
  PRIMARY KEY (singer_id, album_id),
  CONSTRAINT fk_singers
    FOREIGN KEY(singer_id) REFERENCES singers(singer_id)
    ON DELETE CASCADE
);

-- When converting to Spanner:
--   * SERIAL type does not exist in Spanner
--   * song_data json type must be converted to jsonb in Spanner
--   * FOREIGN KEY relation can be converted to Spanner INTERLEAVED table
CREATE TABLE IF NOT EXISTS songs (
  singer_id     int,
  album_id      int,
  song_id       SERIAL,
  song_name     VARCHAR,
  song_data     json,
  PRIMARY KEY (singer_id, album_id, song_id),
  CONSTRAINT fk_albums
    FOREIGN KEY(singer_id, album_id) REFERENCES albums(singer_id, album_id)
    ON DELETE CASCADE
);

-- When converting to Spanner:
--   * Duplicated index, already taken care of by songs primary key
CREATE INDEX IF NOT EXISTS singer_album_song ON songs(singer_id, album_id);
