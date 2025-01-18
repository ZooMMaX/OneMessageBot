# Database SQLite

Built-in support for SQLite.

To configure SQLite, set the appropriate settings in `BotSettings`:

- `botSettings.setDbType(DbType.SQLITE)`
- `botSettings.setDbName("name")`

To specify the database path, set `botSettings.setDbUrl("jdbc:sqlite:./path/to/")`.  
The database name will be automatically applied from `botSettings.setDbName("name")`.