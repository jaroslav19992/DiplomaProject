package TestMaker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFileLocation));
            Stage stage = new Stage();
            stage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
            stage.setScene(new Scene(root));
            stage.setResizable(isResizeable);
            stage.initModality(modality);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens new window from fxml, stops any next operations until current window will be closed
     *
     * @param fxmlFileLocation location of window fxml file
     */
    public static void openNewWindowAndWait(String fxmlFileLocation, boolean isResizeable, Modality modality) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFileLocation));
            Stage stage = new Stage();
            stage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
            stage.setScene(new Scene(root));
            stage.setResizable(isResizeable);
            stage.initModality(modality);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set new pane as a child of parent pane
     * @param rootPane parent pane, in witch child will be located
     * @param fxmlFileLocation location to fxml file of child pane
     */
    public static void setUpNewPaneOnBorderPane(BorderPane rootPane, String fxmlFileLocation) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFileLocation));
            if (!rootPane.getChildren().isEmpty()) {
                rootPane.getChildren().remove(0);
            }
            rootPane.setCenter(loader.load());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Close window
     * @param node child window element
     */
    public static void closeCurrentWindow(Node node) {
        node.getScene().getWindow().hide();
    }
}
