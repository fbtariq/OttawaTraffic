package com.example.fahadtariq.ottawatraffic.models;

public abstract class Model {

    protected int id;
    protected Double latitude;
    protected Double longitude;
    protected String message;
    protected String messageFrench;
    protected String description;
    protected String descriptionFrench;

    public Model() {
        this.id = 0;
        this.latitude = null;
        this.longitude = null;
        this.message = null;
        this.messageFrench = null;
        this.description = null;
        this.descriptionFrench = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageFrench() {
        return messageFrench;
    }

    public void setMessageFrench(String messageFrench) {
        this.messageFrench = messageFrench;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionFrench() {
        return descriptionFrench;
    }

    public void setDescriptionFrench(String descriptionFrench) {
        this.descriptionFrench = descriptionFrench;
    }

    public abstract String mapTitle();
    public abstract String mapSnippet();
}
