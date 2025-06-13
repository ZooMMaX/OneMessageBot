# InlineKeyboard

The `InlineKeyboard` class builder is designed for sending buttons in the form of a keyboard.

**Examples:**

```java
import space.zoommax.view.InlineKeyboard;

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

Parameters:

- `chatId` - chat ID
- `code` - keyboard code

It can be returned as a `ViewMessage` or executed using the `run()` method.