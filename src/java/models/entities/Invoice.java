package models.entities;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private String subjectName;

    private String additionalInfo;

    private Invoice.Status status;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data",
    columnDefinition = "oid")
    private byte[] data;

    public enum Status {
        ISSUED("Wystawiona"),
        PAID("Zapłacona");

        @Getter
        private final String val;

        Status(String s){
            this.val = s;
        }

        @Override
        public String toString(){
            return val;
        }
    }
}
