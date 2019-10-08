package com.example.mainuddin.myapplication34.ui.media;


import android.app.Notification;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;
import es.dmoral.toasty.Toasty;

import static kotlin.text.Typography.amp;

public class MediaActivity extends AppCompatActivity {

    public static Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    public static MediaPlayer mp = new MediaPlayer();
    public static  ImageView image;
    int totalTime;
    ImageView imageView;
    public static boolean nxt;
    int p;

    TextView textView;
    TextView textView1;
    String title;
    String artist;
    String album;
    String titleq;
    Bitmap bm;
    int play,pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_media);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        // Media Player

        Intent intent = getIntent();

        title = intent.getStringExtra("pos");
        artist = intent.getStringExtra("art");
        album = intent.getStringExtra("alb");


        textView = findViewById(R.id.song_title);
        textView1 = findViewById(R.id.song_artist);
        p  = intent.getIntExtra("p",0);


        if(nxt){
            nxt = false;
            Audio audio = getNest();
            title = audio.getTitle();
            titleq = audio.getImagepath();
        }

        textView.setText(title);
        textView1.setText(artist);

        titleq = intent.getStringExtra("title");
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
        ContentResolver res = this.getContentResolver();
        InputStream in;
        bm = null;
        try {
            in = res.openInputStream(uri);

            bm= BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            InputStream is = this.getResources().openRawResource(R.raw.image);
            bm = BitmapFactory.decodeStream(is);
        }


        Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // Use generated instance
                //work with the palette here
                int defaultValue = 0x000000;
                int vibrant = palette.getVibrantColor(defaultValue);
                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                int muted = palette.getMutedColor(defaultValue);
                int mutedLight = palette.getLightMutedColor(defaultValue);
                int mutedDark = palette.getDarkMutedColor(defaultValue);

                Drawable myIcon = getResources().getDrawable( R.drawable.ic_skip_previous_black_24dp );
                Drawable myIcon1 = getResources().getDrawable( R.drawable.ic_skip_next_black_24dp );
                Drawable myIcon2 = getResources().getDrawable( R.drawable.ic_play_arrow_black_24dp );
                Drawable myIcon3 = getResources().getDrawable( R.drawable.ic_pause_black_24dp );


                if(vibrant==mutedDark ){
                    mutedDark = palette.getDarkVibrantColor(vibrant);
                }

                play = (mutedDark);
                pause = (mutedDark);



                myIcon.setTint(mutedDark);
                myIcon1.setTint(mutedDark);
                myIcon2.setTint(mutedDark);
                myIcon3.setTint(mutedDark);


                textView.setTextColor(mutedDark);
                textView1.setTextColor(mutedDark);

                LinearLayout linearLayout = findViewById(R.id.medic_base);

                linearLayout.setBackgroundColor(vibrant);

            }
        });



        image=(ImageView)findViewById(R.id.albumart);
        image.setImageBitmap(bm);




        String[] abspath = getAudioPath(title);




        try {
            if(!mp.isPlaying()){






                mp.setDataSource(abspath[0]);
                mp.prepare();

            }else{

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        mp.setLooping(true);

        mp.seekTo(0);
        mp.start();
        playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        // Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();

        startService();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {


        if (!mp.isPlaying()) {
            // Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            Drawable myIcon3 = getResources().getDrawable( R.drawable.ic_pause_black_24dp );
            myIcon3.setTint(play);


        } else {
            // Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            Drawable myIcon2 = getResources().getDrawable( R.drawable.ic_play_arrow_black_24dp );
            myIcon2.setTint(pause);

        }

    }

    public void nxtBtnClick(View view) {

       // startService(view);


       // startService(view);
        if (mp.isPlaying()) {
            Audio audio = getNest();
            title = audio.getTitle();
            titleq = audio.getImagepath();

        } else {
            Audio audio = getNest();
            title = audio.getTitle();
            titleq = audio.getImagepath();
        }

        MediaActivity.mp.stop();
        MediaActivity.mp = new MediaPlayer();
        Intent myIntent = new Intent(view.getContext(), MediaActivity.class);
        //String s = view.findViewById(R.id.subtitle).toString();
        //String s = (String) parent.getI;
        if(p+1>=Media_list_activity.ListElementsArrayList.size()){
            p = -1;
        }
        myIntent.putExtra("p",(p+1));
        myIntent.putExtra("pos",Media_list_activity.ListElementsArrayList.get((p+1)).getTitle());
        myIntent.putExtra("art",Media_list_activity.ListElementsArrayList.get((p+1)).getArtist());
        myIntent.putExtra("alb",Media_list_activity.ListElementsArrayList.get((p+1)).getAlbum());
        myIntent.putExtra("title",Media_list_activity.ListElementsArrayList.get((p+1)).getImagepath());
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        System.out.println(title);
        System.out.println(titleq);

    }

    public void prevBtnClick(View view) {

       // startService(view);
        //startService(view);
        if (mp.isPlaying()) {
            Audio audio = getNest();
            title = audio.getTitle();
            titleq = audio.getImagepath();

        } else {
            Audio audio = getNest();
            title = audio.getTitle();
            titleq = audio.getImagepath();
        }

        MediaActivity.mp.stop();
        MediaActivity.mp = new MediaPlayer();
        Intent myIntent = new Intent(view.getContext(), MediaActivity.class);
        //String s = view.findViewById(R.id.subtitle).toString();
        //String s = (String) parent.getI;
        if(p-1<0){
            p = Media_list_activity.ListElementsArrayList.size();
        }
        myIntent.putExtra("p",(p-1));
        myIntent.putExtra("pos",Media_list_activity.ListElementsArrayList.get((p-1)).getTitle());
        myIntent.putExtra("art",Media_list_activity.ListElementsArrayList.get((p-1)).getArtist());
        myIntent.putExtra("alb",Media_list_activity.ListElementsArrayList.get((p-1)).getAlbum());
        myIntent.putExtra("title",Media_list_activity.ListElementsArrayList.get((p-1)).getImagepath());
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        System.out.println(title);
        System.out.println(titleq);

    }

    private String[] getAudioPath(String songTitle) {

        final Cursor mInternalCursor = getContentResolver().query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA },
                MediaStore.Audio.Media.TITLE+ "=?",
                new String[] {songTitle},
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        final Cursor mExternalCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA },
                MediaStore.Audio.Media.TITLE+ "=?",
                new String[] {songTitle},
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        Cursor[] cursors = {mInternalCursor, mExternalCursor};
        final MergeCursor mMergeCursor = new MergeCursor(cursors);

        int count = mMergeCursor.getCount();

        String[] songs = new String[count];
        String[] mAudioPath = new String[count];
        int i = 0;
        if (mMergeCursor.moveToFirst()) {
            do {
                songs[i] = mMergeCursor.getString(mMergeCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                mAudioPath[i] = mMergeCursor.getString(mMergeCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                i++;
            } while (mMergeCursor.moveToNext());
        }

        mMergeCursor.close();
        return mAudioPath;
    }
    public void startService() {
        Intent serviceIntent = new Intent(MediaActivity.this, NotificationService.class);
        serviceIntent.putExtra("p",(p));
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }


    public Audio getNest(){
        return Media_list_activity.ListElementsArrayList.get((p+1)%Media_list_activity.ListElementsArrayList.size());
    }
    public int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }


}