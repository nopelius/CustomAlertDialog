package com.example.customalertdialog.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;
import com.example.customalertdialog.helpers.EarMarkImageSelector;
import com.example.customalertdialog.helpers.MarkList;

import java.util.ArrayList;

public class OwnerInfoDialogFragment extends DialogFragment {

    DialogListener listener;
    EarMarkImageSelector earMarkImageSelector;
    View mView;

    public interface DialogListener {
        void changeDeer(DialogFragment fragment, int i);
    }

    public static OwnerInfoDialogFragment newInstance(String owner, MarkList markList) {
        OwnerInfoDialogFragment f = new OwnerInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("owner", owner);
        args.putSerializable("markList", markList);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OwnerInfoDialogFragment.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement: " + e);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = getLayoutInflater().inflate(R.layout.show_owner_dialog, null);
        earMarkImageSelector = new EarMarkImageSelector();
        String owner = getArguments().getString("owner");
        MarkList markList = (MarkList) getArguments().getSerializable("markList");

        setEarMarkImage(owner);
        TextView txt = mView.findViewById(R.id.textView);
        txt.setText(owner);
        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> OwnerInfoDialogFragment.this.getDialog().cancel());
        builder.setView(mView);

        TextView numberOfOwned = mView.findViewById(R.id.numberOfOwnedDeers);
        numberOfOwned.setText(markList.deerNumbersMarkedForOwner(owner).size() + " kpl");

        TextView numberOfMarked = mView.findViewById(R.id.numberOfMarkedDeers);
        numberOfMarked.setText(markList.deerNumbersMarkedForOwner(owner).size() + " kpl");

        addDeersToListView(markList.deerNumbersMarkedForOwner(owner), R.id.markedForTheOwner);
        addDeersToListView(markList.deerNumbersMarkedByUser(owner), R.id.ownerHasMarked);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setEarMarkImage(String owner) {
        ImageView image = mView.findViewById(R.id.imageView);
        image.setImageResource(earMarkImageSelector.getEarMarkImage(owner));
    }

    private void addDeersToListView(ArrayList<Integer> deerNumbers, int anId) {
        ListView listView = mView.findViewById(anId);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                getActivity(), R.layout.activity_list_view, R.id.textView, deerNumbers
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> listener.changeDeer(
                OwnerInfoDialogFragment.this, deerNumbers.get(i))
        );
        listView.setDividerHeight(1);
    }
}