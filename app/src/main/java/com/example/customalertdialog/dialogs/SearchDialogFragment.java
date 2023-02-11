package com.example.customalertdialog.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;
import com.example.customalertdialog.entities.MarkList;

import java.util.ArrayList;

public class SearchDialogFragment extends DialogFragment {

    DialogListener listener;
    MarkList markedDeer;
    ArrayList<Integer> unmarkedDeerNumbers;

    public interface DialogListener {
        void markDeer(DialogFragment dialog, int deerNumber);
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
        builder.setView(mView);

        //getArguments().getSerializable("markList");

        Button searchButton = mView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            listener.markDeer(SearchDialogFragment.this, 22);
        });
        return builder.create();
    }
}

