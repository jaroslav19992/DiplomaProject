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
    private static final String WINDOWS_TITLE = "Diploma project KM-17, Ishchak Yaroslav";

    /**
     * Opens new window from fxml
     *
     * @param fxmlFileLocation location of window fxml file
     */
    public Object openNewWindow(String fxmlFileLocation, boolean isResizeable, Modality modality) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFileLocation));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Diploma project KM-17, Ishchak Yaroslav");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
            stage.setScene(new Scene(root));
            stage.setResizable(isResizeable);
            stage.initModality(modality);
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Opens new window from fxml, stops any next operations until current window will be closed
     *
     * @param fxmlFileLocation location of window fxml file
     * @return
     */
    public Object openNewWindowAndWait(String fxmlFileLocation, boolean isResizeable, Modality modality) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(Main.class.getResource(fxmlFileLocation));
            Stage stage = new Stage();
            stage.setTitle(WINDOWS_TITLE);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("logo_mini.png")));
            stage.setScene(new Scene(root));
            stage.setResizable(isResizeable);
            stage.initModality(modality);
            stage.showAndWait();
            return loader.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set new pane as a child of parent pane
     * @param rootPane parent pane, in witch child will be located
     * @param fxmlFileLocation location to fxml file of child pane
     * @return class controller
     */
    public Object setUpNewPaneOnBorderPane(BorderPane rootPane, String fxmlFileLocation) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFileLocation));
            if (!rootPane.getChildren().isEmpty()) {
                rootPane.getChildren().remove(0);
            }
            rootPane.setCenter(loader.load());
            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Close window
     * @param node child window element
     */
    public void closeCurrentWindow(Node node) {
        node.getScene().getWindow().hide();
    }
}
