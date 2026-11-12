package sk.upjs.edo.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sk.upjs.edo.EdoApp;
import sk.upjs.edo.Factory;
import sk.upjs.edo.users.UserDao;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    private final UserDao userDao = Factory.INSTANCE.getUserDao();

    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the user exists and the password is correct
        if (userDao.authenticate(username, password)) {
            System.out.println("Prihlásenie úspešné");
            System.out.println("Prihlásený používateľ: " + username);
        } else {
            System.out.println("Prihlásenie zlyhalo");
        }
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
