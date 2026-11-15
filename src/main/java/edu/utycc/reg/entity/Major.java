package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "major")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Short id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "major")
    private List<MajorYear> majorYears;

    public Short getId() {
        return id;
    }

    public Major setId(Short id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Major setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Major setName(String name) {
        this.name = name;
        return this;
    }

    public List<MajorYear> getMajorYears() {
        return majorYears;
    }

    public Major setMajorYears(List<MajorYear> majorYears) {
        this.majorYears = majorYears;
        return this;
    }

    
}
