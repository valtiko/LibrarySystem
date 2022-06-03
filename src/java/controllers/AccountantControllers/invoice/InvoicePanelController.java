package controllers.AccountantControllers.invoice;

import controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.entities.Invoice;
import models.tables.InvoiceTableDto;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoicePanelController implements Initializable {

    private final InvoiceService service = InvoiceService.getInstance();
    @FXML
    public TableView<InvoiceTableDto> newInvoiceList;

    @FXML
    public TableColumn<InvoiceTableDto,String> col_addtionalInfo;

    @FXML
    public TableColumn<InvoiceTableDto,String> col_invoiceNumber;

    @FXML
    public TableColumn<InvoiceTableDto,String> col_subjectName;

    @FXML
    public TableColumn<InvoiceTableDto,String> col_status;

    @FXML
    public TextField numer_faktury;

    @FXML
    public TextField nazwa_podmiotu;

    @FXML
    public TextField informacje_dodatkowe_faktury;

    private byte[] data;

    private final SceneController sceneController = new SceneController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = new byte[0];
        initializeTable();
    }

    private void initializeTable() {
        newInvoiceList.getItems().clear();
        newInvoiceList.getItems().addAll(service.getInvoices().stream()
                .map(el -> new InvoiceTableDto(
                        el.getId(),
                        el.getNumber(),
                        el.getSubjectName(),
                        el.getStatus(),
                        el.getAdditionalInfo(),
                        el.getData()))
                .collect(Collectors.toList()));
        col_invoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        col_subjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_addtionalInfo.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
        newInvoiceList.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    try {
                        ustawWyborFaktury(mouseEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void backToMainScreen() throws IOException {
        SceneController.startScene("AccountantScenes/AccountantMainPanel");
    }

    public void acceptAction() throws IOException {
        service.confirList(newInvoiceList);
        sceneController.showAlert("Zapisano faktury.","Informacja","Pozycje zostały zapisane w bazie danych.",
                Alert.AlertType.INFORMATION);
    }

    public void dodajFaktureDoListy() {
        if(!numer_faktury.getText().isBlank() &&
                !nazwa_podmiotu.getText().isBlank() &&
                !informacje_dodatkowe_faktury.getText().isBlank() &&
                data.length > 0){
            newInvoiceList.getItems()
                    .add(
                            new InvoiceTableDto(
                                    null,
                                    numer_faktury.getText(),
                                    nazwa_podmiotu.getText(),
                                    Invoice.Status.ISSUED,
                                    informacje_dodatkowe_faktury.getText(),
                                    data));
            newInvoiceList.refresh();
            Stream.of(numer_faktury, nazwa_podmiotu, informacje_dodatkowe_faktury).forEach(TextInputControl::clear);
            data = new byte[0];
        }

    }

    public void usunFakture(ActionEvent actionEvent) {

    }

    public void ustawWyborFaktury(MouseEvent mouseEvent) throws IOException {
        Stage dialog = new Stage();
        FXMLLoader parent = new FXMLLoader(InvoicePanelController.class.getResource("/scenes/AccountantScenes/AccountantInvoiceEditModal.fxml"));
        InvoiceModalController modalController = new InvoiceModalController();
        modalController.setWybranaPozycjaFaktury(newInvoiceList.getSelectionModel().getSelectedItem());
        parent.setController(modalController);
        dialog.setScene(new Scene(parent.load()));
        dialog.initOwner(((Node)mouseEvent.getSource()).getScene().getWindow() );
        dialog.initModality(Modality.APPLICATION_MODAL);

        dialog.show();
        dialog.addEventFilter(WindowEvent.WINDOW_HIDING, (e) -> newInvoiceList.refresh());
        dialog.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> newInvoiceList.refresh());
    }

    public void zaladujFakture(ActionEvent actionEvent) throws IOException {
        data = new byte[0];
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("PDF files (*.PDF)", "*.pdf");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(((Node)actionEvent.getSource()).getScene().getWindow());
        data = Files.readAllBytes(Path.of(file.getPath()));
    }
}



