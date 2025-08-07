package space.zoommax.viewGroup;

import lombok.Builder;
import space.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Builder
public class DocumentMessage implements ViewMessage {
    private String viewMessageTextEncoded;
    private long chatId;
    @Builder.Default
    private String onMessageFlag = "";
    private File fileAsFile;
    private String fileAsString;
    private byte[] fileAsBytes;
    private String caption;
    private Keyboard keyboard;
    private List<File> filesAsFiles;
    private List<String> captions;
    @Builder.Default
    private boolean notify = false;
    private String viewMessageToUpdate;
    @Builder.Default
    private boolean needUpdate = false;
    @Builder.Default
    private long updateTime = 0;

    public String toString() {
        return "DocumentMessage{" +
                "chatId=" + chatId +
                ", onMessageFlag=" + onMessageFlag+
                ", fileAsFile=" + fileAsFile +
                ", fileAsString=" + fileAsString +
                ", fileAsBytes=" + Base64.getEncoder().encodeToString(fileAsBytes) +
                ", caption=" + caption +
                ", keyboard=" + keyboard +
                ", filesAsFiles=" + filesAsFiles +
                ", captions=" + captions +
                ", notify=" + notify +
                '}';
    }

    public DocumentMessage fstring() {
        if (viewMessageTextEncoded != null && viewMessageTextEncoded.startsWith("DocumentMessage")) {
            viewMessageTextEncoded = viewMessageTextEncoded.substring(0, viewMessageTextEncoded.length() - 1).replace("DocumentMessage{", "")
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
            this.fileAsFile = new File(map.get("fileAsFile"));
            this.fileAsString = map.get("fileAsString");
            this.fileAsBytes = Base64.getDecoder().decode(map.get("fileAsBytes"));
            this.caption = map.get("caption");
            this.keyboard = Keyboard.builder()
                    .chatId(Long.parseLong(map.get("chatId")))
                    .code(map.get("keyboard"))
                    .build();
            this.filesAsFiles = null;
            this.captions = null;
            this.notify = Boolean.parseBoolean(map.get("notify"));
            return this;
        }
        return null;
    }

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
        if (!notify) {
            if (needUpdate) {
                updateViewMessageTime(chatId, needUpdate, viewMessageToUpdate, updateTime);
            } else {
                updateViewMessageTime(chatId, false, "", 0);
            }
        }
    }
}
