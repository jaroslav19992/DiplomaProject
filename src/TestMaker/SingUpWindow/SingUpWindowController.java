package TestMaker.SingUpWindow;


import TestMaker.UserDataTransfer;
import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static TestMaker.WindowTools.*;


public class SingUpWindowController {

    @FXML
    public TextField lastName_textField;

    @FXML
    public TextField firstName_textField;

    @FXML
    private TextField passwordRepeat_textField;

    @FXML
    public TextField email_textField;

    @FXML
    public TextField password_textField;

    @FXML
    private Label error_label;

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
        //set no error
        error_label.setVisible(false);

        //SingUp button click
        SingUpButton.setOnAction(event -> {
            DBHandler dbHandler = new DBHandler();

            try {
                if (checkForCorrectInfo()) {
                    error_label.setVisible(false);

                    //If teacher access chose
                    if (radioButton_teacher.isSelected()) {
                        //give control to access window and transfer user data
                        transferUserInfo();
                        openNewWindowAndWait("SingUpWindow/AccessWindow/AccessWindow.fxml", false, Modality.APPLICATION_MODAL);
                        registerTeacher(dbHandler);
                        openNewWindow("MainProgramWindow/MainWindow.fxml", true, Modality.NONE);
                        main_pane.getScene().getWindow().hide();
                        //if pupil access token
                    } else {
                        registerPupil(dbHandler);
                        //close registration window
                        main_pane.getScene().getWindow().hide();
                    }

                    //debug user info
                    System.out.println("Sing UP user with user data:\n" +
                            "User_name: " + UserDataTransfer.userName + " \n" +
                            "Password: " + UserDataTransfer.password + " \n" +
                            "First_name: " + UserDataTransfer.firstName + " \n" +
                            "Last_name: " + UserDataTransfer.lastName + " \n" +
                            "E-mail: " + UserDataTransfer.email + " \n" +
                            "Access_token: " + UserDataTransfer.accessToken +"\n" );
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        back_button.setOnAction(event -> {
            openNewWindow("LoginWindow/LoginWindow.fxml", false, Modality.NONE);
            main_pane.getScene().getWindow().hide();
        });
    }

    /**
     * Register user with teacher access token
     * @param dbHandler database connector class
     */
    private void registerPupil(DBHandler dbHandler) {
        // last visit and registration date
        Date date = new Date();
        SimpleDateFormat formatForRegDate = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat formatForVisitDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        transferUserInfo();
        try {
            dbHandler.singUpNewUser(userName_textField.getText(), password_textField.getText(), firstName_textField.getText(),
                    lastName_textField.getText(), email_textField.getText(), Constants.PUPIL_ACCESS_TOKEN,
                    formatForRegDate.format(date), formatForVisitDate.format(date));
        } catch (SQLException exception) {
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Помилка з'єднання з сервером");
            alert.showAndWait();
        }
        openNewWindow("MainProgramWindow/MainWindow.fxml", false, Modality.NONE);
    }

    /**
     * Register user with pupil access token
     * @param dbHandler database connector class
     */
    private void registerTeacher(DBHandler dbHandler) {
        if (UserDataTransfer.isRegisterAccessGained) {
            //get register and visit date
            Date date = new Date();
            SimpleDateFormat formatForRegDate = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat formatForVisitDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            try {
                dbHandler.singUpNewUser(UserDataTransfer.userName, UserDataTransfer.password, UserDataTransfer.firstName,
                        UserDataTransfer.lastName, UserDataTransfer.email, UserDataTransfer.accessToken,
                        formatForRegDate.format(date), formatForVisitDate.format(date));
            } catch (SQLException exception) {
                exception.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText("Помилка з'єднання з сервером");
                alert.showAndWait();
            }
        }
    }

    //get user info snd give it to transfer info class
    private void transferUserInfo() {
        UserDataTransfer.userName = userName_textField.getText();
        UserDataTransfer.password = password_textField.getText();
        UserDataTransfer.firstName = firstName_textField.getText();
        UserDataTransfer.lastName = lastName_textField.getText();
        UserDataTransfer.email = email_textField.getText();
        if (radioButton_teacher.isSelected()) {
            UserDataTransfer.accessToken = Constants.TEACHER_ACCESS_TOKEN;
        } else {
            UserDataTransfer.accessToken = Constants.PUPIL_ACCESS_TOKEN;

        }
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
        if (validateText(password_textField.getText())) {
            error_label.setText("Пароль не повинен містити символи  \" / \\ [ ] : ; | = , + * ? < > .");
            error_label.setVisible(true);
            return false;
        }
        if (password_textField.getText().length() < 8 || password_textField.getText().length() > 20) {
            error_label.setText("Пароль повинен містити не менше ніж 8 символів і не більше ніж 20");
            error_label.setVisible(true);
            return false;
        }
        if (isOnlyLatLetters(password_textField.getText())) {
            error_label.setText("пароль повинен містити лише букви латинського алфавіту та не містити пробілів");
            error_label.setVisible(true);
            return false;
        }
        /*-------------------------------Password check-----------------------------*/


        /*-------------------------------Username check-----------------------------*/
        if (validateText(userName_textField.getText())) {
            error_label.setText("Логін не повинен містити символи  \" / \\ [ ] : ; | = , + * ? < >");
            error_label.setVisible(true);
            return false;
        }
        if (userName_textField.getText().length() > 20 || userName_textField.getText().length() < 5) {
            error_label.setText("Логін не повинен складатися менше ніж з 5 і більше ніж з 20 симаолів");
            error_label.setVisible(true);
            return false;
        }
        if (isOnlyLatLetters(userName_textField.getText())) {
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
        String SQLQueryForUsername = "SELECT " + Constants.USER_NAME_HASH + " FROM " + Constants.USERS_INFO_TABLE_NAME + " WHERE " + Constants.USER_NAME_HASH + " = "
                + userName_textField.getText().hashCode();
        String SQLQueryForEmail = "SELECT " + Constants.EMAIL + " FROM " + Constants.USERS_INFO_TABLE_NAME + " WHERE " + Constants.EMAIL + " = "
                + "\"" + email_textField.getText() + "\"";

        //username check
        if (dbHandler.getDataFromDB(SQLQueryForUsername).next()) {
            error_label.setText("Користувач з таким ім'ям уже існує");
            error_label.setVisible(true);
            return false;
        }
        //e-mail check
        if (dbHandler.getDataFromDB(SQLQueryForEmail).next()) {
            error_label.setText("Даний E-mail уже використовується");
            error_label.setVisible(true);
            return false;
        }
        /*----------------------------Check if no user data duplicates-----------------------------*/

        return true;
    }

    //check is there is no bad symbols
    public boolean validateText(String str) {
        return str.contains("\\") || str.contains("/") || str.contains("\"") || str.contains("[") || str.contains("]")
                || str.contains(";") || str.contains(":") || str.contains("=") || str.contains(",") || str.contains("+")
                || str.contains("*") || str.contains("?") || str.contains("<") || str.contains(">");
    }

    //check is there is only Latin alphabet letters or not
    public boolean isOnlyLatLetters(String str) {
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                return true;
            }
            if (String.valueOf(c).matches("([а-я]|[А-Я])")) {
                return true;
            }
        }
        return false;
    }
}
