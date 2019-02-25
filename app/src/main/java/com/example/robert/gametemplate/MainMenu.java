package com.example.robert.gametemplate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenu extends AppCompatActivity {

    private View mContentView;


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnClickListener mPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newIntent = new Intent(getApplicationContext(), PlayGame.class);
            startActivity(newIntent);
        }
    };

    private final View.OnClickListener mHelpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newIntent = new Intent(getApplicationContext(), Help.class);
            startActivity(newIntent);
        }


    };

    private final View.OnClickListener mOptionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newIntent = new Intent(getApplicationContext(), Options.class);
            startActivity(newIntent);
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Set the button onClick listeners:
        findViewById(R.id.playButton).setOnClickListener(mPlayListener);
        findViewById(R.id.helpButton).setOnClickListener(mHelpListener);
        findViewById(R.id.optionsButton).setOnClickListener(mOptionsListener);
        findViewById(R.id.quitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to quit the app
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }


}
