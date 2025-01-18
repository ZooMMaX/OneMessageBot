# Button
**Implementation of a Button in the Application (Bot)**

Telegram buttons consist of the text displayed on the button and the action executed when the button is pressed.

The library supports creating buttons with the following functionalities:
- Callback actions
- Links to external resources
- Opening Telegram Mini Apps

When creating buttons using strings, consider reserved characters:

- `{`
- `}`
- `;`

If needed, these characters can be escaped using the `\` character.

## Methods to Create a Button

### Formatted String

To use this method, create a string in the format `{text_on_button;button_action}`.

- `text_on_button` - the text displayed on the button
- `button_action` - the action executed when the button is pressed

If `button_action` starts with:

- `mapp`, the button will open a Telegram Mini App.
- `http` or `tg`, the button will open a link.
- In other cases, the button will perform a callback action.

**Examples:**

```java
import ru.zoommax.utils.keyboard.Button;

Button button1 = new Button("{I will open a Telegram Mini App;mapphttp://example.com}");
Button button2 = new Button("{I will open a link;http://example.com}");
Button button3 = new Button("{I will also open a link;tg://username}");
Button button4 = new Button("{I will perform a callback action;any_data}");
```

### Object

To use this method, create an instance of the `Button` class with `text` and `action` fields.

- `text` - the text displayed on the button
- `action` - the action executed when the button is pressed

If `action` starts with:

- `mapp`, the button will open a Telegram Mini App.
- `http` or `tg`, the button will open a link.
- In other cases, the button will perform a callback action.

**Examples:**

```java
Button button1 = new Button("I will open a Telegram Mini App", "mapphttp://example.com");
Button button2 = new Button("I will open a link", "http://example.com");
Button button3 = new Button("I will also open a link", "tg://username");
Button button4 = new Button("I will perform a callback action", "any_data");
```

### Object with Type Specification

To use this method, create an instance of the `Button` class with `text`, `action`, and `type` fields.

- `text` - the text displayed on the button
- `action` - the action executed when the button is pressed
- `type` - the type of the button

`type` can take the following values:

- `CALLBACK` - callback action
- `LINK` - link
- `MINI_APP` - Telegram Mini App

**Examples:**

```java
Button button1 = new Button("I will open a Telegram Mini App", "http://example.com", ButtonType.MINI_APP);
Button button2 = new Button("I will open a link", "http://example.com", ButtonType.LINK);
Button button3 = new Button("I will also open a link", "tg://username", ButtonType.LINK);
Button button4 = new Button("I will perform a callback action", "any_data", ButtonType.CALLBACK);
```