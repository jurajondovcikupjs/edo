package sk.upjs.edo;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // login
        System.out.println("Používateľ: " + username);
        System.out.println("Heslo: " + password);
    }

    @FXML
    private void handleForgotPassword() {
        // password recovery
        System.out.println("Kliknuté na zabudnuté heslo");
    }

    @FXML
    private void createAccountButtonOnAction() {
        var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/sk/upjs/edo/Register.fxml"));

        try {
            var rootPane = loader.load();
            var scene = new javafx.scene.Scene((Parent) rootPane);
            var stage = new javafx.stage.Stage();
            stage.setTitle("Registrácia");
            stage.setScene(scene);
            EdoApp.applyWindowIcon(stage);
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
