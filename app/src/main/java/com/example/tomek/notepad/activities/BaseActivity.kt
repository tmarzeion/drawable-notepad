package com.example.tomek.notepad.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Method used to hide keyboard
     */
    protected fun hideSoftKeyboard() {
        if (this.currentFocus != null) {
            try {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.currentFocus!!.applicationWindowToken, 0)
            } catch (e: RuntimeException) {
                //ignore
            }

        }
    }

}