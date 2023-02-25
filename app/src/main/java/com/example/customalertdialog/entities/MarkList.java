package com.example.customalertdialog.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarkList implements Serializable {
    List<Mark> marks;

    public MarkList() {
        this.marks = new ArrayList<>();
    }

    public MarkList(ArrayList<Mark> marks) {
        this.marks = marks;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public Mark findMarkWithNumber(int deerNumber) {
        for(Mark mark : getMarks()) {
            if(mark.getDeerNumber() == deerNumber) {
                return mark;
            }
        }
        return null;
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

    public ArrayList<Integer> deerNumbersMarkedForOwner(String owner) {
        ArrayList<Integer> deerNumbers = new ArrayList<>();
        for(Mark mark : getMarks()) {
            if(Objects.equals(mark.getOwner(), owner)) {
                deerNumbers.add(mark.getDeerNumber());
            }
        }
        return deerNumbers;
    }

    public ArrayList<Integer> deerNumbersMarkedByUser(String user) {
        ArrayList<Integer> deerNumbers = new ArrayList<>();
        for(Mark mark : getMarks()) {
            if(Objects.equals(mark.getMarker(), user)) {
                deerNumbers.add(mark.getDeerNumber());
            }
        }
        return deerNumbers;
    }
}
