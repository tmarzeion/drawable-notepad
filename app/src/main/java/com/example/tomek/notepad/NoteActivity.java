package com.example.tomek.notepad;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    private static final double MENU_MARGIN_RELATIVE_MODIFIER = 0.3;
    private LinearLayout mSliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mSliderLayout = (LinearLayout) findViewById(R.id.sliderMenu);
        ViewGroup.LayoutParams params = mSliderLayout.getLayoutParams();
        params.height = calculateMenuMargin();

        /*
        // open keyboard as default
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        */

        // disable soft keyboard when editText is focused
        EditText editText = ((EditText) findViewById(R.id.editText));
        disableSoftInputFromAppearing(editText);


        //test
        manageContextMenuBar(editText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }


    public void toggleMenu(MenuItem item) {
        if (findViewById(R.id.sliderMenu).getVisibility() == View.VISIBLE) {
            findViewById(R.id.sliderMenu).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sliderMenu).setVisibility(View.VISIBLE);
        }
    }

    private int calculateMenuMargin() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return  ((int) Math.round(height * MENU_MARGIN_RELATIVE_MODIFIER));
    }

    private void refreshSizeOptionsUnderline() {
        TextView size10 = (TextView) findViewById(R.id.textViewSize10);
        TextView size12 = (TextView) findViewById(R.id.textViewSize12);
        TextView size14 = (TextView) findViewById(R.id.textViewSize14);
        TextView size18 = (TextView) findViewById(R.id.textViewSize18);
        TextView size24 = (TextView) findViewById(R.id.textViewSize24);
        TextView size36 = (TextView) findViewById(R.id.textViewSize36);

        size10.setPaintFlags(0);
        size12.setPaintFlags(0);
        size14.setPaintFlags(0);
        size18.setPaintFlags(0);
        size24.setPaintFlags(0);
        size36.setPaintFlags(0);
    }

    public void toggleKeyboard(MenuItem item) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    private static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    private void manageContextMenuBar(EditText editText) {

        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            public void onDestroyActionMode(ActionMode mode) {
                if (findViewById(R.id.sliderMenu).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.sliderMenu).setVisibility(View.GONE);
                }
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                if (findViewById(R.id.sliderMenu).getVisibility() == View.GONE) {
                    findViewById(R.id.sliderMenu).setVisibility(View.VISIBLE);
                }
                return true;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return true;
            }
        });
    }


    public void formatTextActionPerformed(View view) {

        EditText editText = ((EditText) findViewById(R.id.editText));
        Spannable spannable = editText.getText();

        int posStart = editText.getSelectionStart();
        int posEnd = editText.getSelectionEnd();

        if (view.getTag().toString().equals("bold")) {
            spannable.setSpan(new StyleSpan(Typeface.BOLD), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("italic")) {
            spannable.setSpan(new StyleSpan(Typeface.ITALIC), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("underline")) {
            spannable.setSpan(new UnderlineSpan(), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("strikethrough")) {
            spannable.setSpan(new StrikethroughSpan(), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("textBlack")) {
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("textRed")) {
            spannable.setSpan(new ForegroundColorSpan(Color.RED), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("textBlue")) {
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("textGreen")) {
            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("textYellow")) {
            spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("highlightWhite")) {
            spannable.setSpan(new BackgroundColorSpan(238238238), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("highlightRed")) {
            spannable.setSpan(new BackgroundColorSpan(Color.RED), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("highlightBlue")) {
            spannable.setSpan(new BackgroundColorSpan(Color.BLUE), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("highlightGreen")) {
            spannable.setSpan(new BackgroundColorSpan(Color.GREEN), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("highlightYellow")) {
            spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("10")) {
            spannable.setSpan(new AbsoluteSizeSpan(10, true), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("12")) {
            spannable.setSpan(new AbsoluteSizeSpan(12, true), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("14")) {
            spannable.setSpan(new AbsoluteSizeSpan(14, true), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("18")) {
            spannable.setSpan(new AbsoluteSizeSpan(18, true), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("24")) {
            spannable.setSpan(new AbsoluteSizeSpan(24, true), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (view.getTag().toString().equals("36")) {
            spannable.setSpan(new AbsoluteSizeSpan(36, true), posStart, posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }



        editText.setText(spannable);


    }
}
