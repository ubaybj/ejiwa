package com.puskesmascilandak.e_jiwa.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.puskesmascilandak.e_jiwa.R;

import java.util.Calendar;

import static com.puskesmascilandak.e_jiwa.util.CalendarHelper.today;

public class DialogHelper {
    public static void showDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .show();
    }

    public static void showShortTimeToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLongTimeToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, String message, int length) {
        Toast.makeText(context, message, length).show();
    }

    public static void showDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener, Calendar defaultCalendar) {
        Calendar calendar = defaultCalendar;
        if (defaultCalendar == null) calendar = today();

        DatePickerDialog dialog = new DatePickerDialog(context, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
