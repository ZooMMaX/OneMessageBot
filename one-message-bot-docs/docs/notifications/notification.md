# Notifications

The library supports sending notifications to chats.

To create a notification, use the `CreateNotification` class:

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

As a result, a notification with the text `Hello notify` will be created and sent to the chat with ID `123456789`.

Parameters:

- `message` - the text of the notification
- `tg_id` - the chat ID
- `image` - a `String` path to the image or video
- `notificationType` - the type of notification, either `FULL` or `ALERT`
- `fileType` - a `String` representing the file type, either `image` or `video`

`NotificationType` defines the type of notification:

- `FULL` - full notification (appears immediately, with buttons for other unread notifications displayed under the message),
- `ALERT` - a message appears in the chat indicating there are unread notifications, with buttons for other unread notifications displayed under it. Clicking on the notification button opens a pop-up ALERT according to the Telegram Bot API documentation.