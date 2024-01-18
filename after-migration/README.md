# PostgreSQL to Spanner Migration Example - After Migrating to Spanner

This application exemplifies the application updated to work with Spanner PostgreSQL only. The only changes made here are that we cleaned up the code from [during-migration](../during-migration) to remove CloudSQL specific application logic.

## Schema

The application operates over the same schema as [during-migration](../during-migration) for Spanner PostgreSQL.

## How to Run

This assumes you have gone through the [during-migration](../during-migration) setup to initialize a Spanner PostgreSQL database, so we won't cover those details here.

You can then start the application as follows:

```shell
# Starts the application connecting to Spanner PostgreSQL
docker-compose up spanner
```
