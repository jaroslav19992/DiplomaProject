package TestMaker.SingUpWindow;


import TestMaker.DBTools.DBHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SingUpWindowController {

    @FXML
    private TextField lastName;

    @FXML
    private Label acces_level_lavel;

    @FXML
    private TextField firstName_textField;

    @FXML
    private TextField passwordRepeat_textField;

    @FXML
    private Label password_label;

    @FXML
    private TextField email_textField;

    @FXML
    private TextField password_textField;

    @FXML
    private RadioButton radioButton_pupil;

    @FXML
    private Label passwordRepeat_label;

    @FXML
    private Label error_label;

    @FXML
    private ToggleGroup access_level;

    @FXML
    private TextField userName_textField;

    @FXML
    private RadioButton radioButton_teacher;

    @FXML
    private Button SingUpButton;


    @FXML
    public void initialize() {
        error_label.setVisible(false);
        SingUpButton.setOnAction(event -> {
            if (checkForCorrectInfo()/*&& access gained*/ ) {
                error_label.setVisible(false);
                DBHandler dbHandler = new DBHandler();
//                dbHandler.singUpNewUser(userName_textField.getText(),
//                        password_textField.getText(), firstName_textField.getText(), lastName.getText(),
//                        email_textField.getText(), Constants.TEACHER_ACCESS_TOKEN);
                System.out.println("user " + userName_textField.getText() + " singed up");
            }
        });
    }

    /**
     * Check is user insert correct information
     * @return is user can register
     */
    private boolean checkForCorrectInfo() {
        //empty fields check
        if (email_textField.getText().equals("") || password_textField.getText().equals("") || firstName_textField.getText().equals("") ||
                lastName.getText().equals("") || userName_textField.getText().equals("")) {
            error_label.setText("Заповніть усі поля");
            error_label.setVisible(true);
            return false;
        }

        //password check
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
        //username check
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

        //e-mail check
        if (!email_textField.getText().contains("@") || !email_textField.getText().contains(".")) {
            error_label.setText("Не вірний формат E-mail");
            error_label.setVisible(true);
            return false;
        }
        return true;
    }

    //check is there is no bad symbols
    public boolean validateText(String str) {
        return !str.contains("\\") && !str.contains("/") && !str.contains("\"") && !str.contains("[") && !str.contains("]")
                && !str.contains(";") && !str.contains(":") && !str.contains("=") && !str.contains(",") && !str.contains("+")
                && !str.contains("*") && !str.contains("?") && !str.contains("<") && !str.contains(">") ;
    }

    //check is there is only Latin alphabet letters or not
    public boolean isOnlyLatLetters(String str) {
        for (char c: str.toCharArray()) {
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
