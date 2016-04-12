package by.bsu.up.chat.common.models;

import java.io.Serializable;

public class Message implements Serializable {

    private Long id;
    private String name;
    private String timestamp;
    private String text;
    private Boolean edited;
    private Boolean wasEdited;
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public Boolean isWasEdited() {
        return wasEdited;
    }

    public void setWasEdited(Boolean wasEdited) {
        this.wasEdited = wasEdited;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", text='" + text + '\'' +
                ", edited='" + edited + '\'' +
                ", wasEdited='" + wasEdited + '\'' +
                ", deleted='" + deleted + '\'' +
                '}';
    }
}
