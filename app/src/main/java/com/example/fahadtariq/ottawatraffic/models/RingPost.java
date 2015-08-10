package com.example.fahadtariq.ottawatraffic.models;

public class RingPost extends Model {

    protected int midBlockId;
    protected String street1;
    protected String street2;
    protected String street3;
    protected String side;
    protected String adjacentTo;
    protected String core;
    protected String notes;

    public int getMidBlockId() {
        return midBlockId;
    }

    public void setMidBlockId(int midBlockId) {
        this.midBlockId = midBlockId;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getAdjacentTo() {
        return adjacentTo;
    }

    public void setAdjacentTo(String adjacentTo) {
        this.adjacentTo = adjacentTo;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String mapTitle() {
        return "Adjacent to: " + this.adjacentTo;
    }

    @Override
    public String mapSnippet() {
        return this.notes;
    }
}
