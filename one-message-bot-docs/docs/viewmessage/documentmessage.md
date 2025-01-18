# DocumentMessage

The `DocumentMessage` class builder is designed for sending documents.

**Examples:**

```java
import ru.zoommax.view.DocumentMessage;
import ru.zoommax.view.ViewMessage;

public class example {

    public static void main(String[] args) {
        DocumentMessage documentMessage = DocumentMessage.builder()
                .chatId(123456789)
                .documentAsUrl("https://example.com/document.pdf")
                /*optional
                .documentAsFile(new File("path/to/document.pdf"))
                .documentAsBytes(new byte[]{1,2,3,4,5})
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
                
        documentMessage.run();
    }
}
```

Parameters:

- `documentAsUrl` - URL of the document
- `documentAsFile` - document file
- `documentAsBytes` - byte array of the document
- `chatId` - chat ID
- `caption` - description of the document
- `keyboard` - the keyboard `Keyboard`
- `onMessageFlag` - flag used for message processing
- `notify` - indicates whether the message is a notification. Default is `false`.

It can be returned as a `ViewMessage` or executed using the `run()` method.