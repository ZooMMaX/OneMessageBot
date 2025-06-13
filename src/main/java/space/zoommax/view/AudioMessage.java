package space.zoommax.view;

import lombok.Builder;
import space.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Builder
public class AudioMessage implements ViewMessage {
    private String viewMessageTextEncoded;
    private long chatId;
    @Builder.Default
    private String onMessageFlag = "";
    private File audioAsFile;
    private byte[] audioAsBytes;
    private String audioAsUrl;
    private String caption;
    private Keyboard keyboard;
    private List<File> audiosAsFiles;
    private List<String> captions;
    @Builder.Default
    private boolean notify = false;
    private String viewMessageToUpdate;
    @Builder.Default
    private boolean needUpdate = false;
    @Builder.Default
    private long updateTime = 0;

    public String toString() {
        return "AudioMessage{" +
                "chatId=" + chatId +
                ",onMessageFlag=" + onMessageFlag +
                ",audioAsFile=" + audioAsFile +
                ",audioAsBytes=" + Base64.getEncoder().encodeToString(audioAsBytes) +
                ",audioAsUrl=" + audioAsUrl +
                ",caption=" + caption +
                ",keyboard=" + keyboard +
                ",audiosAsFiles=" + audiosAsFiles +
                ",captions=" + captions +
                ",notify=" + notify +
                '}';
    }

    public AudioMessage fstring() {
        if (viewMessageTextEncoded != null && viewMessageTextEncoded.startsWith("AudioMessage")) {
            viewMessageTextEncoded = viewMessageTextEncoded.substring(0, viewMessageTextEncoded.length() - 1).replace("AudioMessage{", "")
                    .replaceAll("=,", "=null,");
            HashMap<String, String> map = new HashMap<>();
            for (String s : viewMessageTextEncoded.split(",")) {
                String[] keyValue = s.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
            this.chatId = Long.parseLong(map.get("chatId"));
            if (map.get("onMessageFlag").equals("null")) {
                this.onMessageFlag = "";
            }else {
                this.onMessageFlag = map.get("onMessageFlag");
            }
            this.audioAsFile = new File(map.get("audioAsFile"));
            this.audioAsBytes = Base64.getDecoder().decode(map.get("audioAsBytes"));
            this.audioAsUrl = map.get("audioAsUrl");
            this.caption = map.get("caption");
            this.keyboard = Keyboard.builder()
                    .chatId(Long.parseLong(map.get("chatId")))
                    .code(map.get("keyboard"))
                    .build();
            this.audiosAsFiles = null;
            this.captions = null;
            this.notify = Boolean.parseBoolean(map.get("notify"));
            return this;
        }
        return null;
    }

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
        if (!notify) {
            if (needUpdate) {
                updateViewMessageTime(chatId, needUpdate, viewMessageToUpdate, updateTime);
            } else {
                updateViewMessageTime(chatId, false, "", 0);
            }
        }
    }
}
