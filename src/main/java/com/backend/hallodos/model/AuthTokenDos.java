package com.backend.hallodos.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "tokensDos")
public class AuthTokenDos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String token;
    
    @OneToOne(targetEntity = Dosen.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Dosen dosen;
    
    @Column(name = "created_at")
    private Date created_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Dosen getUser() {
        return dosen;
    }

    public void setUser(Dosen dosen) {
        this.dosen = dosen;
    }

    public AuthTokenDos(Dosen dosen) {
        this.dosen = dosen;
        this.created_at = new Date();
        this.token = UUID.randomUUID().toString();
    }

    public AuthTokenDos() {
    }

}
