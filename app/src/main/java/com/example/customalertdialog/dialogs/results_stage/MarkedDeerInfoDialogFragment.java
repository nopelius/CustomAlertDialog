package com.example.customalertdialog.dialogs.results_stage;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class MarkedDeerInfoDialogFragment extends DialogFragment {

    public static MarkedDeerInfoDialogFragment newInstance(Mark mark) {
        Bundle args = new Bundle();
        MarkedDeerInfoDialogFragment fragment = new MarkedDeerInfoDialogFragment();
        args.putSerializable("mark", mark);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.marked_deer_info_dialog, null);

        Mark mark = (Mark) getArguments().getSerializable("mark");

        EarMarkImageSelector earMarkImageSelector = new EarMarkImageSelector();
        ImageView image = mView.findViewById(R.id.imageView);
        image.setImageResource(earMarkImageSelector.getEarMarkImage(mark.getOwner()));

        TextView txt = mView.findViewById(R.id.textView);
        txt.setText("Vasa " + mark.getDeerNumber() + ": " + mark.getOwner());

        TextView markerTxt = mView.findViewById(R.id.markerInfo);
        markerTxt.setText("Merkkaaja: " + mark.getMarker() + " (" + mark.getTime() + ")");

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> MarkedDeerInfoDialogFragment.this.getDialog().cancel());

        builder.setView(mView);
        return builder.create();
    }
}
