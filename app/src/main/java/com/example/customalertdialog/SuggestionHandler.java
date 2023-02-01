package com.example.customalertdialog;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class SuggestionHandler {
    String[] owners;
    String[] previousSelections;

    public SuggestionHandler(String[] owners) {
        this.owners = owners;
        this.previousSelections = new String[] {"", "", ""};
    }

    public void setSuggestion(String suggestion) {
        if(Arrays.asList(previousSelections).contains(suggestion)) {
            int removalIndex = ArrayUtils.indexOf(previousSelections, suggestion);
            moveTheLine(removalIndex);
        } else {
            moveTheLine(2);
        }
        this.previousSelections[0] = suggestion;
    }

    private void moveTheLine(int removalIndex) {
        this.previousSelections[removalIndex] = "";
        for(int i=removalIndex-1; i>-1; i--){
            this.previousSelections[i+1] = this.previousSelections[i];
        }
    }

    public String[] getSuggestions() {
        String[] suggestions = new String[]{"", "", ""};
        Collections.shuffle(Arrays.asList(owners));
        int offset = 0;
        for (int i=0; i<3; i++) {
            if(Objects.equals(previousSelections[i], "")){
                offset = selectOffset(offset, suggestions, owners);
                suggestions[i] = owners[offset];
            } else {
                suggestions[i] = previousSelections[i];
            }
        }
        return suggestions;
    }

    private int selectOffset(int i, String[] suggestions, String[] owners) {
        if(Arrays.asList(suggestions).contains(owners[i])){
            return selectOffset(i+1, suggestions, owners);
        } else {
            return i;
        }
    }

}
