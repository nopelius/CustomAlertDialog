package com.example.customalertdialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customalertdialog.dialogs.MarkDialogFragment;
import com.example.customalertdialog.dialogs.SearchDialogFragment;
import com.example.customalertdialog.dialogs.SuggestionHandler;
import com.example.customalertdialog.entities.Mark;
import com.example.customalertdialog.entities.MarkList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MarkDialogFragment.DialogListener, SearchDialogFragment.DialogListener {

    SuggestionHandler suggestionHandler;
    InputMethodManager imm;
    Toast t;
    ArrayList<Integer> unmarkedDeer;
    MarkList markList;

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
        openDialogButton.setOnClickListener(v -> showMarkDialog("Merkkaa vasa numero 12", 12));
        Button openMarkOrRemoveDialogButton = findViewById(R.id.markOrRemove);
        openMarkOrRemoveDialogButton.setOnClickListener(v -> showMarkOrRemoveDialog(20));

        unmarkedDeer = new ArrayList<>();
        unmarkedDeer.add(1);
        unmarkedDeer.add(2);
        unmarkedDeer.add(3);
        unmarkedDeer.add(10);
        unmarkedDeer.add(12);

        markList = new MarkList();
        markList.addMark("Jussi", "Pekka", 5);
        markList.addMark("Kalle", "Kanerva", 20);
        markList.addMark("Simo Siivola", "Harri", 50);


        ImageButton searchButton = findViewById(R.id.imageButton);
        searchButton.setOnClickListener(v -> {
            DialogFragment newFragment = SearchDialogFragment.newInstance(unmarkedDeer, markList);
            newFragment.show(getSupportFragmentManager(), "dialog");
        });
    }

    protected void showMarkOrRemoveDialog(int deerNumber) {
        View mView = getLayoutInflater().inflate(R.layout.remark_or_remove_dialog, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        TextView txt = mView.findViewById(R.id.textView);
        txt.setText("Vasa: " + deerNumber);
        dialog.show();
        Button remarkButton = mView.findViewById(R.id.remarkButton);
        remarkButton.setOnClickListener(v -> {
            dialog.cancel();
            showMarkDialog("Merkkaa uudelleen vasa: " + deerNumber, deerNumber);
        });
        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> dialog.cancel());
    }

    protected void showMarkDialog(String msg, int deerNumber) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment markFragment = MarkDialogFragment.newInstance(
                OWNERS, suggestionHandler.getSuggestions(), msg, deerNumber
        );
        markFragment.show(getSupportFragmentManager(), "anotherDialog");
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
    public void closeDialogAndStartMarkingDeer(DialogFragment dialog, EditText editText, int deerNumber) {
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        dialog.getDialog().cancel();
        showMarkDialog("Merkataan vasa: " + deerNumber, deerNumber);
    }

    @Override
    public void changeDeer(DialogFragment dialog, int deerNumber) {
        dialog.getDialog().cancel();
        showMarkDialog("Uudellenmerkataan vasa: " + deerNumber, deerNumber);
    }

    @Override
    public void markDeerToOwner(DialogFragment dialog, int deerNumber, String owner) {
        suggestionHandler.setSuggestion(owner);
        dialog.getDialog().cancel();
        if(unmarkedDeer.contains(deerNumber)) {
            makeToast("Merkattiin vasa " + deerNumber + " omistajalle: " + owner);
        } else if(markList.findMarkWithNumber(deerNumber) != null){
            Mark previousMark = markList.findMarkWithNumber(deerNumber);
            makeToast("Merkattiin käyttäjälle " + previousMark.getOwner() + " merkattu vasa " +
                    deerNumber + " käyttäjälle: " + owner);
        } else {
            makeToast("Vasan merkkaamisessa tapahtui virhe - vasaa ei merkattu.");
        }

    }

    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

}