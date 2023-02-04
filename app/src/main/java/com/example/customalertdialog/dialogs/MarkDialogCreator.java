package com.example.customalertdialog.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customalertdialog.R;

public class MarkDialogCreator extends AppCompatActivity {

    View mView;
    InputMethodManager imm;
    boolean ownerSelected;
    EarMarkImageSelector earMarkImageSelector;

    public MarkDialogCreator(View mView, InputMethodManager imm) {
        this.mView = mView;
        this.imm = imm;
        this.ownerSelected = false;
        this.earMarkImageSelector = new EarMarkImageSelector();
    }

    public AlertDialog createDialog(Context context, SuggestionHandler suggestionsHandler) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_dropdown_item_1line, suggestionsHandler.owners
        );
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Button markButton = mView.findViewById(R.id.markButton);
                markButton.setEnabled(charSequence.length() > 2);
                setEarMarkImage(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        String[] suggestions = suggestionsHandler.getSuggestions();
        setSuggestionButtons(mView, textView, suggestions);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        return dialog;
    }

    public void openDialog(AlertDialog dialog) {
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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

    private void setEarMarkImage(String owner) {
        ImageView image = mView.findViewById(R.id.imageView);
        if(earMarkImageSelector.imageShouldBeChanged(owner)){
            image.setImageResource(earMarkImageSelector.getEarMarkImage(owner));
        }
    }
}
