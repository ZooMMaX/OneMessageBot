package space.zoommax.db;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import space.zoommax.BotApp;
import ru.zoommax.MongoDBConnector;
import space.zoommax.utils.db.DbType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Getter
@Setter
public class MessageTimeUpdatePojo extends MongoDBConnector {
    private long chatId;
    private long updateTime;
    private long viewMessageBeforeUpdateTime;
    private long lastViewMessageUpdateTime;
    private boolean needUpdate = false;
    private String viewMessageToUpdate;

    public MessageTimeUpdatePojo(){};

    @SuppressWarnings("unchecked")
    private MongoCollection<MessageTimeUpdatePojo> collection() {
        return (MongoCollection<MessageTimeUpdatePojo>) getCollection("messageToUpdate", BotApp.botSettings.getDbName(), this);
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

    public MessageTimeUpdatePojo find() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("chatId", this.chatId)).first();
        } else {
            return sqlFind(BotApp.botSettings.getDbConnection());
        }
    }

    public List<MessageTimeUpdatePojo> findAll() {
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
            updateTime BIGINT,
            viewMessageBeforeUpdateTime BIGINT,
            lastViewMessageUpdateTime BIGINT,
            needUpdate BOOLEAN,
            viewMessageToUpdate TEXT
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
        final String query = "INSERT INTO users (chatId, updateTime, viewMessageBeforeUpdateTime, lastViewMessageUpdateTime, needUpdate, viewMessageToUpdate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.chatId);
            statement.setLong(2, this.updateTime);
            statement.setLong(3, this.viewMessageBeforeUpdateTime);
            statement.setLong(4, this.lastViewMessageUpdateTime);
            statement.setBoolean(5, this.needUpdate);
            statement.setString(6, this.viewMessageToUpdate);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlUpdate(Connection connection) {
        final String query = "UPDATE users SET updateTime = ?, viewMessageBeforeUpdateTime = ?, lastViewMessageUpdateTime = ?, needUpdate = ?, viewMessageToUpdate = ? WHERE chatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.updateTime);
            statement.setBoolean(2, this.needUpdate);
            statement.setLong(3, this.lastViewMessageUpdateTime);
            statement.setLong(4, this.viewMessageBeforeUpdateTime);
            statement.setString(5, this.viewMessageToUpdate);
            statement.setLong(6, this.chatId);
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

    public MessageTimeUpdatePojo sqlFind(Connection connection) {
        final String query = "SELECT * FROM users WHERE chatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, this.chatId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                MessageTimeUpdatePojo messageTimeUpdatePojo = new MessageTimeUpdatePojo();
                messageTimeUpdatePojo.setChatId(rs.getLong("chatId"));
                messageTimeUpdatePojo.setUpdateTime(rs.getLong("updateTime"));
                messageTimeUpdatePojo.setViewMessageBeforeUpdateTime(rs.getLong("viewMessageBeforeUpdateTime"));
                messageTimeUpdatePojo.setLastViewMessageUpdateTime(rs.getLong("lastViewMessageUpdateTime"));
                messageTimeUpdatePojo.setNeedUpdate(rs.getBoolean("needUpdate"));
                messageTimeUpdatePojo.setViewMessageToUpdate(rs.getString("viewMessageToUpdate"));
                return messageTimeUpdatePojo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MessageTimeUpdatePojo> sqlFindAll(Connection connection) {
        final String query = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            List<MessageTimeUpdatePojo> messageTimeUpdatePojoList = new ArrayList<>();
            while (rs.next()) {
                MessageTimeUpdatePojo messageTimeUpdatePojo = new MessageTimeUpdatePojo();
                messageTimeUpdatePojo.setChatId(rs.getLong("chatId"));
                messageTimeUpdatePojo.setUpdateTime(rs.getLong("updateTime"));
                messageTimeUpdatePojo.setViewMessageBeforeUpdateTime(rs.getLong("viewMessageBeforeUpdateTime"));
                messageTimeUpdatePojo.setLastViewMessageUpdateTime(rs.getLong("lastViewMessageUpdateTime"));
                messageTimeUpdatePojo.setNeedUpdate(rs.getBoolean("needUpdate"));
                messageTimeUpdatePojo.setViewMessageToUpdate(rs.getString("viewMessageToUpdate"));
                messageTimeUpdatePojoList.add(messageTimeUpdatePojo);
            }
            return messageTimeUpdatePojoList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
