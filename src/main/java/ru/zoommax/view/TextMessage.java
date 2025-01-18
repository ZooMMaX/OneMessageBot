package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

@Builder
public class TextMessage implements ViewMessage{
    private String text;
    private long chatId;
    private Keyboard keyboard;
    private String onMessageFlag =  "";
    private boolean notify = false;

    @Override
    public void run() {
        sendText(chatId, text, onMessageFlag, notify);
        sendKeyboard(chatId, keyboard, onMessageFlag, notify);
    }
}
