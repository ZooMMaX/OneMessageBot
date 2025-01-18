# Listener

To create an event handler, you need to create a class that implements the `ViewMessageImpl` interface, implement its methods, and add the `@ViewMessageListener` annotation to the class. The library performs a broadcast of events to the classes marked with the `@ViewMessageListener` annotation.

For example:

```java
import ru.zoommax.utils.ViewMessageListener;
import ru.zoommax.view.TextMessage;

@ViewMessageListener
public class Start implements ViewMessageImpl {

    @Override
    public ViewMessage onMessage(String message, int messageId, long chatId, String onMessageFlag, Update update) {
        return TextMessage.builder()
                .text(message)
                .chatId(chatId)
                .build();
    }

    @Override
    public ViewMessage onCommand(String command, int messageId, long chatId, Update update) {
        return null;
    }

    @Override
    public ViewMessage onPicture(PhotoSize[] photoSize, String caption, int messageId, long chatId, Update update) {
        return null;
    }

    @Override
    public ViewMessage onCallbackQuery(String data, int messageId, long chatId, Update update) {
        return null;
    }

    @Override
    public ViewMessage onInlineQuery(String query, String queryId, long chatId, Update update) {
        return null;
    }

    @Override
    public ViewMessage onChosenInlineResult(String resultId, long queryId, String chatId, Update update) {
        return null;
    }
}
```

The above class implements an echo bot.

The `ViewMessage` can be one of the following types:

- `TextMessage` - for sending text messages
- `PhotoMessage` - for sending photos
- `DocumentMessage` - for sending documents
- `VideoMessage` - for sending videos
- `AudioMessage` - for sending audio
- `InlineKeyboard` - for sending inline keyboard buttons

The `onMessage` method handles text messages from the user and can be used for custom data input by the user. To process custom data in a specific handler, set the `onMessageFlag` when creating any type of message. It will be saved in the database and passed to the `onMessage` method as `onMessageFlag` when the event occurs. The `onMessageFlag` is automatically cleared when the event occurs but is passed as part of the event.

The `onCommand` method handles commands from the user that start with `/`.

The `onPicture` method handles images sent by the user.

The `onCallbackQuery` method handles requests from keyboard buttons.