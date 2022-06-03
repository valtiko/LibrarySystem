package models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authorName")
    private String authorName;
    @Column(name = "lastName")
    private String lastName;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    private List<Book> books;


    public Author(String authorName, String lastName) {
        this.authorName = authorName;
        this.lastName = lastName;
        this.books = new ArrayList<>();
    }

    public Author() {
    }


    @Override
    public String toString() {
        return authorName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorName, author.authorName) && Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName, lastName);
    }
}
