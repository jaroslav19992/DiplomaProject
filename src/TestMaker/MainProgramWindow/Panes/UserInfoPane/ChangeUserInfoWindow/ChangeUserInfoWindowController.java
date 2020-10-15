package TestMaker.MainProgramWindow.Panes.UserInfoPane.ChangeUserInfoWindow;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.UserInfoHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import java.sql.ResultSet;
import java.sql.SQLException;

import static TestMaker.WindowTools.openNewWindowAndWait;

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
        setGlobalEventHandler(main_pane);

        error_label.setVisible(false);

        setTextForTextFields();

        //Register button click
        applyChangesButton.setOnAction(event -> {

            try {
                if (checkForCorrectInfo()) {
                    if (radioButton_teacher.isSelected()) {
                        openNewWindowAndWait("SingUpWindow/AccessWindow/AccessWindow.fxml",
                                false, Modality.APPLICATION_MODAL);
                    } else {
                        UserInfoHandler.isAccessGained = true;
                    }
                    if (UserInfoHandler.isAccessGained) {
                        error_label.setVisible(false);
                        changeUserInfo();
                        transferUserInfo();

                        error_label.setTextFill(Color.GREEN);
                        error_label.setText("Нові дані успішно завантажені на сервер");
                        error_label.setVisible(true);
                    }
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

        back_button.setOnAction(event -> main_pane.getScene().getWindow().hide());
    }

    /**
     * Create SQL query to update user info. if password fields is not touched password will be the same
     */
    private void changeUserInfo() throws SQLException {
        String SQLQuery = "UPDATE " + DBConstants.DB_NAME + "." + DBConstants.USERS_INFO_TABLE_NAME + " SET " + DBConstants.USER_NAME_HASH +
                " = " + "'" + userName_textField.getText().hashCode() + "'" + ", " + DBConstants.PASSWORD_HASH + " = " + "'" +
                ((password_textField.getText().equals("")) ? UserInfoHandler.password.hashCode() : password_textField.getText().hashCode())
                + "'" + ", " + DBConstants.EMAIL + " = " + "'" + email_textField.getText() + "'" + ", " + DBConstants.FIRST_NAME + " = " +
                "'" + firstName_textField.getText() + "'" + ", " + DBConstants.LAST_NAME + " = " + "'" + lastName_textField.getText()
                + "'" + ", " + DBConstants.ACCESS_TOKEN + " = " + "'" + ((radioButton_teacher.isSelected()) ? (DBConstants.TEACHER_ACCESS_TOKEN) :
                (DBConstants.PUPIL_ACCESS_TOKEN)) + "' WHERE (" + DBConstants.USER_NAME_HASH + " = " + UserInfoHandler.userName.hashCode() + ")";

        DBHandler.loadDataToDB(SQLQuery);
    }


    /**
     * Set info from user data transfer to text fields
     */
    private void setTextForTextFields() {
        userName_textField.setText(UserInfoHandler.userName);
        email_textField.setText(UserInfoHandler.email);
        firstName_textField.setText(UserInfoHandler.firstName);
        lastName_textField.setText(UserInfoHandler.lastName);
    }

    /**
     * Get user info snd give it to transfer info class
     */
    private void transferUserInfo() {
        UserInfoHandler.userName = userName_textField.getText();
        if (!password_textField.getText().equals("")) {
            UserInfoHandler.password = password_textField.getText();
        }
        UserInfoHandler.firstName = firstName_textField.getText();
        UserInfoHandler.lastName = lastName_textField.getText();
        UserInfoHandler.email = email_textField.getText();
        if (radioButton_teacher.isSelected()) {
            UserInfoHandler.accessToken = DBConstants.TEACHER_ACCESS_TOKEN;
        } else {
            UserInfoHandler.accessToken = DBConstants.PUPIL_ACCESS_TOKEN;

        }
    }

    /**
     * Check is user insert correct information
     *
     * @return is user can register
     */
    private boolean checkForCorrectInfo() throws SQLException {
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
            int oldPasswordHash = getOldPasswordHash();

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
        String SQLQueryForUsername = "SELECT " + DBConstants.USER_NAME_HASH + " FROM usersInfo WHERE " + DBConstants.USER_NAME_HASH + " = "
                + userName_textField.getText().hashCode();
        String SQLQueryForEmail = "SELECT " + DBConstants.EMAIL + " FROM usersInfo WHERE " + DBConstants.EMAIL + " = "
                + "\"" + email_textField.getText() + "\"";

        //if username changed
        if (userName_textField.getText().hashCode() != UserInfoHandler.userName.hashCode()) {
            //username check
            if (DBHandler.getDataFromDB(SQLQueryForUsername).next()) {
                error_label.setText("Користувач з таким ім'ям уже існує");
                error_label.setVisible(true);
                return false;
            }
        }

        //if e-mail changed
        if (email_textField.getText().hashCode() != UserInfoHandler.email.hashCode()) {
            //e-mail check
            if (DBHandler.getDataFromDB(SQLQueryForEmail).next()) {
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
     * @return old password ahsh
     */
    private int getOldPasswordHash() {
        ResultSet resultSet;
        String SQLQuery = "SELECT " + DBConstants.PASSWORD_HASH + " FROM " + DBConstants.DB_NAME + "." +
                DBConstants.USERS_INFO_TABLE_NAME + " WHERE " +
                DBConstants.USER_NAME_HASH + " = " + UserInfoHandler.userName.hashCode();
        try {
            resultSet = DBHandler.getDataFromDB(SQLQuery);
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

    /**
     * Keys pressed handler
     *
     * @param root object, add handler to
     */
    private void setGlobalEventHandler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                applyChangesButton.fire();
                ev.consume();
            }
            if (ev.getCode() == KeyCode.ESCAPE) {
                back_button.fire();
            }
        });
    }
}
