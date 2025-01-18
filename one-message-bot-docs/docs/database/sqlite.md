# Database SQLite

Встроенная поддержка SQLite.

Для указания SQLite установите соответствующие настройки в `BotSettings`:

- `botSettings.setDbType(DbType.SQLITE)`
- `botSettings.setDbName("name")`

Чтобы настроить путь к базе данных, установите `botSettings.setDbUrl("jdbc:sqlite:./path/to/")`,
имя базы данных будет использоваться автоматически из `botSettings.setDbName("name")`.