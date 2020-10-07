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
                UserInfoHandler.userName = userDataSet.getString(Constants.USER_NAME_HASH);
                UserInfoHandler.password = userDataSet.getString(Constants.PASSWORD_HASH);
                UserInfoHandler.firstName = userDataSet.getString(Constants.FIRST_NAME);
                UserInfoHandler.lastName = userDataSet.getString(Constants.LAST_NAME);
                UserInfoHandler.email = userDataSet.getString(Constants.EMAIL);
                UserInfoHandler.accessToken = userDataSet.getString(Constants.ACCESS_TOKEN);
                UserInfoHandler.registrationDate = userDataSet.getString(Constants.REG_DATE);
                UserInfoHandler.lastVisitDate = userDataSet.getString(Constants.LAST_VISIT_DATE);

                //debug user info
                System.out.println("Sing IN user with user data:\n" +
                        "User_name: " + UserInfoHandler.userName +" \n" +
                        "Password: " + UserInfoHandler.password +" \n" +
                        "First_name: " + UserInfoHandler.firstName +" \n" +
                        "Last_name: " + UserInfoHandler.lastName +" \n" +
                        "E-mail: " + UserInfoHandler.email +" \n" +
                        "Access_token: " + UserInfoHandler.accessToken + "\n" +
                        "Registration date: " + UserInfoHandler.registrationDate + "\n" +
                        "Last visit: " + UserInfoHandler.lastVisitDate);
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
