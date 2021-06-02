package com.ab.hicarerun.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.ab.hicarerun.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class FragmentScheduleDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private FragmentScheduleDatePicker.onDatePickerListener mDatePickerListener=null;

    public FragmentScheduleDatePicker.onDatePickerListener getmDatePickerListener() {
        return mDatePickerListener;
    }

    void setmDatePickerListener(FragmentScheduleDatePicker.onDatePickerListener mDatePickerListener) {
        this.mDatePickerListener = mDatePickerListener;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c;
        int year = 0;
        int month = 0;
        int day = 0;


        c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Calendar mCMax  = Calendar.getInstance();
        mCMax.add(Calendar.DATE,+4);
        Calendar mCMin  =
                Calendar.getInstance();
        mCMin .add(Calendar.DATE,-30);


        DatePickerDialog dialogFrag = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        dialogFrag.getDatePicker().setMinDate(mCMin.getTime().getTime());
        dialogFrag.getDatePicker().setMaxDate(mCMax.getTime().getTime());
        // Create a new instance of DatePickerDialog and return it
        return dialogFrag ;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        if(mDatePickerListener!=null){
            mDatePickerListener.onDateSet(view,year,month,day);
        }
    }
    public interface onDatePickerListener {
        void onDateSet(DatePicker view, int year, int month, int day);
    }
}