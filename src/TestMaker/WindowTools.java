package TestMaker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class WindowTools {
    /**
     * Opens new window from fxml
     *
     * @param fxmlFileLocation location of window fxml file
     */
    public static void openNewWindow(String fxmlFileLocation, boolean isResizeable, Modality modality) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(Main.class.getResource(fxmlFileLocation));
            Stage stage = new Stage();
            stage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
            stage.getIcons().add(new Image("@../../Images/logo_mini.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(isResizeable);
            stage.initModality(modality);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getCause());
            e.printStackTrace();
        }

    }

    public static void openNewWindowAndWait(String fxmlFileLocation, boolean isResizeable, Modality modality) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(fxmlFileLocation));
        } catch (IOException e) {
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
        stage.getIcons().add(new Image("@../../Images/logo_mini.png"));
        stage.setScene(new Scene(root));
        stage.setResizable(isResizeable);
        stage.initModality(modality);
        stage.showAndWait();
    }

    public void setUpNewPaneOnBorderPane(BorderPane rootPane, String fxmlFileLocation) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFileLocation));
            if (!rootPane.getChildren().isEmpty()) {
                rootPane.getChildren().remove(0);
            }
            rootPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
