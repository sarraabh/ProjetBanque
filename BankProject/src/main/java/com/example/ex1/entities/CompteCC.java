package com.example.ex1.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Date;


@Entity

@DiscriminatorValue(value = "CC")
public class CompteCC extends Compte {
    @Getter @Setter
    private double decouvert;

    public CompteCC(Long codeCompte, @NonNull double solde, @NonNull Date datecreation, @NonNull Client client, @NonNull Employee employee, Collection<Operations> operations, @NonNull double decouvert) {
        super(codeCompte, solde, datecreation, client, employee, operations);
        this.decouvert = decouvert;
    }

    public CompteCC(@NonNull double solde, @NonNull Date datecreation, @NonNull Client client, @NonNull Employee employee, @NonNull double decouvert) {
        super(solde, datecreation, client, employee);
        this.decouvert = decouvert;
    }

    public CompteCC() {

    }
}
