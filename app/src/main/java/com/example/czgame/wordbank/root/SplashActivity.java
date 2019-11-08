package com.example.czgame.wordbank.root;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.czgame.wordbank.R;
import com.github.zeng1990java.widget.WaveProgressView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        WaveProgressView waveProgressView = findViewById(R.id.wave_progress_view);
        waveProgressView.setMax(100);
        animWave(waveProgressView, 5 * 1000);


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout relativeLayout = findViewById(R.id.splash);
        if (isDark) {
            relativeLayout.setBackgroundColor(Color.BLACK);
        } else {
            relativeLayout.setBackgroundColor(Color.WHITE);
        }


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, WalkThrough.class);
                startActivity(i);
                finish();
            }
        }, 2000);

    }

    private void animWave(WaveProgressView waveProgressView, long duration) {
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", 0, waveProgressView.getMax());
        progressAnim.setDuration(duration);
        progressAnim.setRepeatCount(ObjectAnimator.INFINITE);
        progressAnim.setRepeatMode(ObjectAnimator.RESTART);
        progressAnim.start();
    }
}