package sk.upjs.edo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sk.upjs.edo.Factory;
import sk.upjs.edo.settings.Settings;
import sk.upjs.edo.settings.SettingsDao;
import sk.upjs.edo.users.User;
import sk.upjs.edo.users.UserDao;

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

    private final UserDao userDao = Factory.INSTANCE.getUserDao();
    private final SettingsDao settingsDao = Factory.INSTANCE.getSettingsDao();

    @FXML
    private void handleRegisterAction() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            infoText.setText("Všetky polia sú povinné!");
            infoText.setFill(Color.RED);
            System.out.println("Všetky polia sú povinné!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            infoText.setText("Heslá sa nezhodujú!");
            infoText.setFill(Color.RED);
            System.out.println("Heslá sa nezhodujú!");
            return;
        }

        if (verifyEmailFormat(email)) {
            infoText.setText("Neplatný formát e-mailu!");
            infoText.setFill(Color.RED);
            return;
        }

        // register user logic
        try {
            User newUser = new User(null, name, email, password, java.time.LocalDate.now(), true);
            User createdUser = userDao.create(newUser);
            settingsDao.initialize(createdUser.user_id(),
                    new Settings(createdUser.user_id(), "sk", "light", "blue", true));

            infoText.setText("Registrácia úspešná!");
            infoText.setFill(Color.GREEN);
            System.out.println("Vytvorený používateľ: " + createdUser);
        } catch (Exception e) {
            infoText.setText("Registrácia zlyhala: " + e.getMessage());
            infoText.setFill(Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() {
        // Logika pre návrat na prihlasovacie okno (zmena scény)
        System.out.println("Prechod na prihlásenie");
    }

    private boolean verifyEmailFormat(String email) {
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            infoText.setText("Neplatný formát e-mailu!");
            infoText.setFill(Color.RED);
            System.out.println("Neplatný formát e-mailu: " + email);
            return false;
        }
        return true;
    }
}
