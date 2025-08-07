package space.zoommax;

import com.pengrad.telegrambot.model.Update;

public class GroupBotApp implements Runnable{
    private Update update;
    public GroupBotApp(Update update){
        this.update = update;
    }

    @Override
    public void run() {

    }
}
