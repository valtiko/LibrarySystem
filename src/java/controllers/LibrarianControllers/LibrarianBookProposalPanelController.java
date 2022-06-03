package controllers.LibrarianControllers;

import Database.Repository.BookProposalRepository;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.entities.BookProposal;
import models.tables.BookProposalTable;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LibrarianBookProposalPanelController implements Initializable {
    private final BookProposalRepository proposalRepository = BookProposalRepository.getInstance();

    @FXML
    public TableView<BookProposalTable> newBooksSuggestions;
    @FXML
    public TableColumn col_titleSug;
    @FXML
    public TableColumn col_readerSug;
    @FXML
    public TableColumn col_authorSug;
    @FXML
    public Button sendListButton;
    @FXML
    public Button rejectProposalButton;

    ObservableList<BookProposalTable> observableList = FXCollections.observableArrayList();

    private void initializeTable() {
        List<BookProposalTable> proposals = proposalRepository.getTableProposals();
        observableList.addAll(proposals);
        col_titleSug.setCellValueFactory(new PropertyValueFactory<BookProposalTable, String>("tittle"));
        col_authorSug.setCellValueFactory(new PropertyValueFactory<BookProposalTable, String>("author"));
        col_readerSug.setCellValueFactory(new PropertyValueFactory<BookProposalTable, String>("clientName"));
        newBooksSuggestions.setItems(observableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        if(newBooksSuggestions.getItems().isEmpty()) sendListButton.setDisable(true);
    }

    private void resetTable() {
        newBooksSuggestions.getItems().removeAll(newBooksSuggestions.getItems());
        initializeTable();
    }

    public void sendList() {
        List<BookProposalTable> bookProposals = newBooksSuggestions.getItems();
        List<BookProposal> proposals = proposalRepository.getProposalList(bookProposals);
        for (BookProposal o : proposals) {
            o.setStatus(BookProposal.Status.SEND.toString());
            proposalRepository.updateBookProposal(o);
        }
        sendListButton.setDisable(true);
        rejectProposalButton.setDisable(true);
        resetTable();
    }

    public void rejectBookProposal() {
        List<BookProposalTable> bookProposals = newBooksSuggestions.getSelectionModel().getSelectedItems();
        List<BookProposal> proposals = proposalRepository.getProposalList(bookProposals);
        for (BookProposal o : proposals) {
            o.setStatus(BookProposal.Status.REJECTED.toString());
            proposalRepository.updateBookProposal(o);
        }
        rejectProposalButton.setDisable(true);
        resetTable();
    }

    public void changeAvailabilityOfReturnButtons() {
        if(newBooksSuggestions.getSelectionModel().getSelectedItem() == null){
            rejectProposalButton.setDisable(true);
        }
        else {
            sendListButton.setDisable(false);
            rejectProposalButton.setDisable(false);
        }
    }

    public void backToMainScreen() {
        try {
            SceneController.startScene("LibrarianScenes/LibrarianMainPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
