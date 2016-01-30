package com.example.tomek.notepad;

import android.text.Html;
import android.text.Spannable;

/**
 * Created by tomek on 30.01.16.
 */
public class Note {

    private int mId;
    private Spannable mSpannable;
    private String title;

    public Note(int id, Spannable spannable) {
        mId = id;
        mSpannable = spannable;

        String htmlSpan = Html.toHtml(spannable).toString();
        title = htmlSpan; //.substring(0,htmlSpan.length()-1);
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
