package TestMaker;

import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataChecker {
    private boolean isAccessGained = false;

    public UserDataChecker(int usernameHash, int passwordHash) {

        getUserData(usernameHash, passwordHash);
    }

    //Connecting to the DB and checking is user with such user data is exist
    private void getUserData(int usernameHash, int passwordHash) {
        DBHandler dbHandler = new DBHandler();
        String SQLQuery = "SELECT * FROM " + Constants.USERS_INFO_TABLE_NAME + " where "
                + Constants.USER_NAME_HASH + " = " + usernameHash + " and " + Constants.PASSWORD_HASH + " = " + passwordHash;
        System.out.println("Execute SQL query: " + SQLQuery);
        ResultSet userDataSet = dbHandler.executeSQLQuery(SQLQuery);

        try {
            if (userDataSet.next()) {
                //gain access
                isAccessGained = true;

                //set up user info in transfer class
                UserInfoTransfer.firstName = userDataSet.getString(Constants.FIRST_NAME);
                UserInfoTransfer.lastName = userDataSet.getString(Constants.LAST_NAME);
                UserInfoTransfer.email = userDataSet.getString(Constants.EMAIL);
                UserInfoTransfer.accessToken = userDataSet.getString(Constants.ACCESS_TOKEN);

                //debug user info
                System.out.println("Sing IN user with user data:\n" +
                        "User_name: " + UserInfoTransfer.userName +" \n" +
                        "Password: " + UserInfoTransfer.password +" \n" +
                        "First_name: " + UserInfoTransfer.firstName +" \n" +
                        "Last_name: " + UserInfoTransfer.lastName +" \n" +
                        "E-mail: " + UserInfoTransfer.email +" \n" +
                        "Access_token: " + UserInfoTransfer.accessToken);
            } else {
                //block access
                System.out.println("No user with equal data");
                isAccessGained = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Access validation getter
    public boolean isAccessGained() {
        return isAccessGained;
    }
}
