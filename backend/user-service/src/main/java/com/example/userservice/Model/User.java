package com.example.userservice.Model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.example.userservice.Model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Getter
@Setter
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NaturalId(mutable=true)
    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId(mutable=true)
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

    @Column(name = "Klayaddress")
    private String klayaddress;


    @ManyToMany(fetch = FetchType.LAZY) //지연로딩
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), //외래키
            inverseJoinColumns = @JoinColumn(name = "role_id")) //반대 엔티티의 외래키
    private Set<Role> roles = new HashSet<>(); //순서 상관 없는 집합

    public User() {

    }

    //생성자
    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cash = Long.valueOf(0);
    }

}
