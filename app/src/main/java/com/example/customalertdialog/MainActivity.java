package com.example.customalertdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String[] OWNERS = new String[] {
      "Simo Siivola", "Pasi Poromies", "Lassi Lapinmies", "Laila Lakkasuo", "Repa Revontuli",
      "Timo Tunturi", "Sakke Suoperä", "Heikki Heinähattu", "Kullervo Kullanhuuhtoja"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openDialogButton = findViewById(R.id.mark);
        openDialogButton.setOnClickListener(v -> showDialog());
    }

    protected void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, OWNERS
        );
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        setSuggestionButtons(mView, textView);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setSuggestionButtons(View mView, AutoCompleteTextView textView) {
        for(int an_id : new int[]{R.id.suggestion1, R.id.suggestion2, R.id.suggestion3}){
            Button suggestion = mView.findViewById(an_id);
            suggestion.setOnClickListener(v -> {
                textView.setText(suggestion.getText());
            });
        }
    }
}