package TestMaker.SingUpWindow;


import TestMaker.UserInfoTransfer;
import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.SQLException;

import static TestMaker.WindowTools.*;


public class SingUpWindowController {

    @FXML
    public TextField lastName_textField;

    @FXML
    private Label accessLevel_label;

    @FXML
    public TextField firstName_textField;

    @FXML
    private TextField passwordRepeat_textField;

    @FXML
    private Label password_label;

    @FXML
    public TextField email_textField;

    @FXML
    public TextField password_textField;

    @FXML
    private RadioButton radioButton_pupil;

    @FXML
    private Label passwordRepeat_label;

    @FXML
    private Label error_label;

    @FXML
    private ToggleGroup access_level;

    @FXML
    public TextField userName_textField;

    @FXML
    private RadioButton radioButton_teacher;

    @FXML
    private Button SingUpButton;

    @FXML
    private Button back_button;

    @FXML
    private AnchorPane main_pane;

    @FXML
    public void initialize() {
        error_label.setVisible(false);

        //Register button click
        SingUpButton.setOnAction(event -> {

            try {
                if (checkForCorrectInfo()) {
                    DBHandler dbHandler = new DBHandler();
                    error_label.setVisible(false);

                    //If teacher access chose
                    if (radioButton_teacher.isSelected()) {
                        //give control to access window and transfer user data
                        getUserInfo();
                        openNewWindowAndWait("AccessWindow/AccessWindow.fxml", false, Modality.APPLICATION_MODAL);
                        if (UserInfoTransfer.isRegisterAccessGained) {
                            openNewWindow("MainProgramWindow/MainWindow.fxml", true, Modality.NONE);
                            main_pane.getScene().getWindow().hide();
                        }

                    //If pupil access token
                    } else {
                        dbHandler.singUpNewUser(userName_textField.getText(), password_textField.getText(), firstName_textField.getText(),
                                lastName_textField.getText(), email_textField.getText(), Constants.PUPIL_ACCESS_TOKEN);
                        openNewWindow("MainProgramWindow/MainWindow.fxml", false, Modality.NONE);

                        //close registration window
                        main_pane.getScene().getWindow().hide();
                    }
                    //debug user info
                    System.out.println("Sing UP user with user data:\n" +
                            "User_name: " + UserInfoTransfer.userName +" \n" +
                            "Password: " + UserInfoTransfer.password +" \n" +
                            "First_name: " + UserInfoTransfer.firstName +" \n" +
                            "Last_name: " + UserInfoTransfer.lastName +" \n" +
                            "E-mail: " + UserInfoTransfer.email +" \n" +
                            "Access_token: " + UserInfoTransfer.accessToken);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        back_button.setOnAction(event -> {
            openNewWindow("LoginWindow/LoginWindow.fxml", false, Modality.NONE);
            main_pane.getScene().getWindow().hide();
        });
    }

    //get user info snd give it to transfer info class
    private void getUserInfo() {
        UserInfoTransfer.userName = userName_textField.getText();
        UserInfoTransfer.password = password_textField.getText();
        UserInfoTransfer.firstName = firstName_textField.getText();
        UserInfoTransfer.lastName = lastName_textField.getText();
        UserInfoTransfer.email = email_textField.getText();
        UserInfoTransfer.accessToken = Constants.TEACHER_ACCESS_TOKEN;
    }

    /**
     * Check is user insert correct information
     *
     * @return is user can register
     */
    private boolean checkForCorrectInfo() throws SQLException {
        DBHandler dbHandler = new DBHandler();
        /*-------------------------------Empty fields check-----------------------------*/
        if (email_textField.getText().equals("") || password_textField.getText().equals("") || firstName_textField.getText().equals("") ||
                lastName_textField.getText().equals("") || userName_textField.getText().equals("")) {
            error_label.setText("Заповніть усі поля");
            error_label.setVisible(true);
            return false;
        }
        /*-------------------------------Empty fields check-----------------------------*/


        /*-------------------------------Password check-----------------------------*/
        if (!password_textField.getText().equals(passwordRepeat_textField.getText())) {
            error_label.setText("Паролі не зпівпадають");
            error_label.setVisible(true);
            return false;
        }
        if (!validateText(password_textField.getText())) {
            error_label.setText("Пароль не повинен містити символи  \" / \\ [ ] : ; | = , + * ? < > .");
            error_label.setVisible(true);
            return false;
        }
        if (password_textField.getText().length() < 8 || password_textField.getText().length() > 20) {
            error_label.setText("Пароль повинен містити не менше ніж 8 символів і не більше ніж 20");
            error_label.setVisible(true);
            return false;
        }
        if (!isOnlyLatLetters(password_textField.getText())) {
            error_label.setText("пароль повинен містити лише букви латинського алфавіту та не містити пробілів");
            error_label.setVisible(true);
            return false;
        }
        /*-------------------------------Password check-----------------------------*/


        /*-------------------------------Username check-----------------------------*/
        if (!validateText(userName_textField.getText())) {
            error_label.setText("Логін не повинен містити символи  \" / \\ [ ] : ; | = , + * ? < >");
            error_label.setVisible(true);
            return false;
        }
        if (userName_textField.getText().length() > 20 || userName_textField.getText().length() < 5) {
            error_label.setText("Логін не повинен складатися менше ніж з 5 і більше ніж з 20 симаолів");
            error_label.setVisible(true);
            return false;
        }
        if (!isOnlyLatLetters(userName_textField.getText())) {
            error_label.setText("Логін повинен містити лише букви латинського алфавітута не містити пробілів");
            error_label.setVisible(true);
            return false;
        }
        /*-------------------------------Username check-----------------------------*/


        /*-------------------------------E-mail check-----------------------------*/
        if (!email_textField.getText().contains("@") || !email_textField.getText().contains(".")) {
            error_label.setText("Не вірний формат E-mail");
            error_label.setVisible(true);
            return false;
        }
        /*-------------------------------E-mail check-----------------------------*/

        /*----------------------------Check if no user data duplicates-----------------------------*/
        //create queries
        String SQLQueryForUsername = "SELECT " + Constants.USER_NAME_HASH + " FROM usersInfo WHERE " + Constants.USER_NAME_HASH + " = "
                + userName_textField.getText().hashCode();
        String SQLQueryForEmail = "SELECT " + Constants.EMAIL + " FROM usersInfo WHERE " + Constants.EMAIL + " = "
                + "\"" + email_textField.getText() + "\"";

        //username check
        if (dbHandler.executeSQLQuery(SQLQueryForUsername).next()) {
            error_label.setText("Користувач з таким ім'ям уже існує");
            error_label.setVisible(true);
            return false;
        }
        //e-mail check
        if (dbHandler.executeSQLQuery(SQLQueryForEmail).next()) {
            error_label.setText("Даний E-mail уже використовується");
            error_label.setVisible(true);
            return false;
        }
        /*----------------------------Check if no user data duplicates-----------------------------*/

        return true;
    }

    //check is there is no bad symbols
    public boolean validateText(String str) {
        return !str.contains("\\") && !str.contains("/") && !str.contains("\"") && !str.contains("[") && !str.contains("]")
                && !str.contains(";") && !str.contains(":") && !str.contains("=") && !str.contains(",") && !str.contains("+")
                && !str.contains("*") && !str.contains("?") && !str.contains("<") && !str.contains(">");
    }

    //check is there is only Latin alphabet letters or not
    public boolean isOnlyLatLetters(String str) {
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                return false;
            }
            if (String.valueOf(c).matches("([а-я]|[А-Я])")) {
                return false;
            }
        }
        return true;
    }
}
