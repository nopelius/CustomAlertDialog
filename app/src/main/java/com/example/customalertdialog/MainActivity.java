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

    SuggestionHandler suggestionHandler;

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
        suggestionHandler = new SuggestionHandler(OWNERS);
    }

    protected void showDialog() {
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        MarkDeerDialog markDeerDialog = new MarkDeerDialog(mView, imm);
        final AlertDialog dialog = markDeerDialog.createDialog( MainActivity.this, suggestionHandler);
        markDeerDialog.openDialog(dialog);
        createActionButtonsForMarkDeerDialog(mView, dialog, imm);
    }

    private void createActionButtonsForMarkDeerDialog(View mView, AlertDialog dialog, InputMethodManager imm) {
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);

        Button markButton = mView.findViewById(R.id.markButton);
        markButton.setOnClickListener(v-> {
            suggestionHandler.setSuggestion(textView.getText().toString());
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            dialog.cancel();
        });
        markButton.setEnabled(false);

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v-> {
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            dialog.cancel();
        });
    }
}