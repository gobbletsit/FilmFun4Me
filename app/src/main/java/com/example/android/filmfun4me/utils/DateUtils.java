package com.example.android.filmfun4me.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String INPUT_DATE_FORMAT = "yyyy-mm-dd";
    private static final String OUTPUT_DATE_FORMAT = "dd, MMM yyyy";

    public static String formatDate(String date, String TAG) {

        SimpleDateFormat formatInput = new SimpleDateFormat(INPUT_DATE_FORMAT, java.util.Locale.getDefault());
        SimpleDateFormat formatOutput = new SimpleDateFormat(OUTPUT_DATE_FORMAT, java.util.Locale.getDefault());

        String formattedDate = "";
        try {
            Date newDate = formatInput.parse(date);
            formattedDate = formatOutput.format(newDate);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
        return formattedDate;
    }

}
