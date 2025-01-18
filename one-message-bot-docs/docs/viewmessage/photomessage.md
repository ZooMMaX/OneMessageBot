# PhotoMessage

Represents the builder class `PhotoMessage`, which is intended for sending images.

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

Parameters:

- `chatId` - the chat identifier
- `photoAsUrl` - the URL of the image
- `photoAsFile` - the image file
- `photoAsBytes` - the bytes of the image
- `keyboard` - the keyboard in the `Keyboard` format
- `caption` - the description of the image
- `onMessageFlag` - the flag used for processing messages
- `notify` - indicates whether the message is a notification. By default, it's `false`

The message can be returned as a `ViewMessage` or run using the `run()` method.