import java.sql.Connection;

public class CreateDatabase {
    public void createDatabase(Connection connection, String dbName) {
        String sql = "CREATE DATABASE " + dbName;
        try {
            var statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Database created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Database creation failed", e);
        }
    }

    public static void main(String[] args) throws Exception {
        var dbConnection = new DBConnection();
        var createDatabase = new CreateDatabase();

        var connection = dbConnection.tryConnection();
        createDatabase.createDatabase(connection, "easyMart");
    }
}
