package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionsTypes.OneAnswer;

import TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane.QuestionController;
import TestMaker.MainProgramWindow.Panes.TestsPane.TestsConstants;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class OneAnswerController extends TestsConstants implements QuestionController {
    @FXML
    private VBox answerVariants_vBox;

    @FXML
    private ToggleGroup correctAnswer_toggleGroup;

    @FXML
    private AnchorPane main_anchorPane;

    @FXML
    private Button addVariant_button;

    private int numberOfVariants;

    @FXML
    void initialize() {
        numberOfVariants = answerVariants_vBox.getChildren().size();
        addVariant_button.setOnAction(event -> {
            createNewVariant(null);
        });
        setRemoveVariantButtonAction();
    }

    /**
     * Set button actions for all buttons which should to remove answer variant
     */
    private void setRemoveVariantButtonAction() {
        for (int i = 0; i < answerVariants_vBox.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) answerVariants_vBox.getChildren().get(i);
            Button button = (Button)hBox.getChildren().get(2);
            button.setOnAction(event -> {
                 answerVariants_vBox.getChildren().remove(button.getParent());
            });
        }
    }

    private void createNewVariant(String variantText) {
        numberOfVariants++;
        //HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(hBOX_SPACING);
        //CheckBox
        RadioButton radioButton = new RadioButton("");
        //TextField
        TextField textField = new TextField();
        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.setText(variantText);
        textField.setPromptText("Варіант " + numberOfVariants);
        textField.setPrefWidth(TEXT_FIELD_PREF_WIDTH);
        //Remove variant button
        ImageView imageView = new ImageView("/TestMaker/Assets/Images/icons/minus32.png");
        imageView.setFitHeight(REMOVE_VARIANT_BUTTON_IMAGE_FIT_WIDTH);
        imageView.setFitWidth(REMOVE_VARIANT_BUTTON_IMAGE_FIT_HEIGHT);
        Button button = new Button("", imageView);
        //Compound elements
        hBox.getChildren().addAll(radioButton, textField, button);
        answerVariants_vBox.getChildren().add(answerVariants_vBox.getChildren().size()-1, hBox);
        //Update remove button actions
        setRemoveVariantButtonAction();
    }

    @Override
    public ArrayList<String> getQuestionsVariantsList() {
        return null;
    }

    @Override
    public ArrayList<String> getAnswersVariantsList() {
        return null;
    }

    @Override
    public void createNewQuestionVariant(String variantText) {

    }

    @Override
    public void createNewAnswerVariant(String variantText) {

    }
}
