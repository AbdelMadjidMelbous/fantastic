package com.example.fantastic.model;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "Questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false)
    private int num_question;
    @Column(nullable=false)
    private String ennonce;
    @Column(nullable=false)
    private int poids;
    @Column(nullable=false)
    private String reponse;
    @Column(name = "proposition1")
    private String proposition1;
    @Column(name = "proposition2")
    private String proposition2;
    @Column(name = "proposition3")
    private String proposition3;



    @ManyToOne
    @JoinColumn(name="niveau_id")
    private Niveau niveau;


    public Question() {
    }

    public Question(int num_question,String ennonce, int poids, String reponse) {
        this.num_question = num_question;
        this.ennonce = ennonce;
        this.poids = poids;
        this.reponse = reponse;
    }

    public Question(int num_question,String ennonce, int poids, String reponse, Niveau niveau, String proposition1,String proposition2,String proposition3) {
        this.num_question = num_question;
        this.ennonce = ennonce;
        this.poids = poids;
        this.reponse = reponse;
        this.niveau = niveau;
        this.proposition1=proposition1;
        this.proposition2=proposition2;
        this.proposition3=proposition3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getEnnonce() {
        return ennonce;
    }

    public void setEnnonce(String ennonce) {
        this.ennonce = ennonce;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public String getProposition1() {
        return proposition1;
    }

    public void setProposition1(String proposition1) {
        this.proposition1 = proposition1;
    }

    public String getProposition2() {
        return proposition2;
    }

    public void setProposition2(String proposition2) {
        this.proposition2 = proposition2;
    }

    public String getProposition3() {
        return proposition3;
    }

    public void setProposition3(String proposition3) {
        this.proposition3 = proposition3;
    }

    public int getNum_question() {
        return num_question;
    }

    public void setNum_question(int num_question) {
        this.num_question = num_question;
    }
}
