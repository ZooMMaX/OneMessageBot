# Button
**Реализация кнопки в приложении (боте)**

Кнопки в телеграм представляют собой текст на кнопке и действие,
которое будет выполняться при нажатии на кнопку.

Библиотека поддерживает создание кнопок с:
- callback действием
- ссылкой на внешний ресурс
- открытием телеграм мини приложения

При создании кнопок с помощью строки следует учитывать зарезервированные
символы:

- `{`
- `}`
- `;`

В случае необходимости эти символы можно экранировать, используя символ `\`.

## Способы создания кнопки

### Форматированная строка

Для этого создайте строку в формате `{text_on_button;button_action}`

- `text_on_button` - текст на кнопке
- `button_action` - действие, которое будет выполняться при нажатии на кнопку

Если `button_action` начинается с:

- `mapp`, то кнопка будет открывать телеграм мини приложение.
- `http` или `tg`, то кнопка будет открывать ссылку.
- в остальных случаях кнопка будет выполнять callback действие.

**Примеры:**

```java
import ru.zoommax.utils.keyboard.Button;

Button button1 = new Button("{Я открою телеграм мини приложение;mapphttp://example.com}");
Button button2 = new Button("{Я открою ссылку;http://example.com}");
Button button3 = new Button("{Я тоже открою ссылку;tg://username}");
Button button4 = new Button("{Я выполню callback действие;any_data}");
```

### Объект

Для этого создайте объект класса `Button` с полями `text` и `action`.

- `text` - текст на кнопке
- `action` - действие, которое будет выполняться при нажатии на кнопку

Если `action` начинается с:

- `mapp`, то кнопка будет открывать телеграм мини приложение
- `http` или `tg`, то кнопка будет открывать ссылку
- в остальных случаях кнопка будет выполнять callback действие

**Примеры:**

```java

Button button1 = new Button("Я открою телеграм мини приложение", "mapphttp://example.com");
Button button2 = new Button("Я открою ссылку", "http://example.com");
Button button3 = new Button("Я тоже открою ссылку", "tg://username");
Button button4 = new Button("Я выполню callback действие", "any_data");
```

### Объект с указанием типа

Для этого создайте объект класса `Button` с полями `text`, `action` и `type`

- `text` - текст на кнопке
- `action` - действие, которое будет выполняться при нажатии на кнопку
- `type` - тип кнопки

`type` может принимать значения:

- `CALLBACK` - callback действие
- `LINK` - ссылка
- `MINI_APP` - телеграм мини приложение

**Примеры:**

```java
Button button1 = new Button("Я открою телеграм мини приложение", "http://example.com", ButtonType.MINI_APP);
Button button2 = new Button("Я открою ссылку", "http://example.com", ButtonType.LINK);
Button button3 = new Button("Я тоже открою ссылку", "tg://username", ButtonType.LINK);
Button button4 = new Button("Я выполню callback действие", "any_data", ButtonType.CALLBACK);
```