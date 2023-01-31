package com.example.customalertdialog;

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
        this.previousSelections[2] = this.previousSelections[1];
        this.previousSelections[1] = this.previousSelections[0];
        this.previousSelections[0] = suggestion;
    }

    public String[] getSuggestions() {
        String[] suggestions = new String[]{"", "", ""};
        Collections.shuffle(Arrays.asList(owners));
        for (int i=0; i<3; i++) {
            if(Objects.equals(previousSelections[i], "")){
                suggestions[i] = owners[i];
            } else {
                suggestions[i] = previousSelections[i];
            }
        }
        return suggestions;
    }

}
