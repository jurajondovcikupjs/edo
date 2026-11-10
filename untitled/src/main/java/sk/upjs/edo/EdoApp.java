package sk.upjs.edo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EdoApp extends Application {

    private static final String ICON_PATH = "/sk/upjs/edo/assets/logo.png";
    private static Image windowIcon;

    @Override
    public void start(Stage stage) throws Exception {
        var loader = new FXMLLoader(getClass().getResource("/sk/upjs/edo/LogInScreen.fxml"));
        Parent rootPane = loader.load();

        var scene = new Scene(rootPane);
        stage.setTitle("Log in");
        stage.setScene(scene);
        applyWindowIcon(stage);
        stage.show();
    }

    /**
     * Sets the application icon on the given stage. Call it for every window/stage
     * you open - the icon is loaded once and reused.
     */
    public static void applyWindowIcon(Stage stage) {
        if (windowIcon == null) {
            var url = EdoApp.class.getResource(ICON_PATH);
            if (url == null) {
                System.err.println("Icon resource not found: " + ICON_PATH);
                return;
            }
            var image = new Image(url.toString());
            if (image.isError()) {
                System.err.println("Icon could not be loaded: " + image.getException());
                return;
            }
            windowIcon = image;
        }
        stage.getIcons().setAll(windowIcon);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
