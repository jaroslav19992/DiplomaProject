package TestMaker.DBTools;

import java.sql.*;

public class DBHandler {
    Connection dbConnection;

    public Connection getDbConnection() throws SQLException {
        String connectionString = "jdbc:mysql://" + Configs.dbHost + ":" + Configs.dbPort + "/" + Configs.dbName+ "?serverTimezone=UTC";

        dbConnection = DriverManager.getConnection(connectionString, Configs.dbUser, Configs.dbPassword);

        return dbConnection;
    }

    //Add data to the database table
    public void singUpNewUser(String username, String password, String firstName, String lastName,
                              String email, String accessToken, String regDate, String lastVisitDate) throws SQLException {
        String insertString = "INSERT INTO " + Constants.USERS_INFO_TABLE_NAME + " (" + Constants.USER_NAME_HASH + ", "
                + Constants.PASSWORD_HASH + ", " + Constants.FIRST_NAME + ", " + Constants.LAST_NAME + ", " + Constants.EMAIL
                + ", " + Constants.ACCESS_TOKEN + ", " + Constants.REG_DATE + ", "
                + Constants.LAST_VISIT_DATE + ")" + " VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insertString);
            preparedStatement.setInt(1, username.hashCode());
            preparedStatement.setInt(2, password.hashCode());
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, accessToken);
            preparedStatement.setString(7, regDate);
            preparedStatement.setString(8, lastVisitDate);

            preparedStatement.executeUpdate();
            System.out.println("user " + username + " singed up");

        System.out.println("user " + username + " singed up");
    }

    /**
     * Send SQL query to DB and returns result set
     * @param SQLQuery string
     * @return set of data from db
     */
    public ResultSet getDataFromDB(String SQLQuery ) throws SQLException {
        System.out.println("Execute SQL query: " + SQLQuery);
            Connection connection = getDbConnection();
            Statement statement = connection.createStatement();

        return statement.executeQuery(SQLQuery);
    }

    public void loadDataTODB(String SQLQuery) throws SQLException {
        System.out.println("Execute SQL query: " + SQLQuery);
        Connection connection = getDbConnection();
        Statement statement = connection.createStatement();
        statement.execute(SQLQuery);
    }
}
