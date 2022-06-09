package com.example.ex1.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 1)

public class Operations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long numeroOperation;

    @NonNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="YYYY-MM-dd")
    @Getter @Setter
    private Date dateOperation;

    @NonNull
    @Getter @Setter
    private double montant;

    @ManyToOne
    @JoinColumn(name = "numCompte")
    @Getter @Setter
    private Compte compte;

    @ManyToOne
    @JoinColumn(name = "Code_emp")
    @Getter @Setter
    private Employee employee;
}
