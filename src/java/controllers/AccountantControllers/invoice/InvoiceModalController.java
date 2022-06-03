package controllers.AccountantControllers.invoice;


import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import controllers.SceneController;
import models.entities.Invoice;
import models.tables.InvoiceTableDto;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class InvoiceModalController implements Initializable {

    @FXML
    private ComboBox<Invoice.Status> priorityComboBox;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public Button pobierzButton;
    private InvoiceTableDto wybranaPozycja;

    private final SceneController sceneController = new SceneController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priorityComboBox.getItems().addAll(Invoice.Status.values());
        progressBar.setVisible(false);
    }

    public void pobierzFakture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.pdf"));
        pobierzButton.setDisable(true);
        progressBar.setDisable(false);
        progressBar.setProgress(0);
        progressBar.setVisible(true);
        Task<Void> copyWorker = new DownloadTask(
                fileChooser.showSaveDialog(null),
                wybranaPozycja.getData(),
                (e) -> {
                    pobierzButton.setDisable(false);
                    progressBar.progressProperty().unbind();
                });
        progressBar.progressProperty().bind(copyWorker.progressProperty());

        Thread thread = new Thread(copyWorker);
        thread.setDaemon(true);
        thread.start();
    }

    public void zapiszZmiany(ActionEvent actionEvent) {
        if(priorityComboBox.getValue() == null){
            sceneController.showAlert("Nie można zapisać.","Błąd","Nie uzupełniono statusu.",
                    Alert.AlertType.ERROR);
            return;
        }
        wybranaPozycja.setStatus(priorityComboBox.getValue());
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setWybranaPozycjaFaktury(InvoiceTableDto selectedItem) {
        this.wybranaPozycja = selectedItem;
    }

    private static class DownloadTask extends Task<Void>{
        private final File file;
        private final byte[] data;

        private final Consumer<Void> sideEffect;

        public DownloadTask(File file, byte[] data, Consumer<Void> sideEffect){
            this.file = file;
            this.data = data;
            this.sideEffect = sideEffect;
        }

        @Override
        protected Void call() throws Exception {
            try(InputStream is = new ByteArrayInputStream(data);
                OutputStream os = Files.newOutputStream(file.toPath())){
                long nread = 0L;
                byte[] buf = new byte[1024];
                int n;
                while((n = is.read(buf)) > 0){
                    os.write(buf, 0, n);
                    nread += n;
                    updateProgress(nread, data.length);
                }
            }
            return null;
        }

        @Override
        protected void succeeded() {
            sideEffect.accept(null);
        }

        @Override
        protected void failed() {
            sideEffect.accept(null);
        }

        @Override
        protected void cancelled() {
            sideEffect.accept(null);
        }
    }
}


