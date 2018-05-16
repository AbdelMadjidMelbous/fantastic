package com.example.fantastic.controller;

import com.example.fantastic.model.Eleve;
import com.example.fantastic.model.Question;
import com.example.fantastic.model.ReponseEleve;
import com.example.fantastic.repository.EleveRepository;
import com.example.fantastic.repository.QuestionRepository;
import com.example.fantastic.repository.ReponseEleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class EleveController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    EleveRepository eleveRepository;
    @Autowired
    ReponseEleveRepository reponseEleveRepository;

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
}
