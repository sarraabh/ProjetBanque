package com.example.ex1.dao;

import com.example.ex1.entities.Operations;
import com.example.ex1.entities.Retrait;
import com.example.ex1.entities.Versement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface OperationRepository extends JpaRepository<Operations, Long> {
    @Query("select o from Operations o where o.compte.codeCompte = :id")
    @RestResource(path = "compteop")
    List<Operations> findAllByCompte_CodeCompte(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from operations o where o.num_compte = :id and o.dtype = 'R'")
    @RestResource(path = "compteopr")
    List<Retrait> findAllByTypeR(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from operations o where o.num_compte = :id and o.dtype = 'V'")
    @RestResource(path = "compteopv")
    List<Versement> findAllByTypeV(@Param("id") Long id);
}
