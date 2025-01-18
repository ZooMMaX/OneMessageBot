# Localization

Встроенная поддержка локализации.
По умолчанию используется английский язык.
Библиотека имеет две локализации: `default_en_US` - английский, `default_ru_RU` - русский.

Для указания локализации установите соответствующие настройки в `BotSettings`:

- `botSettings.setDefaultLanguage("default_en_US")` - устанавливает язык по умолчанию. Можно использовать `default_en_US`, `default_ru_RU` или собственную локализацию.
- `botSettings.setLanguageDirPath("/path/to/translations")` - устанавливает путь к каталогу с переводами

### default_en_US

Это шаблон локализации для английского языка. При создании локализаций, для изменения перевода внутри библиотеки используется этот шаблон.

```json
{
  "main": {
    "bot_starting": "Bot starting...",
    "notification": "You have unread notifications",
    "notify_close": "Close"
  },
  "keyboard": {
    "next_button": "➡️",
    "prev_button": "⬅️"
  }
}
```

### Правила создания локализации

1. Все ключи должны быть в нижнем регистре, слова разделяются символом `_`.
2. Все значения должны быть в строковом формате
3. Используйте вложенность.
4. Файлы должны иметь формат и расширение `json`

### Использование локализации в Вашем коде

Установите в `BotSettings` параметры `defaultLanguage` и `languageDirPath`.
Запустите бота. Название языка будет таким же, как и название файла с переводами.

```java
import ru.zoommax.BotApp;
import ru.zoommax.utils.lang.LocalizationManager;

LocalizationManager localizationManager = BotApp.localizationManager;
String botStart = localizationManager.getTranslationForLanguage(BotApp.getUserLanguage("chatId"), "main.bot_starting");
```

Доступ к вложенным ключам производится путём передачи строки,
где разделителем между ключами является символ `.`

Метод `BotApp.getUserLanguage(String chatId)` возвращает название языка пользователя, сохранённое в базе данных.

Метод `BotApp.setUserLanguage(String chatId, String language)` устанавливает язык пользователя, сохраняя его в базе данных.
`String language` - название языка, которое совпадает с названием файла перевода в каталоге переводов.

`LocalizationManager` можно использовать, как основной менеджер переводов в Вашем проекте.