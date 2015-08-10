package com.example.fahadtariq.ottawatraffic.models;

public class Camera extends Model {
    protected String type;
    protected int number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String mapTitle() {
        return description;
    }

    @Override
    public String mapSnippet() {
        return null;
    }
}
