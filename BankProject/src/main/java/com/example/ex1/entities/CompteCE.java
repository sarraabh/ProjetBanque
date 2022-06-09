package com.example.ex1.entities;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.Date;


@Entity

@DiscriminatorValue(value = "CE")
public class CompteCE extends Compte {
    @Getter @Setter
    private double taux;

    public CompteCE(Long codeCompte, @NonNull double solde, @NonNull Date datecreation, @NonNull Client client, @NonNull Employee employee, Collection<Operations> operations, @NonNull double taux) {
        super(codeCompte, solde, datecreation, client, employee, operations);
        this.taux = taux;
    }

    public CompteCE(@NonNull double solde, @NonNull Date datecreation, @NonNull Client client, @NonNull Employee employee, @NonNull double taux) {
        super(solde, datecreation, client, employee);
        this.taux = taux;
    }

    public CompteCE() {

    }
}
