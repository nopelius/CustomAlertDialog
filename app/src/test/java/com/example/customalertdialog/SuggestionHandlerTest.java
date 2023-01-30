package com.example.customalertdialog;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;


public class SuggestionHandlerTest {
    final String[] dummyOwners = new String[] {"a", "b", "c", "d", "e", "f", "g"};

    @Test
    public void suggestionsWithoutPreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        String[] suggestions = handler.getSuggestions();
        assertEquals(3, suggestions.length);
        assertThat(suggestions).doesNotHaveDuplicates();
        assertTrue(Arrays.asList(dummyOwners).contains(suggestions[0]));
        assertTrue(Arrays.asList(dummyOwners).contains(suggestions[1]));
        assertTrue(Arrays.asList(dummyOwners).contains(suggestions[2]));
    }
}
