package com.example.webtoonservice.model;

import com.example.webtoonservice.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// lombok 라이브러리
@Getter
@Setter
@Entity
@Table(name = "toon")
public class Toon extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tno")
    private Integer tno;

    private String title;

    private String artist;

    private String day;

    private String genre;

    private Long user_no;
    
    @JsonManagedReference // 순환참조를 막기위한 부모클래스에 참조하는 어노테이션
    @OneToOne(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL, // toon객체를 변경/삭제할 때, 해당 객체를 참조하고 있는 모든 객체들이 변경/삭제된다.
            orphanRemoval = true, //orphanRemoval옵션은 기존 NULL처리된 자식을 DELETE 한다
            mappedBy = "toon") // toon의 thumbnail으로 연관관계의 주인을 정해줍니다.
    private ToonThumbnail toonThumbnail;


    @JsonManagedReference
    @OneToMany(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "toon")
    private Set<Episode> episode = new HashSet<>();

    @JsonManagedReference
    @OneToMany(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "toon")
    private Set<Fav> fav = new HashSet<>();

    public Toon(){

    }

    public Toon(String title, String artist, String day, String genre, Long user){
        this.title = title;
        this.artist = artist;
        this.day = day;
        this.genre = genre;
        this.user_no = user;
    }

}