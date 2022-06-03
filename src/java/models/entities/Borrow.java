package models.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;
    @Temporal(TemporalType.DATE)
    @Column(name = "borrowDate")
    private Date borrowDate;
    @Column(name = "wasExtended")
    private String wasExtended;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;
    @Temporal(TemporalType.DATE)
    @Column(name = "endOfBorrowDate")
    private Date endOfBorrowDate;
    @OneToOne(mappedBy = "borrow", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Return aReturn;

    public Borrow(){

    }

    public enum WasExtended {
        EXTENDED,
        NOT_EXTENDED,
        REJECTED,
        REQUESTED
    }

    public Borrow(Person person, Book book) {
        this.person = person;
        this.borrowDate = new Date();
        this.wasExtended = WasExtended.NOT_EXTENDED.toString();
        this.book = book;
        this.endOfBorrowDate = createEndOfBorrowDate();
        this.aReturn = null;
    }
    private Date createEndOfBorrowDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,7);
        return calendar.getTime();
    }
}
