package TestMaker;

import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataChecker {
    private boolean isAccessGained = false;

    public UserDataChecker(){

    }

    //Connecting to the DB and checking is user with such user data is exist
    public void getUserData(int usernameHash, int passwordHash) throws SQLException {
        DBHandler dbHandler = new DBHandler();
        String SQLQuery = "SELECT * FROM " + Constants.USERS_INFO_TABLE_NAME + " where "
                + Constants.USER_NAME_HASH + " = " + usernameHash + " and " + Constants.PASSWORD_HASH + " = " + passwordHash;
        ResultSet userDataSet = dbHandler.getDataFromDB(SQLQuery);

            if (userDataSet.next()) {
                //gain access
                isAccessGained = true;

                //set up user info in transfer class
                UserDataTransfer.userName = userDataSet.getString(Constants.USER_NAME_HASH);
                UserDataTransfer.password = userDataSet.getString(Constants.PASSWORD_HASH);
                UserDataTransfer.firstName = userDataSet.getString(Constants.FIRST_NAME);
                UserDataTransfer.lastName = userDataSet.getString(Constants.LAST_NAME);
                UserDataTransfer.email = userDataSet.getString(Constants.EMAIL);
                UserDataTransfer.accessToken = userDataSet.getString(Constants.ACCESS_TOKEN);
                UserDataTransfer.registrationDate = userDataSet.getString(Constants.REG_DATE);
                UserDataTransfer.lastVisitDate = userDataSet.getString(Constants.LAST_VISIT_DATE);

                //debug user info
                System.out.println("Sing IN user with user data:\n" +
                        "User_name: " + UserDataTransfer.userName +" \n" +
                        "Password: " + UserDataTransfer.password +" \n" +
                        "First_name: " + UserDataTransfer.firstName +" \n" +
                        "Last_name: " + UserDataTransfer.lastName +" \n" +
                        "E-mail: " + UserDataTransfer.email +" \n" +
                        "Access_token: " + UserDataTransfer.accessToken + "\n" +
                        "Registration date: " + UserDataTransfer.registrationDate + "\n" +
                        "Last visit: " + UserDataTransfer.lastVisitDate);
            } else {
                //block access
                System.out.println("No user with equal data");
                isAccessGained = false;
            }
    }

    //Access validation getter
    public boolean isAccessGained() {
        return isAccessGained;
    }


}
