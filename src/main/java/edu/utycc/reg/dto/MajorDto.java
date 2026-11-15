package edu.utycc.reg.dto;

public class MajorDto {
    private Short id;
    private String code;
    private String name;
    
    public Short getId() {
        return id;
    }
    public MajorDto setId(Short id) {
        this.id = id;
        return this;
    }
    public String getCode() {
        return code;
    }
    public MajorDto setCode(String code) {
        this.code = code;
        return this;
    }
    public String getName() {
        return name;
    }
    public MajorDto setName(String name) {
        this.name = name;
        return this;
    }

    
}