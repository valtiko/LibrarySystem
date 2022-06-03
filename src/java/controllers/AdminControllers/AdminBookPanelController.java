package controllers.AdminControllers;

import Database.Repository.AuthorRepository;
import Database.Repository.BookRepository;
import controllers.LoginController;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.entities.Author;
import models.entities.Book;
import models.tables.BookTable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminBookPanelController implements Initializable {
    private final BookRepository bookRepository = BookRepository.getInstance();
    private final AuthorRepository authorRepository = AuthorRepository.getInstance();
    public static List<BookTable> bookList;
    private static List<Author> authorList;
    private SceneController sceneController = new SceneController();
    @FXML
    private TableView<BookTable> bookTableView;
    @FXML
    private TableColumn<BookTable, String> col_titleNew;
    @FXML
    private TableColumn<BookTable, String> col_authorNew;
    @FXML
    private TableColumn<BookTable, String> col_categoryNew;
    @FXML
    private TableColumn<BookTable, String> isbnColumn;


    @FXML
    private TextField bookNameTextField;
    @FXML
    private ComboBox<Author> authorComboBox;
    @FXML
    private TextField categoryTextField;
    @FXML
    private TextField searchTextField;


    ObservableList<BookTable> observableList = FXCollections.observableArrayList();

    private void initializeTable() {
        List<BookTable> bookList = bookRepository.getBookTables();
        observableList.addAll(bookList);
        col_titleNew.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        col_authorNew.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_categoryNew.setCellValueFactory(new PropertyValueFactory<>("category"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        bookTableView.setItems(observableList);

    }

    public void getSelected() {
        BookTable selectedItem = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            bookNameTextField.setText(selectedItem.getBookName());
            categoryTextField.setText(selectedItem.getCategory());
            authorComboBox.setValue(null);
        }

    }


    public void searchText() {
        bookList = bookRepository.getBookTables();
        List<BookTable> listOfAllBooks = new ArrayList<>(bookList);
        List<BookTable> filteredList = listOfAllBooks
                .stream()
                .filter(bookTable -> bookTable.getBookName().toLowerCase()
                        .contains(searchTextField.getText().toLowerCase())
                        || bookTable.getAuthor().toLowerCase()
                        .contains(searchTextField.getText().toLowerCase()))
                .collect(Collectors.toList());
        bookTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void editBook() {
        Book book;
        BookTable selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            sceneController.showAlert("Bład", "", "Zaznacz poprawnie ksiazke w tabeli", Alert.AlertType.ERROR);
        } else {
            if (authorComboBox.getValue() != null) {
                book = bookRepository.findBookByISBN(selectedBook.getISBN());
                book.setAuthor(authorComboBox.getValue());
                book.setBookName(bookNameTextField.getText());
                book.setCategory(categoryTextField.getText());
                try {
                    bookRepository.updateBook(book);
                    bookTableView.getItems().removeAll(bookTableView.getItems());
                    bookList = bookRepository.getBookTables();
                    bookTableView.getItems().addAll(bookList);
                    searchTextField.setText(null);
                    bookTableView.refresh();
                } catch (Exception e) {
                    sceneController.showAlert("Bład", "", "Podaj inne dane", Alert.AlertType.ERROR);

                }
            } else {
                sceneController.showAlert("Bład", "", "Wybierz wszystkie pola", Alert.AlertType.ERROR);

            }
        }
        bookNameTextField.setText("");
        categoryTextField.setText("");
        authorComboBox.setValue(authorList.get(0));
    }

    @FXML
    public void addBook() {
        if (authorComboBox.getValue() != null && !bookNameTextField.getText().isEmpty() && !categoryTextField.getText().isEmpty()) {
            try {
                Author authorFromDatabase = authorRepository.findByNameAndLastName(authorComboBox.getValue().getAuthorName(), authorComboBox.getValue().getLastName());
                Book newBook = new Book(bookNameTextField.getText(), authorFromDatabase, categoryTextField.getText());
                bookRepository.createISBN(newBook);
                bookRepository.createBook(newBook);
                bookTableView.getItems().removeAll(bookTableView.getItems());
                bookList = bookRepository.getBookTables();
                bookTableView.getItems().addAll(bookList);
                searchTextField.setText(null);
                bookTableView.refresh();
            } catch (Exception e) {
                sceneController.showAlert("Blad poczas dodawania", "", "Blad poczas dodawania", Alert.AlertType.ERROR);
            }
        } else {
            sceneController.showAlert("Bład", "", "Wypelnij wszystkie pola", Alert.AlertType.ERROR);

        }

    }

    @FXML
    public void goAddAuthorScene() {
        try {
            SceneController.startScene("AdminScenes/AddAuthorPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController.loggedUser = null;
    }

    @FXML
    public void goAddUserScene() {
        try {
            SceneController.startScene("AdminScenes/AdminUserPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController.loggedUser = null;
    }


    @FXML
    public void deleteBook() {
        int index = bookTableView.getSelectionModel().getFocusedIndex();
        if (index >= 0 && index < bookTableView.getItems().size()) {
            Book bookByISBN = bookRepository.findByISBN(isbnColumn.getCellData(index));
            if (bookByISBN.getAvailability().equals(Book.Availability.BORROWED.toString())) {
                sceneController.showAlert("Bląd", "", "Ksiazka ktora chcesz usunac jest wypozyczona", Alert.AlertType.ERROR);
            } else {
                bookRepository.deleteBook(bookByISBN);
                bookTableView.getItems().removeAll(bookTableView.getItems());
                bookList = bookRepository.getBookTables();
                bookTableView.getItems().addAll(bookList);
                bookTableView.refresh();
            }
        } else {
            sceneController.showAlert(
                    "Błąd",
                    "",
                    "Zaznacz ksiazke na liscie",
                    Alert.AlertType.ERROR);
        }
        bookNameTextField.setText("");
        categoryTextField.setText("");
        authorComboBox.setValue(null);
        searchTextField.setText(null);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        initializeComBox();
    }

    public void initializeComBox() {
        authorList = new ArrayList<>();
        authorList.addAll((authorRepository.getAllAuthors()));
        authorComboBox.setItems(FXCollections.observableArrayList(authorList));
        authorComboBox.setValue(authorList.get(0));
    }

    @FXML
    public void goReportPanel() {
        try {
            SceneController.startScene("AdminScenes/AdminReportPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
