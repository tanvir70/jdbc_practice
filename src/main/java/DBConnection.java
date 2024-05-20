import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String password = "1234";

    public Connection tryConnection() throws Exception{
        return DriverManager.getConnection(URL, USER, password);
    }

    public static void main(String[] args) throws Exception {
        var database = new DBConnection();
        var connection = database.tryConnection();

        if (connection != null) {
            System.out.println("Connection successful");
        } else {
            System.out.println("Connection failed");
        }
    }

}
