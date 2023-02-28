package com.example.customalertdialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customalertdialog.dialogs.marking_stage.MarkDialogFragment;
import com.example.customalertdialog.dialogs.results_stage.MarkedDeerInfoDialogFragment;
import com.example.customalertdialog.dialogs.marking_stage.OwnerInfoDialogFragment;
import com.example.customalertdialog.dialogs.marking_stage.RemarkOrRemoveDialogFragment;
import com.example.customalertdialog.dialogs.marking_stage.SearchDeerDialogFragment;
import com.example.customalertdialog.dialogs.results_stage.SearchMarkedDeerDialogFragment;
import com.example.customalertdialog.dialogs.marking_stage.SearchOwnerDialogFragment;
import com.example.customalertdialog.helpers.SuggestionHandler;
import com.example.customalertdialog.helpers.Mark;
import com.example.customalertdialog.helpers.MarkList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MarkDialogFragment.DialogListener, SearchDeerDialogFragment.DialogListener,
        RemarkOrRemoveDialogFragment.DialogListener, SearchOwnerDialogFragment.DialogListener,
        SearchMarkedDeerDialogFragment.DialogListener, OwnerInfoDialogFragment.DialogListener {

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

        markList = new MarkList();
        markList.addMark("Jussi", "Pekka", 5);
        markList.addMark("Kalle", "Kanerva", 20);
        markList.addMark("Simo Siivola", "Harri", 50);
        markList.addMark("Simo Siivola", "Karri", 51);
        markList.addMark("Simo Siivola", "Ari", 52);
        markList.addMark("Simo Siivola", "Jari", 53);
        markList.addMark("Simo Siivola", "Ari", 54);

        Button openDialogButton = findViewById(R.id.searchFromMarkedDeer);
        openDialogButton.setOnClickListener(v -> {
            DialogFragment newFragment = SearchMarkedDeerDialogFragment.newInstance(markList);
            newFragment.show(getSupportFragmentManager(), "dialog");
        });

        unmarkedDeer = new ArrayList<>();
        unmarkedDeer.add(1);
        unmarkedDeer.add(2);
        unmarkedDeer.add(3);
        unmarkedDeer.add(10);
        unmarkedDeer.add(12);

        Button openMarkedDeerInfo = findViewById(R.id.markedDeerInfo);
        openMarkedDeerInfo.setOnClickListener(
                v -> {
                    DialogFragment newFragment = MarkedDeerInfoDialogFragment.newInstance(
                            markList.findMarkWithNumber(20)
                    );
                    newFragment.show(getSupportFragmentManager(), "dialog");
                }
        );

        ImageButton searchButton = findViewById(R.id.imageButton);
        searchButton.setOnClickListener(v -> {
            DialogFragment newFragment = SearchDeerDialogFragment.newInstance(unmarkedDeer, markList);
            newFragment.show(getSupportFragmentManager(), "dialog");
        });

        Button searchForOwner = findViewById(R.id.searchForOwnerButton);
        searchForOwner.setOnClickListener(v -> {
            DialogFragment newFragment = SearchOwnerDialogFragment.newInstance(OWNERS);
            newFragment.show(getSupportFragmentManager(), "dialog");
        });
    }

    protected void showRemarkOrRemoveDialog(Mark mark) {
        DialogFragment newFragment = RemarkOrRemoveDialogFragment.newInstance(mark);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void closeInputsAndShowOwnerDialog(DialogFragment dialog, EditText editText) {
        closeTheInputs(dialog, editText);
        DialogFragment newFragment = OwnerInfoDialogFragment.newInstance(
                editText.getText().toString(), markList
        );
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void showMarkDialog(String msg, int deerNumber) {
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
    public void openTheInputs(TextView textView) {
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
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
    public void changeDeer(DialogFragment dialog, Mark mark) {
        dialog.getDialog().cancel();
        showRemarkOrRemoveDialog(mark);
    }

    @Override
    public void changeDeer(DialogFragment fragment, int deerNumber) {
        System.out.println("boom");
        fragment.getDialog().cancel();
        showRemarkOrRemoveDialog(markList.findMarkWithNumber(deerNumber));
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

    @Override
    public void removeMark(Dialog dialog, int deerNumber) {
        dialog.cancel();
        makeToast("Poistetaan merkkaus vasalta numero: " + deerNumber);
    }

    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

}