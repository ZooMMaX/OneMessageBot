# VideoMessage

Represents the builder class `VideoMessage`, which is intended for sending videos.

**Examples:**

```java
import space.zoommax.view.VideoMessage;

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

Parameters:

- `chatId` - the chat identifier
- `videoAsUrl` - the URL of the video
- `videoAsFile` - the video file
- `videoAsBytes` - the byte array of the video
- `caption` - the caption text
- `keyboard` - the keyboard in `Keyboard` format
- `onMessageFlag` - the flag used for processing messages
- `notify` - indicates whether the message is a notification. By default, it's `false`

The message can be returned as a `ViewMessage` or run using the `run()` method.