package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nrc_states")
public class NrcState {

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(nullable = false)
    private String code;

    @Column(name = "number_en", nullable = false)
    private String numberEn;

    @Column(name = "number_mm", nullable = false)
    private String numberMm;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "name_mm", nullable = false)
    private String nameMm;


    public Short getId() {
        return id;
    }

    public NrcState setId(Short id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public NrcState setCode(String code) {
        this.code = code;
        return this;
    }

    public String getNumberEn() {
        return numberEn;
    }

    public NrcState setNumberEn(String numberEn) {
        this.numberEn = numberEn;
        return this;
    }

    public String getNumberMm() {
        return numberMm;
    }

    public NrcState setNumberMm(String numberMm) {
        this.numberMm = numberMm;
        return this;
    }

    public String getNameEn() {
        return nameEn;
    }

    public NrcState setNameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public String getNameMm() {
        return nameMm;
    }

    public NrcState setNameMm(String nameMm) {
        this.nameMm = nameMm;
        return this;
    }
}
