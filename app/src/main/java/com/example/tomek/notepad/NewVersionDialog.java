package com.example.tomek.notepad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

public class NewVersionDialog extends Dialog {

    View okButton;
    CheckBox neverShowAgainCheckbox;
    View redirectToGooglePlayButton;

    public NewVersionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_version_dialog);
        okButton = findViewById(R.id.version_dialog_ok);
        neverShowAgainCheckbox = findViewById(R.id.version_dialog_dont_show_again);
        redirectToGooglePlayButton = findViewById(R.id.google_play_redirect_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        redirectToGooglePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = "com.tmarzeion.drawablenotepadflutter";
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    public boolean isDontShowAgainChecked() {
        return neverShowAgainCheckbox.isChecked();
    }

}