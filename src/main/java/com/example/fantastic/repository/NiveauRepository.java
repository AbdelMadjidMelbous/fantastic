package com.example.fantastic.repository;

import com.example.fantastic.model.Annee;
import com.example.fantastic.model.Module;
import com.example.fantastic.model.Niveau;
import com.example.fantastic.model.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
public interface NiveauRepository extends CrudRepository<Niveau, Long> {
    @Query("select n from Niveau n where n.annee = :annee")
    List<Niveau> findByAnnee(@Param("annee") Annee annee);
    @Query("select n1 from Niveau n1 where n1.annee = :annee and n1.module = :module")
    List<Niveau> findByAnneeModule(@Param("annee") Annee annee,@Param("module") Module module);
    @Query ("select n1 from Niveau n1 where n1.annee = :annee and n1.module = :module and n1.difficulte = :difficulte")
    List<Niveau> findByDifficulte (@Param("annee") Annee annee,@Param("module") Module module,@Param("difficulte") int difficulte);
}
