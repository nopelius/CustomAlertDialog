package com.example.customalertdialog.helpers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Mark implements Serializable {

    String owner;
    String marker;
    String markTime;
    int deerNumber;

    public Mark(String owner, String marker, int deerNumber){
        this.owner = owner;
        this.marker = marker;
        this.deerNumber = deerNumber;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        markTime = simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public int getDeerNumber() {
        return deerNumber;
    }

    public String getOwner() {
        return owner;
    }

    public String getMarker() { return marker; }

    public String getTime() { return markTime; }

}
