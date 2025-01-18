package ru.zoommax.utils;

import lombok.extern.slf4j.Slf4j;
import ru.zoommax.db.NotificationPojo;
import ru.zoommax.db.UserPojo;
import ru.zoommax.utils.db.NotificationType;


import java.nio.charset.StandardCharsets;

@Slf4j
public class CreateNotification implements Runnable{
    private final String message;
    private final String tg_id;
    private final String image;
    private final NotificationType notificationType;
    private final String fileType;
    public CreateNotification(String message, String tg_id, String image, NotificationType notificationType, String fileType) {
        if (notificationType == NotificationType.ALERT) {
            if (message.getBytes(StandardCharsets.UTF_8).length > 200 * 2) {
                Exception ex = new Exception();
                ex.fillInStackTrace();
                throw new RuntimeException("Message length limit exceeded", ex);
            }
        }

        if (image == null) {
            image = "";
        }
        if (fileType == null) {
            fileType = "";
        }

        this.image = image;
        this.message = message;
        this.tg_id = tg_id;
        this.notificationType = notificationType;
        this.fileType = fileType;
    }

    @Override
    public void run() {
        NotificationPojo notificationPojo = new NotificationPojo();
        notificationPojo.setMessage(message);
        notificationPojo.setTg_id(tg_id);
        notificationPojo.setFile(image);
        notificationPojo.setNotificationType(notificationType);
        notificationPojo.setFileType(fileType);
        notificationPojo.insert();

        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(Long.parseLong(tg_id));
        userPojo = userPojo.find();
        userPojo.setShowNotif(true);
        userPojo.update();
    }
}
