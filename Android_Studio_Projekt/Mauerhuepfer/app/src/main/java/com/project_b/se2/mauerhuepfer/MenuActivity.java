package com.project_b.se2.mauerhuepfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MenuActivityClass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonLaunch).setOnClickListener(this);
        findViewById(R.id.buttonOption).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent myIntent;
        switch (v.getId()) {
            case R.id.buttonLaunch:
                myIntent = new Intent(MenuActivity.this, NetworkActivity.class);
                MenuActivity.this.startActivity(myIntent);
                break;
            case R.id.buttonOption:
                myIntent = new Intent(MenuActivity.this, OptionsActivity.class);
                MenuActivity.this.startActivity(myIntent);
                break;
            default:
                Log.e(TAG, "unknown Button");
        }
    }
}
