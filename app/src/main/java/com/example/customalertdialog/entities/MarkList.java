package com.example.customalertdialog.entities;

import java.util.ArrayList;
import java.util.List;

public class MarkList {
    List<Mark> marks;

    public MarkList() {
        this.marks = new ArrayList<>();
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void addMark(String owner, String marker, int deerNumber) {
        for (int i=0; i<marks.size(); i++){
            if(marks.get(i).deerNumber > deerNumber) {
                marks.add(i, new Mark(owner, marker, deerNumber));
                return;
            }
        }
        marks.add(new Mark(owner, marker, deerNumber));
    }

    public void removeMark(int deerNumber) {
        for (int i=0; i<marks.size(); i++){
            if(marks.get(i).deerNumber == deerNumber) {
                marks.remove(i);
                break;
            }
        }
    }
}
