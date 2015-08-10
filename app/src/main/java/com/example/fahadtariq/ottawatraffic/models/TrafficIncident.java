package com.example.fahadtariq.ottawatraffic.models;

public class TrafficIncident extends Model {
    @Override
    public String mapTitle() {
        return description;
    }

    @Override
    public String mapSnippet() {
        return message;
    }
}
