# PostgreSQL to Spanner Migration Example

Application to exemplify migration from a PostgreSQL database to Spanner (using the PostgreSQL dialect).

This application is divided into three self-contained projects, to better exemplify the changes necessary in each step. The projects are:

* [before-migration](before-migration): contains an application that uses CloudSQL PostgreSQL.
* [during-migration](during-migration): contains an application that can use either CloudSQL PostgreSQL and Spanner PostgreSQL.
* [after-migration](after-migration): contains an application that can use Spanner PostgreSQL.

See the README in each of these projects to learn how to run each scenario.

