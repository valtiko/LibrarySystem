package controllers;

import Database.Repository.PersonRepository;
import controllers.security.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.entities.Person;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class RegisterController {

    PersonRepository personRepository = PersonRepository.getInstance();
    SceneController sceneController = new SceneController();

    @FXML
    private TextField nameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;


    public void backToLoginScene() throws IOException {
        SceneController.startScene("LoginScene");
    }

    public void register() throws IOException {
        Person person = new Person(nameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText(), Person.Role.USER);
        if (validation(person)) {
            person.setPassword(hashPassword(passwordField.getText()));
            personRepository.createClient(person);
            SceneController.startScene("LoginScene");
        }

    }

    public Boolean validation(Person person) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(person.getEmail());
        if (person.getName().isEmpty() || person.getLastName().isEmpty()) {
            sceneController.showAlert("Błąd", "Błąd", "Musisz podać imię i nazwisko", Alert.AlertType.ERROR);
            return false;
        }
        if (!matcher.matches()) {
            sceneController.showAlert("Błąd", "Błąd", "Musisz podać email", Alert.AlertType.ERROR);
            return false;
        }
        if (person.getPassword().length() < 5) {
            sceneController.showAlert("Błąd", "Błąd", "Hasło musi mieć więcej niż 5 znaków", Alert.AlertType.ERROR);
            return false;
        }
        sceneController.showAlert("Sukces", "Sukces", "Rejestracja przebiegła pomyślnie", Alert.AlertType.CONFIRMATION);
        return true;
    }
    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

}

