package com.example.customalertdialog;

import java.util.Arrays;
import java.util.Collections;

public class SuggestionHandler {
    String[] owners;

    public SuggestionHandler(String[] owners) {
        this.owners = owners;
    }

    public String[] getSuggestions() {
        Collections.shuffle(Arrays.asList(owners));
        return new String[] {
                owners[0], owners[1], owners[2]
        };
    }

}
