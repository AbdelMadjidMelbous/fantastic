package com.example.fantastic.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ReponseEleve")
public class ReponseEleve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "Reponse")
    private String reponse;

    @Column(name = "note")
    private Long note;

    @Column(name = "encours")
    private boolean encours;

    public ReponseEleve() {
    }

    public ReponseEleve(Eleve eleve, Question question, String reponse) {
        this.eleve = eleve;
        this.question = question;
        this.reponse = reponse;
    }

    public ReponseEleve(Eleve eleve, Question question, String reponse, Long note) {
        this.eleve = eleve;
        this.question = question;
        this.reponse = reponse;
        this.note = note;
    }

    public ReponseEleve(Eleve eleve, Question question, String reponse, Long note, boolean encours) {
        this.eleve = eleve;
        this.question = question;
        this.reponse = reponse;
        this.note = note;
        this.encours = encours;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
