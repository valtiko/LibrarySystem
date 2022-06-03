package models.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = {CascadeType.ALL})
    private List<Borrow> borrows;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<BookProposal> bookProposalList;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Reservation> reservationsList;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Return> returnList;

    public Person() {

    }

    public Person(String name, String lastName, String email, String password, Role role) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role.toString();
        this.borrows = new ArrayList<>();
    }

    public Person(String name, String lastName, String email, String role) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.borrows = new ArrayList<>();
    }

    public enum Role {
        USER,
        LIBRARIAN,
        ACCOUNTANT,
        ADMIN
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(lastName, person.lastName) && Objects.equals(email, person.email) && Objects.equals(role, person.role) && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, email, password, role);
    }
}
