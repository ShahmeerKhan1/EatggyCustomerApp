package com.yummoidmkschinky.customerapp.utiles;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yummoidmkschinky.customerapp.R;

import java.util.Calendar;

public class MyDatePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePicker mDatePicker;

    public MyDatePickerDialog(@NonNull Context context) {
        super(context);
        init();
    }

    protected MyDatePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected MyDatePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lay_calander, null);
        mDatePicker = view.findViewById(R.id.datePicker);
        setView(view);
    }

    public void showDatePicker(DatePickerDialog.OnDateSetListener listener, Calendar defaultDate) {

        setButton(BUTTON_POSITIVE, "Done", this);

        mDateSetListener = listener;

        if (defaultDate == null) {
            defaultDate = Calendar.getInstance();

        }
        int year = defaultDate.get(Calendar.YEAR);
        int monthOfYear = defaultDate.get(Calendar.MONTH);
        int dayOfMonth = defaultDate.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);

        show();

    }


    @Override
    public void onClick(@NonNull DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:

                if (mDateSetListener != null) {
                    mDatePicker.clearFocus();
                    mDateSetListener.onDateSet(mDatePicker, mDatePicker.getYear(),
                            mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                }

                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + which);
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
    }
}