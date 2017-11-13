package com.hafidzquran.rumahquran.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.hafidzquran.rumahquran.R;

public class SplashScreenActivity extends Activity {
    protected static int SplashTime = 3000;
//    private AnimatedSvgView animatedSvgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*animatedSvgView = (AnimatedSvgView)findViewById(R.id.logoAnimasi);
        animatedSvgView.postDelayed(new Runnable() {
            @Override
            public void run() {
                animatedSvgView.start();
            }
        }, 500);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SplashTime);
    }
}