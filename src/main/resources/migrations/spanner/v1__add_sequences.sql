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

-- WARNING:
-- Ideally you should also set the SKIP RANGE of the following sequences not to
-- conflict with existing SERIAL values from the PostgreSQL database.

CREATE SEQUENCE singer_id_seq BIT_REVERSED_POSITIVE;
ALTER TABLE singers ALTER COLUMN singer_id SET DEFAULT nextval('singer_id_seq');

CREATE SEQUENCE album_id_seq BIT_REVERSED_POSITIVE;
ALTER TABLE albums ALTER COLUMN album_id SET DEFAULT nextval('album_id_seq');

CREATE SEQUENCE song_id_seq BIT_REVERSED_POSITIVE;
ALTER TABLE songs ALTER COLUMN song_id SET DEFAULT nextval('song_id_seq');
