# Database Overview

По умолчанию OneMessageBot использует SQLite базу данных, но можно использовать MongoDB
или другую реляционную базу данных через JDBC, указав соответствующие настройки.

Для указания базы данных установите соответствующие настройки в `BotSettings`:

- MongoDB: `botSettings.setDbType(DbType.MONGODB)`
- SQLite: `botSettings.setDbType(DbType.SQLITE)`
- Custom: `botSettings.setDbType(DbType.CUSTOM)`

При использовании `DbType.CUSTOM` необходимо указать:

- URL базы данных: `botSettings.setDbUrl(url)` без имени базы данных
- Пользователь: `botSettings.setDbUser(user)`
- Пароль: `botSettings.setDbPassword(password)`
- Имя базы данных: `botSettings.setDbName(name)`
- JDBC `Connection` `botSettings.setDbConnection(connection)`

[MongoDB documentation](/database/mongodb)

[SQLite documentation](/database/sqlite)

[Custom database documentation](/database/custom)