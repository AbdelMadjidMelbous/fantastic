package com.example.fantastic.controller;

import com.example.fantastic.model.*;
import com.example.fantastic.model.Module;
import com.example.fantastic.repository.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Controller

public class EleveController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    EleveRepository eleveRepository;
    @Autowired
    ReponseEleveRepository reponseEleveRepository;
    @Autowired
    NiveauEleveRepository niveauEleveRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    AnneeRepository anneeRepository;
    @Autowired
    NiveauRepository niveauRepository;

    @RequestMapping(value = "/repondre/{id_eleve}/{id_question}", method = RequestMethod.GET)
    public String repondre(@PathVariable Long id_eleve,@PathVariable Long id_question, Model model){
        Question q= questionRepository.findById(id_question).get();
        model.addAttribute("question", q);
        Eleve e= eleveRepository.findById(id_eleve).get();
        model.addAttribute("eleve", e);
        model.addAttribute("newReponse", new ReponseEleve());
        return "Enfant/RepondreQuiz";
    }

    @RequestMapping(value="/envoyerReponse", method = RequestMethod.POST) // à tester
    @ResponseBody
    public String saveReponse(@RequestParam Long eleve_id, @RequestParam Long question_id, @RequestParam String reponse){
        //System.out.println(reponseEleve.getEleve().getId());
        // System.out.println(reponseEleve.getReponse());

        Question question = questionRepository.findById(question_id).get();
        String msg = "Votre réponse est incorrecte, La bonne réponse est "+question.getReponse();
        Eleve eleve = eleveRepository.findById(eleve_id).get();
        ReponseEleve reponseEleve1 = reponseEleveRepository.findByQuestionEleve(question,eleve);
        System.out.println(reponse);
        reponseEleve1.setReponse(reponse);
        if (reponseEleve1.getReponse().equals(question.getReponse()))
        {
            reponseEleve1.setNote(question.getPoids());
            msg = "Votre réponse est correcte";
        }

        reponseEleveRepository.save(reponseEleve1);

        return msg;
    }
    @RequestMapping(value = "/listeNiveaux/{id_eleve}/{id_module}/{id_annee}",method = RequestMethod.GET)
    public String listeNiveau(@PathVariable Long id_eleve,@PathVariable Long id_module,@PathVariable Long id_annee,Model model){
    model.addAttribute("niveauxPassee",niveauEleveRepository.niveauPassee(eleveRepository.findById(id_eleve).get(),moduleRepository.findById(id_module).get(),anneeRepository.findById(id_annee).get(),false));
    model.addAttribute("nieauEnCours",niveauEleveRepository.niveauEnCours(eleveRepository.findById(id_eleve).get(),moduleRepository.findById(id_module).get(),anneeRepository.findById(id_annee).get(),true));
    model.addAttribute("niveauxRestant",niveauRestant(eleveRepository.findById(id_eleve).get(),moduleRepository.findById(id_module).get(),anneeRepository.findById(id_annee).get()));
    model.addAttribute("module",moduleRepository.findById(id_module).get());
    return "Enfant/ListeNiveaux";
    }

    public List<Niveau> niveauRestant (Eleve eleve, Module module, Annee annee){
        List<Niveau> niveauPassee=niveauEleveRepository.niveauPassee(eleve,module,annee,false);
        List<Niveau> niveauRestant=niveauRepository.findByAnneeModule(annee,module);
        niveauRestant.remove(niveauEleveRepository.niveauEnCours(eleve,module,annee,true));
        for (int i=0; i< niveauPassee.size();i++){
            Niveau n=niveauPassee.get(i);
            niveauRestant.remove(n);
        }
        return niveauRestant;
    }

}
