package com.example.webtoonservice.model;

import com.example.webtoonservice.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="toon_thumbnail")
public class ToonThumbnail extends DateAudit{
    @Id
    @Column(name="ttno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ttno;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_type")
    private String fileType;

    @Column(name="file_uri")
    private String fileUri;

    @Column(name="file_size")
    private long fileSize;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference // 순환참조를 막기위한 자식클래스에 참조하는 어노테이션
    @OneToOne
    @JoinColumn(name="toon_no")
    private Toon toon;

    public ToonThumbnail(){

    }

    public ToonThumbnail(String fileName, String fileType, String fileUri, Long fileSize){
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}