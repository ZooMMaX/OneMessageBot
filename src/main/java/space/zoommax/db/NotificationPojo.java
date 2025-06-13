package space.zoommax.db;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import space.zoommax.BotApp;
import ru.zoommax.MongoDBConnector;
import space.zoommax.utils.db.DbType;
import space.zoommax.utils.db.NotificationType;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;

import static com.mongodb.client.model.Filters.eq;

@Setter
@Getter
public class NotificationPojo extends MongoDBConnector {
    private String uid = guid();
    private String tg_id;
    private String message;
    private Long date = System.currentTimeMillis();
    private boolean read = false;
    private NotificationType notificationType = NotificationType.ALERT;
    private String file = "";
    private String fileType = "";


    public String guid() {
        CRC32 crc32 = new CRC32();
        crc32.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        String result = Long.toHexString(crc32.getValue());
        crc32.reset();
        return result;
    }

    public NotificationPojo() {
    }

    @SuppressWarnings("unchecked")
    private MongoCollection<NotificationPojo> collection() {
        return (MongoCollection<NotificationPojo>) getCollection("notifications", BotApp.botSettings.getDbName(), this);
    }

    public boolean exist() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("uid", this.uid)).first() != null;
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
            final String result = collection().replaceOne(eq("uid", this.uid), this).toString();
            return result.contains("matchedCount=1");
        } else {
            return sqlUpdate(BotApp.botSettings.getDbConnection());
        }
    }

    public boolean delete() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            final String result = collection().deleteOne(eq("uid", this.uid)).toString();
            return result.contains("deletedCount=1");
        } else {
            return sqlDelete(BotApp.botSettings.getDbConnection());
        }
    }

    public NotificationPojo find() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("uid", this.uid)).first();
        } else {
            return sqlFind(BotApp.botSettings.getDbConnection());
        }
    }

    public List<NotificationPojo> findAllByTgId() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("tg_id", this.tg_id)).into(new ArrayList<>());
        } else {
            return sqlFindAllByTgId(BotApp.botSettings.getDbConnection());
        }
    }

    public List<NotificationPojo> findAll() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find().into(new ArrayList<>());
        } else {
            return sqlFindAll(BotApp.botSettings.getDbConnection());
        }
    }

    public NotificationPojo findByUID() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("uid", this.uid)).first();
        } else {
            return sqlFindByUID(BotApp.botSettings.getDbConnection());
        }
    }

    public void ensureTableExists(Connection connection) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS notifications (uid TEXT PRIMARY KEY, tg_id TEXT, message TEXT, date BIGINT, is_read BOOLEAN, notificationType TEXT, file TEXT, fileType TEXT)";
        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.executeUpdate();
            System.out.println("Table checked or created: notifications");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean sqlExist(Connection connection) {
        final String query = "SELECT COUNT(*) FROM notifications WHERE uid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.uid);
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

        final String query = "INSERT INTO notifications (uid, tg_id, message, date, is_read, notificationType, file, fileType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.uid);
            statement.setString(2, this.tg_id);
            statement.setString(3, this.message);
            statement.setLong(4, this.date);
            statement.setBoolean(5, this.read);
            statement.setString(6, this.notificationType.name());
            statement.setString(7, this.file);
            statement.setString(8, this.fileType);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlUpdate(Connection connection) {
        final String query = "UPDATE notifications SET tg_id = ?, message = ?, date = ?, is_read = ?, notificationType = ?, file = ?, fileType = ? WHERE uid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.tg_id);
            statement.setString(2, this.message);
            statement.setLong(3, this.date);
            statement.setBoolean(4, this.read);
            statement.setString(5, this.notificationType.name());
            statement.setString(6, this.file);
            statement.setString(7, this.fileType);
            statement.setString(8, this.uid);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlDelete(Connection connection) {
        final String query = "DELETE FROM notifications WHERE uid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.uid);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public NotificationPojo sqlFind(Connection connection) {
        final String query = "SELECT * FROM notifications WHERE uid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.uid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                NotificationPojo notification = new NotificationPojo();
                notification.setUid(rs.getString("uid"));
                notification.setTg_id(rs.getString("tg_id"));
                notification.setMessage(rs.getString("message"));
                notification.setDate(rs.getLong("date"));
                notification.setRead(rs.getBoolean("is_read"));
                notification.setNotificationType(NotificationType.valueOf(rs.getString("notificationType")));
                notification.setFile(rs.getString("file"));
                notification.setFileType(rs.getString("fileType"));
                return notification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NotificationPojo> sqlFindAll(Connection connection) {
        final String query = "SELECT * FROM notifications";
        List<NotificationPojo> notifications = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                NotificationPojo notification = new NotificationPojo();
                notification.setUid(rs.getString("uid"));
                notification.setTg_id(rs.getString("tg_id"));
                notification.setMessage(rs.getString("message"));
                notification.setDate(rs.getLong("date"));
                notification.setRead(rs.getBoolean("is_read"));
                notification.setNotificationType(NotificationType.valueOf(rs.getString("notificationType")));
                notification.setFile(rs.getString("file"));
                notification.setFileType(rs.getString("fileType"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public NotificationPojo sqlFindByUID(Connection connection) {
        final String query = "SELECT * FROM notifications WHERE uid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.uid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                NotificationPojo notification = new NotificationPojo();
                notification.setUid(rs.getString("uid"));
                notification.setTg_id(rs.getString("tg_id"));
                notification.setMessage(rs.getString("message"));
                notification.setDate(rs.getLong("date"));
                notification.setRead(rs.getBoolean("is_read"));
                notification.setNotificationType(NotificationType.valueOf(rs.getString("notificationType")));
                notification.setFile(rs.getString("file"));
                notification.setFileType(rs.getString("fileType"));
                return notification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NotificationPojo> sqlFindAllByTgId(Connection connection) {
        final String query = "SELECT * FROM notifications WHERE tg_id = ?";
        List<NotificationPojo> notifications = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.tg_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                NotificationPojo notification = new NotificationPojo();
                notification.setUid(rs.getString("uid"));
                notification.setTg_id(rs.getString("tg_id"));
                notification.setMessage(rs.getString("message"));
                notification.setDate(rs.getLong("date"));
                notification.setRead(rs.getBoolean("is_read"));
                notification.setNotificationType(NotificationType.valueOf(rs.getString("notificationType")));
                notification.setFile(rs.getString("file"));
                notification.setFileType(rs.getString("fileType"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

}
