package models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Temporal(TemporalType.DATE)
    @Column(name = "createDate")
    private Date createDate;
    @Column(name = "typeReport")
    private String typeReport;
    @Column(length = 10485760, name = "pdfContent")
    private String pdfContent;

    public Report() {
    }

    public enum ReportType {
        List,
        Invoice
    }

    public Report(Date createDate, String typeReport, String pdfContent) {
        this.createDate = createDate;
        this.typeReport = typeReport;
        this.pdfContent = pdfContent;
    }
}
