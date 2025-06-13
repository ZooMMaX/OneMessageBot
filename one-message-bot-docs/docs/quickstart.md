# Quickstart

## Add a dependency to your project

Maven:
```xml
<dependency>
    <groupId>space.zoommax</groupId>
    <artifactId>OneMessageBot</artifactId>
    <version>2.1.4.7</version>
</dependency>
```

Gradle:
```groovy
implementation 'space.zoommax:OneMessageBot:2.1.4.7'
```

## Create a bot

### Configure bot settings

```java
package test.cl;

import space.zoommax.BotSettings;
import space.zoommax.utils.db.DbType;

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

- `botToken` - Bot token obtained from @BotFather. Required.
- `dbType` - Database type. Optional. Can be `DbType.SQLITE`, `DbType.MONGODB`, or `DbType.CUSTOM`. Default: `DbType.SQLITE`.
- `buttonsRows` - Number of button rows. Optional. Default: 4.
- `dbName` - Database name. Optional. Default: `BotApp`.
- `languageDirPath` - Path to the directory with translations. Optional. Default: empty string.
- `defaultLanguage` - Default language. Optional. Default: `default_en_US`.
- `dbConnection` - Database connection. Optional. Default: `null`.
- `dbUrl` - Database connection URL. Optional. Default: `null`.
- `dbUser` - Database connection username. Optional. Default: empty string.
- `dbPassword` - Database connection password. Optional. Default: empty string.
- `disableGithubUrl` - Details can be found in the [LICENSE](https://github.com/ZooMMaX/OneMessageBot/blob/master/LICENSE) file.

### Run the bot

```java
package test.cl;

import space.zoommax.BotApp;

public class Main {
    public static void main(String[] args) {
        //BotSettings botSettings = BotSettings.builder()
        // ...
        // .build();

        new Thread(new BotApp(botSettings)).start();
    }
}
```

### Create a "window"

```java
package test.cl;

import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import space.zoommax.BotApp;
import space.zoommax.utils.CreateNotification;
import space.zoommax.utils.ViewMessageImpl;
import space.zoommax.utils.ViewMessageListener;
import space.zoommax.utils.db.NotificationType;
import space.zoommax.utils.keyboard.Keyboard;
import space.zoommax.view.TextMessage;
import space.zoommax.view.ViewMessage;

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
            if (chatId == 000000) {
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