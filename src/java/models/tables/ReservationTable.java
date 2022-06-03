package models.tables;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationTable {
    private String tittle,author,ISBN,category;
    private Date date,endOfReservationDate;
    private String clientName;
}
