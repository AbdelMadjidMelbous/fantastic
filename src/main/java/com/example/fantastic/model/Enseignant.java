package com.example.fantastic.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Enseignant")
public class Enseignant {
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

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "enseignant")
    private List<ClasseEtudiant> classeEtudiants;

    public Enseignant() {
    }

    public Enseignant(String nom, String prenom, String username) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
    }

    public Enseignant(String nom, String prenom, String username, List<ClasseEtudiant> classeEtudiants) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.classeEtudiants = classeEtudiants;
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

    public List<ClasseEtudiant> getClasseEtudiants() {
        return classeEtudiants;
    }

    public void setClasseEtudiants(List<ClasseEtudiant> classeEtudiants) {
        this.classeEtudiants = classeEtudiants;
    }
}

