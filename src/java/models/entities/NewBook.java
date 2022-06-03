package models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class NewBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private int quantity;

}
