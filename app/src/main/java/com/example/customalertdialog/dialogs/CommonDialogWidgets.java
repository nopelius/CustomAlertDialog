package com.example.customalertdialog.dialogs;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.FragmentActivity;

import com.example.customalertdialog.R;

public class CommonDialogWidgets {

    public static AutoCompleteTextView autoCompleteTextViewForOwners(
            View mView, FragmentActivity activity, String[] owners
    ) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity, android.R.layout.simple_dropdown_item_1line, owners
        );
        AutoCompleteTextView textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        return textView;
    }
}
