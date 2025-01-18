# AudioMessage

The `AudioMessage` class builder is designed for sending audio files.

**Examples:**

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

Parameters:

- `chatId` - the chat ID
- `audioAsUrl` - the URL of the audio file
- `audioAsFile` - the audio file
- `audioAsBytes` - the bytes of the audio file
- `caption` - the caption for the audio file
- `keyboard` - the keyboard `Keyboard`
- `onMessageFlag` - the flag used for message processing
- `notify` - indicates whether the message is a notification. Default is `false`.

It can be returned as a `ViewMessage` or executed using the `run()` method.