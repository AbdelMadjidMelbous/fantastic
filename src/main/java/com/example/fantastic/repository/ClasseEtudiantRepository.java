package com.example.fantastic.repository;

import com.example.fantastic.model.Annee;
import com.example.fantastic.model.ClasseEtudiant;
import com.example.fantastic.model.Eleve;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClasseEtudiantRepository  extends CrudRepository<ClasseEtudiant,Long>{
    @Query("select c.eleves from ClasseEtudiant c where c.annee = :annee AND c.numero = :numero")
    List<Eleve> findEleveByClass(@Param("annee") Annee annee, @Param("numero") int numero);
    @Query("select c from ClasseEtudiant c where c.annee = :annee AND c.numero = :numero")
    ClasseEtudiant findClass(@Param("annee") Annee annee, @Param("numero") int numero);

}
