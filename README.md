# PostgreSQL to Spanner Migration Example

This is the sample application used to exemplify a migration from an open-source PostgreSQL database (Cloud SQL) to a Spanner PostgreSQL dialect database.

It uses the open-source JDBC driver for both databases.

## Database Schema

### Open-source PostgreSQL (Cloud SQL)

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

## Application Updates

The original application, which worked only with the open-source PostgreSQL database, had to be updated to work with Spanner PostgreSQL dialect as well.

Below we go over the necessary modifications.

First, we parameterized the application to boot up using either Cloud SQL or Spanner. For this purpose we introduced the [DatabaseChoice](src/main/java/com/google/DatabaseChoice.java) abstraction. In a production scenario we would most likely have used a feature flag here.
We modified our [docker-compose.yml](docker-compose.yml) file to input the database choice on initialization:

```yaml
services:
  app-cloudsql
    ...
    command: [ "java", "-jar", "app.jar", "cloudsql" ]
  app-spanner:
    ...
    command: [ "java", "-jar", "app.jar", "spanner" ]
```

Secondly, we proceeded to set up a Spanner connection. In order to keep using the PostgreSQL drivers in our application (instead of Spanner specific drivers), we had to configure [PGAdapter](https://github.com/GoogleCloudPlatform/pgadapter/tree/postgresql-dialect?tab=readme-ov-file#google-cloud-spanner-pgadapter). PGAdapter serves as a proxy that translates the PostgreSQL wire-protocol into the equivalent for Spanner databases that use the PostgreSQL interface. Our application connects to PGAdapter instead of Spanner. There are [multiple ways to use PGAdapter](https://cloud.google.com/spanner/docs/pgadapter#execution-env). We used the [distroless Docker image](https://github.com/GoogleCloudPlatform/pgadapter/tree/postgresql-dialect?tab=readme-ov-file#distroless-docker-image), as it is independent of the implementation of our application. The changes are reflected in our [docker-compose.yml](docker-compose.yml) file:

```yaml
services:
  ...
  pgadapter:
    image: gcr.io/cloud-spanner-pg-adapter/pgadapter-distroless
    ...
    
  app-spanner:
    depends_on:
      - pgadapter
    command: [ "java", "-jar", "app.jar", "spanner" ]
    ...
```

With that, we configured the connection of JDBC to PGAdapter, by following this [guide](https://github.com/GoogleCloudPlatform/pgadapter/blob/postgresql-dialect/docs/jdbc.md).

Next, we inspected the in memory representation of our query results, since some types were migrated in the Spanner schema. As a reminder, our Spanner schema used `jsonb`s instead of `json`s and `int8`s instead of `int4`s. When handling `json` values, we were using Java `String`s, so we didn’t need any modifications here as `jsonb` objects can be stored in `String`s. On the other hand, when handling `int4` values, we were storing them into memory using the primitive `int` type. This domain is smaller than the new `int8` column type, so we need to make sure we use primitive `long`s instead.

Finally, we had to take a look at the queries we were issuing. The only problem identified was that we are performing a `CAST` from `text` to `json` when inserting the `song_data` column for a [Song](src/main/java/com/google/models/Song.java). Since we are using the `jsonb` type now, we need to modify the casting accordingly.

```sql
-- PostgreSQL
INSERT INTO songs (singer_id, album_id, song_id, song_name, song_data)
VALUES (?, ?, DEFAULT, ?, CAST(? AS JSON))
RETURNING song_id;

-- Updated (Spanner)
INSERT INTO songs (singer_id, album_id, song_id, song_name, song_data)
VALUES (?, ?, DEFAULT, ?, CAST(? AS JSONB))
RETURNING song_id
```

## How to Run

### Open-source PostgreSQL (Cloud SQL)

First set up a Cloud SQL PostgreSQL [instance](https://cloud.google.com/sql/docs/postgres/create-instance), [database](https://cloud.google.com/sql/docs/postgres/create-manage-databases) and [user](https://cloud.google.com/sql/docs/postgres/create-manage-users).

Next copy the `env.sample` into a `.env` file. This will be used by `docker-compose`. In the copied file configure the following information:

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

Next copy the `env.sample` into a `.env` file. This will be used by `docker-compose`. In the copied file configure the following information:

```shell
SPANNER_PROJECT=<gcp project-id>
SPANNER_INSTANCE=<spanner instance-id>
SPANNER_DATABASE=<spanner database-id>
```

You can then start the application as follows:

```shell
docker-compose up app-spanner
```
You can stop the application like so:

```shell
docker-compose down pgadapter app-spanner
```
