package TestMaker.DBTools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBHandler extends Configs{
    Connection dbConnection;

    public Connection getDbConnection () throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);

        return dbConnection;
    }
}
