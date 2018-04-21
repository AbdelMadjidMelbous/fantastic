package com.example.fantastic.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Cours")
public class SupportDeCours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false)
    private String originaleName;
    @Column(nullable=false)
    private String hachedName;
    @Column(nullable=false)
    private Date date;

    public SupportDeCours(String originaleName, String hachedName, Date date) {
        this.originaleName = originaleName;
        this.hachedName = hachedName;
        this.date = date;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="niveau_id")
    private Niveau niveau;

    public SupportDeCours() {
    }

    public SupportDeCours(String originaleName, String hachedName, Niveau niveau) {
        this.originaleName = originaleName;
        this.hachedName = hachedName;
        this.niveau = niveau;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginaleName() {
        return originaleName;
    }

    public void setOriginaleName(String originaleName) {
        this.originaleName = originaleName;
    }

    public String getHachedName() {
        return hachedName;
    }

    public void setHachedName(String hachedName) {
        this.hachedName = hachedName;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
