package com.example.czgame.wordbank.ui.diary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.example.czgame.wordbank.ui.media.fragment_music_list.RUNTIME_PERMISSION_CODE;


public class UpdateDiary_voice  extends AppCompatActivity {
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    TextInputEditText subjectEt;
    TextInputLayout s;
    Button cancelBt,playBt,shareBtOnUpdate;
    SqliteDatabase dbUpdate;
    File mAudioFile;
    BarVisualizer mVisualizer;
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.update_daily_voice);

        //passing Update Activity's context to database alass
        dbUpdate = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();

        subjectEt = findViewById(R.id.subjectEditTextIdUpdate1);
        s = findViewById(R.id.subjectEditTextIdUpdate);


        playBt = findViewById(R.id.saveButtonIdPlay);
        cancelBt = findViewById(R.id.cacelButtonIdUpdate);

        shareBtOnUpdate = findViewById(R.id.PauseBtn);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("medialink");
        final String id = intent.getStringExtra("listId");



        mVisualizer = findViewById(R.id.blast);


        subjectEt.setText(sub);
        playBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPlayer==null) {
                    mPlayer = new MediaPlayer();

                    mPlayer = MediaPlayer.create(UpdateDiary_voice.this, Uri.parse(des));
                    //  mPlayer.setDataSource(mAudioFile.getAbsolutePath());
                    // mPlayer.prepare();
                    mPlayer.start();

                    requestAudioPermissions();

                    mVisualizer.show();
                }else{
                    mPlayer.start();
                }


            }
        });
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer != null) {
                    mPlayer.release();
                    mPlayer = null;
                }
            }
        });

        shareBtOnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer.isPlaying()){
                    mPlayer.pause();
                }
            }
        });


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.dialogId);
        RelativeLayout lidtb = findViewById(R.id.two);
        RelativeLayout lidtb1 = findViewById(R.id.lay2);


           // toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

        if (isDark) {

            laybase.setBackgroundColor(Color.BLACK);
            subjectEt.setHintTextColor(Color.rgb(185, 185, 185));
            subjectEt.setTextColor(Color.WHITE);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));



            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            lidtb1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
        } else if (!isDark) {

            laybase.setBackgroundColor(Color.WHITE);
            subjectEt.setHintTextColor(Color.BLACK);
            subjectEt.setTextColor(Color.BLACK);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));



            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            lidtb1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));

        }


    }


    public void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onDestroy() {

        if (mPlayer != null) {
            stopPlaying();
        }

        if (mVisualizer != null)
            mVisualizer.release();

        super.onDestroy();
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            int audioSessionId = mPlayer.getAudioSessionId();
            if (audioSessionId != -1){
                mVisualizer.setAudioSessionId(audioSessionId);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    int audioSessionId = mPlayer.getAudioSessionId();
                    if (audioSessionId != -1){
                        mVisualizer.setAudioSessionId(audioSessionId);
                    }
                } else {

                }
            }
        }
    }

}
