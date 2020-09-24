package TestMaker;

import TestMaker.DBTools.Configs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginWindow/LoginWindow.fxml"));
        primaryStage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
        primaryStage.getIcons().add(new Image("@../../Images/logo_mini.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

//        System.out.println("admin".hashCode()+" "+"admin".hashCode());
//
//        Date dateNow = new Date();
//        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
//
//        System.out.println(formatForDateNow.format(dateNow));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
