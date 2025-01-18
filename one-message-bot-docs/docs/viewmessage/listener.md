# Listener

Чтобы создать обработчик событий, необходимо создать класс,
реализующий интерфейс `ViewMessageImpl`, реализовать его методы
и добавить к классу аннотацию `@ViewMessageListener`.
Библиотека выполняет broadcast-рассылку событий классам, помеченным аннотацией `@ViewMessageListener`.

Например:

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

Класс выше реализует эхо-бота.

`ViewMessage` может быть одним из следующих типов:

- `TextMessage` - для отправки текстовых сообщений
- `PhotoMessage` - для отправки фотографий
- `DocumentMessage` - для отправки документов
- `VideoMessage` - для отправки видео
- `AudioMessage` - для отправки аудио
- `InlineKeyboard` - для отправки кнопок в виде клавиатуры

Метод `onMessage` обрабатывает текстовые сообщения от пользователя.
Может использоваться для ввода произвольных данных пользователем.
Для обработки произвольных данных в конкретном обработчике установите
`onMessageFlag` при создании любого типа сообщения.
Он будет сохранён в базу данных и передан в метод `onMessage` в качестве
`onMessageFlag` при возникновении события. `onMessageFlag` автоматически очищается при возникновении события, но после его передачи.

Метод `onCommand` обрабатывает команды от пользователя, начинающиеся с `/`.

Метод `onPicture` обрабатывает изображения, которые присылаются пользователем.

Метод `onCallbackQuery` обрабатывает запросы от кнопок в клавиатуре.