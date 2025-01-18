# PhotoMessage

Представляет собой билдер класса `PhotoMessage`, который предназначен для отправки изображений.

```java
import ru.zoommax.view.PhotoMessage;
import ru.zoommax.view.ViewMessage;

public class example {

    public static void main(String[] args) {
        PhotoMessage photoMessage = PhotoMessage.builder()
                .chatId(123456789)
                .photoAsUrl("https://example.com/image.jpg")
                /*optional
                .photoAsFile(new File("path/to/image.jpg"))
                .photoAsBytes(new byte[]{1,2,3,4,5})
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

        photoMessage.run();        
    }
}
```

Параметры:

- `chatId` - идентификатор чата
- `photoAsUrl` - URL изображения
- `photoAsFile` - файл изображения
- `photoAsBytes` - байты изображения
- `keyboard` - клавиатура в виде `Keyboard`
- `caption` - описание изображения
- `onMessageFlag` - флаг, который будет использоваться для обработки сообщений
- `notify` - признак, что сообщение является уведомлением. По умолчанию `false`

Может быть возвращён в качестве `ViewMessage` или запущен с помощью метода `run()`.