package ru.zoommax.view;

import lombok.Builder;
import ru.zoommax.utils.keyboard.Keyboard;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class VideoMessage implements ViewMessage {
    private String viewMessageTextEncoded;
    private long chatId;
    @Builder.Default
    private String onMessageFlag = "";
    private File videoAsFile;
    private byte[] videoAsBytes;
    private String videoAsUrl;
    private String caption;
    private Keyboard keyboard;
    private List<File> videosAsFiles;
    private List<String> captions;
    @Builder.Default
    private boolean notify = false;
    private String viewMessageToUpdate;
    @Builder.Default
    private boolean needUpdate = false;
    @Builder.Default
    private long updateTime = 0;

    public String toString() {
        return "VideoMessage{" +
                "chatId=" + chatId +
                ",onMessageFlag=" + onMessageFlag +
                ",videoAsFile=" + videoAsFile +
                ",videoAsBytes=" + Base64.getEncoder().encodeToString(videoAsBytes) +
                ",videoAsUrl=" + videoAsUrl +
                ",caption=" + caption +
                ",keyboard=" + keyboard.keyboardButtonsToString() +
                ",videosAsFiles=" + videosAsFiles +
                ",captions=" + captions +
                ",notify=" + notify +
                '}';
    }

    public VideoMessage fstring() {
        if (viewMessageTextEncoded != null && viewMessageTextEncoded.startsWith("VideoMessage")) {
            viewMessageTextEncoded = viewMessageTextEncoded.substring(0, viewMessageTextEncoded.length() - 1).replace("VideoMessage{", "")
                    .replaceAll("=,", "=null,");
            HashMap<String, String> map = new HashMap<>();
            for (String s : viewMessageTextEncoded.split(",")) {
                String[] keyValue = s.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
            chatId = Long.parseLong(map.get("chatId"));
            if (map.get("onMessageFlag").equals("null")) {
                this.onMessageFlag = "";
            }else {
                this.onMessageFlag = map.get("onMessageFlag");
            }
            videoAsFile = new File(map.get("videoAsFile"));
            videoAsBytes = Base64.getDecoder().decode(map.get("videoAsBytes"));
            videoAsUrl = map.get("videoAsUrl");
            caption = map.get("caption");
            keyboard = Keyboard.builder()
                    .chatId(Long.parseLong(map.get("chatId")))
                    .code(map.get("keyboard"))
                    .build();
            videosAsFiles = Arrays.stream(map.get("videosAsFiles").split(",")).map(File::new).collect(Collectors.toList());
            captions = Arrays.stream(map.get("captions").split(",")).collect(Collectors.toList());
            notify = Boolean.parseBoolean(map.get("notify"));
            return this;
        }
        return null;
    }

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
        if (!notify) {
            if (needUpdate) {
                updateViewMessageTime(chatId, needUpdate, viewMessageToUpdate, updateTime);
            } else {
                updateViewMessageTime(chatId, false, "", 0);
            }
        }
    }
}
