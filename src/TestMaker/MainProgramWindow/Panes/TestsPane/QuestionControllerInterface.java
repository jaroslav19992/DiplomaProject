package TestMaker.MainProgramWindow.Panes.TestsPane;

import javafx.scene.control.ButtonBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public interface QuestionControllerInterface {
    ArrayList<String> getQuestionsVariantsList();
    ArrayList<String> getAnswersVariantsList();
    void setQuestion(String questionType, double questionScore, String questionText, List<String> questionVariants, List<String> answerVariants);
    boolean isDuplicateVariantsExists();
    default void setDefaultCorrectAnswers(VBox vBox) {
        HBox hBox = (HBox) vBox.getChildren().get(0);
        ((ButtonBase) hBox.getChildren().get(0)).fire();
    }

    default void showAnswers(ArrayList<String> correctAnswers) {
        return;
    }
}
