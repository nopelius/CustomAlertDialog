package com.example.customalertdialog;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import com.example.customalertdialog.dialogs.SuggestionHandler;

import java.util.Arrays;
import java.util.Objects;


public class SuggestionHandlerTest {
    final String[] dummyOwners = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    final String[] shortDummyOwners = new String[] {"a", "b", "c"};

    @Test
    public void suggestionsWithoutPreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        suggestionsCheck(
                handler, new String[] {"", "", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithOnePreviousSelection() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("previous owner marked");
        suggestionsCheck(
                handler, new String[] {"previous owner marked", "", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithTwoPreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("oneOwner");
        handler.setSuggestion("twoOwners");
        suggestionsCheck(
                handler, new String[] {"twoOwners", "oneOwner", ""}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithThreePreviousSelections() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("oneOwner");
        handler.setSuggestion("twoOwners");
        handler.setSuggestion("threeOwners");
        suggestionsCheck(
                handler, new String[] {"threeOwners", "twoOwners", "oneOwner"}, dummyOwners
        );
    }

    @Test
    public void suggestionsWithOwnersThatCanBeFound() {
        SuggestionHandler handler = new SuggestionHandler(shortDummyOwners);
        handler.setSuggestion("a");
        suggestionsCheck(handler, new String[] {"a", "", ""}, shortDummyOwners);
    }

    @Test
    public void suggestionsWithOwnerThatCanBeFound() {
        SuggestionHandler handler = new SuggestionHandler(shortDummyOwners);
        handler.setSuggestion("b");
        handler.setSuggestion("a");
        suggestionsCheck(handler, new String[] {"a", "b", "c"}, shortDummyOwners);
    }

    @Test
    public void suggestionsWithRecurringOwner() {
        SuggestionHandler handler = new SuggestionHandler(shortDummyOwners);
        handler.setSuggestion("a");
        handler.setSuggestion("a");
        suggestionsCheck(handler, new String[] {"a", "", ""}, shortDummyOwners);
    }

    @Test
    public void suggestionsWithLaterRecurringOwner() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("a");
        handler.setSuggestion("b");
        handler.setSuggestion("a");
        suggestionsCheck(handler, new String[] {"a", "b", ""}, dummyOwners);
    }

    @Test
    public void suggestionsWithMuchLaterRecurringOwner() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("a");
        handler.setSuggestion("b");
        handler.setSuggestion("c");
        handler.setSuggestion("a");
        suggestionsCheck(handler, new String[] {"a", "c", "b"}, dummyOwners);
    }

    @Test
    public void suggestionsWithAtTheMiddleRecurringOwner() {
        SuggestionHandler handler = new SuggestionHandler(dummyOwners);
        handler.setSuggestion("a");
        handler.setSuggestion("b");
        handler.setSuggestion("c");
        handler.setSuggestion("b");
        suggestionsCheck(handler, new String[] {"b", "c", "a"}, dummyOwners);
    }

    private void suggestionsCheck(SuggestionHandler handler, String[] oughtToBe, String[] owners) {
        String[] suggestions = handler.getSuggestions();
        theThreeSuggestionsContainsNoDuplicates(suggestions);
        suggestionsAreCorrect(suggestions, oughtToBe, owners);
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
