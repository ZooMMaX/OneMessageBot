# Database CUSTOM

Вы можете использовать реляционную базу данных по своему усмотрению.

Для указания базы данных установите соответствующие настройки в `BotSettings`:

- `botSettings.setDbType(DbType.CUSTOM)`
- `botSettings.setDbUrl("url")` без имени базы данных. Пример: `botSettings.setDbUrl("jdbc:postgresql://localhost:5432/")`
- `botSettings.setDbUser("user")`
- `botSettings.setDbPassword("password")`
- `botSettings.setDbName("name")`
- `botSettings.setDbConnection(connection)`, где `connection` - JDBC `Connection` с подключением к базе данных

