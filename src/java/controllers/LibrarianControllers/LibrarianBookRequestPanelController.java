package controllers.LibrarianControllers;

import Database.Repository.*;
import controllers.LoginController;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.entities.*;
import models.tables.ExtendTable;
import models.tables.ReservationTable;
import models.tables.ReturnTable;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class LibrarianBookRequestPanelController implements Initializable {
    private final ReservationRepository reservationRepository = ReservationRepository.getInstance();
    private final ReturnRepository returnRepository = ReturnRepository.getInstance();
    private final BorrowRepository borrowRepository = BorrowRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();
    private SceneController sceneController = new SceneController();
    Person loggedUser = LoginController.loggedUser;

    @FXML
    public TableView<ReturnTable> returnBookList;
    @FXML
    public TableColumn col_titleRet;
    @FXML
    public TableColumn col_authorRet;
    @FXML
    public TableColumn col_returnDate;
    @FXML
    public TableColumn col_readerRet;
    @FXML
    public TableView<ReservationTable> reservationList;
    @FXML
    public TableColumn col_titleRes;
    @FXML
    public TableColumn col_authorRes;
    @FXML
    public TableColumn col_reservationDate;
    @FXML
    public TableColumn col_readerRes;
    @FXML
    public TableColumn col_endOfReservationDate;
    @FXML
    public Button backButton;
    @FXML
    public Button acceptExtensionButton;
    @FXML
    public Button rejectExtensionButton;
    @FXML
    public Button informUserButton;
    @FXML
    public Button acceptReturnButton;
    @FXML
    public Button sendToDestroyedListButton;
    @FXML
    public TableView<ExtendTable> extensionsList;
    @FXML
    public TableColumn col_titleExt;
    @FXML
    public TableColumn col_authorExt;
    @FXML
    public TableColumn col_returnExt;
    @FXML
    public TableColumn col_readerExt;
    @FXML
    public TableColumn col_ISBNRet;
    @FXML
    public TableColumn col_ISBNExt;

    ObservableList<ReservationTable> reservationsList = FXCollections.observableArrayList();
    ObservableList<ReturnTable> returnsList = FXCollections.observableArrayList();
    ObservableList<ExtendTable> extendsList = FXCollections.observableArrayList();

    private void initializeReservations() {
        List<ReservationTable> reservationTables = reservationRepository.getTableReservations();
        reservationsList.addAll(reservationTables);
        col_authorRes.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("author"));
        col_titleRes.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("tittle"));
        col_readerRes.setCellValueFactory(new PropertyValueFactory<ReservationTable, String>("clientName"));
        col_reservationDate.setCellValueFactory(new PropertyValueFactory<ReservationTable, Date>("date"));
        col_endOfReservationDate.setCellValueFactory(new PropertyValueFactory<ReservationTable, Date>("endOfReservationDate"));
        reservationList.setItems(reservationsList);
    }

    private void initializeReturns() {
        List<ReturnTable> returnTables = returnRepository.getTableReturns();
        returnsList.addAll(returnTables);
        col_titleRet.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("tittle"));
        col_authorRet.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("author"));
        col_returnDate.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("endOfBorrowDate"));
        col_ISBNRet.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("ISBN"));
        col_readerRet.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("client"));
        returnBookList.setItems(returnsList);
    }

    private void initializeExtends() {
        List<ExtendTable> extendTables = borrowRepository.getAllExtendRequest();
        extendsList.addAll(extendTables);
        col_authorExt.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("author"));
        col_returnExt.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("endOfBorrowDate"));
        col_titleExt.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("tittle"));
        col_ISBNExt.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("ISBN"));
        col_readerExt.setCellValueFactory(new PropertyValueFactory<ReturnTable, String>("client"));
        extensionsList.setItems(extendsList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeReservations();
        initializeReturns();
        initializeExtends();
    }

    public void backToMainScreen() {
        try {
            SceneController.startScene("LibrarianScenes/LibrarianMainPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetExtendsTable() {
        extensionsList.getItems().removeAll(extensionsList.getItems());
        initializeExtends();
    }

    private void resetReturnTable() {
        returnBookList.getItems().removeAll(returnBookList.getItems());
        initializeReturns();
    }

    private void resetReservationTable() {
        reservationList.getItems().removeAll(reservationList.getItems());
        initializeReservations();
    }

    public void extendBookBorrow() {
        if (extensionsList.getSelectionModel().isEmpty()) return;
        ExtendTable extendTable = extensionsList.getSelectionModel().getSelectedItem();
        borrowRepository.updateExtensionStatus(borrowRepository.getBorrowByBook(
                bookRepository.findByISBN(extendTable.getISBN())), Borrow.WasExtended.EXTENDED.toString());
        acceptExtensionButton.setDisable(true);
        resetExtendsTable();
    }

    public void rejectBorrowExtension() {
        if (extensionsList.getSelectionModel().isEmpty()) return;
        ExtendTable extendTable = extensionsList.getSelectionModel().getSelectedItem();
        borrowRepository.updateExtensionStatus(borrowRepository.getBorrowByBook(
                bookRepository.findByISBN(extendTable.getISBN())), Borrow.WasExtended.REJECTED.toString());
        rejectExtensionButton.setDisable(true);
        resetExtendsTable();
    }

    public void returnBook() {
        ReturnTable returnTable = returnBookList.getSelectionModel().getSelectedItem();
        Book book = bookRepository.findBookByISBN(returnTable.getISBN());
        Return returnBook = returnRepository.getReturn(borrowRepository.getBorrowByBook(book));
        book.setAvailability(Book.Availability.AVAILABLE.toString());
        returnBook.setPerson(loggedUser);
        returnBook.setReturnDate(new Date());
        returnRepository.updateReturn(returnBook);
        bookRepository.updateBook(book);
        sceneController.showAlert("Suckes","","Pomyślnie zwrócono ksiązkę", Alert.AlertType.CONFIRMATION);
        acceptReturnButton.setDisable(true);
        sendToDestroyedListButton.setDisable(true);
        resetReturnTable();
    }

    public void informUser() {
        ReservationTable reservationTable = reservationList.getSelectionModel().getSelectedItem();
        Reservation reservation = reservationRepository.getReservation(
                reservationTable.getTittle(),
                reservationTable.getAuthor(),
                reservationTable.getClientName(),
                reservationTable.getDate()
        );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        reservation.setEndOfReservationDate(calendar.getTime());
        reservationRepository.updateReservation(reservation);
        resetReservationTable();
        informUserButton.setDisable(true);
    }

    public void changeAvailabilityOfExtensionButtons() {
        ExtendTable extendTable = extensionsList.getSelectionModel().getSelectedItem();
        if (extendTable == null) {
            acceptExtensionButton.setDisable(true);
            rejectExtensionButton.setDisable(true);
            return;
        }
        if (bookRepository.isBookAvailable(extendTable.getTittle())) {
            acceptExtensionButton.setDisable(false);
            rejectExtensionButton.setDisable(true);
        } else {
            acceptExtensionButton.setDisable(true);
            rejectExtensionButton.setDisable(false);
        }
    }

    public void changeAvailabilityOfReservationButton() {
        ReservationTable reservationTable = reservationList.getSelectionModel().getSelectedItem();
        if (reservationTable == null || reservationTable.getEndOfReservationDate() != null) {
            informUserButton.setDisable(true);
            return;
        }
        informUserButton.setDisable(!bookRepository.isBookAvailable(reservationTable.getTittle()));
    }

    public void changeAvailabilityOfReturnButtons() {
        if (returnBookList.getSelectionModel().getSelectedItem() == null) {
            acceptReturnButton.setDisable(true);
            sendToDestroyedListButton.setDisable(true);
        }
            else {
            acceptReturnButton.setDisable(false);
            sendToDestroyedListButton.setDisable(false);
        }
    }

    public void changeBookStatus() {
        ReturnTable returnTable = returnBookList.getSelectionModel().getSelectedItem();
        Book book = bookRepository.findBookByISBN(returnTable.getISBN());
        Borrow borrow = borrowRepository.getBorrowByBook(book);
        book.setStatus(Book.Status.DESTROYED.toString());
        bookRepository.updateBook(book);
        borrowRepository.deleteBorrow(borrow);
        acceptReturnButton.setDisable(true);
        sendToDestroyedListButton.setDisable(true);
        resetReturnTable();
    }
}
