package models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BookProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;
    @Transient
    private String clientName;
    @Column
    private String status;

    @Column
    private Double price;
    @Column
    private Integer quantity;

    public BookProposal() {
    }

    public enum Status{
        SEND,
        ORDERED,
        REJECTED,
        PROPOSAL
    }

    public BookProposal(String title, String author, Person person) {
        this.title = title;
        this.author = author;
        this.person = person;
        this.setQuantity(0);
        this.setPrice(0.0);
        this.clientName = person.getName()+" "+person.getLastName();
        this.status = Status.PROPOSAL.toString();
    }


}
