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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.customalertdialog.R;

public class MarkDialogFragment extends DialogFragment {

    DialogListener listener;
    EarMarkImageSelector earMarkImageSelector;

    public static MarkDialogFragment newInstance(
            String[] owners, String[] suggestedOwners, String theMessage, int deerNumber
    ) {
        MarkDialogFragment f = new MarkDialogFragment();

        Bundle args = new Bundle();
        args.putStringArray("owners", owners);
        args.putStringArray("suggestedOwners", suggestedOwners);
        args.putString("theMessage", theMessage);
        args.putInt("deerNumber", deerNumber);

        f.setArguments(args);

        return f;
    }

    public interface DialogListener {
        void markDeerToOwner(DialogFragment dialog, int deerNumber, String owner);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.mark_dialog, null);
        earMarkImageSelector = new EarMarkImageSelector();

        AutoCompleteTextView textView = setMarkingTextField(mView);
        setTitle(mView);
        setSuggestionButtons(mView, textView);
        createActionButtonsForMarkDeerDialog(
                mView, textView, getArguments().getInt("deerNumber")
        );
        ImageView image = mView.findViewById(R.id.imageView);
        image.setImageResource(earMarkImageSelector.randomPlaceholderImage());

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

    private void setTitle(View mView) {
        String alertMessage = getArguments().getString("theMessage");
        TextView title = mView.findViewById(R.id.textView);
        title.setText(alertMessage);
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
                Button markButton = mView.findViewById(R.id.markButton);
                markButton.setEnabled(charSequence.length() > 2);
                setEarMarkImage(charSequence.toString(), mView);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        listener.openTheInputs(MarkDialogFragment.this);
        return textView;
    }

    private void setSuggestionButtons(View mView, AutoCompleteTextView textView) {
        String[] suggestions = getArguments().getStringArray("suggestedOwners");
        int[] buttonIds = new int[]{R.id.suggestion1, R.id.suggestion2, R.id.suggestion3};
        for(int i=0; i<3; i++) {
            Button suggestion = mView.findViewById(buttonIds[i]);
            suggestion.setText(suggestions[i]);
            suggestion.setOnClickListener(v -> {
                textView.setFocusable(false);
                textView.setFocusableInTouchMode(false);
                textView.setText(suggestion.getText());
                textView.setFocusable(true);
                textView.setFocusableInTouchMode(true);
            });
        }
    }

    private void setEarMarkImage(String owner, View mView) {
        ImageView image = mView.findViewById(R.id.imageView);
        image.setImageResource(earMarkImageSelector.getEarMarkImage(owner));
    }

    private void createActionButtonsForMarkDeerDialog(View mView, EditText edit, int deerNumber) {
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        Button markButton = mView.findViewById(R.id.markButton);
        markButton.setOnClickListener(v-> {
            listener.closeTheInputs(MarkDialogFragment.this, textView);
            listener.markDeerToOwner(MarkDialogFragment.this, deerNumber, textView.getText().toString());
        });
        markButton.setEnabled(false);

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v-> {
            listener.closeTheInputs(MarkDialogFragment.this, edit);
            MarkDialogFragment.this.getDialog().cancel();
        });
    }
}


