# AudioMessage

Представляет собой билдер класса `AudioMessage`, который предназначен для отправки аудиофайлов.

**Примеры:**

```java
import ru.zoommax.view.AudioMessage;
import ru.zoommax.view.ViewMessage;

public class example {

    public static void main(String[] args) {
        AudioMessage audioMessage = AudioMessage.builder()
                .chatId(123456789)
                .audioAsUrl("https://example.com/audio.mp3")
                /*optional
                .audioAsFile(new File("path/to/audio.mp3"))
                .audioAsBytes(new byte[]{1,2,3,4,5})
                 */
                .keyboard(Keyboard.builder()
                        .chatId(123456789)
                        .code("{Google;https://google.ru}{Yandex;https://ya.ru}\n" +
                                "{Start;strt}")
                        .build())
                .caption("Hello")
                .onMessageFlag("start")
                .notify(false)
                .build();
                
        audioMessage.run();
    }
}
```

Параметры:

- `chatId` - идентификатор чата
- `audioAsUrl` - URL-адрес аудиофайла
- `audioAsFile` - аудиофайл
- `audioAsBytes` - байты аудиофайла
- `caption` - подпись к аудиофайлу
- `keyboard` - клавиатура `Keyboard`
- `onMessageFlag` - флаг, который будет использоваться для обработки сообщений
- `notify` - признак, что сообщение является уведомлением. По умолчанию `false`

Может быть возвращён в качестве `ViewMessage` или запущен с помощью метода `run()`.