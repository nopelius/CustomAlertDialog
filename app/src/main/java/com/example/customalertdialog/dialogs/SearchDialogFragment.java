package com.example.customalertdialog.dialogs;

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
import com.example.customalertdialog.entities.Mark;
import com.example.customalertdialog.entities.MarkList;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchDialogFragment extends DialogFragment {

    DialogListener listener;
    EditText numberTextField;
    ArrayList<Integer> unmarkedDeer;

    public static SearchDialogFragment newInstance(ArrayList<Integer> unmarkedDeer, MarkList markList) {
        SearchDialogFragment f = new SearchDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putIntegerArrayList("unmarkedDeer", unmarkedDeer);
        args.putSerializable("markList", (Serializable) markList.getMarks());
        f.setArguments(args);

        return f;
    }

    public interface DialogListener {
        void markDeer(DialogFragment dialog, int deerNumber);
        void changeDeer(DialogFragment dialog, int deerNumber);
        void openTheInputs(DialogFragment dialog);
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
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EarMarkImageSelector earMarkImageSelector = new EarMarkImageSelector();
        unmarkedDeer = getArguments().getIntegerArrayList("unmarkedDeer");
        ArrayList<Mark> marks = (ArrayList<Mark>) getArguments().getSerializable("markList");
        MarkList markList = new MarkList(marks);
        View mView = getLayoutInflater().inflate(R.layout.search_dialog, null);

        numberTextField = mView.findViewById(R.id.editTextNumber);
        numberTextField.setFocusableInTouchMode(true);
        numberTextField.requestFocus();
        listener.openTheInputs(SearchDialogFragment.this);

        builder.setView(mView);

        //getArguments().getSerializable("markList");
        Button searchButton = mView.findViewById(R.id.searchButton);
        searchButton.setEnabled(false);

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDialogFragment.this, numberTextField);
            SearchDialogFragment.this.getDialog().cancel();
        });

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
                        listenToMarkDeer(mView, deerNumber);
                    } else if(markList.findMarkWithNumber(deerNumber) != null) {
                        listenToChangeDeer(mView, markList, deerNumber);
                    } else {
                        image.setImageResource(R.drawable.revontulet);
                        txt.setText("Vasaa ei löydy.");
                        markerInfo.setText("");
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
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void listenToMarkDeer(View mView, int deerNumber) {
        ImageView image = mView.findViewById(R.id.imageView);
        TextView txt = mView.findViewById(R.id.textView);
        TextView markerInfo = mView.findViewById(R.id.markerInfo);
        Button searchButton = mView.findViewById(R.id.searchButton);

        image.setImageResource(R.drawable.tyhjatkorvat);
        txt.setText(deerNumber + ": merkkaamaton vasa");
        markerInfo.setText("");

        searchButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDialogFragment.this, numberTextField);
            listener.markDeer(
                    SearchDialogFragment.this,
                    deerNumber
            );
        });
        searchButton.setEnabled(true);
        searchButton.setText("Merkkaa");
    }

    private void listenToChangeDeer(View mView, MarkList markList, int deerNumber) {
        ImageView image = mView.findViewById(R.id.imageView);
        TextView txt = mView.findViewById(R.id.textView);
        TextView markerInfo = mView.findViewById(R.id.markerInfo);
        Button searchButton = mView.findViewById(R.id.searchButton);
        EarMarkImageSelector earMarkImageSelector = new EarMarkImageSelector();

        String owner = markList.findMarkWithNumber(deerNumber).getOwner();
        String marker = markList.findMarkWithNumber(deerNumber).getMarker();
        image.setImageResource(earMarkImageSelector.getEarMarkImage(owner));
        txt.setText(deerNumber + ": " + owner);
        markerInfo.setText("Merkkaaja: " + marker);
        searchButton.setEnabled(true);
        searchButton.setText("Muokkaa");

        searchButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDialogFragment.this, numberTextField);
            listener.changeDeer(
                    SearchDialogFragment.this,
                    Integer.parseInt(numberTextField.getText().toString())
            );
        });
    }
}

