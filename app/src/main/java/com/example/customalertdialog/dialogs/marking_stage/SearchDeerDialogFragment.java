package com.example.customalertdialog.dialogs.marking_stage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;
import com.example.customalertdialog.helpers.EarMarkImageSelector;
import com.example.customalertdialog.helpers.Mark;
import com.example.customalertdialog.helpers.MarkList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchDeerDialogFragment extends DialogFragment {

    DialogListener listener;
    EditText numberTextField;
    ArrayList<Integer> unmarkedDeer;

    ImageView image;
    TextView txt;
    TextView markerInfo;
    Button searchButton;

    public static SearchDeerDialogFragment newInstance(ArrayList<Integer> unmarkedDeer, MarkList markList) {
        SearchDeerDialogFragment f = new SearchDeerDialogFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList("unmarkedDeer", unmarkedDeer);
        args.putSerializable("markList", (Serializable) markList.getMarks());
        f.setArguments(args);
        return f;
    }

    public interface DialogListener {
        void closeDialogAndStartMarkingDeer(DialogFragment dialog, EditText edit, int deerNumber);
        void changeDeer(DialogFragment dialog, Mark mark);
        void openTheInputs(TextView textView);
        void closeTheInputs(DialogFragment dialog, EditText text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement: " + e);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        unmarkedDeer = getArguments().getIntegerArrayList("unmarkedDeer");
        ArrayList<Mark> marks = (ArrayList<Mark>) getArguments().getSerializable("markList");
        MarkList markList = new MarkList(marks);

        View mView = getLayoutInflater().inflate(R.layout.search_dialog, null);
        findElements(mView);
        addActionButtons(mView);
        addSearchText(mView, markList);

        builder.setView(mView);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void findElements(View mView) {
        image = mView.findViewById(R.id.imageView);
        txt = mView.findViewById(R.id.textView);
        markerInfo = mView.findViewById(R.id.markerInfo);
        searchButton = mView.findViewById(R.id.searchButton);
    }

    private void addActionButtons(View mView) {
        Button searchButton = mView.findViewById(R.id.searchButton);
        searchButton.setEnabled(false);

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDeerDialogFragment.this, numberTextField);
            SearchDeerDialogFragment.this.getDialog().cancel();
        });
    }

    private void addSearchText(View mView, MarkList markList) {
        numberTextField = mView.findViewById(R.id.editTextNumber);
        listener.openTheInputs(numberTextField);
        numberTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ImageView image = mView.findViewById(R.id.imageView);
                TextView txt = mView.findViewById(R.id.textView);
                TextView markerInfo = mView.findViewById(R.id.markerInfo);
                if(!charSequence.toString().equals("")) {
                    int deerNumber = Integer.parseInt(charSequence.toString());
                    if(unmarkedDeer.contains(deerNumber)) {
                        listenToMarkDeer(deerNumber);
                    } else if(markList.findMarkWithNumber(deerNumber) != null) {
                        listenToChangeDeer(markList, deerNumber);
                    } else {
                        image.setImageResource(R.drawable.revontulet);
                        txt.setText(deerNumber + " - 404: Vasaa ei löydy.");
                        markerInfo.setText("tämä vasa taitaa olla jossakin aivan muualla...");
                        searchButton.setEnabled(false);
                        searchButton.setText("Haku");
                    }
                } else {
                    searchButton.setEnabled(false);
                    searchButton.setText("Haku");
                    txt.setText("Etsi vasaa");
                    markerInfo.setText("");
                    image.setImageResource(R.drawable.vasa);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void listenToMarkDeer(int deerNumber) {
        image.setImageResource(R.drawable.tyhjatkorvat);
        txt.setText(deerNumber + ": merkkaamaton vasa");
        markerInfo.setText("");

        searchButton.setOnClickListener(v -> {
            listener.closeDialogAndStartMarkingDeer(
                    SearchDeerDialogFragment.this, numberTextField, deerNumber
            );
        });
        searchButton.setEnabled(true);
        searchButton.setText("Merkkaa");
    }

    private void listenToChangeDeer(MarkList markList, int deerNumber) {
        EarMarkImageSelector earMarkImageSelector = new EarMarkImageSelector(
                Arrays.asList(
                        R.drawable.poro, R.drawable.korvamerkki,
                        R.drawable.poronummella, R.drawable.luontoa,
                        R.drawable.hyttynen
                )
        );
        Mark mark = markList.findMarkWithNumber(deerNumber);
        image.setImageResource(earMarkImageSelector.getEarMarkImage(mark.getOwner()));
        txt.setText(deerNumber + ": " + mark.getOwner());
        markerInfo.setText("Merkkaaja: " + mark.getMarker() + " (" + mark.getTime() + ")");
        searchButton.setEnabled(true);
        searchButton.setText("Muokkaa");

        searchButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDeerDialogFragment.this, numberTextField);
            listener.changeDeer(
                    SearchDeerDialogFragment.this,
                    markList.findMarkWithNumber(deerNumber)
            );
        });
    }
}

