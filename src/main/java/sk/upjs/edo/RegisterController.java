package sk.upjs.edo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.concurrent.TimeUnit;

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
    private Text infoText;

    @FXML
    private void handleRegisterAction() throws InterruptedException {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            infoText.setText("Všetky polia sú povinné!");
            infoText.setFill(javafx.scene.paint.Color.RED);
            System.out.println("Všetky polia sú povinné!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            infoText.setText("Heslá sa nezhodujú!");
            infoText.setFill(javafx.scene.paint.Color.RED);
            System.out.println("Heslá sa nezhodujú!");
            return;
        }

        if (verifyEmailFormat(email)) {
            infoText.setText("Neplatný formát e-mailu!");
            infoText.setFill(javafx.scene.paint.Color.RED);
            return;
        }

        // register user logic
        infoText.setText("Registrácia úspešná!");
        infoText.setFill(javafx.scene.paint.Color.GREEN);
        System.out.println("Registrácia používateľa: " + name + " (" + email + ")");
    }

    @FXML
    private void handleBackToLogin() {
        // Logika pre návrat na prihlasovacie okno (zmena scény)
        System.out.println("Prechod na prihlásenie");
    }

    private boolean verifyEmailFormat(String email) {
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            infoText.setText("Neplatný formát e-mailu!");
            infoText.setFill(javafx.scene.paint.Color.RED);
            System.out.println("Neplatný formát e-mailu: " + email);
            return false;
        }
        return true;
    }
}
