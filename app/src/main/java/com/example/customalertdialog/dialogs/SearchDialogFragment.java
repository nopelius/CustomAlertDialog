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
import android.view.inputmethod.InputMethodManager;
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
        void openTheInputs(DialogFragment dialog);
        void closeTheInputs(DialogFragment dialog, EditText text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement");
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
        searchButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDialogFragment.this, numberTextField);
            listener.markDeer(
                    SearchDialogFragment.this,
                    Integer.parseInt(numberTextField.getText().toString())
            );
        });
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
                if(!charSequence.toString().equals("")) {
                    int deerNumber = Integer.parseInt(charSequence.toString());
                    if(unmarkedDeer.contains(deerNumber)) {
                        image.setImageResource(R.drawable.tyhjatkorvat);
                        txt.setText("Merkkaamaton vasa");
                        searchButton.setEnabled(true);
                        searchButton.setText("Merkkaa");
                    } else if(markList.findMarkWithNumber(deerNumber) != null) {
                        String owner = markList.findMarkWithNumber(deerNumber).getOwner();
                        image.setImageResource(earMarkImageSelector.getEarMarkImage(owner));
                        txt.setText(deerNumber + ": " + owner);
                        searchButton.setEnabled(true);
                        searchButton.setText("Muokkaa");
                    } else {
                        image.setImageResource(R.drawable.revontulet);
                        txt.setText("Vasaa ei löydy.");
                        searchButton.setEnabled(false);
                        searchButton.setText("Haku");
                    }
                } else {
                    searchButton.setEnabled(false);
                    searchButton.setText("Haku");
                    txt.setText("Etsi vasaa");
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
}

