package com.example.ex1.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("V")
public class Versement extends Operations {
}
