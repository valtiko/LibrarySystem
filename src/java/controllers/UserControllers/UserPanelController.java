package controllers.UserControllers;


import Database.Repository.*;
import controllers.LoginController;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.entities.*;
import models.tables.BookTable;
import models.tables.BorrowTableElement;
import models.tables.ReservationTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class UserPanelController {


    @FXML
    private TableView bookList;
    @FXML
    private TableColumn col_titleBorrow;
    @FXML
    private TableColumn col_authorBorrow;
    @FXML
    private TableColumn col_categoryBorrow;
    @FXML
    private TableColumn col_isbnBorrow;
    @FXML
    private TableColumn col_availability;
    @FXML
    private TableView historyList;
    @FXML
    private TableColumn col_book;
    @FXML
    private TableColumn col_startOfBorrow;
    @FXML
    private TableColumn col_endOfBorrow;
    @FXML
    private TableColumn col_wasExtended;
    @FXML
    private TableView reservationList;
    @FXML
    private TableColumn col_titleReservation;
    @FXML
    private TableColumn col_authorReservation;
    @FXML
    private TableColumn col_categoryReservation;
    @FXML
    private TableColumn col_isbnReservation;
    @FXML
    private TableColumn col_reservationDate;
    @FXML
    private TextField text_bookTitle;
    @FXML
    private TextField text_bookAuthor;
    @FXML
    private TextField text_bookCategory;
    @FXML
    private TextField text_borrowBook;
    @FXML
    private TextField text_borrowStartDate;
    @FXML
    private TextField text_borrowEndDate;
    @FXML
    private TextField text_resBook;
    @FXML
    private TextField text_resAuthor;
    @FXML
    private TextField text_resCategory;
    @FXML
    private Button borrowButton;
    @FXML
    private Button reservationButton;

    SceneController sceneController = new SceneController();
    BookRepository bookRepository = BookRepository.getInstance();
    BorrowRepository borrowRepository = BorrowRepository.getInstance();
    PersonRepository personRepository = PersonRepository.getInstance();
    ReservationRepository reservationRepository = ReservationRepository.getInstance();
    ReturnRepository returnRepository = ReturnRepository.getInstance();
    ObservableList<BookTable> observableBookList = FXCollections.observableArrayList();
    ObservableList<BorrowTableElement> observableBorrowList = FXCollections.observableArrayList();
    ObservableList<ReservationTable> observableReservationList = FXCollections.observableArrayList();
    Person loggedUser = LoginController.loggedUser;

    public void initialize() {
        loggedUser = personRepository.findByEmail(loggedUser.getEmail());
        initializeBookTable();
        initializeBorrowHistoryTable();
        initializeReservationTable();
        borrowButton.setDisable(true);
        reservationButton.setDisable(true);

    }

    private void initializeBookTable() {
        observableBookList.addAll(bookRepository.getBookTables());
        col_titleBorrow.setCellValueFactory(new PropertyValueFactory<BookTable, String>("bookName"));
        col_authorBorrow.setCellValueFactory(new PropertyValueFactory<BookTable, String>("author"));
        col_categoryBorrow.setCellValueFactory(new PropertyValueFactory<BookTable, String>("category"));
        col_isbnBorrow.setCellValueFactory(new PropertyValueFactory<BookTable, String>("ISBN"));
        col_availability.setCellValueFactory(new PropertyValueFactory<BookTable, String>("availability"));
        bookList.setItems(observableBookList);
    }

    private void initializeBorrowHistoryTable() {
        observableBorrowList.addAll(borrowRepository.getAllPersonBorrows(loggedUser));
        col_book.setCellValueFactory(new PropertyValueFactory<BorrowTableElement, String>("book"));
        col_startOfBorrow.setCellValueFactory(new PropertyValueFactory<BorrowTableElement, Date>("startOfBorrow"));
        col_endOfBorrow.setCellValueFactory(new PropertyValueFactory<BorrowTableElement, Date>("endOfBorrow"));
        col_wasExtended.setCellValueFactory(new PropertyValueFactory<BorrowTableElement, Boolean>("wasExtended"));
        historyList.setItems(observableBorrowList);
    }

    private void initializeReservationTable() {
        observableReservationList.addAll(reservationRepository.getAllUserReservations(loggedUser.getId()));
        col_titleReservation.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("tittle"));
        col_authorReservation.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("author"));
        col_categoryReservation.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("category"));
        col_isbnReservation.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("ISBN"));
        col_reservationDate.setCellValueFactory(new PropertyValueFactory<ReservationTable, Date>("date"));
        reservationList.setItems(observableReservationList);
    }

    public void borrowBook() {
        BookTable bookTable = (BookTable) bookList.getSelectionModel().getSelectedItem();
        Book book = bookRepository.findBookByISBN(bookTable.getISBN());
        if (!borrowRepository.isBookBorrowed(loggedUser.getEmail(), book.getBookName())) {
            borrowRepository.createBorrow(new Borrow(loggedUser, book));
            bookRepository.changeAvailabilityOfBookToBorrowed(book);
            sceneController.showAlert("Sukces", "Sukces", "Wypożyczono Książkę",
                    Alert.AlertType.INFORMATION);
            resetTables();
        } else sceneController.showAlert("Błąd", "Błąd rezerwacji", "Już masz wypożyczoną tą książkę",
                Alert.AlertType.ERROR);
    }

    private void resetTables() {
        historyList.getItems().clear();
        bookList.getItems().clear();
        reservationList.getItems().clear();
        initialize();
    }

    public void sendExtendBorrowRequest() {
        BorrowTableElement borrowTableElement = (BorrowTableElement) historyList.getSelectionModel().getSelectedItem();
        Borrow borrow = borrowRepository.getBorrowByBook(bookRepository.findBookByISBN(borrowTableElement.getBookISBN()));
        if (borrow.getWasExtended().equals(Borrow.WasExtended.NOT_EXTENDED.toString())) {
            borrowRepository.updateExtensionStatus(borrow, Borrow.WasExtended.REQUESTED.toString());
            sceneController.showAlert("Sukces", "", "Wysłano prośbę o przedłużenie",
                    Alert.AlertType.INFORMATION);
            resetTables();
        } else {
            sceneController.showAlert("Błąd", "", "Już wysłałeś prośbę o przedłużenie",
                    Alert.AlertType.ERROR);
        }
    }

    public void changeAvailabilityOfButton() {
        BookTable bookTable = (BookTable) bookList.getSelectionModel().getSelectedItem();
        if (bookTable != null) {
            if (bookTable.getAvailability().equals("AVAILABLE")) {
                borrowButton.setDisable(false);
                reservationButton.setDisable(true);
            } else if (bookTable.getAvailability().equals("BORROWED")) {
                borrowButton.setDisable(true);
                reservationButton.setDisable(false);
            } else {
                borrowButton.setDisable(false);
                reservationButton.setDisable(false);
            }
        }
    }

    public void reserveBook() {
        BookTable bookTable = (BookTable) bookList.getSelectionModel().getSelectedItem();
        Book book = bookRepository.findBookByISBN(bookTable.getISBN());
        if (!borrowRepository.isBookBorrowed(loggedUser.getEmail(), book.getBookName())) {
            if (reservationRepository.isBookAlreadyReserved(loggedUser.getId(), book.getBookName())) {
                sceneController.showAlert("Sukces", "Sukces", "Zarezerwowano Książkę",
                        Alert.AlertType.INFORMATION);
                reservationRepository.createReservation(new Reservation(book, loggedUser));
                resetTables();
            } else sceneController.showAlert("Błąd", "Błąd rezerwacji",
                    "Już zarezerwowałeś tą książkę",
                    Alert.AlertType.ERROR);
        } else sceneController.showAlert("Błąd", "Błąd rezerwacji",
                "Ta książka jest przez ciebie wypożyczona",
                Alert.AlertType.ERROR);
    }

    public void cancelReservation() {
        ReservationTable reservationTable = (ReservationTable) reservationList.getSelectionModel().getSelectedItem();
        Reservation reservation = reservationRepository.findReservationByBook(bookRepository.
                findBookByISBN(reservationTable.getISBN()));
        reservationRepository.deleteReservation(reservation);
        resetTables();
    }

    public void returnBook() {
        BorrowTableElement borrowTableElement = (BorrowTableElement) historyList.getSelectionModel().getSelectedItem();
        Borrow borrow = borrowRepository.getBorrowByBook(bookRepository.findBookByISBN(borrowTableElement.getBookISBN()));
        if (borrow.getAReturn() == null) {
            sceneController.showAlert("Sukces", "Zwrot", "Wysłano prośbę o zwrot",
                    Alert.AlertType.INFORMATION);
            returnRepository.createReturn(new Return(borrow));
        } else sceneController.showAlert("Błąd", "", "Już wysłałeś prośbę o zwrot",
                Alert.AlertType.ERROR);
    }


    public void searchBooks() {
        List<BookTable> books = bookRepository.getBookTables();
        List<BookTable> filteredList = books.stream().filter(bookTable -> bookTable.getBookName().toLowerCase()
                .contains(text_bookTitle.getText().toLowerCase())
                && bookTable.getAuthor().toLowerCase().contains(text_bookAuthor.getText().toLowerCase())
                && bookTable.getCategory().toLowerCase().contains(text_bookCategory.getText().toLowerCase()))
                .collect(Collectors.toList());
        bookList.setItems(FXCollections.observableArrayList(filteredList));
        text_bookTitle.setText("");
        text_bookAuthor.setText("");
        text_bookCategory.setText("");
    }

    public void searchBorrows() {
        List<BorrowTableElement> borrows = borrowRepository.getAllPersonBorrows(loggedUser);
        if ((text_borrowStartDate.getText().matches("[0-9]+") || text_borrowStartDate.getText().isEmpty())
                && text_borrowEndDate.getText().matches("[0-9]+") || text_borrowEndDate.getText().isEmpty()) {
            List<BorrowTableElement> filteredList = borrows.stream().filter(borrowTableElement ->
                    borrowTableElement.getBook().toLowerCase().contains(text_borrowBook.getText().toLowerCase())
                            && borrowTableElement.getStartOfBorrow().toString().contains(text_borrowStartDate.getText())
                            && borrowTableElement.getEndOfBorrow().toString().contains(text_borrowEndDate.getText()))
                    .collect(Collectors.toList());
            historyList.setItems(FXCollections.observableArrayList(filteredList));
            text_borrowBook.setText("");
            text_borrowStartDate.setText("");
            text_borrowEndDate.setText("");
        } else sceneController.showAlert("Błąd", "", "Niepoprawna data", Alert.AlertType.ERROR);
    }
    public void searchReservation() {
        List<ReservationTable> reservations = reservationRepository.getAllUserReservations(loggedUser.getId());
        List<ReservationTable> filteredList = reservations.stream().filter(reservationTable ->
                reservationTable.getTittle().toLowerCase().contains(text_resBook.getText().toLowerCase())
                && reservationTable.getAuthor().toLowerCase().contains(text_resAuthor.getText().toLowerCase())
                && reservationTable.getCategory().toLowerCase().contains(text_resCategory.getText().toLowerCase()))
                .collect(Collectors.toList());
        reservationList.setItems(FXCollections.observableArrayList(filteredList));
        text_resBook.setText("");
        text_resAuthor.setText("");
        text_resCategory.setText("");
    }


    public void logOut() {
        try {
            LoginController.loggedUser = null;
            SceneController.startScene("LoginScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
