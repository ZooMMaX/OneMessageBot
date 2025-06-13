package space.zoommax.db;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import space.zoommax.BotApp;
import ru.zoommax.MongoDBConnector;
import space.zoommax.utils.db.DbType;
import space.zoommax.utils.db.MessageType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Getter
@Setter
public class UserPojo extends MongoDBConnector {
    private long chatId;
    private long ViewMessageId;
    private long lastMessageId;
    private MessageType messageType;
    private String language = BotApp.botSettings.getDefaultLanguage();
    private String onMessageFlag = "";
    private long notificationMessageId = -100;
    private boolean showNotif = true;

    public UserPojo(){}

    @SuppressWarnings("unchecked")
    private MongoCollection<UserPojo> collection() {
        return (MongoCollection<UserPojo>) getCollection("users", BotApp.botSettings.getDbName(), this);
    }

    public boolean exist() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("chatId", this.chatId)).first() != null;
        } else {
            return sqlExist(BotApp.botSettings.getDbConnection());
        }
    }

    public boolean insert() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            if (exist())
                return update();

            final String result = collection().insertOne(this).toString();
            return result.contains("insertedId");
        } else {
            return sqlInsert(BotApp.botSettings.getDbConnection());
        }
    }

    public boolean update() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            final String result = collection().replaceOne(eq("chatId", this.chatId), this).toString();
            return result.contains("matchedCount=1");
        } else {
            return sqlUpdate(BotApp.botSettings.getDbConnection());
        }
    }

    public boolean delete() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            final String result = collection().deleteOne(eq("chatId", this.chatId)).toString();
            return result.contains("deletedCount=1");
        } else {
            return sqlDelete(BotApp.botSettings.getDbConnection());
        }
    }

    public UserPojo find() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("chatId", this.chatId)).first();
        } else {
            return sqlFind(BotApp.botSettings.getDbConnection());
        }
    }

    public List<UserPojo> findAll() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find().into(new ArrayList<>());
        } else {
            return sqlFindAll(BotApp.botSettings.getDbConnection());
        }
    }


    public void ensureTableExists(Connection connection) {
        String createTableQuery = """
        CREATE TABLE IF NOT EXISTS users (
            chatId BIGINT PRIMARY KEY,
            ViewMessageId BIGINT,
            lastMessageId BIGINT,
            messageType TEXT,
            language TEXT,
            onMessageFlag TEXT,
            notificationMessageId BIGINT,
            showNotif BOOLEAN
        )
        """;
        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.executeUpdate();
            System.out.println("Table checked or created: users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean sqlExist(Connection connection) {
        final String query = "SELECT COUNT(*) FROM users WHERE chatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.chatId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlInsert(Connection connection) {
        if (sqlExist(connection))
            return sqlUpdate(connection);

        final String query = "INSERT INTO users (chatId, ViewMessageId, lastMessageId, messageType, language, onMessageFlag, notificationMessageId, showNotif) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.chatId);
            statement.setLong(2, this.ViewMessageId);
            statement.setLong(3, this.lastMessageId);
            statement.setString(4, this.messageType != null ? this.messageType.name() : null);
            statement.setString(5, this.language);
            statement.setString(6, this.onMessageFlag);
            statement.setLong(7, this.notificationMessageId);
            statement.setBoolean(8, this.showNotif);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlUpdate(Connection connection) {
        final String query = "UPDATE users SET ViewMessageId = ?, lastMessageId = ?, messageType = ?, language = ?, onMessageFlag = ?, notificationMessageId = ?, showNotif = ? WHERE chatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.ViewMessageId);
            statement.setLong(2, this.lastMessageId);
            statement.setString(3, this.messageType != null ? this.messageType.name() : null);
            statement.setString(4, this.language);
            statement.setString(5, this.onMessageFlag);
            statement.setLong(6, this.notificationMessageId);
            statement.setBoolean(7, this.showNotif);
            statement.setLong(8, this.chatId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlDelete(Connection connection) {
        final String query = "DELETE FROM users WHERE chatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.chatId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserPojo sqlFind(Connection connection) {
        final String query = "SELECT * FROM users WHERE chatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.chatId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                UserPojo user = new UserPojo();
                user.setChatId(rs.getLong("chatId"));
                user.setViewMessageId(rs.getLong("ViewMessageId"));
                user.setLastMessageId(rs.getLong("lastMessageId"));
                user.setMessageType(rs.getString("messageType") != null ? MessageType.valueOf(rs.getString("messageType")) : null);
                user.setLanguage(rs.getString("language"));
                user.setOnMessageFlag(rs.getString("onMessageFlag"));
                user.setNotificationMessageId(rs.getLong("notificationMessageId"));
                user.setShowNotif(rs.getBoolean("showNotif"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserPojo> sqlFindAll(Connection connection) {
        final String query = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            List<UserPojo> users = new ArrayList<>();
            while (rs.next()) {
                UserPojo user = new UserPojo();
                user.setChatId(rs.getLong("chatId"));
                user.setViewMessageId(rs.getLong("ViewMessageId"));
                user.setLastMessageId(rs.getLong("lastMessageId"));
                user.setMessageType(rs.getString("messageType") != null ? MessageType.valueOf(rs.getString("messageType")) : null);
                user.setLanguage(rs.getString("language"));
                user.setOnMessageFlag(rs.getString("onMessageFlag"));
                user.setNotificationMessageId(rs.getLong("notificationMessageId"));
                user.setShowNotif(rs.getBoolean("showNotif"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
