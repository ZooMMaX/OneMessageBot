package ru.zoommax;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.zoommax.utils.db.DbType;

import java.sql.Connection;

@Builder
@Getter
@Setter
public class BotSettings {
    private String botToken;
    private String languageDirPath = "";
    private String defaultLanguage = "default_en_US";
    private int buttonsRows = 4;

    //database
    private DbType dbType = DbType.SQLITE;
    private String dbName = "BotApp";
    private String dbUrl = "jdbc:sqlite:./";
    private String dbUser = "";
    private String dbPassword = "";
    private Connection dbConnection = null;

    //disable github url
    private String disableGithubUrl = "disabled";
}
