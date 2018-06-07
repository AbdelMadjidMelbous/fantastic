package com.example.fantastic.controller;

import com.example.fantastic.model.*;
import com.example.fantastic.model.Module;
import com.example.fantastic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
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
    @Autowired
    EleveRepository eleveRepository;
    @Autowired
    AnneeRepository anneeRepository;
    @Autowired
    ReponseEleveRepository reponseEleveRepository;
    @Autowired
    NiveauEleveRepository niveauEleveRepository;

    @RequestMapping(value = "/quizz", method = RequestMethod.GET)
    public String quizz(Model model){
        model.addAttribute("annees", anneeRepository.findAll());
        model.addAttribute("modules", moduleRepository.findAll());
        //model.addAttribute("question", questionRepository.findById(21L).get());
        //model.addAttribute("newQuestion", new Question());
        return "Enseignant/ChoixAnneeModule";
    }

    @RequestMapping(value = "/Afficherquizz/{module_id}", method = RequestMethod.GET)
    public String AfficherQuizz(@PathVariable Long module_id,Model model){
        //model.addAttribute("annees", anneeRepository.findAll());
        Module module = moduleRepository.findById(module_id).get();
        model.addAttribute("questions", questionRepository.findByModule(module));
        model.addAttribute("upQuestion", new Question());
        return "Enseignant/Afficher_Quiz_Lecture";
    }

    @RequestMapping(value = "/creerquizz/{annee_id}/{module_id}", method = RequestMethod.GET)
    public String CreerQuizz(@PathVariable Long annee_id,@PathVariable Long module_id,Model model){
        model.addAttribute("annees", anneeRepository.findAll());
        model.addAttribute("annee", anneeRepository.findById(annee_id).get());
        model.addAttribute("module", moduleRepository.findById(module_id).get());
        model.addAttribute("niveaux", niveauRepository.findByAnneeModule(anneeRepository.findById(annee_id).get(),moduleRepository.findById(module_id).get()));
        model.addAttribute("newQuestion", new Question());
        return "Enseignant/AjouterQuizz";
    }

// To Test !
    @RequestMapping(value = "/quizz/{id}", method = RequestMethod.GET)
    public String questionsByNiveau(@PathVariable Long id,Model model){
        List<Question> ListeQuestion=questionRepository.findByNiveau(niveauRepository.findById(id).get());
        model.addAttribute("questions",ListeQuestion );
        return "Enseignant/ListeDesQuestions";
    }

    @RequestMapping(value="/createniveau", method = RequestMethod.POST)
    @ResponseBody
    public String saveNiveau(@RequestParam String niveau_libelle, @RequestParam int niveau_poids, @RequestParam Long annee_id,@RequestParam Long module_id){
        Niveau niveau = new Niveau();
        niveau.setLibelle(niveau_libelle);
        niveau.setPoids(niveau_poids);
        niveau.setAnnee(anneeRepository.findById(annee_id).get());
        niveau.setModule(moduleRepository.findById(module_id).get());
        niveauRepository.save(niveau);
        return niveau.getId().toString();
    }
    @RequestMapping(value="/createquestion", method = RequestMethod.POST)
    public String createQuestion(@ModelAttribute(value = "newQuestion") Question question){
        questionRepository.save(question);
        return "redirect:/quizz";
    }

    @RequestMapping(value = "/niveaux/{module_id}/{annee_id}", method = RequestMethod.GET)
    public String NiveauxByModule(@PathVariable Long module_id,@PathVariable Long annee_id,Model model){
        List<Niveau>  ListeNiveau= niveauRepository.findByAnneeModule(anneeRepository.findById(annee_id).get(),moduleRepository.findById(module_id).get());
        model.addAttribute("niveaux", ListeNiveau);
        return "Enseignant/ListeDesNiveaux";
    }

    @RequestMapping("editQuestion/{id}")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("question", questionRepository.findById(id).get());
        return "Enseignant/EditQuestion";
    }

    @RequestMapping(value="/updatequestion", method = RequestMethod.POST)
    public String saveQuestion(Question question){
        questionRepository.save(question);
        return "redirect:/quizz";
    }

    @RequestMapping(value = "/removeQuestion/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String removeQuestion(@PathVariable(name = "id") Long Id){
        questionRepository.deleteById(Id);
        return Id.toString();
    }

    @RequestMapping(value = "/listeQuestions/{id_eleve}/{id_niveau}",method = RequestMethod.GET)
    public String listeQuestion(@PathVariable Long id_eleve,@PathVariable Long id_niveau,Model model){
        Eleve eleve=eleveRepository.findById(id_eleve).get();
        Niveau niveau=niveauRepository.findById(id_niveau).get();
        model.addAttribute("questionsPassee",reponseEleveRepository.questionPassee(eleve,niveau,false));
        model.addAttribute("questionEnCours",reponseEleveRepository.questionEnCours(eleve,niveau,true));
        model.addAttribute("questionsRestant",questionsRestant(eleve,niveau));
        model.addAttribute("niveau",niveau);
        model.addAttribute("eleve",eleve);
        return "Enfant/ListeQuestions";
    }
    @RequestMapping(value = "/test5/{id_eleve}/{id_question}",method = RequestMethod.GET)
    public String myytesttt(@PathVariable Long id_eleve,@PathVariable Long id_question) {
        Eleve e=eleveRepository.findById(id_eleve).get();
        Question q=questionRepository.findById(id_question).get();
        return "redirect:/quizz";

    }

    public List<Question> questionsRestant (Eleve eleve, Niveau niveau){
        List<Question> questionsPassee=reponseEleveRepository.questionPassee(eleve,niveau,false);
        List<Question> questionsRestant=questionRepository.findByNiveau(niveau);
        questionsRestant.remove(reponseEleveRepository.questionEnCours(eleve,niveau,true));
        for (int i=0; i< questionsPassee.size();i++){
            Question q=questionsPassee.get(i);
            questionsRestant.remove(q);
        }
        return questionsRestant;
    }




}
