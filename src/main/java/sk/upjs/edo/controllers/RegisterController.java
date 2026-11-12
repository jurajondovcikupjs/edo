package sk.upjs.edo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sk.upjs.edo.Factory;
import sk.upjs.edo.exceptions.BadRequestException;
import sk.upjs.edo.exceptions.NotFoundException;
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

    @FXML
    private void handleRegisterAction() throws NotFoundException, BadRequestException {
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
        User newUser = new User(null, name, email, password, java.time.LocalDate.now(), true);
        User createdUser = userDao.create(newUser);

        infoText.setText("Registrácia úspešná!");
        infoText.setFill(javafx.scene.paint.Color.GREEN);
        System.out.println(createdUser);
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
