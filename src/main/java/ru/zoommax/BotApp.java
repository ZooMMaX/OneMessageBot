package ru.zoommax;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.zoommax.db.NotificationPojo;
import ru.zoommax.db.UserMarkupsPojo;
import ru.zoommax.db.UserPojo;
import ru.zoommax.utils.ViewMessageListener;
import ru.zoommax.utils.db.DbType;
import ru.zoommax.utils.db.NotificationType;
import ru.zoommax.utils.keyboard.Button;
import ru.zoommax.utils.keyboard.ButtonType;
import ru.zoommax.utils.keyboard.Keyboard;
import ru.zoommax.utils.lang.LocalizationManager;
import ru.zoommax.view.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BotApp implements Runnable {
    public static BotSettings botSettings;
    private String botToken;
    public static LocalizationManager localizationManager = new LocalizationManager();
    public static TelegramBot bot;

    public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(Thread.currentThread().getStackTrace()[2].toString().split("\\.")[0]))
            .setScanners(Scanners.SubTypes, Scanners.ConstructorsAnnotated, Scanners.MethodsAnnotated, Scanners.FieldsAnnotated, Scanners.TypesAnnotated));
    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ViewMessageListener.class);

    public BotApp(BotSettings settings) {
        botSettings = settings;
        if (settings == null) {
            log.error("BotSettings cannot be null");
            throw new IllegalArgumentException("BotSettings cannot be null");
        }
        if (settings.getBotToken() == null || settings.getBotToken().isEmpty()) {
            log.error("BotToken cannot be null or empty");
            throw new IllegalArgumentException("BotToken cannot be null or empty");
        }
        this.botToken = botSettings.getBotToken();

        if (botSettings.getDefaultLanguage() == null || botSettings.getDefaultLanguage().isEmpty()) {
            botSettings.setDefaultLanguage("default_en_US");
        }

        if (botSettings.getLanguageDirPath() == null || botSettings.getLanguageDirPath().isEmpty()) {
            botSettings.setLanguageDirPath("");
        }

        if (botSettings.getButtonsRows() == 0) {
            botSettings.setButtonsRows(4);
        }

        if (botSettings.getDbType() == null) {
            botSettings.setDbType(DbType.SQLITE);
        }

        if (botSettings.getDbName() == null || botSettings.getDbName().isEmpty()) {
            botSettings.setDbName("BotApp");
        }

        if (botSettings.getDbUrl() == null || botSettings.getDbUrl().isEmpty()) {
            botSettings.setDbUrl("jdbc:sqlite:./");
        }

        if (botSettings.getDbUser() == null || botSettings.getDbUser().isEmpty()) {
            botSettings.setDbUser("");
        }

        if (botSettings.getDbPassword() == null || botSettings.getDbPassword().isEmpty()) {
            botSettings.setDbPassword("");
        }

        if (botSettings.getDisableGithubUrl() == null || botSettings.getDisableGithubUrl().isEmpty()) {
            botSettings.setDisableGithubUrl("disabled");
        }

        if (botSettings.getDbType() == DbType.CUSTOM || botSettings.getDbType() == DbType.SQLITE) {
            if (botSettings.getDbConnection() == null) {
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(botSettings.getDbUrl()+botSettings.getDbName(), botSettings.getDbUser(), botSettings.getDbPassword());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                botSettings.setDbConnection(connection);

                UserPojo userPojo = new UserPojo();
                userPojo.ensureTableExists(botSettings.getDbConnection());

                UserMarkupsPojo userMarkupsPojo = new UserMarkupsPojo();
                userMarkupsPojo.ensureTableExists(botSettings.getDbConnection());

                NotificationPojo notificationPojo = new NotificationPojo();
                notificationPojo.ensureTableExists(botSettings.getDbConnection());
            } else {
                Connection connection = botSettings.getDbConnection();
                UserPojo userPojo = new UserPojo();
                userPojo.ensureTableExists(connection);

                UserMarkupsPojo userMarkupsPojo = new UserMarkupsPojo();
                userMarkupsPojo.ensureTableExists(connection);

                NotificationPojo notificationPojo = new NotificationPojo();
                notificationPojo.ensureTableExists(connection);
            }
        }

        notificationSchedule();
    }

    private void notificationSchedule() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                HashMap<String, ViewMessage> forSend = new HashMap<>();
                HashMap<String, List<Button>> buttons = new HashMap<>();
                List<NotificationPojo> notificationPojos = new NotificationPojo().findAll();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                String msgText = "";
                String image = "";
                String fileType = "";

                for (NotificationPojo notificationPojo : notificationPojos) {
                    UserPojo userPojo = new UserPojo();
                    userPojo.setChatId(Long.parseLong(notificationPojo.getTg_id()));
                    userPojo = userPojo.find();
                    if (userPojo.isShowNotif()) {
                        List<Button> buttonsList = new ArrayList<>();
                        if (buttons.containsKey(notificationPojo.getTg_id())) {
                            buttonsList = buttons.get(notificationPojo.getTg_id());
                        }

                        if (!notificationPojo.isRead()) {
                            if (notificationPojo.getNotificationType() == NotificationType.FULL) {
                                msgText = notificationPojo.getMessage();
                                if (!notificationPojo.getFile().isEmpty()) {
                                    image = notificationPojo.getFile();
                                    fileType = notificationPojo.getFileType();
                                } else {
                                    image = "";
                                    fileType = "";
                                }
                            } else {
                                msgText = localizationManager.getTranslationForLanguage(userPojo.getLanguage(), "main.notification");
                                image = "";
                            }

                            Button button = new Button("\uD83D\uDD34" + sdf.format(new Date(notificationPojo.getDate())), "ntf" + notificationPojo.getUid(), ButtonType.CALLBACK);
                            buttonsList.add(button);
                            buttons.put(notificationPojo.getTg_id(), buttonsList);

                            List<List<Button>> keyboardButtons = new ArrayList<>();
                            for (Button b : buttonsList) {
                                List<Button> row = new ArrayList<>();
                                row.add(b);
                                keyboardButtons.add(row);
                            }


                            Keyboard keyboard = Keyboard.builder()
                                    .chatId(Long.parseLong(notificationPojo.getTg_id()))
                                    .keyboardButtons(keyboardButtons)
                                    .notify(true)
                                    .rows(100)
                                    .build();

                            if (fileType.equalsIgnoreCase("image")) {
                                File file = new File(image);
                                forSend.put(notificationPojo.getTg_id(), PhotoMessage.builder()
                                        .photoAsFile(file)
                                        .caption(msgText)
                                        .chatId(Long.parseLong(notificationPojo.getTg_id()))
                                        .keyboard(keyboard)
                                        .notify(true)
                                        .build());
                            }

                            if (fileType.equalsIgnoreCase("video")) {
                                File file = new File(image);
                                forSend.put(notificationPojo.getTg_id(), VideoMessage.builder()
                                        .videoAsFile(file)
                                        .caption(msgText)
                                        .chatId(Long.parseLong(notificationPojo.getTg_id()))
                                        .keyboard(keyboard)
                                        .notify(true)
                                        .build());
                            }

                            if (image.isEmpty()) {
                                forSend.put(notificationPojo.getTg_id(), TextMessage.builder()
                                        .text(msgText)
                                        .chatId(Long.parseLong(notificationPojo.getTg_id()))
                                        .keyboard(keyboard)
                                        .notify(true)
                                        .build());
                            }
                        }
                    }
                }

                for (Map.Entry<String, ViewMessage> entry : forSend.entrySet()) {
                    UserPojo userPojo = new UserPojo();
                    userPojo.setChatId(Long.parseLong(entry.getKey()));
                    userPojo = userPojo.find();
                    userPojo.setShowNotif(false);
                    userPojo.update();

                    executor.submit(entry.getValue());
                }
            }
        }, 0, 1000);
    }

    @Override
    public void run() {
        localizationManager.loadTranslationsFromResources("/translations");
        if (botSettings.getLanguageDirPath() != null && !botSettings.getLanguageDirPath().isEmpty()){
            try {
                localizationManager.loadTranslations(Paths.get(botSettings.getLanguageDirPath()));
            } catch (IOException e) {
                log.error("Failed to load translations from {}", botSettings.getLanguageDirPath(), e);
            }
        }
        localizationManager.loadTranslationsFromResources("translations");
        bot = new TelegramBot(botToken);
        bot.setUpdatesListener(updates -> {
            for (Update update : updates){
                UserPojo userPojo = new UserPojo();
                long chatId = 0;
                if (update.message() != null) {
                    chatId = update.message().chat().id();
                }else if (update.callbackQuery() != null) {
                    chatId = update.callbackQuery().from().id();
                } else if (update.inlineQuery() != null) {
                    chatId = update.inlineQuery().from().id();
                } else if (update.chosenInlineResult() != null) {
                    chatId = update.chosenInlineResult().from().id();
                }

                long lastMessageId = 0;
                if (update.message() != null) {
                    lastMessageId = update.message().messageId();
                }else if (update.callbackQuery() != null) {
                    lastMessageId = update.callbackQuery().message().messageId();
                }

                userPojo.setChatId(chatId);
                userPojo = userPojo.find();
                if (userPojo == null) {
                    userPojo = new UserPojo();
                    userPojo.setChatId(chatId);
                    userPojo.setLastMessageId(lastMessageId);
                    userPojo.setViewMessageId(0);
                    userPojo.insert();
                }else {
                    if (update.callbackQuery() != null){
                        userPojo.setLastMessageId(-1);
                        userPojo.insert();
                    }else {
                        userPojo.setLastMessageId(lastMessageId);
                        userPojo.insert();
                    }
                }

                ViewMessage viewMessage = null;
                if (update.message() != null) {
                    //text message
                    if (update.message().text() != null) {
                        //command
                        if (update.message().text().startsWith("/")) {
                            //standard start command
                            if (update.message().text().equals("/start")) {
                                long msgId = 0;
                                try {
                                    msgId = bot.execute(new SendMessage(update.message().chat().id(), localizationManager.getTranslationForLanguage(userPojo.getLanguage(), "main.bot_starting"))).message().messageId();
                                    for (int x = 1; x < 1001; x++) {
                                        long finalMsgId = msgId;
                                        int finalX = x;
                                        Runnable r = (() -> {
                                            if (finalMsgId - finalX > 0) {
                                                bot.execute(new DeleteMessage(update.message().chat().id(), (int) (finalMsgId - finalX)));
                                            }
                                        });
                                        executor.submit(r);
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    msgId = 0;
                                }
                                userPojo.setViewMessageId(msgId);
                                userPojo.insert();
                            }
                            //Sending the command to be processed by classes annotated with @ViewMessageListener until a ViewMessage is returned
                            if (annotated != null) {
                                for (Class<?> listener : annotated) {
                                    try {
                                        Method method = listener.getMethod("onCommand", String.class, int.class, long.class, Update.class);
                                        viewMessage = (ViewMessage) method.invoke(listener.getDeclaredConstructor().newInstance(), update.message().text(), update.message().messageId(), update.message().chat().id(), update);
                                        if (viewMessage != null) {
                                            break;
                                        }
                                    } catch (InstantiationException | IllegalAccessException |
                                             InvocationTargetException |
                                             NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        } else {
                            //no command message.
                            //Sending the message to be processed by classes annotated with @ViewMessageListener until a ViewMessage is returned
                            //onMessageFlag will be set to ""
                            if (annotated != null) {
                                for (Class<?> listener : annotated) {
                                    try {
                                        Method method = listener.getMethod("onMessage", String.class, int.class, long.class, String.class, Update.class);
                                        viewMessage = (ViewMessage) method.invoke(listener.getDeclaredConstructor().newInstance(), update.message().text(), update.message().messageId(), update.message().chat().id(), userPojo.getOnMessageFlag(), update);
                                        if (viewMessage != null) {
                                            userPojo.setOnMessageFlag("");
                                            userPojo.insert();
                                            break;
                                        } else {
                                            bot.execute(new DeleteMessage(update.message().chat().id(), update.message().messageId()));
                                        }
                                    } catch (InstantiationException | IllegalAccessException |
                                             InvocationTargetException |
                                             NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    } else if (update.message().photo() != null) {
                        if (annotated != null) {
                            for (Class<?> listener : annotated) {
                                try {
                                    Method method = listener.getMethod("onPicture", PhotoSize[].class, String.class, int.class, long.class, Update.class);
                                    viewMessage = (ViewMessage) method.invoke(listener.getDeclaredConstructor().newInstance(), update.message().photo(), update.message().caption(), update.message().messageId(), update.message().chat().id(), update);
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                         NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }

                if (update.callbackQuery() != null) {
                    if (update.callbackQuery().data().startsWith("ntfс")) {
                        DeleteMessage deleteMessage = new DeleteMessage(chatId, update.callbackQuery().message().messageId());
                        bot.execute(deleteMessage);
                    }
                    if (update.callbackQuery().data().startsWith("ntf")){
                        NotificationPojo notificationPojo = new NotificationPojo();
                        notificationPojo.setUid(update.callbackQuery().data().replace("ntf", ""));
                        notificationPojo = notificationPojo.findByUID();
                        try {
                            if (notificationPojo == null) {
                                bot.execute(new DeleteMessage(chatId, update.callbackQuery().message().messageId()));
                                userPojo.setNotificationMessageId(-100);
                                userPojo.update();
                            }else if (notificationPojo.getNotificationType() == NotificationType.FULL){
                                bot.execute(new DeleteMessage(chatId, update.callbackQuery().message().messageId()));
                                userPojo.setNotificationMessageId(-100);
                                userPojo.setShowNotif(true);
                                userPojo.update();
                                if (!notificationPojo.getFile().isEmpty()){
                                    File file = new File(notificationPojo.getFile());
                                    String fileType = notificationPojo.getFileType();
                                    if (fileType.equals("image")) {
                                        SendPhoto sendPhoto = new SendPhoto(chatId, file).caption(notificationPojo.getMessage()).parseMode(ParseMode.HTML);
                                        sendPhoto.replyMarkup(new InlineKeyboardMarkup(
                                                new InlineKeyboardButton[]{
                                                        new InlineKeyboardButton(localizationManager.getTranslationForLanguage(userPojo.getLanguage(), "main.notify_close")).callbackData("ntfс" + notificationPojo.getUid())
                                                }
                                        ));
                                        bot.execute(sendPhoto);
                                    }
                                    if (fileType.equals("video")) {
                                        SendVideo sendVideo = new SendVideo(chatId, file).caption(notificationPojo.getMessage()).parseMode(ParseMode.HTML);
                                        sendVideo.replyMarkup(new InlineKeyboardMarkup(
                                                new InlineKeyboardButton[]{
                                                        new InlineKeyboardButton(localizationManager.getTranslationForLanguage(userPojo.getLanguage(), "main.notify_close")).callbackData("ntfс" + notificationPojo.getUid())
                                                }
                                        ));
                                        bot.execute(sendVideo);
                                    }
                                }else {
                                    SendMessage sendMessage = new SendMessage(chatId, notificationPojo.getMessage()).parseMode(ParseMode.HTML);
                                    sendMessage.replyMarkup(new InlineKeyboardMarkup(
                                            new InlineKeyboardButton[]{
                                                    new InlineKeyboardButton(localizationManager.getTranslationForLanguage(userPojo.getLanguage(), "main.notify_close")).callbackData("ntfс" + notificationPojo.getUid())
                                            }
                                    ));
                                    bot.execute(sendMessage);
                                }
                                notificationPojo.setRead(true);
                                notificationPojo.update();
                            }else if (bot.execute(new AnswerCallbackQuery(update.callbackQuery().id()).text(notificationPojo.getMessage()).showAlert(true)).isOk()) {
                                bot.execute(new DeleteMessage(chatId, update.callbackQuery().message().messageId()));
                                userPojo.setNotificationMessageId(-100);
                                userPojo.setShowNotif(true);
                                userPojo.update();
                                notificationPojo.setRead(true);
                                notificationPojo.update();
                            }
                        }catch (NullPointerException e){
                            log.error("NotificationPojo is null for uid {}", update.callbackQuery().data().replace("ntf", ""));
                        }
                    }
                    if (update.callbackQuery().data().contains("nextButton")){
                        int keysPage = Integer.parseInt(update.callbackQuery().data().split(":")[1])+1;
                        UserMarkupsPojo userMarkupsPojo = new UserMarkupsPojo();
                        userMarkupsPojo.setTg_id(chatId+"");
                        userMarkupsPojo = userMarkupsPojo.find();

                        String code = userMarkupsPojo.getCode();

                        viewMessage = InlineKeyboard.builder()
                                .chatId(chatId)
                                .keyboard(Keyboard.builder()
                                        .pageKb(keysPage)
                                        .code(code)
                                        .chatId(chatId)
                                        .build())
                                .build();
                    } else if (update.callbackQuery().data().contains("prevButton")){
                        int keysPage = Integer.parseInt(update.callbackQuery().data().split(":")[1])-1;
                        UserMarkupsPojo userMarkupsPojo = new UserMarkupsPojo();
                        userMarkupsPojo.setTg_id(chatId+"");
                        userMarkupsPojo = userMarkupsPojo.find();

                        String code = userMarkupsPojo.getCode();

                        viewMessage = InlineKeyboard.builder()
                                .chatId(chatId)
                                .keyboard(Keyboard.builder()
                                        .pageKb(keysPage)
                                        .code(code)
                                        .chatId(chatId)
                                        .build())
                                .build();
                    } else {
                        if (annotated != null) {
                            for (Class<?> listener : annotated) {
                                try {
                                    Method method = listener.getMethod("onCallbackQuery", String.class, int.class, long.class, Update.class);
                                    viewMessage = (ViewMessage) method.invoke(listener.getDeclaredConstructor().newInstance(), update.callbackQuery().data(), update.callbackQuery().message().messageId(), update.callbackQuery().message().chat().id(), update);
                                    if (viewMessage != null) {
                                        break;
                                    }
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                         NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }

                if (update.inlineQuery() != null) {
                    if (annotated != null) {
                        for (Class<?> listener : annotated) {
                            try {
                                Method method = listener.getMethod("onInlineQuery", String.class, String.class, long.class, Update.class);
                                viewMessage = (ViewMessage) method.invoke(listener.getDeclaredConstructor().newInstance(), update.inlineQuery().query(), update.inlineQuery().id(), update.inlineQuery().from().id(), update);
                                if (viewMessage != null) {
                                    break;
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                if (update.chosenInlineResult() != null) {
                    if (annotated != null) {
                        for (Class<?> listener : annotated) {
                            try {
                                Method method = listener.getMethod("onChosenInlineResult", String.class, long.class, String.class, Update.class);
                                viewMessage = (ViewMessage) method.invoke(listener.getDeclaredConstructor().newInstance(), update.chosenInlineResult().resultId(), update.chosenInlineResult().from().id(), update.chosenInlineResult().query(), update);
                                if (viewMessage != null) {
                                    break;
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                if (viewMessage == null) {
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }else {
                    executor.submit(viewMessage);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                // got bad response from telegram
                e.response().errorCode();
                e.response().description();
            } else {
                // probably network error
                e.printStackTrace();
            }
        });
    }

    public static void setUserLanguage(String chatId, String language) {
        executor.submit(() -> {
            UserPojo userPojo = new UserPojo();
            userPojo.setChatId(Long.parseLong(chatId));
            userPojo.setLanguage(language);
            userPojo.update();
        });
    }

    public static String getUserLanguage(String chatId) {
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(Long.parseLong(chatId));
        userPojo = userPojo.find();
        return userPojo.getLanguage();
    }
}