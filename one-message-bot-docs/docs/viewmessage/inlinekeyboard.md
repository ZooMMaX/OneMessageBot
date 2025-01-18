# InlineKeyboard

Представляет собой билдер класса `InlineKeyboard`, который предназначен для отправки кнопок в виде клавиатуры.

**Примеры:**

```java
import ru.zoommax.view.InlineKeyboard;
import ru.zoommax.view.ViewMessage;

public class example {

    public static void main(String[] args) {
        InlineKeyboard inlineKeyboard = InlineKeyboard.builder()
                .chatId(123456789)
                .code("{Google;https://google.ru}{Yandex;https://ya.ru}\n" +
                        "{Start;strt}")
                .build();
        
        inlineKeyboard.run();
    }
}
```

Параметры:

- `chatId` - идентификатор чата
- `code` - код клавиатуры

Может быть возвращён в качестве `ViewMessage` или запущен с помощью метода `run()`.