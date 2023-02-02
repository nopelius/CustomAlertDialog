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

    SuggestionHandler suggHandler;

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
        suggHandler = new SuggestionHandler(OWNERS);
    }

    protected void showDialog() {
        MarkDeerDialog markDeerDialog = new MarkDeerDialog(OWNERS);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final AlertDialog dialog = markDeerDialog.createDialog(mView, MainActivity.this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        Button markButton = mView.findViewById(R.id.markButton);
        markButton.setOnClickListener(v-> {
            suggHandler.setSuggestion(textView.getText().toString());
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