package com.example.tomek.notepad;

import android.graphics.Bitmap;
import android.text.Spannable;

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


    public Note(int id, Spannable spannable, Bitmap image) {
        mId = id;
        mSpannable = spannable;
        mImage = image;
        rawText = mSpannable.toString();
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
}
