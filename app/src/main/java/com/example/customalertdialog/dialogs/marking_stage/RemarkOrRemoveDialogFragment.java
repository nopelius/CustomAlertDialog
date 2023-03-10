package com.example.customalertdialog.dialogs.marking_stage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;
import com.example.customalertdialog.helpers.EarMarkImageSelector;
import com.example.customalertdialog.helpers.Mark;

public class RemarkOrRemoveDialogFragment extends DialogFragment {

    DialogListener listener;
    
    public static RemarkOrRemoveDialogFragment newInstance(Mark mark) {
        
        Bundle args = new Bundle();
        
        RemarkOrRemoveDialogFragment fragment = new RemarkOrRemoveDialogFragment();
        args.putSerializable("mark", mark);
        fragment.setArguments(args);
        return fragment;
    }

    public interface DialogListener {
        void showMarkDialog(String msg, int deerNumber);
        void removeMark(Dialog dialog, int deerNumber);
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

        Mark mark = (Mark) getArguments().getSerializable("mark");

        EarMarkImageSelector earMarkImageSelector = new EarMarkImageSelector();
        ImageView image = mView.findViewById(R.id.imageView);
        image.setImageResource(earMarkImageSelector.getEarMarkImage(mark.getOwner()));

        TextView txt = mView.findViewById(R.id.textView);
        txt.setText("Vasa " + mark.getDeerNumber() + ": " + mark.getOwner());

        TextView markerTxt = mView.findViewById(R.id.markerInfo);
        markerTxt.setText("Merkkaaja: " + mark.getMarker() + " (" + mark.getTime() + ")");

        addActionButtons(mView, mark);

        builder.setView(mView);
        return builder.create();
    }

    private void addActionButtons(View mView, Mark mark) {
        Button remarkButton = mView.findViewById(R.id.remarkButton);
        remarkButton.setOnClickListener(v -> {
            RemarkOrRemoveDialogFragment.this.getDialog().cancel();
            listener.showMarkDialog(
                    "Merkkaa uudelleen vasa: " + mark.getDeerNumber(), mark.getDeerNumber()
            );
        });

        Button removeButton = mView.findViewById(R.id.removeMarkButton);
        removeButton.setOnClickListener(v -> listener.removeMark(
                RemarkOrRemoveDialogFragment.this.getDialog(),
                mark.getDeerNumber()
            )
        );

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> RemarkOrRemoveDialogFragment.this.getDialog().cancel());
    }
}
