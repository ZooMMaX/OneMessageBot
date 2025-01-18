# Quickstart

## Добавьте зависимость в Ваш проект

Maven:
```xml
<dependency>
    <groupId>ru.zoommax</groupId>
    <artifactId>OneMessageBot</artifactId>
    <version>1.0</version>
</dependency>
```

Gradle:
```groovy
implementation 'ru.zoommax:OneMessageBot:1.0'
```

## Создание бота

### Указать настройки бота

```java
package test.cl;

import ru.zoommax.BotApp;
import ru.zoommax.BotSettings;
import ru.zoommax.utils.db.DbType;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BotSettings botSettings = BotSettings.builder()
                .botToken("XXXX:yyyyyy")
                .dbType(DbType.SQLITE)
                .buttonsRows(5)
                .dbName("TestBotApp")
                .languageDirPath("translations")
                .defaultLanguage("my_default_lang")
                .dbConnection(null)
                .dbUrl("jdbc:sqlite:./")
                .dbUser("")
                .dbPassword("")
                .build();
    }
}
```

- `botToken` - токен бота, полученный у @BotFather. Обязательно.
- `dbType` - тип базы данных. Необязательно. Может быть `DbType.SQLITE`, `DbType.MONGODB` или `DbType.CUSTOM`. По умолчанию `DbType.SQLITE`.
- `buttonsRows` - количество строк кнопок. Необязательно. По умолчанию 4.
- `dbName` - имя базы данных. Необязательно. По умолчанию `BotApp`.
- `languageDirPath` - путь к каталогу с переводами. Необязательно. По умолчанию пустая строка.
- `defaultLanguage` - язык по умолчанию. Необязательно. По умолчанию `default_en_US`.
- `dbConnection` - подключение к базе данных. Необязательно. По умолчанию `null`.
- `dbUrl` - URL подключения к базе данных. Необязательно. По умолчанию `null`.
- `dbUser` - имя пользователя подключения к базе данных. Необязательно. По умолчанию пустая строка.
- `dbPassword` - пароль подключения к базе данных. Необязательно. По умолчанию пустая строка.
- `disableGithubUrl` - подробности в файле [LICENSE](https://github.com/ZooMMaX/OneMessageBot/blob/master/LICENSE).

### Запустите бота

```java
package test.cl;

import ru.zoommax.BotApp;

public class Main {
    public static void main(String[] args) {
        //BotSettings botSettings = BotSettings.builder()
        // ...
        // .build();

        new Thread(new BotApp(botSettings)).start();
    }
}
```

### Создайте "окно"

```java
package test.cl;

import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import ru.zoommax.BotApp;
import ru.zoommax.utils.CreateNotification;
import ru.zoommax.utils.ViewMessageImpl;
import ru.zoommax.utils.ViewMessageListener;
import ru.zoommax.utils.db.NotificationType;
import ru.zoommax.utils.keyboard.Keyboard;
import ru.zoommax.view.TextMessage;
import ru.zoommax.view.ViewMessage;

@ViewMessageListener
public class Start implements ViewMessageImpl {
    @Override
    public ViewMessage onMessage(String message, int messageId, long chatId, String onMessageFlag, Update update) {
        if (onMessageFlag.equals("start")) {
            return TextMessage.builder()
                    .onMessageFlag("start")
                    .text("Hello " + message)
                    .chatId(chatId)
                    .keyboard(Keyboard.builder()
                            .chatId(chatId)
                            .code("{Google;https://google.com}{Yandex;https://ya.ru}\n" +
                                    "{Start;strt}\n" +
                                    "{Google;https://google.com}{Yandex;https://ya.ru}\n" +
                                    "{Start;strt}\n" +
                                    "{Google;https://google.com}{Yandex;https://ya.ru}\n" +
                                    "{Start;strt}\n" +
                                    "{Google;https://google.com}{Yandex;https://ya.ru}\n" +
                                    "{Start;strt}")
                            .build())
                    .build();
        }
        return null;
    }

    @Override
    public ViewMessage onCommand(String command, int messageId, long chatId, Update update) {
        if (command.equals("/start")) {
            if (chatId == 000000){
                BotApp.setUserLanguage(String.valueOf(chatId), "default_en_US");
            }
            return TextMessage.builder()
                    .onMessageFlag("start")
                    .text("Hello")
                    .chatId(chatId)
                    .keyboard(Keyboard.builder()
                            .chatId(chatId)
                            .code("{Google;https://google.com}{Yandex;https://ya.ru}\n" +
                                    "{Start;strt}")
                            .build())
                    .build();
        }
        return null;
    }

    @Override
    public ViewMessage onPicture(PhotoSize[] photoSize, String caption, int messageId, long chatId, Update update) {
        return null;
    }

    @Override
    public ViewMessage onCallbackQuery(String data, int messageId, long chatId, Update update) {
        if (data.equals("strt")) {
            CreateNotification createNotification = new CreateNotification("Hello callback notify", String.valueOf(chatId), null, NotificationType.FULL, null);
            createNotification.run();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return TextMessage.builder()
                    .onMessageFlag("start")
                    .text("Hello callback")
                    .chatId(chatId)
                    .keyboard(Keyboard.builder()
                            .chatId(chatId)
                            .code("{Google;https://google.com}{Yandex;https://ya.ru}\n" +
                                    "{Start;strt}")
                            .build())
                    .build();
        }
        return null;
    }

    @Override
    public ViewMessage onInlineQuery(String query, String queryId, long chatId, Update update) {
        return null;
    }

    @Override
    public ViewMessage onChosenInlineResult(String resultId, long queryId, String chatId, Update update) {
        return null;
    }
}
```