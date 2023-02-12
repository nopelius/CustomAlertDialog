package com.example.customalertdialog.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;
import com.example.customalertdialog.entities.MarkList;

import java.util.ArrayList;

public class SearchDialogFragment extends DialogFragment {

    DialogListener listener;
    MarkList markedDeer;
    ArrayList<Integer> unmarkedDeerNumbers;
    EditText numberTextField;

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
        View mView = getLayoutInflater().inflate(R.layout.search_dialog, null);

        numberTextField = mView.findViewById(R.id.editTextNumber);
        numberTextField.setFocusableInTouchMode(true);
        numberTextField.requestFocus();
        listener.openTheInputs(SearchDialogFragment.this);

        builder.setView(mView);

        //getArguments().getSerializable("markList");

        Button searchButton = mView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> listener.markDeer(
                SearchDialogFragment.this,
                Integer.parseInt(numberTextField.getText().toString())
        ));

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            listener.closeTheInputs(SearchDialogFragment.this, numberTextField);
            SearchDialogFragment.this.getDialog().cancel();
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

