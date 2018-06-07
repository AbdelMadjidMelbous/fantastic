package com.example.fantastic.repository;

import com.example.fantastic.model.Niveau;
import com.example.fantastic.model.SupportDeCours;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportDeCoursRepository extends CrudRepository<SupportDeCours, Long> {
    @Query("select cours from SupportDeCours cours where cours.niveau = :niveau")
    List<SupportDeCours> coursByNiveau (@Param("niveau")Niveau niveau);
}
