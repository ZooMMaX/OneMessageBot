# Welcome to OneMessageBot documentation

## О библиотеке
Данная библиотека реализует удобный фреймворк для создания ботов.
Каждый бот созданный с помощью этой библиотеки работает в режиме "одно окно и кнопки".
Фактически библиотека - это менеджер управления сообщением бота в телеграм,
который сводит бота к представлению в виде одного окна и клавиатуры.

Функции:

<img src="/assets/checkbox.png" alt="drawing" width="24"/> MongoDB (встроенная поддержка)

<img src="/assets/checkbox.png" alt="drawing" width="24"/> SQLite  (встроенная поддержка)

<img src="/assets/checkbox.png" alt="drawing" width="24"/> JDBC указание `Connection`

<img src="/assets/checkbox.png" alt="drawing" width="24"/> Создание многостраничных клавиатур

<img src="/assets/checkbox.png" alt="drawing" width="24"/> Многоязычность с возможностью указания собственных переводов

<img src="/assets/checkbox.png" alt="drawing" width="24"/> Обработка событий в методах согласно `ViewMessageImpl`

<img src="/assets/checkbox.png" alt="drawing" width="24"/> Аннотация `@ViewMessageListener` для регистрации обработчиков событий

<img src="/assets/checkbox.png" alt="drawing" width="24"/> Поддержка уведомлений

<img src="/assets/checkbox.png" alt="drawing" width="24"/> Отправка текстовых сообщений

<img src="/assets/round.png" alt="drawing" width="24"/> Отправка изображений с указанием ссылки на изображение, `java.io.File`, `byte[]`

<img src="/assets/round.png" alt="drawing" width="24"/> Отправка видео с указанием ссылки на видео, `java.io.File`, `byte[]`

<img src="/assets/round.png" alt="drawing" width="24"/> Отправка аудио с указанием ссылки на аудио, `java.io.File`, `byte[]`

<img src="/assets/round.png" alt="drawing" width="24"/> Отправка документов с указанием ссылки на документ, `java.io.File`, `byte[]`

<img src="/assets/round.png" alt="drawing" width="24"/> Отправка `Reply Keyboard`

## Установка

Maven:

```xml
<dependency>
    <groupId>ru.zoommax</groupId>
    <artifactId>OneMessageBot</artifactId>
    <version>1.0</version>
</dependency>
```

Gradle:

```groovy
implementation 'ru.zoommax:OneMessageBot:1.0'
```

## Сторонние зависимости

1. **[java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api)**
    - Репозиторий: [github.com/pengrad/java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api)
    - Версия: 7.0.1

2. **[MongoDBConnector](https://github.com/zoommax/MongoDBConnector)**
    - Репозиторий: [github.com/zoommax/MongoDBConnector](https://github.com/zoommax/MongoDBConnector)
    - Версия: 1.1

3. **[Lombok](https://projectlombok.org/)**
    - Официальный сайт: [projectlombok.org](https://projectlombok.org/)
    - Версия: 1.18.30

4. **[SLF4J](http://www.slf4j.org/)**
    - Официальный сайт: [slf4j.org](http://www.slf4j.org/)
    - Версия: 2.0.11

5. **[Reflections](https://github.com/ronmamo/reflections)**
    - Репозиторий: [github.com/ronmamo/reflections](https://github.com/ronmamo/reflections)
    - Версия: 0.10.2

6. **[Jackson Databind](https://github.com/FasterXML/jackson-databind)**
    - Репозиторий: [github.com/FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind)
    - Версия: 2.18.2

7. **[SQLite JDBC](https://github.com/xerial/sqlite-jdbc)**
    - Репозиторий: [github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)
    - Версия: 3.47.2.0  
