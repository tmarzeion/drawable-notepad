package com.example.tomek.notepad;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    private static final double MENU_MARGIN_RELATIVE_MODIFIER = 0.3;
    private LinearLayout mSliderLayout;
    private int sizeChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mSliderLayout = (LinearLayout) findViewById(R.id.sliderMenu);
        ViewGroup.LayoutParams params = mSliderLayout.getLayoutParams();
        params.height = calculateMenuMargin();

        //default size chosen:
        ((TextView) findViewById(R.id.textViewSize14)).setPaintFlags(((TextView) findViewById(R.id.textViewSize14))
                .getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sizeChosen = Integer.parseInt((findViewById(R.id.textViewSize14)).getTag().toString());
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

    public void textSizeChanged(View v) {
        TextView textView = ((TextView) v);

        refreshSizeOptionsUnderline();
        sizeChosen = Integer.parseInt(textView.getTag().toString());

        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
}
