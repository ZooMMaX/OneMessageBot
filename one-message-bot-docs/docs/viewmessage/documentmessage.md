# DocumentMessage

Представляет собой билдер класса `DocumentMessage`, который предназначен для отправки документов.

**Примеры:**

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

Параметры:

- `documentAsUrl` - URL-адрес документа
- `documentAsFile` - файл документа
- `documentAsBytes` - массив байтов документа
- `chatId` - идентификатор чата
- `caption` - описание документа
- `keyboard` - клавиатура `Keyboard`
- `onMessageFlag` - флаг, который будет использоваться для обработки сообщений
- `notify` - признак, что сообщение является уведомлением. По умолчанию `false`

Может быть возвращён в качестве `ViewMessage` или запущен с помощью метода `run()`.