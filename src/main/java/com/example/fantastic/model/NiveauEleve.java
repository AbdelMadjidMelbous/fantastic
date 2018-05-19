package com.example.fantastic.model;

import javax.persistence.*;

@Entity
@Table(name = "Niveau_Eleve")
public class NiveauEleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "niveau_id")
    private Niveau niveau;

    @Column(name = "note")
    private Long note;

    @Column(name = "encours")
    private boolean encours;


    public NiveauEleve() {
    }

    public NiveauEleve(Eleve eleve, Niveau niveau,boolean encours) {
        this.eleve = eleve;
        this.niveau = niveau;
        this.encours=encours;

    }

    public NiveauEleve(Eleve eleve, Niveau niveau, Long note) {
        this.eleve = eleve;
        this.niveau = niveau;
        this.note = note;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Long getNote() {
        return note;
    }

    public void setNote(Long note) {
        this.note = note;
    }

    public boolean isEncours() {
        return encours;
    }

    public void setEncours(boolean encours) {
        this.encours = encours;
    }
}
