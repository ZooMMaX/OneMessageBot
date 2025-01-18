# Notifications

В библиотеке реализована отправка уведомлений в чаты.

Для создания уведомления используйте `CreateNotification` класс:

```java
public class example {

    public static void main(String[] args) {
        CreateNotification createNotification = new CreateNotification(
                "Hello notify", // message
                String.valueOf(123456789), // tg_id
                null, // image
                NotificationType.FULL, // notificationType
                null // fileType
        );
        createNotification.run();
    }
}
```

В результате будет создано уведомление с текстом `Hello notify` и отправлено в чат с id 123456789

Параметры:

- `message` - текст уведомления
- `tg_id` - id чата
- `image` - `String` путь к изображению или видео
- `notificationType` - тип уведомления, `FULL` или `ALERT`
- `fileType` - `String` тип файла, `image` или `video`

`NotificationType` определяет тип уведомления:

`FULL` - полное уведомление (показывается сразу, под сообщением размещаются кнопки других не прочитанных уведомлений),

`ALERT` - в чате появляется сообщение о том, что есть не прочитанные уведомления, под которыми размещаются кнопки других не прочитанных уведомлений. Нажатие на кнопку уведомления в чате приводит к открытию всплывающего окна ALERT согласно документации Telegram Bot API.