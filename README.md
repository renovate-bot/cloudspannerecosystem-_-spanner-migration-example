# PostgreSQL to Spanner Migration Example - During Migration to Spanner

This application exemplifies the updates required to make the application in [before-migration](../before-migration) to work with both CloudSQL PostgreSQL and Spanner PostgreSQL.

## Schema

The application operates over the same schema as [before-migration](../before-migration) for CloudSQL PostgreSQL. The updated schema for Spanner is the following:

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

This assumes you have gone through the [before-migration](../before-migration) setup to initialize a CloudSQL database, so we won't cover those details here.

First set up a Spanner [instance](https://cloud.google.com/spanner/docs/create-query-database-console#create-instance) and [PostgreSQL database](https://cloud.google.com/spanner/docs/create-query-database-console#create-database) (don't forget to select the PostgreSQL database dialect).

Next update the `env.sample` into a `.env` file. This will be used by `docker-compose`. In the copied file configure the following information:

```shell
CLOUDSQL_INSTANCE_CONNECTION_NAME=<cloudsql instance connection name>
CLOUDSQL_DATABASE=<database name>
CLOUDSQL_USERNAME=<database username>
CLOUDSQL_PASSWORD=<database password>

SPANNER_PROJECT=<gcp project-id>
SPANNER_INSTANCE=<spanner instance-id>
SPANNER_DATABASE=<spanner database-id>
```

You can then start the application as follows:

```shell
# Starts the application connecting to Spanner PostgreSQL
docker-compose up pgadapter spanner

# Otherwise, you can start the original application connecting to CloudSQL PostgreSQL
# Stop previous containers
docker-compose stop pgadapter spanner
# Start the application with CloudSQL
docker-compose up cloudsql
```
