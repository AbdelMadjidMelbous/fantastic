package com.example.fantastic.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ClasseEtudiant")
public class ClasseEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false,unique = true)
    private String libelle;

    @Column(nullable=false)
    private int numero;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="annee_id")
    private Annee annee;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classeEtudiant")
    private List<Eleve> eleves;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="enseignant_id")
    private Enseignant enseignant;


    public ClasseEtudiant() {
    }

    public ClasseEtudiant(String libelle, Annee annee,int numero) {
        this.libelle = libelle;
        this.annee = annee;
        this.numero=numero;
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

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Eleve> getEleves() {
        return eleves;
    }

    public void setEleves(List<Eleve> eleves) {
        this.eleves = eleves;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }
}
