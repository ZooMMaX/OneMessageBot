package space.zoommax;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import space.zoommax.utils.db.DbType;

import java.sql.Connection;

@Builder
@Getter
@Setter
public class BotSettings {
    private String botToken;
    @Builder.Default
    private String languageDirPath = "";
    @Builder.Default
    private String defaultLanguage = "default_en_US";
    @Builder.Default
    private int buttonsRows = 4;

    //database
    @Builder.Default
    private DbType dbType = DbType.SQLITE;
    @Builder.Default
    private String dbName = "BotApp";
    @Builder.Default
    private String dbUrl = "jdbc:sqlite:./";
    @Builder.Default
    private String dbUser = "";
    @Builder.Default
    private String dbPassword = "";
    @Builder.Default
    private Connection dbConnection = null;

    //disable github url
    @Builder.Default
    private String disableGithubUrl = "disabled";
}
