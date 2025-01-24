package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.List;

@Builder
public class AudioMessage implements ViewMessage {
    private long chatId;
    private String onMessageFlag = "";
    private File audioAsFile;
    private byte[] audioAsBytes;
    private String audioAsUrl;
    private String caption;
    private Keyboard keyboard;
    private List<File> audiosAsFiles;
    private List<String> captions;
    private boolean notify = false;

    @Override
    public void run() {
        if ((audioAsFile != null || audioAsBytes != null || audioAsUrl != null) && audiosAsFiles != null) {
            throw new IllegalArgumentException("You can't send an audio and a list of audios at the same time");
        }

        if (audioAsFile != null) {
            sendAudio(chatId, audioAsFile, caption, onMessageFlag, notify);
        } else if (audioAsBytes != null) {
            sendAudio(chatId, audioAsBytes, caption, onMessageFlag, notify);
        } else if (audioAsUrl != null) {
            sendAudio(chatId, audioAsUrl, caption, onMessageFlag, notify);
        } else if (audiosAsFiles != null) {
            sendAudios(chatId, audiosAsFiles, captions, onMessageFlag, notify);
        }
        if (keyboard != null) {
            sendKeyboard(chatId, keyboard, onMessageFlag, notify);
        }
    }
}
