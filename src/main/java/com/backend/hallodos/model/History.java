package com.backend.hallodos.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history")
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "dosenId", referencedColumnName = "id")
    private Dosen dosenId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "mahaId", referencedColumnName = "id")

    private Mahasiswa mahasiswaId;

    @NonNull
    private int income;

    @NonNull
    private int withdrawal;
    private String date;

    public History(Dosen dosenId, Mahasiswa mahasiswaId, @NonNull int income, String date) {
        this.dosenId = dosenId;
        this.mahasiswaId = mahasiswaId;
        this.income = income;
        this.date = date;
    }

    public History(Dosen dosenId, @NonNull int withdrawal, String date) {
        this.dosenId = dosenId;
        this.withdrawal = withdrawal;
        this.date = date;
    }

    


}
