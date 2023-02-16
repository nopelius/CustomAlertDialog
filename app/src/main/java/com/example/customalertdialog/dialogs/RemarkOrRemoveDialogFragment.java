package com.example.customalertdialog.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;

public class RemarkOrRemoveDialogFragment extends DialogFragment {

    DialogListener listener;
    
    public static RemarkOrRemoveDialogFragment newInstance(int deerNumber) {
        
        Bundle args = new Bundle();
        
        RemarkOrRemoveDialogFragment fragment = new RemarkOrRemoveDialogFragment();
        args.putInt("deerNumber", deerNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public interface DialogListener {
        void showMarkDialog(String msg, int deerNumber);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RemarkOrRemoveDialogFragment.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.remark_or_remove_dialog, null);

        TextView txt = mView.findViewById(R.id.textView);
        txt.setText("Vasa: " + getArguments().getInt("deerNumber"));

        Button remarkButton = mView.findViewById(R.id.remarkButton);
        remarkButton.setOnClickListener(v -> {
            RemarkOrRemoveDialogFragment.this.getDialog().cancel();
            listener.showMarkDialog(
                    "Merkkaa uudelleen vasa: " + getArguments().getInt("deerNumber"),
                    getArguments().getInt("deerNumber")
            );
        });
        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> RemarkOrRemoveDialogFragment.this.getDialog().cancel());

        builder.setView(mView);
        return builder.create();
    }
}
