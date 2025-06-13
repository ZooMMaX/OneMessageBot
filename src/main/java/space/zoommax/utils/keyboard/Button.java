package space.zoommax.utils.keyboard;

import lombok.Getter;

@Getter
public class Button {
    private final String text;
    private String action;
    private final ButtonType type;

    public Button(String text, String action, ButtonType type) {
        this.text = text;
        this.action = action;
        this.type = type;
    }

    public Button(String text, String action) {
        this.text = text;
        this.action = action;
        if (action.startsWith("mapp")){
            this.action = action.substring(4);
            this.type = ButtonType.MINI_APP;
        }else if (action.startsWith("http") || action.startsWith("tg")){
            this.type = ButtonType.LINK;
        } else if (action.startsWith("pay")) {
            this.type = ButtonType.PAY;
        } else {
            this.type = ButtonType.CALLBACK;
        }
    }

    public Button(String code) {
        code = code.replaceFirst("\\{", "").replaceFirst("\\}", "").replaceAll(";}", ";null}");
        String[] btn = code.split("(?<!\\\\);");
        this.text = btn[0].replace("\\{", "{").replace("\\}", "}").replace("\\;", ";");
        this.action = btn[1].replace("\\{", "{").replace("\\}", "}").replace("\\;", ";");
        if (action.startsWith("mapp")){
            this.action = action.substring(4);
            this.type = ButtonType.MINI_APP;
        }else if (action.startsWith("http") || action.startsWith("tg")){
            this.type = ButtonType.LINK;
        }else if (action.startsWith("pay")) {
            this.type = ButtonType.PAY;
        } else {
            this.type = ButtonType.CALLBACK;
        }
    }
}
