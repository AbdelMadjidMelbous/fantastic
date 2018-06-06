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
        model.addAttribute("questionsPassee",reponseEleveRepository.questionPassee(eleveRepository.findById(id_eleve).get(),niveauRepository.findById(id_niveau).get(),false));
        model.addAttribute("questionEnCours",reponseEleveRepository.questionEnCours(eleveRepository.findById(id_eleve).get(),niveauRepository.findById(id_niveau).get(),true));
        model.addAttribute("questionsRestant",questionsRestant(eleveRepository.findById(id_eleve).get(),niveauRepository.findById(id_niveau).get()));
        model.addAttribute("niveau",niveauRepository.findById(id_niveau).get());
        return "Enfant/ListeQuestions";
    }
    @RequestMapping(value = "/test5/{id_eleve}/{id_question}",method = RequestMethod.GET)
    public String myytesttt(@PathVariable Long id_eleve,@PathVariable Long id_question) {
        Eleve e=eleveRepository.findById(id_eleve).get();
        Question q=questionRepository.findById(id_question).get();
        changementDeQuestion(e,q);
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

    public void changementDeQuestion(Eleve eleve,Question questionEnCours ){
        //Chercher la question en cours dans la table réponse
        ReponseEleve r=reponseEleveRepository.ReponseByQuestion(questionEnCours,eleve);
        //Changer l'état de la question en cours
        r.setEncours(false);
        reponseEleveRepository.save(r); // updadate??
        //Rechercher la question suivante dans le niveau est l'insérer dans la table réponse elève
        int num_suivant= questionEnCours.getNum_question() +1;
        List<Question> questionSuivante= questionRepository.findByNum_question(num_suivant,questionEnCours.getNiveau());
        //Ajouter la nouvelle question dans la table
        if (questionSuivante.size() != 0){
            ReponseEleve nouvReponse= new ReponseEleve();
            nouvReponse.setEncours(true);
            nouvReponse.setEleve(eleve);
            nouvReponse.setNote(0);
            nouvReponse.setQuestion(questionSuivante.get(0));
            reponseEleveRepository.save(nouvReponse);
        }else {
            //Chercher la première question dans le niveau suivant
            ReponseEleve nouvReponse= new ReponseEleve();
            nouvReponse.setEncours(true);
            nouvReponse.setEleve(eleve);
            nouvReponse.setNote(0);
            num_suivant=1;
            int difSuivante=questionEnCours.getNiveau().getDifficulte()+1;
            List<Niveau> niveauSuivant=niveauRepository.findByDifficulte(questionEnCours.getNiveau().getAnnee(),questionEnCours.getNiveau().getModule(),difSuivante);
            if (niveauSuivant.size() !=0){
                // s'il existe un niveau suivant on sauvgarde dans la table niveauEleve et on cherche la première question de ce niveau
                NiveauEleve niveauEleve= niveauEleveRepository.findByniveau(eleve,questionEnCours.getNiveau());
                niveauEleve.setEncours(false);
                NiveauEleve nouvNiveau= new NiveauEleve();
                nouvNiveau.setEncours(true);
                nouvNiveau.setEleve(eleve);
                nouvNiveau.setNiveau(niveauSuivant.get(0));
                niveauEleveRepository.save(nouvNiveau);
                questionSuivante=questionRepository.findByNum_question(num_suivant,niveauSuivant.get(0));
                nouvReponse.setQuestion(questionSuivante.get(0));
                reponseEleveRepository.save(nouvReponse);
            }

        }

    }


}
