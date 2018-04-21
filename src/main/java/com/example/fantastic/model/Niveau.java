package com.example.fantastic.model;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Niveaux")
public class Niveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false, unique = true)   //must be unique
    private String libelle;
    @Column(nullable=false)
    private int poids;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="module_id")
    private Module module;

    @OneToMany(mappedBy = "niveau")
    private List<Question> questions;

    @OneToMany(mappedBy = "niveau")
    private List<SupportDeCours> cours;

    public Niveau() {
    }

    public Niveau(Long id,String libelle, int poids) {
        this.id=id;
        this.libelle = libelle;
        this.poids = poids;
    }

    public Niveau(String libelle, int poids, Module module, List<Question> questions) {
        this.libelle = libelle;
        this.poids = poids;
        this.module = module;
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
}
