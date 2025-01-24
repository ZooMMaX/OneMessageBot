package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.List;

@Builder
public class DocumentMessage implements ViewMessage{
    private long chatId;
    private String onMessageFlag = "";
    private File fileAsFile;
    private String fileAsString;
    private byte[] fileAsBytes;
    private String caption;
    private Keyboard keyboard;
    private List<File> filesAsFiles;
    private List<String> captions;
    private boolean notify = false;

    @Override
    public void run() {
        if ((fileAsFile != null || fileAsString != null || fileAsBytes != null) && filesAsFiles != null) {
            throw new IllegalArgumentException("You can't send a document and a list of documents at the same time");
        }

        if (fileAsFile != null) {
            sendDocument(chatId, fileAsFile, caption, onMessageFlag, notify);
        } else if (fileAsString != null) {
            sendDocument(chatId, fileAsString, caption, onMessageFlag, notify);
        } else if (fileAsBytes != null) {
            sendDocument(chatId, fileAsBytes, caption, onMessageFlag, notify);
        } else if (filesAsFiles != null) {
            sendDocuments(chatId, filesAsFiles, captions, onMessageFlag, notify);
        }
        if (keyboard != null) {
            sendKeyboard(chatId, keyboard, onMessageFlag, notify);
        }
    }
}
