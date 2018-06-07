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
import java.util.*;

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


    @RequestMapping(value = "/accueil/{id_eleve}/{id_annee}",method = RequestMethod.GET)
    public String getAccueil(@PathVariable Long id_eleve,@PathVariable Long id_annee, Model model){
        model.addAttribute("eleve",eleveRepository.findById(id_eleve).get());
        model.addAttribute("annee",anneeRepository.findById(id_annee).get());
        return "Enfant/Accueil_Enfant";
    }

    @RequestMapping(value = "/niveauEnCours/{id_eleve}/{id_annee}/{id_module}",method = RequestMethod.GET)
    public String  getListeQuestion(@PathVariable long id_eleve, @PathVariable Long id_annee,@PathVariable Long id_module){
        Eleve eleve=eleveRepository.findById(id_eleve).get();
        Annee annee=anneeRepository.findById(id_annee).get();
        Module module=  moduleRepository.findById(id_module).get();
        Niveau nivEncours=niveauEleveRepository.niveauEnCours(eleve,module,annee,true);
        return "redirect:/listeQuestions/"+eleve.getId()+"/"+nivEncours.getId();

    }

    @RequestMapping(value = "/repondre/{id_eleve}/{id_question}", method = RequestMethod.GET)
    public String repondre(@PathVariable Long id_eleve,@PathVariable Long id_question, Model model){
        Question q= questionRepository.findById(id_question).get();
        model.addAttribute("question", q);
        List<String> propositions = new ArrayList<>();
        propositions.add(q.getReponse());
        propositions.add(q.getProposition1());
        propositions.add(q.getProposition2());
        propositions.add(q.getProposition3());
        Collections.shuffle(propositions);
        model.addAttribute("p1", propositions.get(0));
        model.addAttribute("p2", propositions.get(1));
        model.addAttribute("p3", propositions.get(2));
        model.addAttribute("p4", propositions.get(3));
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
        changementDeQuestion(eleve,question);
        return msg;
    }

    @RequestMapping(value = "/consulterReponse/{id_eleve}/{id_question}",method = RequestMethod.GET)
    public String consulter(@PathVariable Long id_eleve,@PathVariable Long id_question,Model model){
        Eleve eleve=eleveRepository.findById(id_eleve).get();
        Question question=questionRepository.findById(id_question).get();
        ReponseEleve reponseEleve=reponseEleveRepository.ReponseByQuestion(question,eleve);
        model.addAttribute("question",question);
        model.addAttribute("reponse",reponseEleve);
        return "Enfant/Afficher_Reponse";
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
