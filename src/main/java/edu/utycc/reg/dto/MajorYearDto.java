package edu.utycc.reg.dto;

public class MajorYearDto {
    private Short id;
    private String yearLabel;
    private Short yearOrder;
    public Short getId() {
        return id;
    }
    public MajorYearDto setId(Short id) {
        this.id = id;
        return this;
    }
    public String getYearLabel() {
        return yearLabel;
    }
    public MajorYearDto setYearLabel(String yearLabel) {
        this.yearLabel = yearLabel;
        return this;
    }
    public Short getYearOrder() {
        return yearOrder;
    }
    public MajorYearDto setYearOrder(Short yearOrder) {
        this.yearOrder = yearOrder;
        return this;
    }
    
    
}