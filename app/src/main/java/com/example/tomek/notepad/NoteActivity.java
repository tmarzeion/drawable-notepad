package com.example.tomek.notepad;

import android.content.Context;
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
}
