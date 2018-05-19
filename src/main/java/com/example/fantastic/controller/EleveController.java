package com.example.fantastic.controller;

import com.example.fantastic.model.*;
import com.example.fantastic.model.Module;
import com.example.fantastic.repository.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="/envoyerReponse", method = RequestMethod.POST)
    public String saveReponse(@ModelAttribute(value = "newReponse") ReponseEleve reponseEleve){
//        System.out.println(reponseEleve.getEleve().getId());
        reponseEleveRepository.save(reponseEleve);
        return "redirect:/cours";
    }
    @RequestMapping(value = "/listeNiveaux/{id_eleve}/{id_module}/{id_annee}",method = RequestMethod.GET)
    public String listeNiveau(@PathVariable Long id_eleve,@PathVariable Long id_module,@PathVariable Long id_annee,Model model){
    model.addAttribute("niveauxPassee",niveauEleveRepository.niveauPassee(eleveRepository.findById(id_eleve).get(),moduleRepository.findById(id_module).get(),anneeRepository.findById(id_annee).get(),false));
    model.addAttribute("nieauEnCours",niveauEleveRepository.niveauEnCours(eleveRepository.findById(id_eleve).get(),moduleRepository.findById(id_module).get(),anneeRepository.findById(id_annee).get(),true));
    model.addAttribute("niveauxRestant",niveauRestant(eleveRepository.findById(id_eleve).get(),moduleRepository.findById(id_module).get(),anneeRepository.findById(id_annee).get()));
    model.addAttribute("module",moduleRepository.findById(id_module).get());
    return "Enfant/ListeNiveaux";
    }

    @RequestMapping(value = "/test2",method = RequestMethod.GET)
    public String test(){
        List<Niveau> n=niveauRestant(eleveRepository.findById(1L).get(),moduleRepository.findById(1L).get(),anneeRepository.findById(1L).get());
        System.out.println(n.size());
        return "redirect:/cours";
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
