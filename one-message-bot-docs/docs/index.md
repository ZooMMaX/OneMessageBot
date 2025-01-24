# Welcome to OneMessageBot Documentation

## About the Library
This library provides a convenient framework for creating bots.  
Every bot created using this library operates in a "single window and buttons" mode.  
Essentially, the library is a bot message manager for Telegram that simplifies bot functionality to a single window and keyboard interface.

Features:

<img src="assets/checkbox.png" alt="drawing" width="24"/> MongoDB (built-in support)

<img src="assets/checkbox.png" alt="drawing" width="24"/> SQLite (built-in support)

<img src="assets/checkbox.png" alt="drawing" width="24"/> JDBC support with `Connection` specification

<img src="assets/checkbox.png" alt="drawing" width="24"/> Creation of multi-page keyboards

<img src="assets/checkbox.png" alt="drawing" width="24"/> Multilingual support with the ability to provide custom translations

<img src="assets/checkbox.png" alt="drawing" width="24"/> Event handling in methods using `ViewMessageImpl`

<img src="assets/checkbox.png" alt="drawing" width="24"/> `@ViewMessageListener` annotation for registering event handlers

<img src="assets/checkbox.png" alt="drawing" width="24"/> Notification support

<img src="assets/checkbox.png" alt="drawing" width="24"/> Sending text messages

<img src="assets/checkbox.png" alt="drawing" width="24"/> Sending images with links, `java.io.File`, or `byte[]`

<img src="assets/checkbox.png" alt="drawing" width="24"/> Sending videos with links, `java.io.File`, or `byte[]`

<img src="assets/checkbox.png" alt="drawing" width="24"/> Sending audio with links, `java.io.File`, or `byte[]`

<img src="assets/checkbox.png" alt="drawing" width="24"/> Sending documents with links, `java.io.File`, or `byte[]`

<img src="assets/round.png" alt="drawing" width="24"/> Sending `Reply Keyboard`

## Installation

Maven:

```xml
<dependency>
    <groupId>ru.zoommax</groupId>
    <artifactId>OneMessageBot</artifactId>
    <version>1.1.0.3</version>
</dependency>
```

Gradle:

```groovy
implementation 'ru.zoommax:OneMessageBot:1.1.0.3'
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