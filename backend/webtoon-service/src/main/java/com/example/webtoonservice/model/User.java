package com.example.webtoonservice.model;

import com.example.webtoonservice.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
}) // users 라는 테이블에 username과 email은 Unique Key
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Column(name = "cash")
    @ColumnDefault("0")
    private Long cash;


    // user_roles라는 조인테이블 생성 : user 와 role 관계 ;; 별도의 테이블을 만들어서 각 테이블의 외래키를 가지고 연관관계를 관리
    @ManyToMany(fetch = FetchType.LAZY) //지연로딩
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), //외래키
            inverseJoinColumns = @JoinColumn(name = "role_id")) //반대 엔티티의 외래키
    private Set<Role> roles = new HashSet<>(); //순서 상관 없는 집합


    @JsonManagedReference
    @OneToMany(fetch=FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "user")
    private Set<Toon> toon = new HashSet<>();


    public User() {

    }

    //생성자
    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}