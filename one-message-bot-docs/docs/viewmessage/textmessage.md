# TextMessage

Represents the builder class `TextMessage`, which is intended for sending text messages.

```java
import space.zoommax.view.TextMessage;

public class example {

    public static void main(String[] args) {
        TextMessage textMessage = TextMessage.builder()
                .text("Hello")
                .chatId(123456789)
                .keyboard(Keyboard.builder()
                        .chatId(123456789)
                        .code("{Google;https://google.ru}{Yandex;https://ya.ru}\n" +
                                "{Start;strt}")
                        .build())
                .onMessageFlag("start")
                .notify(false)
                .build();
        textMessage.run();
    }
}
```

Parameters:

- `text` - the text of the message
- `chatId` - the chat identifier
- `keyboard` - the keyboard in `Keyboard` format
- `onMessageFlag` - the flag used for processing messages
- `notify` - indicates whether the message is a notification. By default, it's `false`

The message can be returned as a `ViewMessage` or run using the `run()` method.