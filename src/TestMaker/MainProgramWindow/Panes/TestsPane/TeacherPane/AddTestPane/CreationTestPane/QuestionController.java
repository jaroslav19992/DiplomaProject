package TestMaker.MainProgramWindow.Panes.TestsPane.TeacherPane.AddTestPane.CreationTestPane;

import java.util.ArrayList;

public interface QuestionController {
    public ArrayList<String> getQuestionsVariantsList();
    public ArrayList<String> getAnswersVariantsList();
    public void createNewQuestionVariant(String variantText);
    public void createNewAnswerVariant(String variantText);
}
