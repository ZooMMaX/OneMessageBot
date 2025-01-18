# Keyboard
**Реализация клавиатуры в приложении (боте)**

Клавиатуры в телеграм представляют собой массив [Button](button.md).

Создать клавиатуру можно с помощью `bulder` класса `Keyboard`.

В `bulder` нужно указать:

- `chatId` - id чата, в котором будет отображаться клавиатура

и

- - `code` - отформатированный код клавиатуры

или

- - `keyboardButtons` - список кнопок клавиатуры

В отформатированной строке для создания новой строки кнопок используется разделитель `\n`.

---

---

**Примеры:**

Установка клавиатуры через `code`:

Кнопки будут одна под другой.

```java
import ru.zoommax.utils.keyboard.Keyboard;

Keyboard keyboard = Keyboard.builder()
    .chatId(123456789)
    .code("{Я открою ссылку;http://example.com}\n{Я выполню callback действие;any_data}")
    .build();
```

Две рядом и третья под ними.

```java
import ru.zoommax.utils.keyboard.Keyboard;

Keyboard keyboard = Keyboard.builder()
    .chatId(123456789)
    .code("{Я открою ссылку;http://example.com}{Я выполню callback действие;any_data}\n{Я тоже открою ссылку;tg://username}")
        .build();
```

Также можно добавить любые символы за пределами `{` и `}`. Такой подход даст возможность
сделать визуальное разделение кнопок в коде. Все символы за пределами `{` и `}`, кроме `\n` будут
игнорироваться.

```java
import ru.zoommax.utils.keyboard.Keyboard;

Keyboard keyboard = Keyboard.builder()
    .chatId(123456789)
    .code("{Я открою ссылку;http://example.com}{Я выполню callback действие;any_data}\n" +
            "_____>!@_________{Я тоже открою ссылку;tg://username}______acfd_______")
        .build();
```

---

Установка клавиатуры через `keyboardButtons`:

Кнопки будут одна под другой.

```java
import ru.zoommax.utils.keyboard.Button;
import ru.zoommax.utils.keyboard.Keyboard;

import java.util.List;

List<Button> buttons = new ArrayList<>();
buttons.

add(new Button("Я открою ссылку", "http://example.com"));
        buttons.

add(new Button("Я выполню callback действие", "any_data"));

List<Button> buttons2 = new ArrayList<>();
buttons2.

add(new Button("Я тоже открою ссылку", "tg://username"));

List<List<Button>> keyboardButtons = new ArrayList<>();
keyboardButtons.add(buttons);
keyboardButtons.add(buttons2);

Keyboard keyboard = Keyboard.builder()
        .chatId(123456789)
        .keyboardButtons(keyboardButtons).build();
```

---

Если установить `code` и `keyboardButtons` одновременно,
то будет выброшено исключение
`IllegalArgumentException("Keyboard cannot have both code and buttons")`.