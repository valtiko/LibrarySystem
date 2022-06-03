package controllers.LibrarianControllers;

import Database.Repository.AuthorRepository;
import Database.Repository.BookRepository;
import Database.Repository.NewBookRepository;
import Database.Repository.ReportRepository;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.entities.Author;
import models.entities.Book;
import models.entities.NewBook;
import models.entities.Report;
import models.tables.DestroyedBookTable;
import models.tables.NewBookTable;

import javax.naming.Binding;
import javax.script.Bindings;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LibrarianReportsPanelController implements Initializable {
    private final BookRepository bookRepository = BookRepository.getInstance();
    private final NewBookRepository newBookRepository = NewBookRepository.getInstance();
    private final AuthorRepository authorRepository = AuthorRepository.getInstance();
    private final ReportRepository reportRepository = ReportRepository.getInstance();
    private SceneController sceneController = new SceneController();

    @FXML
    public TableView<NewBookTable> newBooksList;
    @FXML
    public TableColumn col_titleNew;
    @FXML
    public TableColumn col_authorNew;
    @FXML
    public TableColumn col_quantityNew;
    @FXML
    public TableView<DestroyedBookTable> destroyedBookList;
    @FXML
    public TableColumn col_titleDes;
    @FXML
    public TableColumn col_authorDes;
    @FXML
    public TableColumn col_categoryDes;
    @FXML
    public TableColumn col_isbnDes;
    @FXML
    public ComboBox<String> ISBNComboBox;
    @FXML
    public TextField newBookTitle;
    @FXML
    public TextField newBookAuthor;
    @FXML
    public TextField newBookQuantity;
    @FXML
    public ComboBox<Author> authorComboBox;
    @FXML
    public ComboBox<Book> booksComboBox;
    public Button deleteDestroyedBookButton;
    public Button addNewBookButton;
    public Button deleteNewBookButton;
    public Button addDestroyedBookButton;

    ObservableList<DestroyedBookTable> destroyedList = FXCollections.observableArrayList();
    ObservableList<NewBookTable> newList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDestroyedBookTable();
        initializeNewBookTable();
        initializeAuthorComboBox();
        initializeBookComboBox();
        initializeISBNComboBox();
    }

    private void initializeAuthorComboBox() {
        List<Author> authorList = new ArrayList<>(authorRepository.getAllAuthors());
        authorComboBox.setItems(FXCollections.observableArrayList(authorList));
    }

    private void initializeBookComboBox() {
        List<Book> bookList = new ArrayList<>(bookRepository.getAllBooks());
        booksComboBox.setItems(FXCollections.observableArrayList(bookList
                .stream()
                .filter(book -> book.getStatus().equals(Book.Status.NEW.toString()))
                .filter(book -> book.getAvailability().equals(Book.Availability.AVAILABLE.toString()))
                .filter(distinctByKey(Book::toString))
                .collect(Collectors.toList())));
    }

    private void initializeISBNComboBox() {
        ObservableList<String> bookISBNList = FXCollections.observableArrayList();
        List<Object> bookISBN = bookRepository.getBookISBN();
        for (Object o : bookISBN) {
            bookISBNList.add(o.toString());
        }
        ISBNComboBox.setItems(bookISBNList);
    }

    private void initializeDestroyedBookTable() {
        List<DestroyedBookTable> destroyedBooks = bookRepository.getAllDestroyedBooks();
        destroyedList.addAll(destroyedBooks);
        col_authorDes.setCellValueFactory(new PropertyValueFactory<DestroyedBookTable, String>("author"));
        col_categoryDes.setCellValueFactory(new PropertyValueFactory<DestroyedBookTable, String>("category"));
        col_isbnDes.setCellValueFactory(new PropertyValueFactory<DestroyedBookTable, String>("ISBN"));
        col_titleDes.setCellValueFactory(new PropertyValueFactory<DestroyedBookTable, String>("title"));
        destroyedBookList.setItems(destroyedList);
    }

    private void initializeNewBookTable() {
        List<NewBookTable> newBooks = newBookRepository.getNewBookList();
        newList.addAll(newBooks);
        col_authorNew.setCellValueFactory(new PropertyValueFactory<NewBookTable, String>("author"));
        col_titleNew.setCellValueFactory(new PropertyValueFactory<NewBookTable, String>("title"));
        col_quantityNew.setCellValueFactory(new PropertyValueFactory<NewBookTable, String>("quantity"));
        newBooksList.setItems(newList);
    }

    public void backToMainScreen() {
        try {
            SceneController.startScene("LibrarianScenes/LibrarianMainPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateStringForReport() {
        StringBuilder builder = new StringBuilder();
        List<DestroyedBookTable> destroyedBooks = destroyedBookList.getItems();
        for (DestroyedBookTable book : destroyedBooks) {
            builder.append("destroyedBook(").append(book.getISBN()).append(");\n");
        }
        List<NewBookTable> newBooks = newBooksList.getItems();
        for (NewBookTable book : newBooks) {
            builder.append("newBook(").append(book.getTitle()).append(",")
                    .append(book.getAuthor()).append(",").append(book.getQuantity()).append(");\n");
        }
        return builder.toString();
    }

    public void sendReport() {
        if (newBooksList.getItems().isEmpty() && destroyedBookList.getItems().isEmpty()) {
            sceneController.showAlert("Błąd","","Błąd podczas tworzenia raportu", Alert.AlertType.ERROR);
            return;
        }
        Report report = new Report(new Date(), Report.ReportType.List.toString(), generateStringForReport());
        reportRepository.createReport(report);
        List<DestroyedBookTable> destroyedBooks = destroyedBookList.getItems();
        for (DestroyedBookTable destroyedBook : destroyedBooks) {
            Book book = bookRepository.findBookByISBN(destroyedBook.getISBN());
            book.setStatus(Book.Status.REPORTED.toString());
            bookRepository.updateBook(book);
        }
        clearNewBookList();
        destroyedBookList.getItems().clear();
        newBooksList.getItems().clear();
        sceneController.showAlert("Sukces","","Pomyślnie wysłano raport", Alert.AlertType.INFORMATION);
        initializeDestroyedBookTable();
    }

    public void addDestroyedBookStatus() {
        if (ISBNComboBox.getValue() == null) return;
        Book book = bookRepository.findBookByISBN(ISBNComboBox.getSelectionModel().getSelectedItem());
        book.setStatus(Book.Status.DESTROYED.toString());
        bookRepository.updateBook(book);
        destroyedBookList.getItems().clear();
        initializeDestroyedBookTable();
        reloadComboBoxes();
    }

    public void deleteDestroyedBookStatus() {
        Book book = bookRepository.findBookByISBN(destroyedBookList.getSelectionModel().getSelectedItem().getISBN());
        book.setStatus(Book.Status.NEW.toString());
        bookRepository.updateBook(book);
        destroyedBookList.getItems().clear();
        deleteDestroyedBookButton.setDisable(true);
        initializeDestroyedBookTable();
        reloadComboBoxes();
    }


    public void deleteNewBookFromList() {
        NewBook book = newBookRepository.getBook(newBooksList.getSelectionModel().getSelectedItem().getTitle(),
                newBooksList.getSelectionModel().getSelectedItem().getAuthor());
        newBookRepository.deleteNewBook(book);
        deleteNewBookButton.setDisable(true);
        newBooksList.getItems().clear();
        initializeNewBookTable();
    }

    public void addNewBookToList() {
        if (newBookAuthor.getText().equals("") || newBookTitle.getText().equals("") || newBookQuantity.getText().equals(""))
            return;
        NewBook newBook = new NewBook();
        newBook.setAuthor(newBookAuthor.getText());
        newBook.setTitle(newBookTitle.getText());
        try {
            newBook.setQuantity(Integer.parseInt(newBookQuantity.getText()));
        } catch (NumberFormatException e) {
            sceneController.showAlert("Błąd", "", "Błąd podczas wprowadzania liczby książek", Alert.AlertType.ERROR);
            clearTextFields();
            return;
        }
        newBookRepository.createNewBook(newBook);
        newBooksList.getItems().clear();
        clearTextFields();
        initializeNewBookTable();
    }

    public void filterByAuthor() {
        if (authorComboBox.getSelectionModel().isEmpty()) return;
        List<String> ISBNList = new ArrayList<>();
        List<Book> authorBooks = authorComboBox.getValue().getBooks()
                .stream()
                .filter(book -> book.getStatus().equals(Book.Status.NEW.toString()))
                .filter(book -> book.getAvailability().equals(Book.Availability.AVAILABLE.toString()))
                .collect(Collectors.toList());
        booksComboBox.setItems(FXCollections.observableArrayList(authorBooks
                .stream()
                .filter(distinctByKey(Book::toString))
                .collect(Collectors.toList())));
        for (Book b : authorBooks) {
            ISBNList.add(b.getISBN());
        }
        ISBNComboBox.valueProperty().set(null);
        booksComboBox.valueProperty().set(null);
        ISBNComboBox.setItems(FXCollections.observableArrayList(ISBNList));

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public void filterByBookName() {
        if (booksComboBox.getSelectionModel().isEmpty()) return;
        List<String> ISBNList = new ArrayList<>();
        if (authorComboBox.getValue() == null) {
            List<Object> objects = bookRepository.getISBNListByTitle(booksComboBox.getSelectionModel().getSelectedItem().getBookName());
            for (Object o : objects) {
                ISBNList.add(o.toString());
            }
            ISBNComboBox.setItems(FXCollections.observableArrayList(ISBNList));
        }
        if (authorComboBox.getValue() != null && booksComboBox.getValue() != null) {
            Book book = booksComboBox.getValue();
            List<String> bookList = book.getAuthor().getBooks()
                    .stream()
                    .filter(b -> b.getStatus().equals(Book.Status.NEW.toString()))
                    .filter(b -> b.getAvailability().equals(Book.Availability.AVAILABLE.toString()))
                    .filter(b -> b.getBookName().equals(booksComboBox.getValue().getBookName()))
                    .map(Book::getISBN).collect(Collectors.toList());
            ISBNComboBox.setItems(FXCollections.observableArrayList(bookList));
        }
    }
    private void clearTextFields(){
        newBookQuantity.clear();
        newBookTitle.clear();
        newBookAuthor.clear();
    }

    private void reloadComboBoxes() {
        authorComboBox.getItems().clear();
        authorComboBox.valueProperty().set(null);
        booksComboBox.getItems().clear();
        booksComboBox.valueProperty().set(null);
        ISBNComboBox.getItems().clear();
        ISBNComboBox.valueProperty().set(null);
        initializeBookComboBox();
        initializeISBNComboBox();
        initializeAuthorComboBox();
    }

    public void changeAvailabilityOfDeleteDestroyedBookButton() {
        deleteDestroyedBookButton.setDisable(destroyedBookList.getSelectionModel().getSelectedItem() == null);
    }

    public void changeAvailabilityOfDeleteNewBookButton() {
        deleteNewBookButton.setDisable(newBooksList.getSelectionModel().getSelectedItem() == null);
    }

    private void clearNewBookList(){
        List<NewBookTable> newBooks = newBooksList.getItems();
        for (NewBookTable book : newBooks) {
            newBookRepository.deleteNewBook(newBookRepository.getBook(book.getTitle(),book.getAuthor()));
        }
    }
}
