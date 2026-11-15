package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "file_types")
public class FileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_name", nullable = false, unique = true, length = 50)
    private String typeName;

    @Column(name = "type_name_mm", nullable = true, unique = true, length = 255)
    private String typeNameMm;

    @OneToMany(mappedBy = "fileType", cascade = CascadeType.ALL)
    private List<EnrollmentFile> enrollmentFiles;

    public Integer getId() {
        return id;
    }

    public FileType setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public FileType setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public List<EnrollmentFile> getEnrollmentFiles() {
        return enrollmentFiles;
    }

    public FileType setEnrollmentFiles(List<EnrollmentFile> enrollmentFiles) {
        this.enrollmentFiles = enrollmentFiles;
        return this;
    }

    
}
