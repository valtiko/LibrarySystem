package controllers.AccountantControllers.invoice;

import models.entities.Invoice;

import java.util.List;

public interface InvoiceRepository {

    Invoice getInvoiceById(Long id);

    Invoice getInvoiceByNumber(String number);

    Invoice saveInvoice(Invoice b);

    void deleteInvoice(Invoice b);

    List<Invoice> getInvoiceList();
}
