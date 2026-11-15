package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nrc_townships")
public class NrcTownship {

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(nullable = false)
    private String code;

    @Column(name = "short_en")
    private String shortEn;

    @Column(name = "short_mm")
    private String shortMm;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "name_mm", nullable = false)
    private String nameMm;

    @Column(name = "state_code", nullable = false, length =10)
    private String stateCode;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private NrcState state;

    public Short getId() {
        return id;
    }

    public NrcTownship setId(Short id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public NrcTownship setCode(String code) {
        this.code = code;
        return this;
    }

    public String getShortEn() {
        return shortEn;
    }

    public NrcTownship setShortEn(String shortEn) {
        this.shortEn = shortEn;
        return this;
    }

    public String getShortMm() {
        return shortMm;
    }

    public NrcTownship setShortMm(String shortMm) {
        this.shortMm = shortMm;
        return this;
    }

    public String getNameEn() {
        return nameEn;
    }

    public NrcTownship setNameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public String getNameMm() {
        return nameMm;
    }

    public NrcTownship setNameMm(String nameMm) {
        this.nameMm = nameMm;
        return this;
    }

    public String getStateCode() {
        return stateCode;
    }

    public NrcTownship setStateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public NrcState getState() {
        return state;
    }

    public NrcTownship setState(NrcState state) {
        this.state = state;
        return this;
    }
}
