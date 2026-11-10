package sk.upjs.edo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private void handleRegisterAction() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            System.out.println("Heslá sa nezhodujú!");
            return;
        }

        // Tu pridaj logiku pre uloženie nového používateľa
        System.out.println("Registrácia používateľa: " + name + " (" + email + ")");
    }

    @FXML
    private void handleBackToLogin() {
        // Logika pre návrat na prihlasovacie okno (zmena scény)
        System.out.println("Prechod na prihlásenie");
    }
}
