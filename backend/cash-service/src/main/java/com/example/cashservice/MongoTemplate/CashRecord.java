package com.example.cashservice.MongoTemplate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Getter
@Setter
@NoArgsConstructor
@Document
public class CashRecord {


    @Id
    private ObjectId id;
    private String userid;
    private long change_amount;
    private String content;
    private LocalDateTime createdAt;
    private String toonname;
    private String epiname;
    private String epino;


    @Builder
    public CashRecord(String userid,long change_amount, String content,String toonname,String epiname, String epino ){
        this.userid = userid;
        this.change_amount = change_amount;
        this.content = content;
        this.toonname = toonname;
        this.epiname = epiname;
        this.epino = epino;
        this.createdAt = LocalDateTime.now();
    }


}
