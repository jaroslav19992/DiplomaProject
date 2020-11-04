package TestMaker;

import TestMaker.DOM.DOMxmlReader;
import TestMaker.MainProgramWindow.Panes.TestsPane.Question;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/ConfigTestPane.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("LoginWindow/LoginWindow.fxml"));
        primaryStage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        File file = new File("src/TestMaker/testXML.xml");
        DOMxmlReader parser = new DOMxmlReader(file);
        System.out.println("test name: " + parser.getTestName());
        System.out.println("q. number: " + parser.getAmountOfQuestions());
        System.out.println("is retestable : " + parser.getNumberOfAttempts());
        System.out.println("ev. system: " + parser.getTestEVSystem());
        ArrayList<Question> awd = parser.getQuestionsList();
        System.out.println(awd.get(0).getCorrectAnswer());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
