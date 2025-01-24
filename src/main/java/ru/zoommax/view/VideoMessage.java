package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.List;

@Builder
public class VideoMessage implements ViewMessage {
    private long chatId;
    private String onMessageFlag = "";
    private File videoAsFile;
    private byte[] videoAsBytes;
    private String videoAsUrl;
    private String caption;
    private Keyboard keyboard;
    private List<File> videosAsFiles;
    private List<String> captions;
    private boolean notify = false;

    @Override
    public void run() {
        if ((videoAsFile != null || videoAsBytes != null || videoAsUrl != null) && videosAsFiles != null) {
            throw new IllegalArgumentException("You can't send a video and a list of videos at the same time");
        }

        if (videoAsFile != null) {
            sendVideo(chatId, videoAsFile, caption, onMessageFlag, notify);
        } else if (videoAsBytes != null) {
            sendVideo(chatId, videoAsBytes, caption, onMessageFlag, notify);
        } else if (videoAsUrl != null) {
            sendVideo(chatId, videoAsUrl, caption, onMessageFlag, notify);
        } else if (videosAsFiles != null) {
            sendVideos(chatId, videosAsFiles, captions, onMessageFlag, notify);
        }
        if (keyboard != null) {
            sendKeyboard(chatId, keyboard, onMessageFlag, notify);
        }
    }
}
