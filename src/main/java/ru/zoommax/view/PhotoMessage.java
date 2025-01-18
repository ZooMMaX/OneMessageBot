package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.List;

@Builder
public class PhotoMessage implements ViewMessage {
    private long chatId;
    private String onMessageFlag = "";
    private File photoAsFile;
    private byte[] photoAsBytes;
    private String photoAsUrl;
    private String caption;
    private Keyboard keyboard;
    private List<File> photosAsFiles;
    private List<String> captions;
    private boolean notify = false;

    @Override
    public void run() {
        if ((photoAsFile != null || photoAsBytes != null || photoAsUrl != null) && photosAsFiles != null) {
            throw new IllegalArgumentException("You can't send a photo and a list of photos at the same time");
        }

        if (photoAsFile != null) {
            sendPhoto(chatId, photoAsFile, caption, onMessageFlag, notify);
        } else if (photoAsBytes != null) {
            sendPhoto(chatId, photoAsBytes, caption, onMessageFlag, notify);
        } else if (photoAsUrl != null) {
            sendPhoto(chatId, photoAsUrl, caption, onMessageFlag, notify);
        } else if (photosAsFiles != null) {
            sendPhotos(chatId, photosAsFiles, captions, onMessageFlag, notify);
        }
        sendKeyboard(chatId, keyboard, onMessageFlag, notify);
    }
}
