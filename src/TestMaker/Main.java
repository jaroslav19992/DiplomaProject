package TestMaker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("MainProgramWindow/Panes/TestsPane/TeacherPane/AddTestPane/EditConfigTestPane.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("LoginWindow/LoginWindow.fxml"));
        primaryStage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        Platform.setImplicitExit(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
