# PostgreSQL to Spanner Migration Example

This is the sample application used to exemplify a migration from an open-source PostgreSQL database (CloudSQL) to a Spanner PostgreSQL dialect database.

It uses the open-source JDBC driver for both databases.

## Database Schema

### Open-source PostgreSQL (CloudSQL)

```sql
-- When converting to Spanner:
--   * Table does not contain a primary key column, one must be added
--   * SERIAL type does not exist in Spanner
CREATE TABLE singers (
  singer_id   SERIAL UNIQUE,
  first_name  VARCHAR(1024),
  last_name   VARCHAR(1024)
);

-- When converting to Spanner:
--   * singer_id has int4 type, which must be converted to int8 in Spanner
--   * SERIAL type does not exist in Spanner
--   * album_title UNIQUE must be converted to an UNIQUE index in Spanner
--   * FOREIGN KEY relation can be converted to Spanner INTERLEAVED table
CREATE TABLE albums (
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
CREATE TABLE songs (
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
CREATE INDEX singer_album_song ON songs(singer_id, album_id);
```

### Spanner PostgreSQL

This is the migrated schema on Spanner.

```sql
CREATE SEQUENCE singer_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;
CREATE SEQUENCE album_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;
CREATE SEQUENCE song_id_seq BIT_REVERSED_POSITIVE START COUNTER WITH 1;

CREATE TABLE singers (
  singer_id bigint DEFAULT nextval('singer_id_seq'::text) NOT NULL,
  first_name character varying(1024),
  last_name character varying(1024),
  PRIMARY KEY(singer_id)
);

CREATE TABLE albums (
  singer_id bigint NOT NULL,
  album_id bigint DEFAULT nextval('album_id_seq'::text) NOT NULL,
  album_title character varying,
  PRIMARY KEY(singer_id, album_id)
) INTERLEAVE IN PARENT singers;

CREATE UNIQUE INDEX albums_album_title_key ON albums (album_title) WHERE (album_title IS NOT NULL);

CREATE TABLE songs (
  singer_id bigint NOT NULL,
  album_id bigint NOT NULL,
  song_id bigint DEFAULT nextval('song_id_seq'::text) NOT NULL,
  song_name character varying,
  song_data jsonb,
  PRIMARY KEY(singer_id, album_id, song_id)
) INTERLEAVE IN PARENT albums;
```

## How to Run

### Open-source PostgreSQL (CloudSQL)

First set up a CloudSQL PostgreSQL [instance](https://cloud.google.com/sql/docs/postgres/create-instance), [database](https://cloud.google.com/sql/docs/postgres/create-manage-databases) and [user](https://cloud.google.com/sql/docs/postgres/create-manage-users).

Next copy the `env.cloudsql.sample` into a `.env.cloudsql` file. This will be used by `docker-compose`. In the copied file configure the following information:

```shell
CLOUDSQL_INSTANCE_CONNECTION_NAME=<cloudsql instance connection name>
CLOUDSQL_DATABASE=<database name>
CLOUDSQL_USERNAME=<database username>
CLOUDSQL_PASSWORD=<database password>
```

You can then start the application as follows:

```shell
docker-compose up app-cloudsql
```

You can stop the application like so:

```shell
docker-compose down app-cloudsql
```

### Spanner PostgreSQL

First set up a Spanner [instance](https://cloud.google.com/spanner/docs/create-query-database-console#create-instance) and [PostgreSQL database](https://cloud.google.com/spanner/docs/create-query-database-console#create-database) (don't forget to select the PostgreSQL database dialect).

Next copy the `env.spanner.sample` into a `.env.spanner` file. This will be used by `docker-compose`. In the copied file configure the following information:

```shell
SPANNER_PROJECT=<gcp project-id>
SPANNER_INSTANCE=<spanner instance-id>
SPANNER_DATABASE=<spanner database-id>
```

You can then start the application as follows:

```shell
# Starts the application connecting to Spanner PostgreSQL
docker-compose up -d pgadapter
docker-compose up app-spanner
```
You can stop the application like so:

```shell
docker-compose down pgadapter app-spanner
```
