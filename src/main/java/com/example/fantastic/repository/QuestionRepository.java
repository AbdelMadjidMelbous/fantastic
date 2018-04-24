package com.example.fantastic.repository;

import com.example.fantastic.model.Niveau;
import com.example.fantastic.model.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Query("select q from Question q where q.niveau = :niveau")
    List<Question> findByNiveau(@Param("niveau") Niveau niveau);
    @Query("select q from Question q where q.num_question = :num_question and q.niveau = :niveau")
    List<Question> findByNum_question(@Param("num_question") int num_question,@Param("niveau") Niveau niveau);
}
