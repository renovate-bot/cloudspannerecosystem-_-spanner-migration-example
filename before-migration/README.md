# PostgreSQL to Spanner Migration Example - Before Migrating to Spanner

This application exemplifies a connection with CloudSQL PostgreSQL using the PostgreSQL JDBC driver.

The application operates over the following database schema:

```sql
CREATE TABLE singers (
  singer_id   SERIAL UNIQUE,
  first_name  VARCHAR(1024),
  last_name   VARCHAR(1024)
);

CREATE TABLE albums (
  singer_id     int,
  album_id      SERIAL,
  album_title   VARCHAR UNIQUE,
  PRIMARY KEY (singer_id, album_id),
  CONSTRAINT fk_singers
    FOREIGN KEY(singer_id) REFERENCES singers(singer_id)
    ON DELETE CASCADE
);

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

CREATE INDEX singer_album_song ON songs(singer_id, album_id);
```

The application simply inserts new records for the 3 tables above in a loop every second and prints out the inserted rows.

## How to Run

First set up a CloudSQL PostgreSQL [instance](https://cloud.google.com/sql/docs/postgres/create-instance), [database](https://cloud.google.com/sql/docs/postgres/create-manage-databases) and [user](https://cloud.google.com/sql/docs/postgres/create-manage-users).

Next copy the `env.sample` into a `.env` file. This will be used by `docker-compose`. In the copied file configure the following information:

```shell
CLOUDSQL_INSTANCE_CONNECTION_NAME=<cloudsql instance connection name>
CLOUDSQL_DATABASE=<database name>
CLOUDSQL_USERNAME=<database username>
CLOUDSQL_PASSWORD=<database password>
```

You can then start the application as follows:

```shell
docker-compose up cloudsql
```

