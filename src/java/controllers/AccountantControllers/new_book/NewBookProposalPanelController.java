package controllers.AccountantControllers.new_book;

import controllers.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import models.tables.BookProposalTable;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class NewBookProposalPanelController implements Initializable {

    private final NewBookProposalService service;

    public NewBookProposalPanelController() {
        service = NewBookProposalService.getInstance();
    }
    @FXML
    public TableView<BookProposalTable> newProposalBookTable;

    @FXML
    public TableColumn<BookProposalTable, String> col_authorB;

    @FXML
    public TableColumn<BookProposalTable, String> col_priceB;

    @FXML
    public TableColumn<BookProposalTable, String> col_quantityB;

    @FXML
    public TableColumn<BookProposalTable, String> col_titleB;

    @FXML
    public Button zatwierdzKsiazke;

    @FXML
    public Button usunKsiazkeZListy;

    @FXML
    public Button zatwierdzListe;

    @FXML
    public TextField tytul;

    @FXML
    public TextField autor;

    @FXML
    public TextField cena;

    @FXML
    public TextField ilosc;

    @FXML
    public TextField budzet;

    private final SceneController sceneController = new SceneController();

    private void initializeTable() {
        budzet.setText("1000");
        newProposalBookTable.getItems().clear();
        newProposalBookTable.getItems().addAll(service.getProposal());
        col_titleB.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_authorB.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_priceB.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_quantityB.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        newProposalBookTable.setOnMouseClicked(e -> {
            BookProposalTable selectedItem = newProposalBookTable.getSelectionModel().getSelectedItem();
            Optional.ofNullable(selectedItem).ifPresent(el ->{
                tytul.setText(el.getTitle());
                autor.setText(el.getAuthor());
                cena.setText(el.getPrice());
                ilosc.setText(el.getQuantity());
            });

        });
        zatwierdzKsiazke.setOnMouseClicked(e -> {
            if (!tytul.getText().isBlank() &&
                    !autor.getText().isBlank() &&
                    !cena.getText().isBlank() &&
                    !ilosc.getText().isBlank()) {
                Optional<BookProposalTable> any = newProposalBookTable.getItems().stream()
                        .filter(el -> el.getTitle().equals(tytul.getText()) && el.getAuthor().equals(autor.getText()))
                        .findAny();
                any.ifPresentOrElse(el -> {
                            BookProposalTable selectedItem = newProposalBookTable.getSelectionModel().getSelectedItem();
                            int focusedIndex = newProposalBookTable.getSelectionModel().getFocusedIndex();
                            selectedItem.setPrice(cena.getText());
                            selectedItem.setQuantity(ilosc.getText());
                            newProposalBookTable.refresh();
                            newProposalBookTable.getFocusModel().focus(focusedIndex);
                        }
                        ,() -> newProposalBookTable.getItems()
                                .add(
                                        new BookProposalTable(
                                                tytul.getText(),
                                                autor.getText(),
                                                cena.getText(),
                                                ilosc.getText(),
                                                null))
                );
                Stream.of(tytul, autor, cena, ilosc).forEach(TextInputControl::clear);
            }
        });
        usunKsiazkeZListy.setOnMouseClicked(e -> {
            if (newProposalBookTable.selectionModelProperty().getValue().getSelectedItem() != null) {
                newProposalBookTable.getItems()
                        .remove(newProposalBookTable.selectionModelProperty().getValue().getSelectedItem());
            }
        });
        zatwierdzListe.setOnMouseClicked(e -> {
            double sumaWartosciKsiazek = newProposalBookTable.getItems().stream()
                    .map(el -> Double.parseDouble(el.getPrice()) * Double.parseDouble(el.getQuantity()))
                    .mapToDouble(i -> i)
                    .sum();
            String wartoscBudzetu = budzet.getText().isBlank() ? "0" : budzet.getText();

            if(sumaWartosciKsiazek < Double.parseDouble(wartoscBudzetu)){
                service.confirList(newProposalBookTable);
                initializeTable();
                sceneController.showAlert("Zapisano.","Informacja","Pozycje zostały zapisane w bazie danych.",
                        Alert.AlertType.INFORMATION);
            } else {
                sceneController.showAlert("Przekroczony budżet","Błąd","Wartość książek przekracza budżet.",
                        Alert.AlertType.ERROR);
            }

        });
    }

    public void backToMainScreen() throws IOException {
        SceneController.startScene("AccountantScenes/AccountantMainPanel");
    }
}
