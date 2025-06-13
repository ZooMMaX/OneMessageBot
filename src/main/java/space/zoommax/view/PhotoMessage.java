package space.zoommax.view;

import lombok.Builder;
import space.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Builder
public class PhotoMessage implements ViewMessage {
    private String viewMessageTextEncoded;
    private long chatId;
    @Builder.Default
    private String onMessageFlag = "";
    private File photoAsFile;
    private byte[] photoAsBytes;
    private String photoAsUrl;
    private String caption;
    private Keyboard keyboard;
    private List<File> photosAsFiles;
    private List<String> captions;
    @Builder.Default
    private boolean notify = false;
    private String viewMessageToUpdate;
    @Builder.Default
    private boolean needUpdate = false;
    @Builder.Default
    private long updateTime = 0;

    public String toString() {
        return "PhotoMessage{" +
                "chatId=" + chatId +
                ",onMessageFlag=" + onMessageFlag +
                ",photoAsFile=" + photoAsFile +
                ",photoAsBytes=" + Base64.getEncoder().encodeToString(photoAsBytes) +
                ",photoAsUrl=" + photoAsUrl +
                ",caption=" + caption +
                ",keyboard=" + keyboard +
                ",photosAsFiles=" + photosAsFiles +
                ",captions=" + captions +
                ",notify=" + notify +
                '}';
    }

    public PhotoMessage fstring(){
        if (viewMessageTextEncoded != null && viewMessageTextEncoded.startsWith("PhotoMessage")) {
            viewMessageTextEncoded = viewMessageTextEncoded.substring(0, viewMessageTextEncoded.length() - 1).replace("PhotoMessage{", "")
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
            this.photoAsFile = new File(map.get("photoAsFile"));
            this.photoAsBytes = Base64.getDecoder().decode(map.get("photoAsBytes"));
            this.photoAsUrl = map.get("photoAsUrl");
            this.caption = map.get("caption");
            this.keyboard = Keyboard.builder()
                    .chatId(Long.parseLong(map.get("chatId")))
                    .code(map.get("keyboard"))
                    .build();
            this.photosAsFiles = null;
            this.captions = null;
            this.notify = Boolean.parseBoolean(map.get("notify"));
            return this;
        }
        return null;
    }

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
