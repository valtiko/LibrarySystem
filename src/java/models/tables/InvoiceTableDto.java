package models.tables;


import lombok.AllArgsConstructor;
import lombok.Data;
import models.entities.Invoice;

@AllArgsConstructor
@Data
public class InvoiceTableDto {
    Long id;
    String invoiceNumber;
    String subjectName;
    Invoice.Status status;
    String additionalInfo;
    byte[] data;
}
