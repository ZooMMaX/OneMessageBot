package ru.zoommax.view;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import ru.zoommax.BotApp;
import ru.zoommax.db.MessageTimeUpdatePojo;
import ru.zoommax.utils.db.MessageType;
import ru.zoommax.db.UserPojo;
import ru.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface ViewMessage extends Runnable {

    default String addURL(String text){
        if (BotApp.botSettings.getDisableGithubUrl().equals("disabled")) {
            return text + "\n\n<a href=\"https://github.com/ZooMMaX/OneMessageBot\">Powered By OneMessageBot</a>";
        } else if (BotApp.botSettings.getDisableGithubUrl().equals("enabled")) {
            return text;
        } else {
            return text + "\n\n<a href=\"https://github.com/ZooMMaX/OneMessageBot\">Powered By OneMessageBot</a>";
        }
    }

    default void sendText(long chatId, String text, String onMessageFlag, boolean notify){
        text = addURL(text);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long viewMessageId = userPojo.getViewMessageId();
                long lastMessageId = userPojo.getLastMessageId();
                if (userPojo.getMessageType() == MessageType.TEXT) {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                    );
                    EditMessageText editMessageText = new EditMessageText(chatId, Math.toIntExact(viewMessageId), text);
                    editMessageText.parseMode(ParseMode.HTML);
                    BotApp.bot.execute(editMessageText);
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                    );
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendMessage sendMessage = new SendMessage(chatId, text);
                    sendMessage.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendMessage).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.TEXT);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendMessage sendMessage = new SendMessage(chatId, text);
                sendMessage.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendMessage).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendKeyboard(long chatId, Keyboard keyboard, String onMessageFlag, boolean notify){
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long viewMessageId = userPojo.getViewMessageId();
                EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(chatId, Math.toIntExact(viewMessageId)).replyMarkup(keyboard.getKeyboard());
                BotApp.bot.execute(editMessageReplyMarkup);
            }else {
                long viewMessageId = userPojo.getNotificationMessageId();
                EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(chatId, Math.toIntExact(viewMessageId)).replyMarkup(keyboard.getKeyboard());
                BotApp.bot.execute(editMessageReplyMarkup);
            }
        }
    }

    default void sendPhoto(long chatId, File file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );

                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaPhoto(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendPhoto sendPhoto = new SendPhoto(chatId, file);
                    if (!caption.isEmpty()) {
                        sendPhoto.caption(caption);
                    }
                    sendPhoto.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendPhoto).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendPhoto sendPhoto = new SendPhoto(chatId, file);
                if (!caption.isEmpty()) {
                    sendPhoto.caption(caption);
                }
                sendPhoto.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendPhoto).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendPhoto(long chatId, byte[] file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );

                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaPhoto(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendPhoto sendPhoto = new SendPhoto(chatId, file);
                    if (!caption.isEmpty()) {
                        sendPhoto.caption(caption);
                    }
                    sendPhoto.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendPhoto).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendPhoto sendPhoto = new SendPhoto(chatId, file);
                if (!caption.isEmpty()) {
                    sendPhoto.caption(caption);
                }
                sendPhoto.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendPhoto).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendPhoto(long chatId, String file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );

                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaPhoto(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendPhoto sendPhoto = new SendPhoto(chatId, file);
                    if (!caption.isEmpty()) {
                        sendPhoto.caption(caption);
                    }
                    sendPhoto.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendPhoto).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendPhoto sendPhoto = new SendPhoto(chatId, file);
                if (!caption.isEmpty()) {
                    sendPhoto.caption(caption);
                }
                sendPhoto.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendPhoto).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendPhotos(long chatId, List<File> files, List<String> captions, String onMessageFlag, boolean notify){
        List<String> captionsCopy = new ArrayList<>();
        for (String caption : captions) {
            captionsCopy.add(addURL(caption));
        }
        captions = captionsCopy;

        if (files.size() > 10){
            throw new IllegalArgumentException("You can send up to 10 photos at once");
        }else {
            UserPojo userPojo = new UserPojo();
            userPojo.setChatId(chatId);
            userPojo = userPojo.find();
            if (userPojo != null) {
                if (!notify) {
                    userPojo.setOnMessageFlag(onMessageFlag);
                    userPojo.update();
                    long lastMessageId = userPojo.getLastMessageId();
                    long viewMessageId = userPojo.getViewMessageId();
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                    );
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    InputMediaPhoto[] inputMediaPhotos = new InputMediaPhoto[files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(files.get(i));
                        if (!captions.isEmpty()) {
                            if (i >= captions.size()) {
                                inputMediaPhoto.caption(captions.get(i));
                            }
                        }
                        inputMediaPhotos[i] = inputMediaPhoto;
                    }
                    SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaPhotos);
                    Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                    viewMessageId = messages[messages.length - 1].messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.update();
                } else {
                    long viewMessageId = userPojo.getNotificationMessageId();
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    InputMediaPhoto[] inputMediaPhotos = new InputMediaPhoto[files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(files.get(i));
                        if (!captions.isEmpty()) {
                            if (i >= captions.size()) {
                                inputMediaPhoto.caption(captions.get(i));
                            }
                        }
                        inputMediaPhotos[i] = inputMediaPhoto;
                    }
                    SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaPhotos);
                    Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                    viewMessageId = messages[messages.length - 1].messageId();
                    userPojo.setNotificationMessageId(viewMessageId);
                    userPojo.update();
                }
            }
        }
    }

    default void sendAudio(long chatId, File file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );

                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaAudio(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendAudio sendAudio = new SendAudio(chatId, file);
                    if (!caption.isEmpty()) {
                        sendAudio.caption(caption);
                    }
                    sendAudio.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendAudio).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendAudio sendAudio = new SendAudio(chatId, file);
                if (!caption.isEmpty()) {
                    sendAudio.caption(caption);
                }
                sendAudio.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendAudio).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendAudio(long chatId, String file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );

                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaAudio(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendAudio sendAudio = new SendAudio(chatId, file);
                    if (!caption.isEmpty()) {
                        sendAudio.caption(caption);
                    }
                    sendAudio.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendAudio).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendAudio sendAudio = new SendAudio(chatId, file);
                if (!caption.isEmpty()) {
                    sendAudio.caption(caption);
                }
                sendAudio.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendAudio).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendAudio(long chatId, byte[] file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );

                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaAudio(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendAudio sendAudio = new SendAudio(chatId, file);
                    if (!caption.isEmpty()) {
                        sendAudio.caption(caption);
                    }
                    sendAudio.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendAudio).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendAudio sendAudio = new SendAudio(chatId, file);
                if (!caption.isEmpty()) {
                    sendAudio.caption(caption);
                }
                sendAudio.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendAudio).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendAudios(long chatId, List<File> files, List<String> captions, String onMessageFlag, boolean notify){
        List<String> captionsCopy = new ArrayList<>();
        for (String caption : captions) {
            captionsCopy.add(addURL(caption));
        }
        captions = captionsCopy;
        if (files.size() > 10){
            throw new IllegalArgumentException("You can send up to 10 audios at once");
        }else {
            UserPojo userPojo = new UserPojo();
            userPojo.setChatId(chatId);
            userPojo = userPojo.find();
            if (userPojo != null) {
                if (!notify) {
                    userPojo.setOnMessageFlag(onMessageFlag);
                    userPojo.update();
                    long lastMessageId = userPojo.getLastMessageId();
                    long viewMessageId = userPojo.getViewMessageId();
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                    );
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    InputMediaAudio[] inputMediaAudios = new InputMediaAudio[files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        InputMediaAudio inputMediaAudio = new InputMediaAudio(files.get(i));
                        if (!captions.isEmpty()) {
                            if (i >= captions.size()) {
                                inputMediaAudio.caption(captions.get(i));
                            }
                        }
                        inputMediaAudios[i] = inputMediaAudio;
                    }
                    SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaAudios);
                    Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                    viewMessageId = messages[messages.length - 1].messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }else {
                    long viewMessageId = userPojo.getNotificationMessageId();
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    InputMediaAudio[] inputMediaAudios = new InputMediaAudio[files.size()];
                    for (int i = 0; i < files.size(); i++) {
                        InputMediaAudio inputMediaAudio = new InputMediaAudio(files.get(i));
                        if (!captions.isEmpty()) {
                            if (i >= captions.size()) {
                                inputMediaAudio.caption(captions.get(i));
                            }
                        }
                        inputMediaAudios[i] = inputMediaAudio;
                    }
                    SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaAudios);
                    Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                    viewMessageId = messages[messages.length - 1].messageId();
                    userPojo.setNotificationMessageId(viewMessageId);
                    userPojo.update();
                }
            }
        }
    }

    default void sendVideo(long chatId, File file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaVideo(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendVideo sendVideo = new SendVideo(chatId, file);
                    if (!caption.isEmpty()) {
                        sendVideo.caption(caption);
                    }
                    sendVideo.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendVideo).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            }else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendVideo sendVideo = new SendVideo(chatId, file);
                if (!caption.isEmpty()) {
                    sendVideo.caption(caption);
                }
                sendVideo.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendVideo).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendVideo(long chatId, String file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaVideo(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendVideo sendVideo = new SendVideo(chatId, file);
                    if (!caption.isEmpty()) {
                        sendVideo.caption(caption);
                    }
                    sendVideo.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendVideo).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendVideo sendVideo = new SendVideo(chatId, file);
                if (!caption.isEmpty()) {
                    sendVideo.caption(caption);
                }
                sendVideo.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendVideo).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendVideo(long chatId, byte[] file, String caption, String onMessageFlag, boolean notify){
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaVideo(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendVideo sendVideo = new SendVideo(chatId, file);
                    if (!caption.isEmpty()) {
                        sendVideo.caption(caption);
                    }
                    sendVideo.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendVideo).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendVideo sendVideo = new SendVideo(chatId, file);
                if (!caption.isEmpty()) {
                    sendVideo.caption(caption);
                }
                sendVideo.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendVideo).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendVideos(long chatId, List<File> files, List<String> captions, String onMessageFlag, boolean notify) {
        List<String> captionsCopy = new ArrayList<>();
        for (String caption : captions) {
            captionsCopy.add(addURL(caption));
        }
        captions = captionsCopy;
        if (files.size() > 10){
            throw new IllegalArgumentException("You can send up to 10 videos at once");
        }

        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                InputMediaVideo[] inputMediaVideos = new InputMediaVideo[files.size()];
                for (int i = 0; i < files.size(); i++) {
                    InputMediaVideo inputMediaVideo = new InputMediaVideo(files.get(i));
                    if (!captions.isEmpty()) {
                        if (i >= captions.size()) {
                            inputMediaVideo.caption(captions.get(i));
                        }
                    }
                    inputMediaVideos[i] = inputMediaVideo;
                }
                SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaVideos);
                Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                viewMessageId = messages[messages.length - 1].messageId();
                userPojo.setViewMessageId(viewMessageId);
                userPojo.update();
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                InputMediaVideo[] inputMediaVideos = new InputMediaVideo[files.size()];
                for (int i = 0; i < files.size(); i++) {
                    InputMediaVideo inputMediaVideo = new InputMediaVideo(files.get(i));
                    if (!captions.isEmpty()) {
                        if (i >= captions.size()) {
                            inputMediaVideo.caption(captions.get(i));
                        }
                    }
                    inputMediaVideos[i] = inputMediaVideo;
                }
                SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaVideos);
                Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                viewMessageId = messages[messages.length - 1].messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendDocument(long chatId, File file, String caption, String onMessageFlag, boolean notify) {
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaDocument(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendDocument sendDocument = new SendDocument(chatId, file);
                    if (!caption.isEmpty()) {
                        sendDocument.caption(caption);
                    }
                    sendDocument.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendDocument).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendDocument sendDocument = new SendDocument(chatId, file);
                if (!caption.isEmpty()) {
                    sendDocument.caption(caption);
                }
                sendDocument.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendDocument).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendDocument(long chatId, String file, String caption, String onMessageFlag, boolean notify) {
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaDocument(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendDocument sendDocument = new SendDocument(chatId, file);
                    if (!caption.isEmpty()) {
                        sendDocument.caption(caption);
                    }
                    sendDocument.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendDocument).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            }else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendDocument sendDocument = new SendDocument(chatId, file);
                if (!caption.isEmpty()) {
                    sendDocument.caption(caption);
                }
                sendDocument.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendDocument).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendDocument(long chatId, byte[] file, String caption, String onMessageFlag, boolean notify) {
        caption = addURL(caption);
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                if (userPojo.getMessageType() == MessageType.MEDIA) {
                    EditMessageMedia editMessageMedia = new EditMessageMedia(chatId, Math.toIntExact(viewMessageId), new InputMediaDocument(file));
                    BotApp.bot.execute(editMessageMedia);
                    if (!caption.isEmpty()) {
                        EditMessageCaption editMessageCaption = new EditMessageCaption(chatId, Math.toIntExact(viewMessageId)).caption(caption);
                        BotApp.bot.execute(editMessageCaption);
                    }
                } else {
                    BotApp.bot.execute(
                            new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                    );
                    SendDocument sendDocument = new SendDocument(chatId, file);
                    if (!caption.isEmpty()) {
                        sendDocument.caption(caption);
                    }
                    sendDocument.parseMode(ParseMode.HTML);
                    viewMessageId = BotApp.bot.execute(sendDocument).message().messageId();
                    userPojo.setViewMessageId(viewMessageId);
                    userPojo.setMessageType(MessageType.MEDIA);
                    userPojo.update();
                }
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                SendDocument sendDocument = new SendDocument(chatId, file);
                if (!caption.isEmpty()) {
                    sendDocument.caption(caption);
                }
                sendDocument.parseMode(ParseMode.HTML);
                viewMessageId = BotApp.bot.execute(sendDocument).message().messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendDocuments(long chatId, List<File> files, List<String> captions, String onMessageFlag, boolean notify) {
        List<String> captionsCopy = new ArrayList<>();
        for (String caption : captions) {
            captionsCopy.add(addURL(caption));
        }
        captions = captionsCopy;
        if (files.size() > 10) {
            throw new IllegalArgumentException("You can send up to 10 documents at once");
        }

        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (userPojo != null) {
            if (!notify) {
                userPojo.setOnMessageFlag(onMessageFlag);
                userPojo.update();
                long lastMessageId = userPojo.getLastMessageId();
                long viewMessageId = userPojo.getViewMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(lastMessageId))
                );
                InputMediaDocument[] inputMediaDocuments = new InputMediaDocument[files.size()];
                for (int i = 0; i < files.size(); i++) {
                    InputMediaDocument inputMediaDocument = new InputMediaDocument(files.get(i));
                    if (!captions.isEmpty()) {
                        if (i >= captions.size()) {
                            inputMediaDocument.caption(captions.get(i));
                        }
                    }
                    inputMediaDocuments[i] = inputMediaDocument;
                }
                SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaDocuments);
                Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                viewMessageId = messages[messages.length - 1].messageId();
                userPojo.setViewMessageId(viewMessageId);
                userPojo.setMessageType(MessageType.MEDIA);
                userPojo.update();
            } else {
                long viewMessageId = userPojo.getNotificationMessageId();
                BotApp.bot.execute(
                        new DeleteMessage(chatId, Math.toIntExact(viewMessageId))
                );
                InputMediaDocument[] inputMediaDocuments = new InputMediaDocument[files.size()];
                for (int i = 0; i < files.size(); i++) {
                    InputMediaDocument inputMediaDocument = new InputMediaDocument(files.get(i));
                    if (!captions.isEmpty()) {
                        if (i >= captions.size()) {
                            inputMediaDocument.caption(captions.get(i));
                        }
                    }
                    inputMediaDocuments[i] = inputMediaDocument;
                }
                SendMediaGroup sendMediaGroup = new SendMediaGroup(chatId, inputMediaDocuments);
                Message[] messages = BotApp.bot.execute(sendMediaGroup).messages();
                viewMessageId = messages[messages.length - 1].messageId();
                userPojo.setNotificationMessageId(viewMessageId);
                userPojo.update();
            }
        }
    }

    default void sendReplyKeyboard(long chatId, ReplyKeyboardMarkup replyKeyboardMarkup, String onMessageFlag){

    }

    default String objectToString() {
        if (this instanceof TextMessage) {
            return ((TextMessage) this).toString();
        } else if (this instanceof VideoMessage) {
            return ((VideoMessage) this).toString();
        } else if (this instanceof PhotoMessage) {
            return ((PhotoMessage) this).toString();
        } else if (this instanceof AudioMessage) {
            return ((AudioMessage) this).toString();
        } else if (this instanceof DocumentMessage) {
            return ((DocumentMessage) this).toString();
        } else if (this instanceof Keyboard) {
            return ((Keyboard) this).toString();
        }
        return null;
    }

    static ViewMessage fromString(String text) {
        if (text == null) {
            return null;
        }
        if (text.startsWith("TextMessage")) {
            return TextMessage.builder().viewMessageTextEncoded(text).build().fstring();
        } else if (text.startsWith("VideoMessage")) {
            return VideoMessage.builder().viewMessageTextEncoded(text).build().fstring();
        } else if (text.startsWith("PhotoMessage")) {
            return PhotoMessage.builder().viewMessageTextEncoded(text).build().fstring();
        } else if (text.startsWith("AudioMessage")) {
            return AudioMessage.builder().viewMessageTextEncoded(text).build().fstring();
        } else if (text.startsWith("DocumentMessage")) {
            return DocumentMessage.builder().viewMessageTextEncoded(text).build().fstring();
        } else if (text.startsWith("InlineKeyboard")) {
            return InlineKeyboard.builder().viewMessageTextEncoded(text).build().fstring();
        }
        return null;
    }

    default void updateViewMessageTime(long chatId, boolean needUpdate, String viewMessageToUpdate, long updateTime) {
        MessageTimeUpdatePojo messageTimeUpdatePojo = new MessageTimeUpdatePojo();
        messageTimeUpdatePojo.setChatId(chatId);
        messageTimeUpdatePojo = messageTimeUpdatePojo.find();
        if (messageTimeUpdatePojo == null) {
            messageTimeUpdatePojo = new MessageTimeUpdatePojo();
            messageTimeUpdatePojo.setChatId(chatId);
            messageTimeUpdatePojo.setUpdateTime(0);
            messageTimeUpdatePojo.setLastViewMessageUpdateTime(System.currentTimeMillis());
            messageTimeUpdatePojo.setViewMessageBeforeUpdateTime(System.currentTimeMillis());
            messageTimeUpdatePojo.setNeedUpdate(needUpdate);
            messageTimeUpdatePojo.setViewMessageToUpdate(viewMessageToUpdate);
            messageTimeUpdatePojo.insert();
        }
        if (needUpdate){
            messageTimeUpdatePojo.setNeedUpdate(true);
            messageTimeUpdatePojo.setViewMessageToUpdate(viewMessageToUpdate);
            messageTimeUpdatePojo.setUpdateTime(updateTime);
            messageTimeUpdatePojo.setViewMessageBeforeUpdateTime(System.currentTimeMillis());
            messageTimeUpdatePojo.setLastViewMessageUpdateTime(System.currentTimeMillis());
            messageTimeUpdatePojo.update();
        } else {
            messageTimeUpdatePojo.setUpdateTime(0);
            messageTimeUpdatePojo.setLastViewMessageUpdateTime(System.currentTimeMillis());
            messageTimeUpdatePojo.setNeedUpdate(false);
            messageTimeUpdatePojo.update();
        }
    }

}