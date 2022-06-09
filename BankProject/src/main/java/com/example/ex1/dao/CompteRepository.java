package com.example.ex1.dao;

import com.example.ex1.entities.Compte;
import com.example.ex1.entities.CompteCC;
import com.example.ex1.entities.CompteCE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RepositoryRestResource
public interface CompteRepository extends JpaRepository<Compte, Long> {


    @Query(value="select * from comptes c where compte_type = 'CC' and code_cli = :id", nativeQuery = true)
    @RestResource(path = "clientCompteCC")
    List<CompteCC> findByTypeCC(@Param("id") Long id);

    @Query(nativeQuery = true, value="select * from comptes c where compte_type = 'CE' and code_cli = :id")
    @RestResource(path = "clientCompteCE")
    List<CompteCE> findByTypeCE(@Param("id") Long id);
}
