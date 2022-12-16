package com.example.myattendence2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class MyCalender extends DialogFragment {
    Calendar calendar = Calendar.getInstance();

    public interface ONCalenderOkClickListener{
        void onclick(int year,int month,int day);
    }
    
    public ONCalenderOkClickListener onCalenderOkClickListener;

    public void setOnCalenderOkClickListener(ONCalenderOkClickListener onCalenderOkClickListener) {
        this.onCalenderOkClickListener = onCalenderOkClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((view, year, month, dayOfMonth) -> {
            onCalenderOkClickListener.onclick(year,month,dayOfMonth);
        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    void setdate(int year,int month,int day){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
    }

    String getdate(){
        return android.text.format.DateFormat.format("dd.MM.yyyy", calendar).toString();
    }

}
