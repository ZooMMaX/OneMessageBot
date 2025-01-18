# VideoMessage

Представляет собой билдер класса `VideoMessage`, который предназначен для отправки видео.

**Примеры:**

```java
import ru.zoommax.view.VideoMessage;
import ru.zoommax.view.ViewMessage;

public class example {

    public static void main(String[] args) {
        VideoMessage videoMessage = VideoMessage.builder()
                .chatId(123456789)
                .videoAsUrl("https://example.com/video.mp4")
                /*optional
                .videoAsFile(new File("path/to/video.mp4"))
                .videoAsBytes(new byte[]{1,2,3,4,5})
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
                
        videoMessage.run();
    }
}
```

Параметры:

- `chatId` - идентификатор чата
- `videoAsUrl` - ссылка на видео
- `videoAsFile` - файл видео
- `videoAsBytes` - байтовый массив видео
- `caption` - текст
- `keyboard` - клавиатура `Keyboard`
- `onMessageFlag` - флаг, который будет использоваться для обработки сообщений
- `notify` - признак, что сообщение является уведомлением. По умолчанию `false`

Может быть возвращён в качестве `ViewMessage` или запущен с помощью метода `run()`.