# Database CUSTOM

You can use a relational database of your choice.

To specify the database, configure the following settings in `BotSettings`:

- `botSettings.setDbType(DbType.CUSTOM)`
- `botSettings.setDbUrl("url")` without the database name. Example: `botSettings.setDbUrl("jdbc:postgresql://localhost:5432/")`
- `botSettings.setDbUser("user")`
- `botSettings.setDbPassword("password")`
- `botSettings.setDbName("name")`
- `botSettings.setDbConnection(connection)`, where `connection` is a JDBC `Connection` to the database.