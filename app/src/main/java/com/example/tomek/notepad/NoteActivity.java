package com.example.tomek.notepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Note Activity class that handles:
 * - New notes creation,
 * - Updating existing ones,
 * - Deleting existing ones if text is changed to blank
 * - Text formatting
 */
public class NoteActivity extends AppCompatActivity {

    //TODO Using @String res instead of hardcoded strings
    //TODO Fix keyboard disappearing when keyboard is shown in vertical mode and user change orientation to horizontal
    //TODO AlertDialog for note done button

    // Database Handler
    private DatabaseHandler dbHandler;

    // Percent of total layout height that is prepared for format text panel
    // Default 0.3, Values  0 < x < 1
    private static final double MENU_MARGIN_RELATIVE_MODIFIER = 0.3;

    // format text panel container
    private LinearLayout mSliderLayout;

    // Actual Note ID
    // (is -1 when it's new note)
    private int noteID;

    // EditText panel
    private EditText editText;

    // Spannable used to format text
    // Converted to HTML String in database
    private Spannable spannable;

    // Alert dialog for back button
    AlertDialog alertDialogBackToPrevScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Set Views fields values
        editText = ((EditText) findViewById(R.id.editText));
        mSliderLayout = (LinearLayout) findViewById(R.id.sliderMenu);

        // Get params for format text panel
        ViewGroup.LayoutParams params = mSliderLayout.getLayoutParams();
        params.height = calculateMenuMargin();

        // Create DatabaseHandler
        dbHandler = new DatabaseHandler(getApplicationContext());

        // Get default spannable value
        spannable = editText.getText();

        // Setup AlertDialog
        alertDialogBackToPrevScreen = initAlertDialogBackToPrevScreen();

        // get ID data from indent
        Intent intent = getIntent();
        noteID = Integer.parseInt(intent.getStringExtra("id"));

        // disable soft keyboard when editText is focused
        disableSoftInputFromAppearing(editText);

        // Auto-enable format menu panel when text is selected
        manageContextMenuBar(editText);

        // Auto-enable soft keyboard when activity starts
        toggleKeyboard(null);

        // disable keyboard suggestions
        // suggestions were causing formatting bugs by messing in spannable object
        disableKeyboardSuggestions(editText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creating menu
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    /**
     * Method used for first setup of back button AlertDialog
     */
    private AlertDialog initAlertDialogBackToPrevScreen() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Back to menu").setMessage("Quit without saving changes?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Runtime.getRuntime().gc();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Nothing happens here...
            }
        });
        return builder.create();
    }

    /**
     * Method that Overrides back button behavior
     * When back button is pressed it shows "back button" AlertDialog
     */
    @Override
    public void onBackPressed() {
        if (editText.getText().toString().length() != 0) {
            alertDialogBackToPrevScreen.show();
        }
        else {
            Intent intent = new Intent(NoteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            Runtime.getRuntime().gc();
        }
    }

    /**
     * Method used to toggle format text panel
     * @param item MenuItem that handles that method in .xml android:OnClick
     */
    public void toggleMenu(MenuItem item) {
        if (findViewById(R.id.sliderMenu).getVisibility() == View.VISIBLE) {
            findViewById(R.id.sliderMenu).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sliderMenu).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method that calculates space left for EditText when format text panel is Visible
     * @return Screen independent pixel count of space for EditText
     */
    private int calculateMenuMargin() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return  ((int) Math.round(height * MENU_MARGIN_RELATIVE_MODIFIER));
    }

    /**
     * Method used to toggle soft keyboard
     * @param item MenuItem that handles that method in .xml android:OnClick
     */
    public void toggleKeyboard(@Nullable MenuItem item) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Method used to hide keyboard
     */
    private void hideSoftKeyboard() {
        if (this.getCurrentFocus() != null) {
            try {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
            } catch (RuntimeException e) {
                //ignore
            }
        }
    }

    /**
     *  Disables soft keyboard text suggestions
     *  Solution is caused by formatting text bugs
     *  @param et EditText to disable suggestions for
     */
    private void disableKeyboardSuggestions(EditText et) {
        et.setInputType(et.getInputType()
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | EditorInfo.TYPE_TEXT_VARIATION_FILTER);
    }

    /**
     * Method that prevents soft keyboard appear when EditText is focused
     * @param editText EditText to apply changes to
     */
    private static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    /**
     * Method used to show format text panel when context menu is ON
     * i.e. When text is selected
     * @param editText EditText to apply changes to
     */
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

    /**
     * Method used to format selected text by modifying Spannable object
     * @param view that handles that method in .xml android:OnClick
     */
    public void formatTextActionPerformed(View view) {

        EditText editText = ((EditText) findViewById(R.id.editText));
        spannable = editText.getText();

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

    /**
     * Method used for Saving/Updating/Deleting note with special conditions
     * Handled by "Done button"
     */
    //TODO Disable keyboard if present
    public void saveOrUpdateNote(MenuItem menuItem) {

        spannable = editText.getText();

        if (editText.getText().length() != 0) {
            if (noteID == -1) {
                Note note = new Note (dbHandler.getNoteCount(), spannable);
                dbHandler.createNote(note);

                Toast.makeText(NoteActivity.this, "Note created",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Note note = new Note (noteID, spannable);
                dbHandler.updateNote(note);

                Toast.makeText(NoteActivity.this, "Note updated",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            if (noteID != -1) {

                Note note = new Note (noteID, spannable);
                dbHandler.deleteNote(note);

                Toast.makeText(NoteActivity.this, "Note is blank. Deleting note...",
                        Toast.LENGTH_SHORT).show();
            }
        }
        hideSoftKeyboard();
        Intent intent = new Intent(NoteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Runtime.getRuntime().gc();
    }
}
