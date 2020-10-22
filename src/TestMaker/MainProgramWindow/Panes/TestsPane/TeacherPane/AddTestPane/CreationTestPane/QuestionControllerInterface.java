package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane;

import java.util.ArrayList;
import java.util.List;

public interface QuestionControllerInterface {
    ArrayList<String> getQuestionsVariantsList();
    ArrayList<String> getAnswersVariantsList();
    void setQuestion(String questionType, String questionText, List<String> questionVariants, List<String> answerVariants);
    boolean isDuplicateVariantsExists();
}
