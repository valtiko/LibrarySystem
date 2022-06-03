package controllers.AdminControllers;

import Database.Repository.AuthorRepository;
import controllers.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.entities.Author;

import java.io.IOException;


public class AddAuthorPanelController {
    private final AuthorRepository authorRepository = AuthorRepository.getInstance();
    private SceneController sceneController = new SceneController();


    @FXML
    private TextField lastName;
    @FXML
    private TextField authorName;


    @FXML
    public void goBack() {
        try {
            SceneController.startScene("AdminScenes/AdminBookPanel");
        } catch (IOException e) {
            sceneController.showAlert(
                    "Błąd",
                    "",
                    "Problem ze zmiana sceny",
                    Alert.AlertType.ERROR);
        }
    }

    public void addAuthor() {
        if (!lastName.getText().isEmpty() && !authorName.getText().isEmpty()) {
            try {
                if (authorRepository.findByNameAndLastName(authorName.getText(), lastName.getText()) == null) {
                    authorRepository.createAuthor(new Author(lastName.getText(), authorName.getText()));
                    sceneController.showAlert(
                            "Dodano autoa",
                            "",
                            "Dodano autora",
                            Alert.AlertType.INFORMATION);
                } else {
                    sceneController.showAlert(
                            "Istnieje juz taki autor!",
                            "",
                            "Istnieje juz taki autor, podaj inne dane",
                            Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                sceneController.showAlert(
                        "Istnieje juz taki autor!",
                        "",
                        "Istnieje juz taki autor, podaj inne dane",
                        Alert.AlertType.ERROR);
            }

        } else {
            sceneController.showAlert(
                    "Wszystkie pola nie sa wypelnione",
                    "",
                    "Wszystkie pola nie sa wypelnione",
                    Alert.AlertType.ERROR);
        }
    }

}