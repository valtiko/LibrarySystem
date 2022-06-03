package models.entities;

import lombok.Data;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bookName")
    private String bookName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_Id")
    private Author author;
    @Column(name = "category")
    private String category;
    @Column(name = "status")
    private String status;
    @Column(name = "availability")
    private String availability;
    @Column(name = "ISBN")
    private String ISBN;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = {CascadeType.ALL})
    private List<Borrow> borrows;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = {CascadeType.ALL})
    private List<Reservation> reservations;


    public Book(String bookName, Author author, String category) {
        this.bookName = bookName;
        this.author = author;
        this.category = category;
        this.status = Status.NEW.toString();
        this.availability = Availability.AVAILABLE.toString();
        this.ISBN = "";
        this.borrows = new ArrayList<>();
        this.reservations = new ArrayList<>();

    }

    public Book() {
    }

    public enum Status {
        NEW,
        DESTROYED,
        REPORTED
    }

    public enum Availability {
        AVAILABLE,
        BORROWED
    }

    @Override
    public String toString() {
        return bookName;
    }
}
