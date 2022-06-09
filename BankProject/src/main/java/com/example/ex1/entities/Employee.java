package com.example.ex1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@RequiredArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long code_emp;

    @NonNull
    @Getter @Setter
    private double solde;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Compte> comptes;



    @ManyToOne
    @JoinColumn(name = "code_sup")
    @Getter @Setter
    private Employee employee_sup;

    @ManyToMany
    @JoinTable(name = "Emp_Gr", joinColumns = @JoinColumn(name = "Num_EMP"), inverseJoinColumns = @JoinColumn(name = "Num_Groupe"))
    @JsonIgnore
    @Getter @Setter
    private List<Groupe> groupes;

}
