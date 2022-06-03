package controllers.AccountantControllers.invoice;

import Database.DatabaseAccess;
import javafx.scene.control.TableView;
import models.entities.Invoice;
import models.tables.InvoiceTableDto;

import java.util.List;

public class InvoiceService {

    private static InvoiceService instance;

    private final InvoiceRepository repository;

    private InvoiceService() {
        repository = new InvoiceRepositoryImpl(DatabaseAccess.getEntityManager());
    }

    public static InvoiceService getInstance(){
        if( instance == null ){
            instance = new InvoiceService();
        }
        return instance;
    }

    public void confirList(TableView<InvoiceTableDto> invoiceTableDtoTableView) {
        invoiceTableDtoTableView.getItems().stream()
                .map(it -> {
                    Invoice invoice = new Invoice();
                    invoice.setNumber(it.getInvoiceNumber());
                    invoice.setStatus(it.getStatus());
                    invoice.setSubjectName(it.getSubjectName());
                    invoice.setData(it.getData());
                    invoice.setAdditionalInfo(it.getAdditionalInfo());
                    invoice.setId(it.getId());
                    return invoice;
                })
                .forEach(repository::saveInvoice);
    }

    public List<Invoice> getInvoices() {
        return repository.getInvoiceList();
    }
}
