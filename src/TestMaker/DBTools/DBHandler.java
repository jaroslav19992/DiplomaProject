package TestMaker.DBTools;

import java.sql.*;

public class DBHandler {
    public static Connection dbConnection = null;

    public static Connection getDbConnection() throws SQLException {
        String connectionString = "jdbc:mysql://" + Configs.dbHost + ":" + Configs.dbPort + "/" + Configs.dbName + "?serverTimezone=UTC";
            if (dbConnection == null) {
                dbConnection = DriverManager.getConnection(connectionString, Configs.dbUser, Configs.dbPassword);
            }
        return dbConnection;
    }

    //Add data to the database table
    public static void singUpNewUser(String username, String password, String firstName, String lastName,
                                     String email, String classroom, String accessToken, String regDate, String lastVisitDate) throws SQLException {
        String insertString = "INSERT INTO " + DBConstants.USERS_INFO_TABLE_NAME + " (" + DBConstants.USER_NAME_HASH + ", "
                + DBConstants.PASSWORD_HASH + ", " + DBConstants.FIRST_NAME + ", " + DBConstants.LAST_NAME + ", " + DBConstants.EMAIL
                + ", " + DBConstants.CLASS_ROOM + ", " + DBConstants.ACCESS_TOKEN + ", " + DBConstants.REG_DATE + ", "
                + DBConstants.LAST_VISIT_DATE + ")" + " VALUES (?,?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insertString);
        preparedStatement.setInt(1, username.hashCode());
        preparedStatement.setInt(2, password.hashCode());
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setString(5, email);
        preparedStatement.setString(6, classroom);
        preparedStatement.setString(7, accessToken);
        preparedStatement.setString(8, regDate);
        preparedStatement.setString(9, lastVisitDate);

        preparedStatement.executeUpdate();
        System.out.println("user " + username + " singed up");
    }

    /**
     * Send SQL query to DB and returns result set
     *
     * @param SQLQuery string
     * @return set of data from db
     */
    public static ResultSet getDataFromDB(String SQLQuery) throws SQLException {
        System.out.println("Execute SQL query: " + SQLQuery);
        System.out.println("----------------------------\n");
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();

        return statement.executeQuery(SQLQuery);
    }

    public static void loadDataToDB(String SQLQuery) throws SQLException {
        System.out.println("Execute SQL query: " + SQLQuery);
        System.out.println("----------------------------\n");
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();
        statement.execute(SQLQuery);
    }
}
