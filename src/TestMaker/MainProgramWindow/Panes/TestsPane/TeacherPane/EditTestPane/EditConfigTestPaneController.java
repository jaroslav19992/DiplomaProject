package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.EditTestPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.EditTestPane.editTestQuestionsPane.editTestQuestionsPaneController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.WindowTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EditConfigTestPaneController implements TestsConstants {
    @FXML
    private TextField testName_textField;

    @FXML
    private TextField numberOfAttempts_textField;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private Label numberOfAttempts_label;

    @FXML
    private TextField timeLimit_textField;

    @FXML
    private Button startTestQuestionsEdit_button;

    @FXML
    private RadioButton reTestingDisabled_radioButton;

    @FXML
    private RadioButton timeLimitDisabled_radioButton;

    @FXML
    private ChoiceBox<String> evaluationSystem_choiceBox;

    @FXML
    private ToggleGroup timeLimit_toggleGroup;

    @FXML
    private RadioButton reTestingEnabled_radioButton;

    @FXML
    private RadioButton timeLimitEnabled_radioButton;

    @FXML
    private Label timeLimit_label;

    @FXML
    private Button finishTestEdit_button;

    @FXML
    private Button cancelTestEdit_button;

    @FXML
    private ToggleGroup reTesting_toggleGroup;

    private editTestQuestionsPaneController controller;

    @FXML
    private void initialize() {
        setEvaluationSystemVariants();
        setButtonsActions();
        setGlobalEventHAndler(main_pane);
        setTestProperties();
    }

    private void setTestProperties() {

    }

    private void setGlobalEventHAndler(Parent root) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                finishTestEdit_button.fire();
                ev.consume();
            }
        });
    }

    private void setButtonsActions() {
        reTestingEnabled_radioButton.setOnAction(event -> {
            numberOfAttempts_textField.setDisable(false);
            numberOfAttempts_label.setDisable(false);
        });
        reTestingDisabled_radioButton.setOnAction(event -> {
            numberOfAttempts_textField.setDisable(true);
            numberOfAttempts_label.setDisable(true);
        });
        timeLimitEnabled_radioButton.setOnAction(event -> {
            timeLimit_label.setDisable(false);
            timeLimit_textField.setDisable(false);
        });
        timeLimitDisabled_radioButton.setOnAction(event -> {
            timeLimit_label.setDisable(true);
            timeLimit_textField.setDisable(true);
        });
        startTestQuestionsEdit_button.setOnAction(event -> {
            if (checkForCorrectInfo()) {
                editQuestions();
            }
        });
        cancelTestEdit_button.setOnAction(event -> {
            Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            closeConfirmation.setHeaderText(CLOSE_WINDOW_CONFIRMATION_HEADER);
            closeConfirmation.setTitle(CLOSE_WINDOW_CONFIRMATION_TITLE);
            closeConfirmation.setContentText(CLOSE_WINDOW_CONFIRMATION_CONTEXT);
            ButtonType close = new ButtonType("Закрити");
            ButtonType cancel = new ButtonType("Відміна");
            closeConfirmation.getButtonTypes().clear();
            closeConfirmation.getButtonTypes().addAll(cancel, close);
            Optional<ButtonType> selection = closeConfirmation.showAndWait();
            if (selection.get() == close) {
                main_pane.getScene().getWindow().hide();
            }
        });
    }

    private void editQuestions() {
        System.out.println("Starting to edit " + testName_textField.getText() + "");
        WindowTools windowTools = new WindowTools();
        controller = (editTestQuestionsPaneController) windowTools.openNewWindow("/TestMaker/MainProgramWindow/Panes/" +
                "TestsPane/TeacherPane/EditTestPane/EditConfigTestPaneController.java" +
                "editTestQuestionsPane.fxml", true, Modality.APPLICATION_MODAL);
        windowTools.closeCurrentWindow(main_pane);

//        controller.setTestProperties(testName_textField.getText(), evaluationSystem_choiceBox.getValue(),
//                Integer.parseInt(questionsAmount_textField.getText()),
//                (reTestingEnabled_radioButton.isSelected()) ? (Integer.parseInt(numberOfAttempts_textField.getText())) : (1),
//                (timeLimitEnabled_radioButton.isSelected()) ? (Integer.parseInt(timeLimit_textField.getText())) : (0));
//        controller.setPageFactory();
    }

    /**
     * check is all information witch need for test creation is correct
     *
     * @return
     */
    private boolean checkForCorrectInfo() {
        String SQLQuery = "SELECT " + DBConstants.TEST_NAME + " FROM " + DBConstants.DB_NAME + "."
                + DBConstants.TESTS_LIST_TABLE_NAME + ";";
        try {
            ResultSet resultSet = DBHandler.getDataFromDB(SQLQuery);
            while (resultSet.next()) {
                if (resultSet.getString(DBConstants.TEST_NAME).equals(testName_textField.getText())) {
                    showAlert("Тест з такою назвою уже існує\n Оберіть іншу назву");
                    return false;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        //Test Name check
        if (testName_textField.getText().equals("")) {
            showAlert("Поле вводу назви тесту не повинне бути пустим");
            return false;
        }

        //Time limit check
        if (timeLimitEnabled_radioButton.isSelected()) {
            if (timeLimit_textField.getText().equals("")) {
                showAlert("Введіть кількість часу для проходження тесту\n(У хв.)");
                return false;
            }
            if (!isNumeric(timeLimit_textField.getText())) {
                showAlert("Поле вводу ліміту часу повинне містити тільки цифри");
                return false;
            } else if (Integer.parseInt(timeLimit_textField.getText()) <= 0) {
                showAlert("Неможлива кількість часу");
                return false;
            }
        }

        if (reTestingEnabled_radioButton.isSelected()) {
            if (numberOfAttempts_textField.getText().equals("")) {
                showAlert("Введіть кількість спроб для проходження тесту");
                return false;
            }
            if (!isNumeric(numberOfAttempts_textField.getText())) {
                showAlert("Поле вводу кількості спроб повинне містити тільки цифри");
                return false;
            } else if (Integer.parseInt(numberOfAttempts_textField.getText()) <= 0) {
                showAlert("Неможлива кількість спроб");
                return false;
            }
            if (Integer.parseInt(numberOfAttempts_textField.getText()) == 1) {
                showAlert("Кількість спроб для проходження тесту повинна бути бульшою ніж 1.\n" +
                        "В іншому випадку відмініть можливість повторного проходження");
                return false;
            }
        }
        return true;
    }

    /**
     * parsing string to double value, if exception is throwing - string is not numeric
     *
     * @param str string to check
     * @return is numeric or not
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void showAlert(String contextText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        alert.show();
    }

    /**
     * set evaluation system variants and set 12-as default
     */
    private void setEvaluationSystemVariants() {
        ObservableList<String> evaluationSystemsList = FXCollections.observableArrayList(
                EVAL_SYSTEM_12, EVAL_SYSTEM_5, EVAL_SYSTEM_100);
        evaluationSystem_choiceBox.setItems(evaluationSystemsList);
        evaluationSystem_choiceBox.setValue("12-ти бальна система");
    }

}