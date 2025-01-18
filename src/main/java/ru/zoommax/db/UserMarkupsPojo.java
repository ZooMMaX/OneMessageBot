package ru.zoommax.db;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import ru.zoommax.BotApp;
import ru.zoommax.MongoDBConnector;
import ru.zoommax.utils.db.DbType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Setter
@Getter
public class UserMarkupsPojo extends MongoDBConnector {
    private String tg_id;
    private String code;
    private String codeNotify;

    public UserMarkupsPojo() {
    }

    @SuppressWarnings("unchecked")
    private MongoCollection<UserMarkupsPojo> collection() {
        return (MongoCollection<UserMarkupsPojo>) getCollection("userMarkups", BotApp.botSettings.getDbName(), this);
    }

    public boolean exist() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("tg_id", this.tg_id)).first() != null;
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
            final String result = collection().replaceOne(eq("tg_id", this.tg_id), this).toString();
            return result.contains("matchedCount=1");
        } else {
            return sqlUpdate(BotApp.botSettings.getDbConnection());
        }
    }

    public boolean delete() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            final String result = collection().deleteOne(eq("tg_id", this.tg_id)).toString();
            return result.contains("deletedCount=1");
        } else {
            return sqlDelete(BotApp.botSettings.getDbConnection());
        }
    }

    public UserMarkupsPojo find() {
        if (BotApp.botSettings.getDbType() == DbType.MONGODB) {
            return collection().find(eq("tg_id", this.tg_id)).first();
        } else {
            return sqlFind(BotApp.botSettings.getDbConnection());
        }
    }



    public void ensureTableExists(Connection connection) {
        String createTableQuery = """
    CREATE TABLE IF NOT EXISTS userMarkups (
        tg_id TEXT PRIMARY KEY,
        code TEXT,
        codeNotify TEXT
    )
    """;
        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.executeUpdate();
            System.out.println("Table checked or created: userMarkups");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean sqlExist(Connection connection) {
        final String query = "SELECT COUNT(*) FROM userMarkups WHERE tg_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.tg_id);
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

        final String query = "INSERT INTO userMarkups (tg_id, code, codeNotify) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.tg_id);
            statement.setString(2, this.code);
            statement.setString(3, this.codeNotify);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlUpdate(Connection connection) {
        final String query = "UPDATE userMarkups SET code = ?, codeNotify = ? WHERE tg_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.code);
            statement.setString(2, this.codeNotify);
            statement.setString(3, this.tg_id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sqlDelete(Connection connection) {
        final String query = "DELETE FROM userMarkups WHERE tg_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.tg_id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserMarkupsPojo sqlFind(Connection connection) {
        final String query = "SELECT * FROM userMarkups WHERE tg_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.tg_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                UserMarkupsPojo markup = new UserMarkupsPojo();
                markup.setTg_id(rs.getString("tg_id"));
                markup.setCode(rs.getString("code"));
                markup.setCodeNotify(rs.getString("codeNotify"));
                return markup;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
