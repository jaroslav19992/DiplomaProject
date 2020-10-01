package TestMaker.MainProgramWindow.Panes.UserInfoPane.ChangeUserInfoWindow;

import TestMaker.DBTools.Constants;
import TestMaker.DBTools.DBHandler;
import TestMaker.UserDataTransfer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangeUserInfoWindowController {

    @FXML
    private TextField firstName_textField;

    @FXML
    private TextField passwordRepeat_textField;

    @FXML
    private Label password_label1;

    @FXML
    private Label password_label;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private TextField oldPassword_textField;

    @FXML
    private TextField email_textField;

    @FXML
    private TextField password_textField;

    @FXML
    private RadioButton radioButton_pupil;

    @FXML
    private Button back_button;

    @FXML
    private Label error_label;

    @FXML
    private Label passwordRepeat_label;

    @FXML
    private ToggleGroup access_level;

    @FXML
    private Label accessLevel_label;

    @FXML
    private TextField userName_textField;

    @FXML
    private RadioButton radioButton_teacher;

    @FXML
    private TextField lastName_textField;

    @FXML
    private Button applyChangesButton;

    @FXML
    void initialize() {
        error_label.setVisible(false);

        setTextForTextFields();

        //Register button click
        applyChangesButton.setOnAction(event -> {

            try {
                if (checkForCorrectInfo()) {
                    DBHandler dbHandler = new DBHandler();
                    error_label.setVisible(false);
                    changeUserInfo(dbHandler);
                    transferUserInfo();

                    error_label.setTextFill(Color.GREEN);
                    error_label.setText("Нові дані успішно завантажені на сервер");
                    error_label.setVisible(true);
                }
            } catch (SQLException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка");
                alert.setHeaderText(null);
                alert.setContentText("Помилка з'єднання з сервером");
                alert.showAndWait();
                exception.printStackTrace();
            }

        });

        back_button.setOnAction(event -> {
            main_pane.getScene().getWindow().hide();
        });
    }

    /**
     * Create SQL query to update user info. if password fields is not touched password will be the same
     * @param dbHandler
     */
    private void changeUserInfo(DBHandler dbHandler) {
        String SQLQuery = "UPDATE " + Constants.DB_NAME + "." + Constants.USERS_INFO_TABLE_NAME + " SET " + Constants.USER_NAME_HASH +
                " = " + "'" + userName_textField.getText().hashCode() + "'" + ", " + Constants.PASSWORD_HASH + " = " + "'" +
                ((password_textField.getText().equals("")) ? UserDataTransfer.password.hashCode() : password_textField.getText().hashCode())
                + "'" + ", " + Constants.EMAIL + " = " + "'" + email_textField.getText() + "'" + ", " + Constants.FIRST_NAME + " = " +
                "'" + firstName_textField.getText() + "'" + ", " + Constants.LAST_NAME + " = " + "'" + lastName_textField.getText()
                + "'" + " WHERE " + "(" + Constants.USER_NAME_HASH + " = " + UserDataTransfer.userName.hashCode() + ")";
        try {
            dbHandler.loadDataToDB(SQLQuery);
        } catch (SQLException exception) {
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Помилка з'єднання з сервером");
            alert.showAndWait();
        }
    }

    /**
     * Set info from user data transfer to text fields
     */
    private void setTextForTextFields() {
        userName_textField.setText(UserDataTransfer.userName);
        email_textField.setText(UserDataTransfer.email);
        firstName_textField.setText(UserDataTransfer.firstName);
        lastName_textField.setText(UserDataTransfer.lastName);
    }

    /**
     * Get user info snd give it to transfer info class
     */
    private void transferUserInfo() {
        UserDataTransfer.userName = userName_textField.getText();
        if (!password_textField.getText().equals("")) {
            UserDataTransfer.password = password_textField.getText();
        }
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
        if (email_textField.getText().equals("") || firstName_textField.getText().equals("")
                || lastName_textField.getText().equals("") || userName_textField.getText().equals("")) {
            error_label.setText("Заповніть усі поля");
            error_label.setVisible(true);
            return false;
        }
        /*-------------------------------Empty fields check-----------------------------*/

        /*-------------------------------Password check-----------------------------*/
        if (!oldPassword_textField.getText().equals("") || !password_textField.getText().equals("") ||
                !passwordRepeat_textField.getText().equals("")) {
            int oldPasswordHash = getOldPasswordHash(dbHandler);

            if (oldPassword_textField.getText().hashCode() != oldPasswordHash) {
                error_label.setText("Старий пароль не вірний");
                error_label.setVisible(true);
                return false;
            }

            if (oldPasswordHash == password_textField.getText().hashCode()) {
                error_label.setText("Старий і новий паролі не можуть бути однакові");
                error_label.setVisible(true);
                return false;
            }

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
        String SQLQueryForUsername = "SELECT " + Constants.USER_NAME_HASH + " FROM usersInfo WHERE " + Constants.USER_NAME_HASH + " = "
                + userName_textField.getText().hashCode();
        String SQLQueryForEmail = "SELECT " + Constants.EMAIL + " FROM usersInfo WHERE " + Constants.EMAIL + " = "
                + "\"" + email_textField.getText() + "\"";

        //if username changed
        if (userName_textField.getText().hashCode() != UserDataTransfer.userName.hashCode()) {
            //username check
            if (dbHandler.getDataFromDB(SQLQueryForUsername).next()) {
                error_label.setText("Користувач з таким ім'ям уже існує");
                error_label.setVisible(true);
                return false;
            }
        }

        //if e-mail changed
        if (email_textField.getText().hashCode() != UserDataTransfer.email.hashCode()) {
            //e-mail check
            if (dbHandler.getDataFromDB(SQLQueryForEmail).next()) {
                error_label.setText("Даний E-mail уже використовується");
                error_label.setVisible(true);
                return false;
            }
        }
        /*----------------------------Check if no user data duplicates-----------------------------*/

        return true;
    }

    /**
     * Get old user password hash from db
     *
     * @param dbHandler
     * @return old password ahsh
     */
    private int getOldPasswordHash(DBHandler dbHandler) {
        ResultSet resultSet;
        String SQLQuery = "SELECT " + Constants.PASSWORD_HASH + " FROM " + Constants.DB_NAME + "." +
                Constants.USERS_INFO_TABLE_NAME + " WHERE " +
                Constants.USER_NAME_HASH + " = " + UserDataTransfer.userName.hashCode();
        try {
            resultSet = dbHandler.getDataFromDB(SQLQuery);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
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
