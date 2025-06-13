# Keyboard
**Implementation of a Keyboard in the Application (Bot)**

Keyboards in Telegram consist of an array of [Button](button.md) objects.

You can create a keyboard using the `builder` class `Keyboard`.

In the `builder`, you need to specify:

- `chatId` - the ID of the chat where the keyboard will be displayed

and either:

- `code` - formatted keyboard code

or:

- `keyboardButtons` - a list of keyboard buttons

In a formatted string, the `\n` separator is used to create a new row of buttons.

---

## Examples

### Setting the Keyboard via `code`

Buttons will be arranged vertically.

```java
import space.zoommax.utils.keyboard.Keyboard;

Keyboard keyboard = Keyboard.builder()
        .chatId(123456789)
        .code("{I will open a link;http://example.com}\n{I will perform a callback action;any_data}")
        .build();
```

Two buttons in one row and a third button below them.

```java
import space.zoommax.utils.keyboard.Keyboard;

Keyboard keyboard = Keyboard.builder()
        .chatId(123456789)
        .code("{I will open a link;http://example.com}{I will perform a callback action;any_data}\n{I will also open a link;tg://username}")
        .build();
```

You can also add any characters outside of `{` and `}` to visually separate buttons in the code. All characters outside of `{` and `}` (except `\n`) will be ignored.

```java
import space.zoommax.utils.keyboard.Keyboard;

Keyboard keyboard = Keyboard.builder()
        .chatId(123456789)
        .code("{I will open a link;http://example.com}{I will perform a callback action;any_data}\n" +
                "_____>!@_________{I will also open a link;tg://username}______acfd_______")
        .build();
```

---

### Setting the Keyboard via `keyboardButtons`

Buttons will be arranged vertically.

```java
import space.zoommax.utils.keyboard.Button;
import space.zoommax.utils.keyboard.Keyboard;

import java.util.List;
import java.util.ArrayList;

List<Button> buttons = new ArrayList<>();
buttons.

add(new Button("I will open a link", "http://example.com"));
        buttons.

add(new Button("I will perform a callback action", "any_data"));

List<Button> buttons2 = new ArrayList<>();
buttons2.

add(new Button("I will also open a link", "tg://username"));

List<List<Button>> keyboardButtons = new ArrayList<>();
keyboardButtons.

add(buttons);
keyboardButtons.

add(buttons2);

Keyboard keyboard = Keyboard.builder()
        .chatId(123456789)
        .keyboardButtons(keyboardButtons)
        .build();
```

---

### Note

If both `code` and `keyboardButtons` are set simultaneously, an exception will be thrown:

```java
IllegalArgumentException("Keyboard cannot have both code and buttons")
```