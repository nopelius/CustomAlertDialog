package com.example.customalertdialog.entities;

import java.io.Serializable;

public class Mark implements Serializable {

    String owner;
    String marker;
    int deerNumber;

    public Mark(String owner, String marker, int deerNumber){
        this.owner = owner;
        this.marker = marker;
        this.deerNumber = deerNumber;
    }

    public int getDeerNumber() {
        return deerNumber;
    }

    public String getOwner() {
        return owner;
    }

    public String getMarker() { return marker; }

}
