package com.example.webtoonservice.model;

import com.example.webtoonservice.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="rate")
public class Rate extends DateAudit{
    @Id
    @Column(name="rno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rno;

    @Column(name="username")
    private String username;

    @Column(name="rate")
    private Integer rate;
    

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="episode_id")
    private Episode episode;

    public Rate(){

    }

    public Rate(String username, Integer rate){
        this.username= username;
        this.rate = rate;
    }
}