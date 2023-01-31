package com.example.customalertdialog;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Objects;


public class SuggestionHandlerTest {
    final String[] dummyOwners = new String[] {"a", "b", "c", "d", "e", "f", "g"};

    @Test
    public void suggestionsWithoutPreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(
                suggestions, new String[] {"", "", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithOnePreviousSelection() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("previous owner marked");
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(
                suggestions, new String[] {"previous owner marked", "", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithTwoPreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("oneOwner");
        handler.setSuggestion("twoOwners");
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(
                suggestions, new String[] {"twoOwners", "oneOwner", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithThreePreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("oneOwner");
        handler.setSuggestion("twoOwners");
        handler.setSuggestion("threeOwners");
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(
                suggestions, new String[] {"threeOwners", "twoOwners", "oneOwner"}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithOwnersThatCanBeFound() {
        SuggestionHandler handler = new SuggestionHandler(
                new String[] {"a", "b", "c"}
        );
        handler.setSuggestion("a");
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(
                suggestions, new String[] {"a", "", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithOwnerThatCanBeFound() {
        SuggestionHandler handler = new SuggestionHandler(
                new String[] {"a", "b", "c"}
        );
        handler.setSuggestion("b");
        handler.setSuggestion("a");
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(
                suggestions, new String[] {"a", "b", "c"}, dummyOwners
        );
    }

    private void theThreeSuggestionsContainsNoDuplicates(String suggestions[]) {
        assertEquals(3, suggestions.length);
        assertThat(suggestions).doesNotHaveDuplicates();
    }

    private void suggestionsAreCorrect(String[] whatIs, String[] oughToBe, String[] owners) {
        for(int i=0; i<3; i++) {
            if(Objects.equals(oughToBe[i], "")){
                assertTrue(Arrays.asList(owners).contains(whatIs[i]));
            } else {
                assertEquals(whatIs[i], oughToBe[i]);
            }
        }
    }
}
