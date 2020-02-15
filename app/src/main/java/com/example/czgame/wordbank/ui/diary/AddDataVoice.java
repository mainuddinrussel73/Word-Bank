package com.example.czgame.wordbank.ui.diary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.visualizer.amplitude.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class AddDataVoice extends AppCompatActivity {

    private final double referenceAmplitude = 0.0001;
    private final int AUDIO_RECORDING_DELAY = 1000;
    //Create placeholder for user's consent to record_audio permission.
    //This will be used in handling callback
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    TextInputEditText subjectEt;
    TextInputLayout s;
    TextInputEditText descriptionEt;
    TextInputLayout d;
    AudioRecordView audioRecordView ;
    String pathname;
    SqliteDatabase mydb;
    File mAudioFile;
    boolean isRecording = false;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private static File createAudioFile(Context context, String audioName) throws IOException {
        File audio = null;
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/voice_logs");
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir();
        }
        if (isPresent) {
            audio = new File(Environment.getExternalStorageDirectory() + "/voice_logs/"+audioName+".mp3");
            
        } else {
            // Failure
        }


      
        return audio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_voice);

        subjectEt = findViewById(R.id.subjectEditTextId1);
        s = findViewById(R.id.subjectEditTextId);
        descriptionEt = findViewById(R.id.descriptionEditTextId1);
        d = findViewById(R.id.descriptionEditTextId);

        mydb = new SqliteDatabase(this);

        pathname = "";

        audioRecordView = findViewById(R.id.audioRecordView);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.dialogId);
        RelativeLayout lidtb = findViewById(R.id.two);

        RelativeLayout lay2 = findViewById(R.id.lay2);

        if(isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (isDark) {

            laybase.setBackgroundColor(Color.BLACK);
            subjectEt.setHintTextColor(Color.rgb(185, 185, 185));
            subjectEt.setTextColor(Color.WHITE);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));
            lay2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));


            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
        } else if (!isDark) {

            laybase.setBackgroundColor(Color.WHITE);
            subjectEt.setHintTextColor(Color.BLACK);
            subjectEt.setTextColor(Color.BLACK);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            lay2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));

            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));

        }

    }

    public void startRecording(View view) {
        requestAudioPermissions();
    }

    private void recordAudio() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {



                    if(mRecorder!=null){

                        final double maxAmplitude = mRecorder.getMaxAmplitude();
                       // final double amplitude = 20 * Math.log10(maxAmplitude / referenceAmplitude);

                        System.out.println((int)maxAmplitude*10);
                        audioRecordView.update((int) maxAmplitude*10);
                    }






                    handler.postDelayed(this, AUDIO_RECORDING_DELAY);
                }
            }, AUDIO_RECORDING_DELAY);


        }




        if (!isRecording) {
            try {
                mRecorder.prepare();
                mRecorder.start();
                isRecording = true;
            } catch (IOException e) {
                Log.e("Audio", "prepare() failed");
            }
        } else if (isRecording) {
            isRecording = false;
            stopRecording();
        }
    }

    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            long l = -1;

            Date date = new Date();
            String d = (String) android.text.format.DateFormat.format("dd/MM/yyyy  hh:mm: a",date);

            if(subjectEt.getText().length() == 0){
                Toast.makeText(getApplicationContext(),"You didn't add any subject",Toast.LENGTH_SHORT).show();
            }
            else{
                l = mydb.insertData(subjectEt.getText().toString(),"",
                        pathname,
                        d,1);
            }

            if(l>=0){
                Toasty.success(getApplicationContext(),"Data added",Toast.LENGTH_SHORT).show();
            }
            else {
                Toasty.success(getApplicationContext(), "Data not added", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(AddDataVoice.this,DiaryMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }

    public void startPlaying(View view) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mAudioFile.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("Audio", "prepare() failed");
        }
    }

    public void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    //Requesting run-time permissions

    @Override
    public void onDestroy() {

        if (mPlayer != null) {
            stopPlaying();
        }

        if (mRecorder != null) {
            stopRecording();
        }

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
            recordAudio();
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    recordAudio();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toasty.success(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void save(View view) {
        try {
            mAudioFile = createAudioFile(this, subjectEt.getText().toString().toLowerCase().replace(" ","_"));
            pathname = mAudioFile.getAbsolutePath();
            view.setActivated(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
