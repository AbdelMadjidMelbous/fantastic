package com.example.fantastic.repository;

import com.example.fantastic.model.*;
import com.example.fantastic.model.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public interface NiveauEleveRepository extends CrudRepository<NiveauEleve,Long> {
    // donne les niveaux passées par l'èleve dans un module X
    @Query("select n.niveau from NiveauEleve n where n.eleve = :eleve AND n.niveau.module = :module AND n.niveau.annee = :annee AND n.encours = :encours")
    List<Niveau> niveauPassee(@Param("eleve")Eleve eleve, @Param("module") Module module,@Param("annee") Annee annee,@Param("encours") boolean encours);
    @Query("select n.niveau from NiveauEleve n where n.eleve = :eleve AND n.niveau.module = :module AND n.niveau.annee = :annee AND n.encours = :encours")
    Niveau niveauEnCours(@Param("eleve")Eleve eleve,@Param("module") Module module,@Param("annee") Annee annee,@Param("encours") boolean encours);
    @Query ("select ne from NiveauEleve ne where ne.eleve = :eleve AND ne.niveau = :niveau")
    NiveauEleve findByniveau(@Param("eleve")Eleve eleve,@Param("niveau") Niveau niveau);
}
