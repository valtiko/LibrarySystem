package models.tables;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowTableElement {
    String book;
    String wasExtended;
    String bookISBN;
    Date startOfBorrow,endOfBorrow;
}
