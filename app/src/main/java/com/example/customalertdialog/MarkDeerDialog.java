package com.example.customalertdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MarkDeerDialog extends AppCompatActivity {

    private final String[] owners;
    SuggestionHandler suggestionHandler;

    public MarkDeerDialog(String[] owners) {
        this.owners = owners;
        this.suggestionHandler = new SuggestionHandler(owners);
    }

    public AlertDialog createDialog(View mView, Context context) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_dropdown_item_1line, this.owners
        );
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        String[] suggestions = suggestionHandler.getSuggestions();
        setSuggestionButtons(mView, textView, suggestions);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        return dialog;
    }

    private void setSuggestionButtons(View mView, AutoCompleteTextView textView, String[] suggestions) {
        int[] buttonIds = new int[]{R.id.suggestion1, R.id.suggestion2, R.id.suggestion3};
        for(int i=0; i<3; i++) {
            Button suggestion = mView.findViewById(buttonIds[i]);
            suggestion.setText(suggestions[i]);
            suggestion.setOnClickListener(v -> {
                textView.setFocusable(false);
                textView.setFocusableInTouchMode(false);
                textView.setText(suggestion.getText());
                textView.setFocusable(true);
                textView.setFocusableInTouchMode(true);
            });
        }
    }
}
