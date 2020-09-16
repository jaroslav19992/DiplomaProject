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
        ResultSet userDataSet = dbHandler.executeSQLQuery(SQLQuery);

        try {
            if (userDataSet.next()) {
                //gain access
                isAccessGained = true;

                //set up user info in transfer class
                UserDataTransfer.firstName = userDataSet.getString(Constants.FIRST_NAME);
                UserDataTransfer.lastName = userDataSet.getString(Constants.LAST_NAME);
                UserDataTransfer.email = userDataSet.getString(Constants.EMAIL);
                UserDataTransfer.accessToken = userDataSet.getString(Constants.ACCESS_TOKEN);

                //debug user info
                System.out.println("Sing IN user with user data:\n" +
                        "User_name: " + UserDataTransfer.userName +" \n" +
                        "Password: " + UserDataTransfer.password +" \n" +
                        "First_name: " + UserDataTransfer.firstName +" \n" +
                        "Last_name: " + UserDataTransfer.lastName +" \n" +
                        "E-mail: " + UserDataTransfer.email +" \n" +
                        "Access_token: " + UserDataTransfer.accessToken);
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
