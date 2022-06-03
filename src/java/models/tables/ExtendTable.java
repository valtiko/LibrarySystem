package models.tables;

import lombok.Data;

import java.util.Date;

@Data
public class ExtendTable {
    private String tittle;
    private String author;
    private Date endOfBorrowDate;
    private String client;
    private String ISBN;
}
