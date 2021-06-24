package com.example.webtoonservice.model;

import com.example.webtoonservice.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="fav")
public class Fav extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fno")
    private Integer fno;

    private String title;

    private String username;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="tid")
    private Toon toon;

    public Fav(){

    }

    public Fav(String username, String title, Toon toon){
        this.title = title;
        this.username=username;
        this.toon = toon;
    }

}