package models.tables;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookTable {
    private String bookName,author,category,ISBN,availability;

    public BookTable(String bookName, String author, String category) {
        this.bookName = bookName;
        this.author = author;
        this.category = category;
    }
}
