package com.example.tomek.notepad;

import android.text.Html;
import android.text.Spannable;

/**
 * Created by tomek on 30.01.16.
 */
public class Note {

    private int mId;
    private Spannable mSpannable;
    private String rawText;
    private String title;

    public Note(int id, Spannable spannable) {
        mId = id;
        mSpannable = spannable;

        rawText = mSpannable.toString();

        title = createTitle(rawText);
    }

    private String createTitle(String rawText) {

        String result;

        if (rawText.length() > 25) {
            result = rawText.substring(0,25) + "...";
        }
        else {
            result = rawText;
        }
        return result;
    }

    public int getId() {
        return mId;
    }

    public Spannable getSpannable() {
        return mSpannable;
    }

    public String getTitle() {
        return title;
    }
}
