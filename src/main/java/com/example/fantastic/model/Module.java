package com.example.fantastic.model;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable=false,unique = true)
    private String libelle;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "module")
    private List<Niveau> niveaux;

    public Module() {
    }
    public Module(Long id,String libelle) {
        this.id=id;
        this.libelle = libelle;
    }
    public Module(Long id,String libelle, List<Niveau> niveaux) {
        this.id=id;
        this.libelle = libelle;
        this.niveaux = niveaux;
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

    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(List<Niveau> niveaux) {
        this.niveaux = niveaux;
    }
}
