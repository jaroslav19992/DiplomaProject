package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import TestMaker.WindowTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.io.IOException;

public class ConfigTestPaneController extends TestsConstants {

    @FXML
    private Button startTestCreation_button;

    @FXML
    private TextField questionsAmount_textField;

    @FXML
    private TextField testName_textField;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private RadioButton reTestingEnabled_radioButton;

    @FXML
    private TextField timeLimit_textField;

    @FXML
    private RadioButton timeLimitDisabled_radioButton;

    @FXML
    private ToggleGroup timeLimit_toggleGroup;

    @FXML
    private RadioButton timeLimitEnabled_radioButton;

    @FXML
    private Label timeLimit_label;

    @FXML
    private RadioButton reTestingDisabled_radioButton;

    @FXML
    private ChoiceBox<String> evaluationSystem_choiceBox;

    @FXML
    private ToggleGroup reTesting_toggleGroup;

    @FXML
    private void initialize() {
        setEvaluationSystemVariants();
        setButtonAction();
    }

    private void setButtonAction() {
        timeLimitEnabled_radioButton.setOnAction(event -> {
            timeLimit_label.setDisable(false);
            timeLimit_textField.setDisable(false);
        });
        timeLimitDisabled_radioButton.setOnAction(event -> {
            timeLimit_label.setDisable(true);
            timeLimit_textField.setDisable(true);
        });
        startTestCreation_button.setOnAction(event -> {
            if (checkForCorrectInfo()) {
                try {
                    createTest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void createTest() throws IOException {
        System.out.println("Starting to create "+testName_textField.getText()+"");

                TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.creationTestPaneController.setTestProperties(
                        testName_textField.getText(), evaluationSystem_choiceBox.getValue(), Integer.parseInt(questionsAmount_textField.getText()),
                reTestingEnabled_radioButton.isSelected(), (timeLimitEnabled_radioButton.isSelected())?
                                (Integer.parseInt(timeLimit_textField.getText())):(0));
        WindowTools.openNewWindow("/TestMaker/MainProgramWindow/Panes/TestsPane/" +
                "TeacherPane/AddTestPane/CreationTestPane/creationTestPane.fxml", false, Modality.APPLICATION_MODAL);

        WindowTools.closeCurrentWindow(main_pane);
    }

    /**
     * check is all information witch need for test creation is correct
     *
     * @return
     */
    private boolean checkForCorrectInfo() {
        if (timeLimitEnabled_radioButton.isSelected()) {
            if (timeLimit_textField.getText().equals("")) {
                showAlert("Введіть кількість часу для проходження тесту\n(У хв.)");
                return false;
            }
            if (!isNumeric(timeLimit_textField.getText())) {
                if (!isNumeric(questionsAmount_textField.getText())) {
                    showAlert("Поле вводу ліміту часу повинне містити тільки цифри");
                    return false;
                }
            }
        }
        if (questionsAmount_textField.getText().equals("")) {
            showAlert("Поле вводу кількості питань не повинне бути пусттим");
            return false;
        } else {
            if (!isNumeric(questionsAmount_textField.getText())) {
                showAlert("Поле вводу кількості питань повинне містити тільки цифри");
                return false;
            }
        }
        if (testName_textField.getText().equals("")) {
            showAlert("Поле вводу назви тесту не повинне бути пустим");
            return false;
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
