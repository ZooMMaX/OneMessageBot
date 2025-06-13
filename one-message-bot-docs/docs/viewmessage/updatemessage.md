# Updating a Message by Time

Messages can be updated at a specified time.

To do this, create a `ViewMessage` that represents the message to which the original message will be replaced.

```java
ViewMessage viewMessage = TextMessage.builder()
    .text("Hello, world!")
    .chatId(123456789)
    .build();
```

Then, create a message that will serve as the original for the update and either return it or call its `run()` method.

```java
import space.zoommax.view.TextMessage;

class Example {
    public static void main(String[] args) {
        ViewMessage viewMessage = TextMessage.builder()
                .text("Hello")
                .chatId(123456789)
                .viewMessageToUpdate(viewMessage.toString())
                .needUpdate(true)
                .updateTime(System.currentTimeMillis() + 10000)
                .build();
        viewMessage.run();
    }
} 
```

- `viewMessageToUpdate` - the message that will replace the original one. Takes a `serialized String` from `ViewMessage` as input.
- `needUpdate` - a flag indicating whether an update is needed.
- `updateTime` - the update time in milliseconds.

Note that if the original message has `notify = true`, the update will not occur. Also, the message will not be updated if the original message was modified before the update time.