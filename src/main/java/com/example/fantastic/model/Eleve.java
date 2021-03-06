package com.example.fantastic.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Eleves")
public class Eleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false)
    private String nom;
    @Column(nullable=false)
    private String prenom;
    @Column(nullable=false,unique = true)
    private String username;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "eleve")
    private List<ReponseEleve> reponseEleves;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "eleve")
    private List<NiveauEleve> niveauEleves;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="classeEtudiant_id")
    private ClasseEtudiant classeEtudiant;

    public Eleve() {
    }

    public Eleve(String nom, String prenom, String username) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
    }

    public Eleve(String nom, String prenom, String username, ClasseEtudiant classeEtudiant) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.classeEtudiant = classeEtudiant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ReponseEleve> getReponseEleves() {
        return reponseEleves;
    }

    public void setReponseEleves(List<ReponseEleve> reponseEleves) {
        this.reponseEleves = reponseEleves;
    }

    public List<NiveauEleve> getNiveauEleves() {
        return niveauEleves;
    }

    public void setNiveauEleves(List<NiveauEleve> niveauEleves) {
        this.niveauEleves = niveauEleves;
    }

    public ClasseEtudiant getClasseEtudiant() {
        return classeEtudiant;
    }

    public void setClasseEtudiant(ClasseEtudiant classeEtudiant) {
        this.classeEtudiant = classeEtudiant;
    }
}
