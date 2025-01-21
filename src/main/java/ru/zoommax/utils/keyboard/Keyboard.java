package ru.zoommax.utils.keyboard;

import com.pengrad.telegrambot.model.WebAppInfo;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import ru.zoommax.BotApp;
import ru.zoommax.db.UserMarkupsPojo;
import ru.zoommax.db.UserPojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Builder
public class Keyboard {
    private String code;
    private List<List<Button>> keyboardButtons;
    private InlineKeyboardMarkup keyboard;
    private long chatId;
    private int pageKb = 0;
    private boolean notify = false;
    private int rows = 0;

    private void check() {
        if (code != null && keyboardButtons != null) {
            log.error("Keyboard cannot have both code and buttons");
            throw new IllegalArgumentException("Keyboard cannot have both code and buttons");
        }
    }

    public InlineKeyboardMarkup getKeyboard() {
        UserPojo userPojo = new UserPojo();
        userPojo.setChatId(chatId);
        userPojo = userPojo.find();
        if (keyboard != null) {
            return keyboard;
        }
        check();
        if (code != null) {
            keyboardButtons = new ArrayList<>();
            List<List<String>> bracedStrings = parseBracedString(code);
            for (List<String> bracedString : bracedStrings) {
                List<Button> buttons = new ArrayList<>();
                for (String s : bracedString) {
                    Button button = new Button(s);
                    buttons.add(button);
                }
                keyboardButtons.add(buttons);
            }
        }

        UserMarkupsPojo userMarkupsPojo = new UserMarkupsPojo();
        userMarkupsPojo.setTg_id(String.valueOf(chatId));
        userMarkupsPojo = userMarkupsPojo.find();
        if (!notify) {
            userMarkupsPojo.setCode(keyboardButtonsToString());
        } else {
            userMarkupsPojo.setCodeNotify(keyboardButtonsToString());
        }
        userMarkupsPojo.insert();

        List<List<List<Button>>> keyboardPages = new ArrayList<>();

        int butRows = BotApp.botSettings.getButtonsRows();

        if (rows > 0) {
            butRows = rows;
        }

        for (int i = 0; i < keyboardButtons.size(); i += butRows) {
            keyboardPages.add(keyboardButtons.subList(i, Math.min(i + butRows, keyboardButtons.size())));
        }

        List<InlineKeyboardMarkup> keyboardMarkups = new ArrayList<>();
        for (int i = 0; i < keyboardPages.size(); i++) {
            List<List<Button>> page = keyboardPages.get(i);
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            for (int j = 0; j < page.size(); j++) {
                List<InlineKeyboardButton> btns = new ArrayList<>();
                for (int k = 0; k < page.get(j).size(); k++) {
                    if (page.get(j).get(k).getType() == ButtonType.CALLBACK) {
                        btns.add(new InlineKeyboardButton(page.get(j).get(k).getText()).callbackData(page.get(j).get(k).getAction()));
                    } else if (page.get(j).get(k).getType() == ButtonType.LINK) {
                        btns.add(new InlineKeyboardButton(page.get(j).get(k).getText()).url(page.get(j).get(k).getAction()));
                    } else if (page.get(j).get(k).getType() == ButtonType.MINI_APP) {
                        btns.add(new InlineKeyboardButton(page.get(j).get(k).getText()).webApp(new WebAppInfo(page.get(j).get(k).getAction())));
                    }
                }
                keyboardMarkup.addRow(Arrays.copyOf(btns.toArray(), btns.size(), InlineKeyboardButton[].class));
            }
            InlineKeyboardButton buttonNext = null;
            if (i+1 < keyboardPages.size()) {
                buttonNext = new InlineKeyboardButton(BotApp.localizationManager.getTranslationForLanguage(userPojo.getLanguage(),"keyboard.next_button")).callbackData("nextButton:" + i);
            }
            InlineKeyboardButton buttonPrev = null;
            if (i > 0) {
                buttonPrev = new InlineKeyboardButton(BotApp.localizationManager.getTranslationForLanguage(userPojo.getLanguage(),"keyboard.prev_button")).callbackData("prevButton:" + i);
            }
            if (buttonNext != null && buttonPrev != null) {
                keyboardMarkup.addRow(buttonPrev, buttonNext);
            } else if (buttonNext != null) {
                keyboardMarkup.addRow(buttonNext);
            } else if (buttonPrev != null) {
                keyboardMarkup.addRow(buttonPrev);
            }
            keyboardMarkups.add(keyboardMarkup);
        }
        return keyboardMarkups.get(pageKb);
    }

    private String keyboardButtonsToString() {
        StringBuilder sb = new StringBuilder();
        for (List<Button> row : keyboardButtons) {
            for (Button button : row) {
                sb.append("{").append(button.getText().replace(";", "\\;").replace("{", "\\{").replace("}", "\\}")).append(";");
                if (button.getType() == ButtonType.CALLBACK) {
                    sb.append(button.getAction().replace(";", "\\;").replace("{", "\\{").replace("}", "\\}"));
                } else if (button.getType() == ButtonType.LINK) {
                    sb.append(button.getAction().replace(";", "\\;").replace("{", "\\{").replace("}", "\\}"));
                } else if (button.getType() == ButtonType.MINI_APP) {
                    sb.append("mapp").append(button.getAction().replace(";", "\\;").replace("{", "\\{").replace("}", "\\}"));
                }
                sb.append("}");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private List<List<String>> parseBracedString(String input) {
        List<List<String>> result = new ArrayList<>();

        // Split by newline
        String[] lines = input.split("\n");

        for (String line : lines) {
            List<String> pair = new ArrayList<>();
            int start = 0;
            while (start < line.length()) {
                int openBrace = line.indexOf('{', start);
                int closeBrace = line.indexOf('}', openBrace);

                if (openBrace == -1 || closeBrace == -1) {
                    break;
                }

                // Extract content inside braces
                String content = line.substring(openBrace, closeBrace +1);

                pair.add(content);

                start = closeBrace + 1; // Move past the current closing brace
            }
            result.add(pair);
        }

        return result;
    }

}
