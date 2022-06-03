package controllers.AdminControllers;

import Database.Repository.PersonRepository;
import controllers.LoginController;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.entities.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class AdminUserPanelController implements Initializable {
    private final PersonRepository personRepository = new PersonRepository();
    private List<Person> personList;
    private List<Person.Role> roleList;
    private SceneController sceneController = new SceneController();

    @FXML
    private TableView<Person> personTableView;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> emailColumn;
    @FXML
    private TableColumn<Person, String> roleColumn;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private ComboBox roleComboBox;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchTextField;


    ObservableList<Person> personObservableList = FXCollections.observableArrayList();

    private void initializeTable() {
        personList = personRepository.getAllClients();
        personObservableList.addAll(personList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        personTableView.setItems(personObservableList);

    }

    @FXML
    public void goReportPanel() {
        try {
            SceneController.startScene("AdminScenes/AdminReportPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logout() {
        try {
            SceneController.startScene("LoginScene");

        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController.loggedUser = null;


    }


    @FXML
    private void goBookPanel() {
        try {
            SceneController.startScene("AdminScenes/AdminBookPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToUserPanel() {
        try {
            SceneController.startScene("AdminScenes/AdminUserPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getSelected() {
        Person selectedItem = personTableView.getSelectionModel().getSelectedItem();
        nameTextField.setText(selectedItem.getName());
        lastNameTextField.setText(selectedItem.getLastName());
        emailTextField.setText(selectedItem.getEmail());
        roleComboBox.setValue(selectedItem.getRole());
    }

    @FXML
    public void editUser() {
        try {
            if (emailTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || lastNameTextField.getText()
                    .isEmpty() || roleComboBox.getValue().equals("")) {
                sceneController.showAlert("Blad",
                        "",
                        "Wypelnij wszystkie pola",
                        Alert.AlertType.ERROR);
            } else {
                Person selectedItems = personTableView.getSelectionModel().getSelectedItem();
                Person person = personRepository.findByEmail(selectedItems.getEmail());
                person.setEmail(emailTextField.getText());
                person.setName(nameTextField.getText());
                person.setLastName(lastNameTextField.getText());
                person.setRole(roleComboBox.getValue().toString());
                personRepository.updateClient(person);
                personTableView.getItems().removeAll(personTableView.getItems());
                personList = personRepository.getAllClients();
                personTableView.getItems().addAll(personList);
                personTableView.refresh();
            }
        } catch (Exception e) {
            sceneController.showAlert("Bład",
                    "",
                    "Podaj inne dane, istnieje juz taki uzytkownik",
                    Alert.AlertType.ERROR);

        }
        nameTextField.setText("");
        emailTextField.setText("");
        lastNameTextField.setText("");
        roleComboBox.setValue("");
        searchTextField.setText(null);
    }


    @FXML
    public void deleteUser() throws IOException {
        int index = personTableView.getSelectionModel().getFocusedIndex();
        if (index >= 0) {
            deleteButton.setDisable(false);
            personRepository.deleteClient(personRepository.findByNameLastNameEmailRole(
                    nameColumn.getCellData(index),
                    lastNameColumn.getCellData(index),
                    emailColumn.getCellData(index),
                    roleColumn.getCellData(index)));
            personTableView.getItems().removeAll(personTableView.getItems());
            personList = personRepository.getAllClients();
            personTableView.getItems().addAll(personList);
        } else {
            sceneController.showAlert(
                    "Bład",
                    "",
                    "Zaznacz uzytkownika na liscie",
                    Alert.AlertType.ERROR);
        }
        personTableView.refresh();
        nameTextField.setText("");
        lastNameTextField.setText("");
        emailTextField.setText("");
        roleComboBox.setValue("");
        searchTextField.setText(null);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBox();
        initializeTable();
    }

    private void initializeComboBox() {
        roleList = new ArrayList<>();
        roleList.addAll(Arrays.asList(Person.Role.values()));
        roleComboBox.setItems(FXCollections.observableArrayList(Person.Role.values()));
    }


    public void searchText() {
        personList = personRepository.getAllClients();
        List<Person> listOfAllPersons = new ArrayList<>(personList);
        List<Person> filteredList = listOfAllPersons
                .stream()
                .filter(person -> person.getName()
                        .toLowerCase()
                        .contains(searchTextField.getText().toLowerCase())
                        || person.getLastName()
                        .toLowerCase()
                        .contains(searchTextField.getText().toLowerCase())
                        || person.getEmail()
                        .toLowerCase()
                        .contains(searchTextField.getText().toLowerCase())).collect(Collectors.toList());
        personTableView.setItems(FXCollections.observableArrayList(filteredList));
    }
}

