package com.example.customalertdialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customalertdialog.dialogs.MarkDialogCreator;
import com.example.customalertdialog.dialogs.SearchDialogFragment;
import com.example.customalertdialog.dialogs.SuggestionHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        SearchDialogFragment.DialogListener {

    SuggestionHandler suggestionHandler;
    InputMethodManager imm;
    Toast t;

    private static final String[] OWNERS = new String[] {
      "Simo Siivola", "Pasi Poromies", "Lassi Lapinmies", "Laila Lakkasuo", "Repa Revontuli",
      "Timo Tunturi", "Sakke Suoperä", "Heikki Heinähattu", "Kullervo Kullanhuuhtoja"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        suggestionHandler = new SuggestionHandler(OWNERS);

        Button openDialogButton = findViewById(R.id.mark);
        openDialogButton.setOnClickListener(v -> showMarkDialog("Merkkaa vasa numero 12"));
        Button openMarkOrRemoveDialogButton = findViewById(R.id.markOrRemove);
        openMarkOrRemoveDialogButton.setOnClickListener(v -> showMarkOrRemoveDialog(20));

        ArrayList<Integer> unmarkedDeer = new ArrayList<>();
        unmarkedDeer.add(1);
        unmarkedDeer.add(2);
        unmarkedDeer.add(10);

        ImageButton searchButton = findViewById(R.id.imageButton);
        searchButton.setOnClickListener(v -> {
            DialogFragment newFragment = SearchDialogFragment.newInstance(unmarkedDeer);
            newFragment.show(getSupportFragmentManager(), "dialog");
        });
    }

    protected void showMarkOrRemoveDialog(int deerNumber) {
        View mView = getLayoutInflater().inflate(R.layout.remark_or_remove_dialog, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        TextView txt = mView.findViewById(R.id.textView);
        txt.setText("Mitä haluat tehdä vasalle numero: " + deerNumber);
        dialog.show();
        Button remarkButton = mView.findViewById(R.id.remarkButton);
        remarkButton.setOnClickListener(v -> {
            dialog.cancel();
            showMarkDialog("Merkkaa uudelleen vasa: " + deerNumber);
        });
        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> dialog.cancel());
    }

    protected void showMarkDialog(String msg) {
        View mView = getLayoutInflater().inflate(R.layout.mark_dialog, null);
        MarkDialogCreator markDialogCreator = new MarkDialogCreator(mView, imm, msg);
        final AlertDialog dialog = markDialogCreator.createDialog( MainActivity.this, suggestionHandler);
        markDialogCreator.openDialog(dialog);
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

    @Override
    public void openTheInputs(DialogFragment dialog) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void closeTheInputs(DialogFragment dialog, EditText editText) {
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void markDeer(DialogFragment dialog, int deerNumber) {
        dialog.getDialog().cancel();
        makeToast("Merkattiin vasa: " + deerNumber);
    }

    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

}