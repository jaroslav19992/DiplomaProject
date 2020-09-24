package TestMaker;

public class UserDataTransfer {
    public static String userName;
    public static String password;
    public static String firstName;
    public static String lastName;
    public static String email;
    public static String accessToken;
    public static String registrationDate;
    public static String lastVisitDate;
    public static boolean isRegisterAccessGained = false;

    public static void eraseTransferInfo(){
        userName = null;
        password = null;
        firstName = null;
        lastName = null;
        email = null;
        accessToken = null;
        registrationDate = null;
        lastVisitDate = null;
        isRegisterAccessGained = false;
    }
}
