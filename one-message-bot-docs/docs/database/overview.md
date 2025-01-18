# Database Overview

By default, OneMessageBot uses an SQLite database, but you can switch to MongoDB or any other relational database via JDBC by configuring the appropriate settings.

To specify the database, set the following options in `BotSettings`:

- MongoDB: `botSettings.setDbType(DbType.MONGODB)`
- SQLite: `botSettings.setDbType(DbType.SQLITE)`
- Custom: `botSettings.setDbType(DbType.CUSTOM)`

When using `DbType.CUSTOM`, the following must be specified:

- Database URL: `botSettings.setDbUrl(url)` without the database name
- User: `botSettings.setDbUser(user)`
- Password: `botSettings.setDbPassword(password)`
- Database name: `botSettings.setDbName(name)`
- JDBC `Connection`: `botSettings.setDbConnection(connection)`

[MongoDB documentation](/database/mongodb)  
[SQLite documentation](/database/sqlite)  
[Custom database documentation](/database/custom)