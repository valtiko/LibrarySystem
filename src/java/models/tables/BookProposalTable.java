package models.tables;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookProposalTable {
    String title;
    String author;
    String price;
    String quantity;
    String clientName;


}
