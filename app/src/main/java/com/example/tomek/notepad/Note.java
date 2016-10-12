package com.example.tomek.notepad;

import android.graphics.Bitmap;
import android.text.Spannable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class that represents single Note
 */
public class Note {

    // KEY_ID of Note
    private final int mId;

    // Spannable used to format text
    private final Spannable mSpannable;

    //
    private final String mTitle;

    // Raw text used to make titles
    private final String rawText;

    //Painting
    private final Bitmap mImage;

    //Dates (Default value)
    private Date dateUpdated = new Date();

    //Formatter
    private static final DateFormat dt = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss", Locale.getDefault());





    public Note(int id, String title, Spannable spannable, Bitmap image, Date dateUpdated) {
        mId = id;
        mTitle = title;
        mSpannable = spannable;
        mImage = image;
        rawText = mSpannable.toString();
        this.dateUpdated = dateUpdated;
    }

    public int getId() {
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public Spannable getSpannable() {
        return mSpannable;
    }

    public String getRawText() {
        return rawText;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public String getFormattedDateUpdatted() {
        return dt.format(dateUpdated);
    }
}
