package com.example.ex1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "compte_type",
        discriminatorType = DiscriminatorType.STRING, length = 2)
@Table(name = "Comptes")
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long codeCompte;

    @NonNull
    @Getter @Setter
    private double solde;

    @NonNull
    @Getter @Setter
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="YYYY-MM-dd")
    private Date datecreation;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "code_cli")
    @Getter @Setter
    private Client client;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "code_emp")
    @Getter @Setter
    private Employee employee;

    @OneToMany(mappedBy = "compte", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Operations> operations;


}
