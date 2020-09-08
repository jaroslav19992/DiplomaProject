package TestMaker.DBTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);

        return dbConnection;
    }

    //Add data to the database table
    public void singUpNewUser(String username, String password, String firstName, String lastName, String email, String accessToken) {
        String insertString = "INSERT INTO " + Constants.USERS_INFO_TABLE_NAME + " (" + Constants.USER_NAME_HASH + ", "
                + Constants.PASSWORD_HASH + ", " + Constants.FIRST_NAME + ", " + Constants.LAST_NAME + ", " + Constants.EMAIL
                + ", " + Constants.ACCESS_TOKEN + ")" + " VALUES (?,?,?,?,?,?)";
       /* try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insertString);
            preparedStatement.setInt(1, username.hashCode());
            preparedStatement.setInt(2, password.hashCode());
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, accessToken);

            preparedStatement.executeUpdate();
            System.out.println("user " + username + " singed up");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }*/
        System.out.println("user " + username + " singed up");

    }

    /*public void pullDataToDatabase(String username, String password, String firstName,
                                   String lastName, String eMail, String accessToken) {
        DBHandler dbHandler = new DBHandler();
//                dbHandler.singUpNewUser(username, password, firstName, lastName, eMail, accessToken);

    }*/
}
