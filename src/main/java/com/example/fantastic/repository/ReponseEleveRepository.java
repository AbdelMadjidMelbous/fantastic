package com.example.fantastic.repository;

import com.example.fantastic.model.Eleve;
import com.example.fantastic.model.Niveau;
import com.example.fantastic.model.Question;
import com.example.fantastic.model.ReponseEleve;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReponseEleveRepository extends CrudRepository<ReponseEleve,Long> {
    // La liste des question que l'éleve X à répondu dans un niveau Y
    @Query("select r.question from ReponseEleve r where r.eleve = :eleve AND r.question.niveau = :niveau AND r.encours = :encours")
    List<Question> questionPassee(@Param("eleve")Eleve eleve, @Param("niveau") Niveau niveau, @Param("encours") boolean encours);
    @Query("select r.question from ReponseEleve r where r.eleve = :eleve AND r.question.niveau = :niveau AND r.encours = :encours")
    Question questionEnCours(@Param("eleve")Eleve eleve, @Param("niveau") Niveau niveau, @Param("encours") boolean encours);
}
