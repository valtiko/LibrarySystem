package models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;
    @Temporal(TemporalType.DATE)
    @Column
    private Date reservationDate;
    @Temporal(TemporalType.DATE)
    @Column
    private Date endOfReservationDate;
    
    public Reservation() {
    }

    public Reservation(Book book, Person person) {
        this.book = book;
        this.person = person;
        this.reservationDate = new Date();
    }

}
