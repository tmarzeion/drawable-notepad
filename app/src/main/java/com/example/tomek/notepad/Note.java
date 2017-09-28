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
    private int mId = -1;

    // Spannable used to format text
    private Spannable mSpannable;

    //
    private String mTitle;

    // Raw text used to make titles
    private String rawText;

    //Painting
    private Bitmap mImage;

    //Dates (Default value)
    private Date dateUpdated = new Date();

    //Formatter
    private static final DateFormat dt = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss", Locale.getDefault());

    public Note() {

    }

    public Note(int id, String title, Spannable spannable, Bitmap image, Date dateUpdated) {
        this.mId = id;
        this.mTitle = title;
        this.mSpannable = spannable;
        this.mImage = image;
        this.rawText = mSpannable.toString(); //TODO Remove this field and make this value accessible through utility method
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

    public void setId(int id) {
        this.mId = id;
    }

    public void setSpannable(Spannable spannable) {
        this.mSpannable = spannable;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
