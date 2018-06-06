package com.example.fantastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Created by AbdelMadjid on 02/05/2018.
 */
@Entity
@Table(name = "annee")
public class Annee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false,unique = true)
    private String libelle;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "annee")
    private List<Niveau> niveaux;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "annee")
    private List<ClasseEtudiant> classeEtudiants;

    public Annee() {
    }

    public Annee(String libelle) {
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(List<Niveau> niveaux) {
        this.niveaux = niveaux;
    }




}
