package com.example.fantastic.controller;

import com.example.fantastic.model.*;
import com.example.fantastic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller

public class EnseignantController {
    @Autowired
    ClasseEtudiantRepository classeEtudiantRepository;
    @Autowired
    AnneeRepository anneeRepository;
    @Autowired
    ReponseEleveRepository reponseEleveRepository;
    @Autowired
    EleveRepository eleveRepository;
    @Autowired
    NiveauRepository niveauRepository;
    @Autowired
    QuestionRepository questionRepository;


    //Récupérer la liste des étudiants d'une classe
    @RequestMapping(value = "/classe/{id_annee}/{num_class}", method = RequestMethod.GET)
    public String getClass(@PathVariable Long id_annee, @PathVariable int num_class, Model model){
        Annee annee=anneeRepository.findById(id_annee).get();
        ClasseEtudiant classeEtudiant=classeEtudiantRepository.findClass(annee,num_class);
        List<Eleve> eleves= classeEtudiantRepository.findEleveByClass(annee,num_class);
        model.addAttribute("eleves",eleves);
        model.addAttribute("class",classeEtudiant);
        return "Enseignant/Suivi_Eleve_Classe";
    }

    // Récupérer la liste des réponses d'un élève pour un niveau X
    @RequestMapping(value = "/reponseByNiveau/{id_eleve}/{id_niveau}", method = RequestMethod.GET)
    public String getreponseByNiveau(@PathVariable Long id_eleve, @PathVariable Long id_niveau, Model model){
        Niveau niveau=niveauRepository.findById(id_niveau).get();
        Eleve eleve=eleveRepository.findById(id_eleve).get();
       List<ReponseEleve> reponseEleves=reponseEleveRepository.ReponseByNiveau(eleve,niveau);
        model.addAttribute("reponses",reponseEleves);
        model.addAttribute("niveau",niveau);
        model.addAttribute("eleve", eleve);
        return "Enseignant/Suivi_Eleve_Niveau";
    }

    //Récupérer la liste des réponses des élèves pour une question X
    @RequestMapping(value = "/allReponseByQuestion/{id_question}", method = RequestMethod.GET)
    public String getAllReponseByQuestion(@PathVariable Long id_question, Model model){
        Question question=questionRepository.findById(id_question).get();
        List<ReponseEleve> reponseEleves=reponseEleveRepository.AllReponseByQuestion(question);
        model.addAttribute("reponses",reponseEleves);
        model.addAttribute("question", question);
        return "Enseignant/Suivi_Eleve_quest";
    }


}
