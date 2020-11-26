package TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.TestResultsPane;

import TestMaker.MainProgramWindow.Panes.TestsPane.PupilPane.PassingTestPane.PassingTestPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TestResultsPaneController {
    @FXML
    private Label attempts_label;
    @FXML
    private Label score_Label;
    @FXML
    private Button closeTest_button;
    @FXML
    private Button showResults_button;
    @FXML
    private Label proverb_label;
    private Pane parentPane;
    private PassingTestPaneController parentController;

    //TODO: завантажити сюди фхмл якось

    public TestResultsPaneController(AnchorPane pane) {
        parentPane = pane;
    }

    public void setTestResults(Double score, int evSystem, int leftAttempts) {
        score_Label.setText(score + " зі " + evSystem);
        attempts_label.setText(String.valueOf(leftAttempts));
        showProverb();
        setButtonActions();
    }

    /**
     * Show random proverb from file at label
     */
    private void showProverb() {
        ArrayList<String> proverbs = new ArrayList<>();
        //read proverbs from file line by line
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("./src/TestMaker/Assets/proverbs.txt")));
            String line = reader.readLine();
            while (line != null) {
                proverbs.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        proverb_label.setText(proverbs.get(new Random().nextInt(proverbs.size())));
    }

    private void setButtonActions() {
        closeTest_button.setOnAction(event -> {
            parentPane.getScene().getWindow().hide();
        });

        showResults_button.setOnAction(event -> {
            AnchorPane rootPane = (AnchorPane) parentPane.getParent();
            rootPane.getChildren().remove(parentPane);
            parentController.removeTestButtons();
            parentController.getMainPane().getScene().getWindow().setOnCloseRequest(event1 -> {
            });
            parentController.showCorrectAnswers();
        });
    }

    public void giveAccess(PassingTestPaneController parentController) {
        this.parentController = parentController;
    }
}
