package TestMaker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class WindowTools {
    /**
     * Opens new window from fxml
     * @param fxmlFileLocation location of window fxml file
     */
    public static void openNewWindow(String fxmlFileLocation, boolean isResizeable, Modality modality) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(fxmlFileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        assert root != null;
        stage.setScene(new Scene(root));
        stage.setResizable(isResizeable);
        stage.initModality(modality);
        stage.show();
    }
}
