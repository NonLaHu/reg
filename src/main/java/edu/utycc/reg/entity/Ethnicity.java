package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ethnicity")
public class Ethnicity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "nameEn", nullable = false, length = 50)
    private String nameEn;

    @Column(name = "nameMm", length = 50)
    private String nameMm;

    // Getters and Setters
    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameMm() {
        return nameMm;
    }

    public void setNameMm(String nameMm) {
        this.nameMm = nameMm;
    }
}
