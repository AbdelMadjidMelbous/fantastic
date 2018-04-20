package com.example.fantastic.controller;

import com.example.fantastic.model.*;
import com.example.fantastic.repository.ModuleRepository;
import com.example.fantastic.repository.NiveauRepository;
import com.example.fantastic.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
@Controller
public class QuizzController {
    @Autowired
    NiveauRepository niveauRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    QuestionRepository questionRepository;

    @RequestMapping(value = "/quizz", method = RequestMethod.GET)
    public String quizz(Model model){
        model.addAttribute("modules", moduleRepository.findAll());
        model.addAttribute("niveaux", niveauRepository.findAll());
        model.addAttribute("newQuestion", new Question());
        return "Enseignant/AjouterQuizz";
    }

    @RequestMapping(value="/createniveau", method = RequestMethod.POST)
    @ResponseBody
    public String saveNiveau(@RequestParam String niveau_libelle, @RequestParam int niveau_poids, @RequestParam Long module_id){
        Niveau niveau = new Niveau();
        niveau.setLibelle(niveau_libelle);
        niveau.setPoids(niveau_poids);
        niveau.setModule(moduleRepository.findById(module_id).get());
        niveauRepository.save(niveau);
        return niveau.getId().toString();
    }
    @RequestMapping(value="/createquestion", method = RequestMethod.POST)
    public String saveQuestion(@ModelAttribute(value = "newQuestion") Question question){
        questionRepository.save(question);
        return "redirect:/quizz";
    }
}
