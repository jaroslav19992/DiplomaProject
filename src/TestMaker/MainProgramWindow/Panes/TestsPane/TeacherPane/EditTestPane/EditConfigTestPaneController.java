package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.EditTestPane;

import TestMaker.DBTools.DBConstants;
import TestMaker.DBTools.DBHandler;
import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.EditTestPane.EditTestQuestionsPane.EditTestQuestionsPaneController;
import TestMaker.MainProgramWindow.Panes.TestMakerTest;
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

import java.sql.PreparedStatement;
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

    private EditTestQuestionsPaneController controller;
    private TestMakerTest currentTest;

    @FXML
    private void initialize() {
        setEvaluationSystemVariants();
        setButtonsActions();
        setGlobalEventHAndler(main_pane);
    }

    public void setTestProperties(TestMakerTest test) {
        this.currentTest = test;
        testName_textField.setText(test.getTestName());
        switch (test.getEvSystem()) {
            case 5: {
                evaluationSystem_choiceBox.setValue(EVAL_SYSTEM_5);
                break;
            }
            case 12: {
                evaluationSystem_choiceBox.setValue(EVAL_SYSTEM_12);
                break;
            }
            case 100: {
                evaluationSystem_choiceBox.setValue(EVAL_SYSTEM_100);
                break;
            }
        }
        if (test.getNumberOfAttempts() == 1) {
            reTestingDisabled_radioButton.setSelected(true);
            numberOfAttempts_textField.setDisable(true);
            numberOfAttempts_label.setDisable(true);
        } else {
            reTestingEnabled_radioButton.setSelected(true);
            numberOfAttempts_textField.setDisable(false);
            numberOfAttempts_label.setDisable(false);
            numberOfAttempts_textField.setText(String.valueOf(test.getNumberOfAttempts()));
        }

        if (test.getTimeLimit() == 0) {
            timeLimitDisabled_radioButton.setSelected(true);
            timeLimit_textField.setDisable(true);
            timeLimit_label.setDisable(true);
        } else {
            timeLimitEnabled_radioButton.setSelected(true);
            timeLimit_textField.setDisable(false);
            timeLimit_label.setDisable(false);
            timeLimit_textField.setText(String.valueOf(test.getTimeLimit()));
        }
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

        finishTestEdit_button.setOnAction(event1 -> {
            Alert finishConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            finishConfirmation.setHeaderText("Підвердіть дію");
            finishConfirmation.setTitle("Підтвердіть дію");
            finishConfirmation.setContentText("Ви дійсно бажаєте завершити редагування тесту?");
            ButtonType finish = new ButtonType("Заверщити");
            ButtonType cancel = new ButtonType("Відміна");
            finishConfirmation.getButtonTypes().clear();
            finishConfirmation.getButtonTypes().addAll(cancel, finish);
            Optional<ButtonType> selection = finishConfirmation.showAndWait();
            if (selection.get() == finish) {
                try {
                    //Try to insert file into table "tests list"
                    String SQLQuery = "UPDATE `" + DBConstants.DB_NAME + "`.`" + DBConstants.TESTS_LIST_TABLE_NAME + "` SET `"
                            + DBConstants.TEST_NAME + "` = ?, `"+ DBConstants.EV_SYSTEM+"` = ?, `"+ DBConstants.TIME_LIMIT
                            +"` = ?, `"+DBConstants.NUMBER_OF_ATTEMPTS+"` = ? WHERE (`"+ DBConstants.ID_TESTS_LIST+"` = ?);";
                    PreparedStatement preparedStatement = DBHandler.getDbConnection().prepareStatement(SQLQuery);
                    preparedStatement.setString(1, testName_textField.getText());

                    preparedStatement.setInt(2, getEvSystem());
                    preparedStatement.setInt(3, (timeLimitEnabled_radioButton.isSelected()) ?
                            (Integer.parseInt(timeLimit_textField.getText())) : (0));
                    preparedStatement.setInt(4, (reTestingEnabled_radioButton.isSelected()) ?
                            (Integer.parseInt(numberOfAttempts_textField.getText())) : (1));
                    preparedStatement.setInt(5, currentTest.getIdInTestsList());
                    preparedStatement.execute();
                    //if no errors during test loading show success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Успіх створення тесту");
                    alert.setHeaderText("Тест " + testName_textField.getText() + " успішно створено");
                    alert.setContentText("Оновіть сторінку тестів");
                    alert.showAndWait();
                    main_pane.getScene().getWindow().hide();
                } catch (SQLException exception) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Деталі помилки:\n" + exception.getMessage());
                    errorAlert.setHeaderText("Помилка завантеження тесту до бази даних\nПеревірте з'єднання з мережею інтернет");
                    errorAlert.setTitle("Помилка");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void editQuestions() {
        System.out.println("Starting to edit " + testName_textField.getText() + "");
        WindowTools windowTools = new WindowTools();
        controller = (EditTestQuestionsPaneController) windowTools.openNewWindow("/TestMaker/MainProgramWindow/Panes/" +
                "TestsPane/TeacherPane/EditTestPane/EditTestQuestionsPane/" +
                "EditTestQuestionsPane.fxml", true, Modality.APPLICATION_MODAL);
        windowTools.closeCurrentWindow(main_pane);

        controller.setTestProperties(currentTest.getIdInTestsList(), testName_textField.getText(),
                getEvSystem(), currentTest.getAmountOfQuestions(),
                (reTestingEnabled_radioButton.isSelected()) ? (Integer.parseInt(numberOfAttempts_textField.getText())) : (1),
                (timeLimitEnabled_radioButton.isSelected()) ? (Integer.parseInt(timeLimit_textField.getText())) : (0),
                currentTest.getTestQuestions());
        controller.setPageFactory();
    }

    private int getEvSystem() {
        switch (evaluationSystem_choiceBox.getValue()) {
            case EVAL_SYSTEM_5: {
                return 5;
                            }
            case EVAL_SYSTEM_12: {
                return 12;
            }
            case EVAL_SYSTEM_100: {
                return 100;
            }
        }
        return 0;
    }

    /**
     * check is all information witch need for test creation is correct
     *
     * @return
     */
    private boolean checkForCorrectInfo() {
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
