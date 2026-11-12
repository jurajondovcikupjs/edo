package sk.upjs.edo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;

public class SettingsController {

    @FXML
    private ChoiceBox<String> languageChoiceBox;

    @FXML
    private ComboBox<String> themeComboBox;

    @FXML
    private ColorPicker accentColorPicker;

    @FXML
    private CheckBox notificationsCheckBox;

    @FXML
    private CheckBox soundCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // Naplnenie mozbnosti pre Jazyk
        languageChoiceBox.setItems(FXCollections.observableArrayList(
                "Slovenčina", "English", "Deutsch", "Čeština"
        ));
        languageChoiceBox.setValue("Slovenčina");

        // Naplnenie moznosti pre Tému
        themeComboBox.setItems(FXCollections.observableArrayList(
                "Svetlá (Light)", "Tmavá (Dark)", "Systémová"
        ));
        themeComboBox.setValue("Svetlá (Light)");
    }

    @FXML
    private void handleSave() {
        String selectedLanguage = languageChoiceBox.getValue();
        String selectedTheme = themeComboBox.getValue();
        Color selectedColor = accentColorPicker.getValue();
        boolean notifications = notificationsCheckBox.isSelected();
        boolean sound = soundCheckBox.isSelected();

        System.out.println("--- Uložené nastavenia ---");
        System.out.println("Jazyk: " + selectedLanguage);
        System.out.println("Téma: " + selectedTheme);
        System.out.println("Accent Farba (Hex): #" + selectedColor.toString().substring(2, 8));
        System.out.println("Notifikácie: " + notifications + " | Zvuk: " + sound);
    }

    @FXML
    private void handleCancel() {
        System.out.println("Zmeny zrušené.");
    }
}