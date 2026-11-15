package edu.utycc.reg.dto;

public class FieldUpdateRequest {
    private String path;
    private String value;
    private Long entityId;

    public String getPath() {
        return path;
    }
    public FieldUpdateRequest setPath(String path) {
        this.path = path;
        return this;
    }
    public String getValue() {
        return value;
    }
    public FieldUpdateRequest setValue(String value) {
        this.value = value;
        return this;
    }
    public Long getEntityId() {
        return entityId;
    }
    public FieldUpdateRequest setEntityId(Long entityId) {
        this.entityId = entityId;
        return this;
    }

}
