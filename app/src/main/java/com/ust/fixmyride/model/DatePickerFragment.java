package com.ust.fixmyride.model;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private int day;
    private int month;
    OnDateSetListener ondateSet;
    private int year;

    public void setCallBack(OnDateSetListener ondate) {
        this.ondateSet = ondate;
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.year = args.getInt("year");
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this.ondateSet, this.year, this.month, this.day);
    }
}
