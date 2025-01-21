package ru.zoommax.utils.lang;

import ru.zoommax.db.UserPojo;

public class UserLanguage {
    public static void setUserLanguage(long chatId, String language) {
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        userPojo.setLanguage(language);
        userPojo.update();
    }

    public static String getUserLanguage(long chatId) {
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        return userPojo.getLanguage();
    }
}
