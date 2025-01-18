package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

@Builder
public class InlineKeyboard implements ViewMessage {
    private long chatId;
    private Keyboard keyboard;
    private String onMessageFlag = "";
    private boolean notify = false;

    @Override
    public void run() {
        sendKeyboard(chatId, keyboard, onMessageFlag, notify);
    }
}
