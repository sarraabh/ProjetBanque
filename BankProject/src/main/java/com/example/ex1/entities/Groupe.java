package com.example.ex1.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@RequiredArgsConstructor
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long codeGroupe;

    @NonNull
    @Getter @Setter
    private String nomGroupe;

    @ManyToMany(mappedBy = "groupes", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Employee> employes;
}
