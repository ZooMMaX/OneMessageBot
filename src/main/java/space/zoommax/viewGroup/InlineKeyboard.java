package space.zoommax.viewGroup;

import lombok.Builder;
import space.zoommax.utils.keyboard.Keyboard;

import java.util.HashMap;

@Builder
public class InlineKeyboard implements ViewMessage {
    private String viewMessageTextEncoded;
    private long chatId;
    private Keyboard keyboard;
    @Builder.Default
    private String onMessageFlag = "";
    @Builder.Default
    private boolean notify = false;
    private String viewMessageToUpdate;
    @Builder.Default
    private boolean needUpdate = false;
    @Builder.Default
    private long updateTime = 0;

    public String toString() {
        return "InlineKeyboard{" +
                "chatId=" + chatId +
                ", keyboard=" + keyboard.keyboardButtonsToString() +
                ", onMessageFlag=" + onMessageFlag +
                ", notify=" + notify +
                '}';
    }

    public InlineKeyboard fstring() {
        if (viewMessageTextEncoded != null && viewMessageTextEncoded.startsWith("InlineKeyboard")) {
            viewMessageTextEncoded = viewMessageTextEncoded.substring(0, viewMessageTextEncoded.length() - 1).replace("InlineKeyboard{", "")
                    .replaceAll("=,", "=null,");
            HashMap<String, String> map = new HashMap<>();
            for (String s : viewMessageTextEncoded.split(",")) {
                String[] keyValue = s.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
            this.chatId = Long.parseLong(map.get("chatId"));
            this.keyboard = Keyboard.builder()
                    .chatId(Long.parseLong(map.get("chatId")))
                    .code(map.get("keyboard"))
                    .build();
            if (map.get("onMessageFlag").equals("null")) {
                this.onMessageFlag = "";
            }else {
                this.onMessageFlag = map.get("onMessageFlag");
            }
            this.notify = Boolean.parseBoolean(map.get("notify"));
            return this;
        }
        return null;
    }

    @Override
    public void run() {
        if (keyboard != null) {
            sendKeyboard(chatId, keyboard, onMessageFlag, notify);
        }
        if (!notify) {
            if (needUpdate) {
                updateViewMessageTime(chatId, needUpdate, viewMessageToUpdate, updateTime);
            } else {
                updateViewMessageTime(chatId, false, "", 0);
            }
        }
    }
}
