# Localization

Built-in localization support.  
By default, English is used as the language. The library supports two localizations: `default_en_US` for English and `default_ru_RU` for Russian.

To set the localization, configure the corresponding settings in `BotSettings`:

- `botSettings.setDefaultLanguage("default_en_US")` - sets the default language. You can use `default_en_US`, `default_ru_RU`, or your custom localization.
- `botSettings.setLanguageDirPath("/path/to/translations")` - sets the path to the directory with translations.

### default_en_US

This is the localization template for the English language. When creating localizations, this template is used to modify the translation inside the library.

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

### Localization Creation Rules

1. All keys should be in lowercase, with words separated by underscores (`_`).
2. All values must be in string format.
3. Use nesting.
4. Files should be in `json` format and extension.

### Using Localization in Your Code

Set the `defaultLanguage` and `languageDirPath` parameters in `BotSettings`.  
Start the bot. The language name will be the same as the name of the translation file.

```java
import ru.zoommax.BotApp;
import ru.zoommax.utils.lang.LocalizationManager;

LocalizationManager localizationManager = BotApp.localizationManager;
String botStart = localizationManager.getTranslationForLanguage(BotApp.getUserLanguage("chatId"), "main.bot_starting");
```

Access nested keys by passing a string, where the separator between keys is the period (`.`) character.

The method `BotApp.getUserLanguage(String chatId)` returns the user's language name, which is saved in the database.

The method `BotApp.setUserLanguage(String chatId, String language)` sets the user's language and saves it in the database.  
`String language` - the language name, which matches the translation file name in the translation directory.

`LocalizationManager` can be used as the main translation manager in your project.