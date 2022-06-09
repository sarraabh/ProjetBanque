package com.example.ex1.dao;

import com.example.ex1.entities.CompteCC;
import com.example.ex1.entities.Employee;
import com.example.ex1.entities.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    @Query(value = "select g.employes from Groupe g where g.codeGroupe = :id")
    @RestResource(path = "employeegroup")
    List<Employee> findByGroupe(@Param("id") Long id);
}
