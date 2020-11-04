package TestMaker;

public class UserInfoHandler {
    public static String userName;
    public static String password;
    public static String firstName;
    public static String lastName;
    public static String email;
    public static String accessToken;
    public static String registrationDate;
    public static String lastVisitDate;
    public static String classroom;
    public static boolean isAccessGained = false;

    /**
     * Clear all data in user transfer info to avoid data stole
     */
    public static void eraseTransferInfoFull(){
        userName = null;
        password = null;
        firstName = null;
        lastName = null;
        email = null;
        accessToken = null;
        registrationDate = null;
        lastVisitDate = null;
        isAccessGained = false;
    }

    /**
     * Clear private data in user transfer info to avoid data stole
     */
    public static void eraseTransferInfoPrivate(){
        password = null;
    }
}
