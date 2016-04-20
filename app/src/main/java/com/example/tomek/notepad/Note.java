package com.example.tomek.notepad;

import android.graphics.Bitmap;
import android.text.Spannable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that represents single Note
 */
public class Note {

    // KEY_ID of Note
    private int mId;

    // Spannable used to format text
    private Spannable mSpannable;

    // Raw text used to make titles
    private String rawText;

    //Painting
    private Bitmap mImage;

    //Dates
    private Date dateUpdated;

    //Formatter
    private static final DateFormat dt = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss");





    public Note(int id, Spannable spannable, Bitmap image, Date dateUpdated) {
        mId = id;
        mSpannable = spannable;
        mImage = image;
        rawText = mSpannable.toString();
        this.dateUpdated = dateUpdated;
    }

    public int getId() {
        return mId;
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
