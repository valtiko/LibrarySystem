package controllers;

import Database.Repository.PersonRepository;
import controllers.security.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.entities.Person;

import java.io.IOException;

public class LoginController{

    @FXML
    TextField emailField;
    @FXML
    PasswordField passwordField;

    PersonRepository personRepository = PersonRepository.getInstance();
    SceneController sceneController = new SceneController();
    public static Person loggedUser;

    public void Login() {
        String email = emailField.getText();
        String password = passwordField.getText();
        Person person = personRepository.findByEmail(email);
        if (person != null) {
            String role = person.getRole();
            if (BCrypt.checkpw(password,person.getPassword())) {
                try {
                    loggedUser = person;
                    switch (role) {
                        case "USER" -> SceneController.startScene("UserScenes/UserPanels");
                        case "LIBRARIAN" -> SceneController.startScene("LibrarianScenes/LibrarianMainPanel");
                        case "ADMIN" -> SceneController.startScene("AdminScenes/AdminBookPanel");
                        case "ACCOUNTANT" -> SceneController.startScene("AccountantScenes/AccountantMainPanel");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else sceneController.showAlert("Błędne hasło","Błąd","Podane hasło jest nieprawidłowe",
                    Alert.AlertType.ERROR);
        }else sceneController.showAlert("Błędny email","Błąd","Podaj prawidłowy email",
                Alert.AlertType.ERROR);
    }

    public void goToRegister() throws IOException {
        SceneController.startScene("RegisterScene");
    }
}
