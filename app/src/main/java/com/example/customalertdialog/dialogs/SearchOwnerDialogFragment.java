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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;

public class SearchOwnerDialogFragment extends DialogFragment {

    DialogListener listener;
    EarMarkImageSelector earMarkImageSelector;

    public static SearchOwnerDialogFragment newInstance(String[] owners) {
        SearchOwnerDialogFragment f = new SearchOwnerDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray("owners", owners);
        f.setArguments(args);
        return f;
    }

    public interface DialogListener {
        void closeInputsAndShowOwnerDialog(DialogFragment dialog, EditText text);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.search_owner_dialog, null);
        earMarkImageSelector = new EarMarkImageSelector();
        setMarkingTextField(mView);
        addActionButtons(mView);
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

    private AutoCompleteTextView setMarkingTextField(View mView) {
        String[] owners = getArguments().getStringArray("owners");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_dropdown_item_1line, owners
        );

        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Button markButton = mView.findViewById(R.id.searchButton);
                markButton.setEnabled(charSequence.length() > 2);
                setEarMarkImage(charSequence.toString(), mView);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        listener.openTheInputs(SearchOwnerDialogFragment.this);
        return textView;
    }

    private void setEarMarkImage(String owner, View mView) {
        ImageView image = mView.findViewById(R.id.imageView);
        image.setImageResource(earMarkImageSelector.getEarMarkImage(owner));
    }

    private void addActionButtons(View mView) {
        Button searchButton = mView.findViewById(R.id.searchButton);
        searchButton.setEnabled(false);
        searchButton.setOnClickListener(v -> {
                listener.closeInputsAndShowOwnerDialog(
                        SearchOwnerDialogFragment.this,
                        mView.findViewById(R.id.autoCompleteTextView)
                );
                SearchOwnerDialogFragment.this.getDialog().cancel();
            }
        );

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            listener.closeTheInputs(
                    SearchOwnerDialogFragment.this,
                    mView.findViewById(R.id.autoCompleteTextView)
            );
            SearchOwnerDialogFragment.this.getDialog().cancel();
        });
    }
}

