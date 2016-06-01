package com.project_b.se2.mauerhuepfer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * Created by rohrbe on 29.05.16.
 */
public class OptionsActivity extends AppCompatActivity {
    private static String PREFS_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_options);

        PREFS_NAME = getString(R.string.memory);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        String playerName = settings.getString("playerName", null);
        String hostName = settings.getString("hostName", null);

        ((EditText) findViewById(R.id.text_player)).setText(playerName);
        ((EditText) findViewById(R.id.text_host)).setText(hostName);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save Changes
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("playerName", ((EditText) findViewById(R.id.text_player)).getText().toString());
        editor.putString("hostName", ((EditText) findViewById(R.id.text_host)).getText().toString());

        // Commit the edits!
        editor.commit();
    }
}