package TestMaker.DBTools;

import java.sql.*;

public class DBHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName+ "?serverTimezone=UTC";

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);

        return dbConnection;
    }

    //Add data to the database table
    public void singUpNewUser(String username, String password, String firstName, String lastName,
                              String email, String accessToken, String regDate, String lastVisitDate) {
        String insertString = "INSERT INTO " + Constants.USERS_INFO_TABLE_NAME + " (" + Constants.USER_NAME_HASH + ", "
                + Constants.PASSWORD_HASH + ", " + Constants.FIRST_NAME + ", " + Constants.LAST_NAME + ", " + Constants.EMAIL
                + ", " + Constants.ACCESS_TOKEN + ", " + Constants.REG_DATE + ", "
                + Constants.LAST_VISIT_DATE + ")" + " VALUES (?,?,?,?,?,?,?,?)";
        try {
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
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        System.out.println("user " + username + " singed up");
    }

    /**
     * Send SQL query to DB and returns result set
     * @param SQLQuery string
     * @return set of data from db
     */
    public ResultSet executeSQLQuery(String SQLQuery ) {
        ResultSet resultSet = null;
        try {
            Connection connection = getDbConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(SQLQuery);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Execute SQL query: " + SQLQuery);
        return resultSet;
    }
}
