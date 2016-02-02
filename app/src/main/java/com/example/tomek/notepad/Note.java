package com.example.tomek.notepad;

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

    // Title for MainActivity ListView
    private String title;


    public Note(int id, Spannable spannable) {
        mId = id;
        mSpannable = spannable;
        rawText = mSpannable.toString();
        title = createTitle(rawText, 40);
    }

    /**
     * Method used to get title for MainActivity ListView
     * @param rawText String representation of text without format tags
     * @param howManyCharsToDisplay Maximum chars to display for title
     * @return Title with length < howManyCharsToDisplay
     */
    private String createTitle(String rawText, int howManyCharsToDisplay) {

        String result;

        if (rawText.length() > howManyCharsToDisplay) {
            result = rawText.substring(0, howManyCharsToDisplay) + "...";
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

    public String getRawText() {
        return rawText;
    }
}
