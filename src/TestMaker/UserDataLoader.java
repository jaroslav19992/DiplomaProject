package TestMaker;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataLoader {
    private boolean isAccessGained = false;

    public UserDataLoader(){

    }

    //Connecting to the DB and checking is user with such user data is exist
    public void getUserData(int usernameHash, int passwordHash) throws SQLException {
        String SQLQuery = "SELECT * FROM " + DBConstants.USERS_INFO_TABLE_NAME + " where "
                + DBConstants.USER_NAME_HASH + " = " + usernameHash + " and " + DBConstants.PASSWORD_HASH + " = " + passwordHash;
        ResultSet userDataSet = DBHandler.getDataFromDB(SQLQuery);

            if (userDataSet.next()) {
                //gain access
                isAccessGained = true;

                //set up user info in transfer class
                UserInfoHandler.userName = userDataSet.getString(DBConstants.USER_NAME_HASH);
                UserInfoHandler.password = userDataSet.getString(DBConstants.PASSWORD_HASH);
                UserInfoHandler.firstName = userDataSet.getString(DBConstants.FIRST_NAME);
                UserInfoHandler.lastName = userDataSet.getString(DBConstants.LAST_NAME);
                UserInfoHandler.email = userDataSet.getString(DBConstants.EMAIL);
                UserInfoHandler.classroom = userDataSet.getString(DBConstants.CLASS_ROOM);
                UserInfoHandler.accessToken = userDataSet.getString(DBConstants.ACCESS_TOKEN);
                UserInfoHandler.registrationDate = userDataSet.getString(DBConstants.REG_DATE);
                UserInfoHandler.lastVisitDate = userDataSet.getString(DBConstants.LAST_VISIT_DATE);

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
