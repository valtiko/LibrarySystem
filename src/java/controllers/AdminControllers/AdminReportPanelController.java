package controllers.AdminControllers;

import Database.Repository.ReportRepository;
import controllers.PDFController;
import controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.entities.Report;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminReportPanelController implements Initializable {
    private final ReportRepository reportRepository = ReportRepository.getInstance();
    private List<Report> reportList;
    ObservableList<Report> reportObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<Report> reportTableView;

    @FXML
    TableColumn<String, String> reportDateColumn;
    @FXML
    TableColumn<Report.ReportType, String> typeOfReportColumn;


    @FXML

    public void goBackToUserPanel() {
        try {
            SceneController.startScene("AdminScenes/AdminUserPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBookPanel() {
        try {
            SceneController.startScene("AdminScenes/AdminBookPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeTable() {
        reportList = reportRepository.getAllReports();
        reportObservableList.addAll(reportList);
        reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        typeOfReportColumn.setCellValueFactory(new PropertyValueFactory<>("typeReport"));
        reportTableView.setItems(reportObservableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
    }

    @FXML
    public void generateReport() {
        Report report = reportTableView.getSelectionModel().getSelectedItem();
        PDFController.createPDF(report.getPdfContent(), report.getCreateDate());
    }

    @FXML
    public void deleteReport() {
        Report report = reportTableView.getSelectionModel().getSelectedItem();
        reportRepository.deleteReport(report);
        reportTableView.refresh();
        reportTableView.getItems().removeAll(reportTableView.getItems());
        reportList = reportRepository.getAllReports();
        reportTableView.getItems().addAll(reportList);
        reportTableView.refresh();
    }
}
