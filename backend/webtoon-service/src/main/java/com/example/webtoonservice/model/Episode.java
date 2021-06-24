package com.example.webtoonservice.model;

import com.example.webtoonservice.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="episode") 
public class Episode extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name ="eno")
    private Integer eno;

    @Column(name="epi_title")
    private String epiTitle;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="toon_id")
    private Toon toon;


    @JsonManagedReference
    @OneToOne(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "episode")
    private EpiThumbnail epiThumbnail;

    @JsonManagedReference
    @OneToOne(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "episode")
    private EpiToon epiToon;

    @JsonManagedReference
    @OneToMany(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy="episode")
    private Set<Comment> comments = new HashSet<>();

    @JsonManagedReference
    @OneToMany(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "episode")
    private Set<Rate> rate = new HashSet<>();

    public Episode(){
        
    }

    public Episode(String epiTitle, Toon toon){
        this.epiTitle= epiTitle;
        this.toon = toon;
    }


}