# OneMessageBot

![GitHub stars](https://img.shields.io/github/stars/ZooMMaX/OneMessageBot?style=social)
![GitHub forks](https://img.shields.io/github/forks/ZooMMaX/OneMessageBot?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/ZooMMaX/OneMessageBot?style=social)
![GitHub issues](https://img.shields.io/github/issues/ZooMMaX/OneMessageBot?style=social)
![GitHub open pull requests](https://img.shields.io/github/issues-pr/ZooMMaX/OneMessageBot?style=social)

![GitHub last commit](https://img.shields.io/github/last-commit/ZooMMaX/OneMessageBot?style=social)
![Maven Central Version](https://img.shields.io/maven-central/v/ru.zoommax/OneMessageBot?style=plastic)

<details>
<summary>How to read version</summary>

The version is marked with four numbers separated by a dot X.Y.W.Z
- X - Changes when changes affecting backward compatibility are made (for example, deprecated methods are removed)
- Y - Changes when external dependencies are updated, removed, or changed
- W - Changes when a new feature is added to the library
- Z - Changes when fixes are applied to existing code.

The values ​​are not reset. They only increase.
</details>

## License

[OneMessageBotLicense](https://github.com/ZooMMaX/OneMessageBot/blob/master/LICENSE.md)

## Documentation

[OneMessageBot Documentation](https://zoommax.github.io/OneMessageBot/)

## Quick Start

[OneMessageBot Quick Start](https://zoommax.github.io/OneMessageBot/quickstart/)

## Example bot

### Example of a bot based on the OneMessageBot library

[Source code](https://github.com/ZooMMaX/ExampleOneMessageBot)

[Bot in Telegram](https://t.me/eombl_bot)

## About the Library
This library provides a convenient framework for creating bots.  
Every bot created using this library operates in a "single window and buttons" mode.  
Essentially, the library is a bot message manager for Telegram that simplifies bot functionality to a single window and keyboard interface.

Features:

- [X] MongoDB (built-in support)

- [X] SQLite (built-in support)

- [X] JDBC support with `Connection` specification

- [X] Creation of multi-page keyboards

- [X] Multilingual support with the ability to provide custom translations

- [X] Event handling in methods using `ViewMessageImpl`

- [X] `@ViewMessageListener` annotation for registering event handlers

- [X] Notification support

- [X] Sending text messages

- [X] Sending images with links, `java.io.File`, or `byte[]`

- [X] Sending videos with links, `java.io.File`, or `byte[]`

- [X] Sending audio with links, `java.io.File`, or `byte[]`

- [X] Sending documents with links, `java.io.File`, or `byte[]`

- [X] Updatable messages by time

- [ ] Sending `Reply Keyboard`

## Installation

Maven:

```xml
<dependency>
    <groupId>ru.zoommax</groupId>
    <artifactId>OneMessageBot</artifactId>
    <version>1.1.3.3</version>
</dependency>
```

Gradle:

```groovy
implementation 'ru.zoommax:OneMessageBot:1.1.3.3'
```

## Third-party Dependencies

1. **[java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api)** ::
   Repository: [github.com/pengrad/java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api) ::
   Version: 7.0.1

2. **[MongoDBConnector](https://github.com/zoommax/MongoDBConnector)** ::
   Repository: [github.com/zoommax/MongoDBConnector](https://github.com/zoommax/MongoDBConnector) ::
   Version: 1.1

3. **[Lombok](https://projectlombok.org/)** ::
   Official site: [projectlombok.org](https://projectlombok.org/) ::
   Version: 1.18.30

4. **[SLF4J](http://www.slf4j.org/)** ::
   Official site: [slf4j.org](http://www.slf4j.org/) ::
   Version: 2.0.11

5. **[Reflections](https://github.com/ronmamo/reflections)** ::
   Repository: [github.com/ronmamo/reflections](https://github.com/ronmamo/reflections) ::
   Version: 0.10.2

6. **[Jackson Databind](https://github.com/FasterXML/jackson-databind)** ::
   Repository: [github.com/FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind) ::
   Version: 2.18.2

7. **[SQLite JDBC](https://github.com/xerial/sqlite-jdbc)** ::
   Repository: [github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) ::
   Version: 3.47.2.0

## Donate

### Crypto

- Bitcoin -  `bc1p30r32x7dty8ga6qqe9xe3x46h8pgq3tkddcl94z6e2ww85wqrups9g86s7`
- Litecoin - `ltc1ptlkdjs3ph5n8m4g9jvcjcgujwytxu3hm0963wnfr7nutrtplq0sstpdavf`
- USDT
  - ERC20 -  `0x168c7CBFA7aeBD697A5b07Bf7B7B50aa0fFef80D`
  - BEP20 -  `0x168c7CBFA7aeBD697A5b07Bf7B7B50aa0fFef80D`
  - TRC20 -  `TCDwJAoVmc4nMzZauWhKraK44k3kcBJBxv`
- TRX -      `TCDwJAoVmc4nMzZauWhKraK44k3kcBJBxv`

### Fiats

- RUB
- - [YooMoney](https://yoomoney.ru/to/41001243274649)