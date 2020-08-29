package TestMaker;

public class UserDataChecker {
    private boolean isAccessGained = true;

    public UserDataChecker(int loginHash, int passwordHash) {
        connectToDatabase(loginHash, passwordHash);
    }

    //Connecting to the DB and checking is user with such user data is exist
    private void connectToDatabase(int loginHash, int passwordHash) {
        //TODO:
        //Connect to DB and check for login and password
        //if exist -
    }

    //Access validation getter
    public boolean isAccessGained() {
        return isAccessGained;
    }
}
