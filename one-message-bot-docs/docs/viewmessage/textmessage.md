# TextMessage

Представляет собой билдер класса `TextMessage`, который предназначен для отправки текстовых сообщений.

```java
import ru.zoommax.view.TextMessage;

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

Параметры:

- `text` - текст сообщения
- `chatId` - идентификатор чата
- `keyboard` - клавиатура `Keyboard`
- `onMessageFlag` - флаг, который будет использоваться для обработки сообщений
- `notify` - признак, что сообщение является уведомлением. По умолчанию `false`

Может быть возвращён в качестве `ViewMessage` или запущен с помощью метода `run()`.