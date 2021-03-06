package com.example.fantastic.model;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Niveaux",uniqueConstraints = {
        @UniqueConstraint(columnNames = { "libelle", "module_id" })})
public class Niveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false)   //must be unique
    private String libelle;
    @Column(nullable=false)
    private int poids;
    @Column(nullable=false)
    private int difficulte;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="module_id")
    private Module module;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="annee_id")
    private Annee annee;

    @OneToMany(mappedBy = "niveau")
    private List<Question> questions;

    @OneToMany(mappedBy = "niveau")
    private List<SupportDeCours> cours;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "niveau")
    private List<NiveauEleve> niveauEleves;


    public Niveau() {
    }

    public Niveau(Long id,String libelle, int poids) {
        this.id=id;
        this.libelle = libelle;
        this.poids = poids;
    }

    public Niveau(String libelle, int poids, int difficulte) {
        this.libelle = libelle;
        this.poids = poids;
        this.difficulte = difficulte;
    }

    public Niveau(String libelle, int poids, Module module, List<Question> questions) {
        this.libelle = libelle;
        this.poids = poids;
        this.module = module;
        this.questions = questions;
    }

    public Niveau(String libelle, int poids, Module module, Annee annee, List<Question> questions) {
        this.libelle = libelle;
        this.poids = poids;
        this.module = module;
        this.annee = annee;
        this.questions = questions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SupportDeCours> getCours() {
        return cours;
    }

    public void setCours(List<SupportDeCours> cours) {
        this.cours = cours;
    }

    public Long getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    public List<NiveauEleve> getNiveauEleves() {
        return niveauEleves;
    }

    public void setNiveauEleves(List<NiveauEleve> niveauEleves) {
        this.niveauEleves = niveauEleves;
    }
}
