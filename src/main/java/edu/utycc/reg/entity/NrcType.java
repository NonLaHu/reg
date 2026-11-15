package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nrc_types")
public class NrcType {

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "name_mm", nullable = false)
    private String nameMm;

    public Short getId() {
        return id;
    }

    public NrcType setId(Short id) {
        this.id = id;
        return this;
    }

    public String getNameEn() {
        return nameEn;
    }

    public NrcType setNameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public String getNameMm() {
        return nameMm;
    }

    public NrcType setNameMm(String nameMm) {
        this.nameMm = nameMm;
        return this;
    }

    
}
