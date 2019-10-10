package com.example.mainuddin.myapplication34.ui.media;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.data.word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import es.dmoral.toasty.Toasty;

import static com.example.mainuddin.myapplication34.ui.media.MyNotificationReceiver.CANCEL_ACTION;

public class Media_list_activity extends AppCompatActivity {

    Context context;

    public static final int RUNTIME_PERMISSION_CODE = 7;


    ListView listView;

    public static List<Audio> ListElementsArrayList = new ArrayList<Audio>();
    ;

    Audiolist_adapter adapter;

    ContentResolver contentResolver;

    private Toast mToastToShow;
    Cursor cursor;

    Button sort;
    public static int position;
    Uri uri;
    boolean isUp;
    LinearLayout myView;
    public static Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    public static MediaPlayer mp = new MediaPlayer();
    public static ImageView image;
    int totalTime;

    int pro = 0;
    int pro1 = 0;

    TextView textView;
    TextView textView1;
    String title;
    String artist;
    String album;
    String titleq;
    Bitmap bm;
    int play, pause;
    Button showhide;
    boolean isClicked = false;
    Drawable myIcon;
    Drawable myIcon1;
    Drawable myIcon2;
    Drawable myIcon3;

    Toolbar toolbar;
    public static  Button pauseBtn, nxtBtn;
    FloatingActionButton fab;

    Intent serviceIntent ;
    Button button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        myView = findViewById(R.id.medic_base);
        myView.setVisibility(View.INVISIBLE);
        //showhide = myView.findViewById(R.id.music_hide_show);
        isUp = false;

        playBtn = (Button) myView.findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) myView.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) myView.findViewById(R.id.remainingTimeLabel);


        textView = myView.findViewById(R.id.song_title);
        textView1 = myView.findViewById(R.id.song_artist);

        fab = findViewById(R.id.fabup);

        pauseBtn = myView.findViewById(R.id.prevBtn);
        nxtBtn = myView.findViewById(R.id.nxtBtn);

        volumeBar = (SeekBar) myView.findViewById(R.id.volumeBar);


        context = getApplicationContext();


        AndroidRuntimePermission();
        serviceIntent =  new Intent(Media_list_activity.this, NotificationService.class);

        if(ListElementsArrayList.isEmpty())
            GetAllMediaMp3Files();
        for (int i = 0; i < ListElementsArrayList.size(); i++) {
            // System.out.println("list : "+ListElementsArrayList.get(i).getTitle());
        }



        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(ListElementsArrayList.get(position).getImagepath()));
        ContentResolver res = getContentResolver();
        InputStream in;
        Bitmap bitmap = null;
        try {
            in = res.openInputStream(uri);

            bitmap = BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            InputStream is = getResources().openRawResource(R.raw.image);
            bitmap = BitmapFactory.decodeStream(is);
        }



        adapter = new Audiolist_adapter(this);
        listView = (ListView) findViewById(R.id.listviews);
        listView.setAdapter(adapter);



        MyNotificationReceiver yourBR = null;
        yourBR = new MyNotificationReceiver();
        yourBR.setMainActivityHandler(this);
        IntentFilter callInterceptorIntentFilter = new IntentFilter("CANCEL_ACTION");
        registerReceiver(yourBR,  callInterceptorIntentFilter);


        IntentFilter callInterceptorIntentFilter1 = new IntentFilter("NEXT_ACTION");
        registerReceiver(yourBR,  callInterceptorIntentFilter1);


        button = toolbar.findViewById(R.id.sortsong);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showToast(view);
                //Toasty.info(Media_list_activity.this,"Total : "+ ListElementsArrayList.size(),Toasty.LENGTH_LONG);
                System.out.println(pro);
                if (isClicked || mp.isPlaying()) {
                    if (!isUp) {
                        slideUp(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        //Media_list_activity.position= position;


                        isClicked = true;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon2);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                            //volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();

                        }


                    } else {
                        slideDown(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                        isClicked = false;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon2);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                           // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();


                        }
                    }
                    isUp = !isUp;
                }else if(isClicked || !mp.isPlaying()){
                    if (!isUp) {
                        slideUp(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        //Media_list_activity.position= position;


                        isClicked = true;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon1);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                           // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();

                        }


                    } else {
                        slideDown(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                        isClicked = false;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon1);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                           // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();


                        }
                    }
                    isUp = !isUp;
                }else if(!isClicked || mp.isPlaying()){
                    if (!isUp) {
                        slideUp(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        //Media_list_activity.position= position;


                        isClicked = false;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon1);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                            // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();

                        }


                    } else {
                        slideDown(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                        isClicked = true;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon1);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                            // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();


                        }
                    }
                    isUp = !isUp;
                }else if(!isClicked || !mp.isPlaying()){
                    if (!isUp) {
                        slideUp(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        //Media_list_activity.position= position;


                        isClicked = false;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon1);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                            // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();

                        }


                    } else {
                        slideDown(myView);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                        isClicked = true;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);

                            titleq = ListElementsArrayList.get(position).getImagepath();
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                            ContentResolver res = getContentResolver();
                            InputStream in;
                            bm = null;
                            try {
                                in = res.openInputStream(uri);

                                bm = BitmapFactory.decodeStream(in);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                InputStream is = getResources().openRawResource(R.raw.image);
                                bm = BitmapFactory.decodeStream(is);
                            }


                            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Use generated instance
                                    //work with the palette here
                                    int defaultValue = 0x000000;
                                    int vibrant = getDominantColor(bm);
                                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                    int muted = palette.getMutedColor(defaultValue);
                                    int mutedLight = palette.getLightMutedColor(defaultValue);
                                    int mutedDark = getComplimentColor(vibrant);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = getWindow();
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(vibrant);
                                        setTitleColor(vibrant);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }


                                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);
                                    if(isClicked){

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    }else{

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        fab.setBackground(fabtint);
                                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        //isClicked = false;
                                    }
                                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                    //fab.setBackgroundColor(mutedDark);
                                    play = (mutedDark);
                                    pause = (mutedDark);

                                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                    myIcon3.setTint(play);
                                    myIcon.setTint(play);
                                    myIcon1.setTint(play);
                                    myIcon2.setTint(play);
                                    nxtBtn.setBackground(myIcon3);
                                    pauseBtn.setBackground(myIcon);
                                    playBtn.setBackground(myIcon1);


                                    //myIcon3.setTint(mutedDark);
                                    //myIcon2.setTint(mutedDark);


                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = (ImageView) myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
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

                            // volumeBar.setProgress(pro);
                            volumeBar.setOnSeekBarChangeListener(
                                    new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                            float volumeNum = progress / 100f;
                                            mp.setVolume(volumeNum, volumeNum);
                                            pro = progress;
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
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();


                        }
                    }
                    isUp = !isUp;
                }

            }
        });





        myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
        myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
        myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
        myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

        RelativeLayout relativeLayout = findViewById(R.id.content_music);
        LinearLayout linearLayout = relativeLayout.findViewById(R.id.listview);

        if (MainActivity.isDark && ListElementsArrayList.size() != 0) {


            relativeLayout.setBackgroundColor(Color.rgb(64, 64, 64));
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_viewdark));
            listView.setAdapter(adapter);
        } else if (!MainActivity.isDark && ListElementsArrayList.size() != 0) {

            relativeLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.listview_border));
            listView.setAdapter(adapter);

        } else if (MainActivity.isDark && ListElementsArrayList.size() == 0) {

            relativeLayout.setBackgroundColor(Color.rgb(64, 64, 64));
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_viewdark));

        } else if (!MainActivity.isDark && ListElementsArrayList.size() == 0) {

            relativeLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.listview_border));


        }


        // ListView on item selected listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                System.out.println(pro);

                //startService(view);
                // System.out.println("click");
                Media_list_activity.position = position;


                mp.stop();
                mp = new MediaPlayer();

                isClicked = true;


                if (!isUp) {


                    slideUp(myView);


                    // Media Player


                    title = ListElementsArrayList.get(position).getTitle();
                    artist = ListElementsArrayList.get(position).getArtist();
                    album = ListElementsArrayList.get(position).getAlbum();


                    textView.setText(title);
                    textView1.setText(artist);

                    titleq = ListElementsArrayList.get(position).getImagepath();
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                    Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                    ContentResolver res = getContentResolver();
                    InputStream in;
                    bm = null;
                    try {
                        in = res.openInputStream(uri);

                        bm = BitmapFactory.decodeStream(in);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        InputStream is = getResources().openRawResource(R.raw.image);
                        bm = BitmapFactory.decodeStream(is);
                    }


                    Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            //work with the palette here
                            int defaultValue = 0x000000;
                            int vibrant = getDominantColor(bm);
                            int vibrantLight = palette.getLightVibrantColor(defaultValue);
                            int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                            int muted = palette.getMutedColor(defaultValue);
                            int mutedLight = palette.getLightMutedColor(defaultValue);
                            int mutedDark = getComplimentColor(vibrant);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrant);
                                setTitleColor(vibrant);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                if (actionBarToolbar != null)
                                    actionBarToolbar.setTitleTextColor(mutedDark);
                            }


                            fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);
                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);
                            if(isClicked){

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                //isClicked = true;
                            }else{

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                //isClicked = false;
                            }

                            // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                            //fab.setBackgroundColor(mutedDark);
                            play = (mutedDark);
                            pause = (mutedDark);

                            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                            Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                            Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                            myIcon3.setTint(play);
                            myIcon.setTint(play);
                            myIcon1.setTint(play);
                            myIcon2.setTint(play);
                            nxtBtn.setBackground(myIcon3);
                            pauseBtn.setBackground(myIcon);
                            playBtn.setBackground(myIcon2);


                            //myIcon3.setTint(mutedDark);
                            //myIcon2.setTint(mutedDark);


                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);

                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                        }
                    });


                    image = (ImageView) myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);


                    String[] abspath = getAudioPath(title);

                    try {
                        if (!mp.isPlaying()) {
                            mp.setDataSource(abspath[0]);
                            mp.prepare();


                        } else {


                        }
                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }


                    //   if(! other.equals("yes") ) {
                    mp.setLooping(true);
                    mp.seekTo(0);

                    mp.start();


                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon2.setTint(play);
                    playBtn.setBackground(myIcon2);
                    //myIcon3.setTint(play);
                    // myIcon2.setTint(play);
                    mp.setVolume(0.5f, 0.5f);
                    totalTime = mp.getDuration();

                    // Position Bar
                    positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
                    positionBar.setMax(totalTime);
                    positionBar.setOnSeekBarChangeListener(
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    if (fromUser) {
                                        mp.seekTo(progress);
                                        positionBar.setProgress(progress);
                                        //pro = progress;
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
                  //  volumeBar.setProgress(pro);
                    volumeBar.setOnSeekBarChangeListener(
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                    float volumeNum = progress / 100f;
                                    mp.setVolume(volumeNum, volumeNum);
                                    pro = progress;
                                    System.out.println("PROGRESS"+pro);
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
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }).start();

                    startService();
                    //myButton.setText("Slide up");
                } else {
                    slideUp(myView);
                    // myButton.setText("Slide down");
                }
                isUp = !isUp;

                Toast.makeText(Media_list_activity.this, parent.getAdapter().getItem(position).toString(), Toast.LENGTH_LONG).show();

            }
        });

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // startService(view);

                System.out.println(pro);

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

                mp.stop();
                mp = new MediaPlayer();

                if (position + 1 >= Media_list_activity.ListElementsArrayList.size()) {
                    Media_list_activity.position = -1;
                }
                Media_list_activity.position++;
                {

                    // playBtn = (Button) findViewById(R.id.playBtn);
                    // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
                    //  remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

                    // Media Player


                    title = ListElementsArrayList.get(position).getTitle();
                    artist = ListElementsArrayList.get(position).getArtist();
                    album = ListElementsArrayList.get(position).getAlbum();


                    textView.setText(title);
                    textView1.setText(artist);

                    titleq = ListElementsArrayList.get(position).getImagepath();
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                    Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                    ContentResolver res = getContentResolver();
                    InputStream in;
                    bm = null;
                    try {
                        in = res.openInputStream(uri);

                        bm = BitmapFactory.decodeStream(in);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        InputStream is = getResources().openRawResource(R.raw.image);
                        bm = BitmapFactory.decodeStream(is);
                    }


                    Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            //work with the palette here
                            int defaultValue = 0x000000;
                            int vibrant = getDominantColor(bm);
                            int vibrantLight = palette.getLightVibrantColor(defaultValue);
                            int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                            int muted = palette.getMutedColor(defaultValue);
                            int mutedLight = palette.getLightMutedColor(defaultValue);
                            int mutedDark = getComplimentColor(vibrant);


                            if (vibrant == 0) {
                                mutedDark = palette.getDarkVibrantColor(vibrant);
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrant);
                                setTitleColor(vibrant);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                if (actionBarToolbar != null)
                                    actionBarToolbar.setTitleTextColor(mutedDark);
                            }

                            play = (mutedDark);
                            pause = (mutedDark);
                            // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));


                            fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);

                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);

                            if(isClicked){

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                //isClicked = true;
                            }else{

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                //isClicked = false;
                            }


                            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                            Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                            Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                            myIcon3.setTint(play);
                            myIcon.setTint(play);
                            myIcon1.setTint(play);
                            myIcon2.setTint(play);
                            nxtBtn.setBackground(myIcon3);
                            pauseBtn.setBackground(myIcon);
                            playBtn.setBackground(myIcon2);

                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);

                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                        }
                    });


                    image = (ImageView) myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);


                    String[] abspath = getAudioPath(title);

                    try {
                        if (!mp.isPlaying()) {
                            mp.setDataSource(abspath[0]);
                            mp.prepare();

                        } else {


                        }
                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }


                    //   if(! other.equals("yes") ) {
                    mp.setLooping(true);
                    mp.seekTo(0);

                    mp.start();
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon2.setTint(play);
                    playBtn.setBackground(myIcon2);
                    // myIcon3.setTint(play);
                    mp.setVolume(0.5f, 0.5f);
                    totalTime = mp.getDuration();

                    // Position Bar
                    positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
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
                    //volumeBar = (SeekBar) myView.findViewById(R.id.volumeBar);
                   // volumeBar.setProgress(pro);
                    volumeBar.setOnSeekBarChangeListener(
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    System.out.println("peogress"+pro);
                                    float volumeNum = progress / 100f;
                                    mp.setVolume(volumeNum, volumeNum);
                                    pro = progress;
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
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }).start();

                    startService();
                    //myButton.setText("Slide up");
                }


            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startService(view);
                //startService(view);


                System.out.println(pro);
                if (mp.isPlaying()) {
                    Audio audio = getNest();
                    title = audio.getTitle();
                    titleq = audio.getImagepath();

                } else {
                    Audio audio = getNest();
                    title = audio.getTitle();
                    titleq = audio.getImagepath();
                }

                mp.stop();
                mp = new MediaPlayer();

                if (position - 1 < 0) {
                    Media_list_activity.position = Media_list_activity.ListElementsArrayList.size();
                }
                Media_list_activity.position--;

                {


                    title = ListElementsArrayList.get(position).getTitle();
                    artist = ListElementsArrayList.get(position).getArtist();
                    album = ListElementsArrayList.get(position).getAlbum();


                    textView.setText(title);
                    textView1.setText(artist);

                    titleq = ListElementsArrayList.get(position).getImagepath();
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                    Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                    ContentResolver res = getContentResolver();
                    InputStream in;
                    bm = null;
                    try {
                        in = res.openInputStream(uri);

                        bm = BitmapFactory.decodeStream(in);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        InputStream is = getResources().openRawResource(R.raw.image);
                        bm = BitmapFactory.decodeStream(is);
                    }


                    Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            //work with the palette here
                            int defaultValue = 0x0000FF;
                            int vibrant = getDominantColor(bm);
                            int vibrantLight = palette.getLightVibrantColor(defaultValue);
                            int vibrantDark = palette.getDarkVibrantColor(vibrant);
                            int muted = palette.getMutedColor(defaultValue);
                            int mutedLight = palette.getLightMutedColor(defaultValue);
                            int mutedDark = getComplimentColor(vibrant);


                            if (vibrant == 0) {
                                mutedDark = palette.getDarkVibrantColor(vibrant);
                                // vibrant++;

                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrant);
                                setTitleColor(vibrant);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                if (actionBarToolbar != null)
                                    actionBarToolbar.setTitleTextColor(mutedDark);

                            }


                            play = (mutedDark);
                            pause = (mutedDark);

                            //fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));


                            fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);


                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);

                            if(isClicked){

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                //isClicked = true;
                            }else{

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                //isClicked = false;
                            }

                            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                            Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                            Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                            myIcon3.setTint(play);
                            myIcon.setTint(play);
                            myIcon1.setTint(play);
                            myIcon2.setTint(play);
                            nxtBtn.setBackground(myIcon3);
                            pauseBtn.setBackground(myIcon);
                            playBtn.setBackground(myIcon2);

                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);

                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                        }
                    });


                    image = (ImageView) myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);


                    String[] abspath = getAudioPath(title);

                    try {
                        if (!mp.isPlaying()) {
                            mp.setDataSource(abspath[0]);
                            mp.prepare();

                        } else {


                        }
                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }


                    //   if(! other.equals("yes") ) {
                    mp.setLooping(true);
                    mp.seekTo(0);

                    mp.start();
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon2.setTint(play);
                    playBtn.setBackground(myIcon2);
                    // playBtn.setBackgroundColor(play);
                    mp.setVolume(0.5f, 0.5f);
                    totalTime = mp.getDuration();

                    // Position Bar
                    positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
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
                    //volumeBar = (SeekBar) myView.findViewById(R.id.volumeBar);
                   // volumeBar.setProgress(pro);
                    volumeBar.setOnSeekBarChangeListener(
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    float volumeNum = progress / 100f;
                                    mp.setVolume(volumeNum, volumeNum);
                                    pro = progress;
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
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }).start();

                    startService();
                    //myButton.setText("Slide up");
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying()) {
                    // Stopping
                    mp.start();
                    playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon3.setTint(play);


                } else {
                    // Playing
                    mp.pause();
                    playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                    myIcon2.setTint(play);

                }
            }
        });




        sort = findViewById(R.id.sortsong);
        sort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(MainActivity.this, sort);
                //Inflating the Popup using xml file
                Context wrapper = new ContextThemeWrapper(Media_list_activity.this, R.style.YOURSTYLE1);
                if(MainActivity.isDark){
                    wrapper = new ContextThemeWrapper(Media_list_activity.this, R.style.YOURSTYLE);

                }else{
                    wrapper = new ContextThemeWrapper(Media_list_activity.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, sort);
                popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());



                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Ascending")){
                            Collections.sort(ListElementsArrayList);
                            //  adapter = new MyListAdapter(getParent());
                            listView=(ListView)findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                        }else if(item.getTitle().equals("Descending")){
                            Collections.sort(ListElementsArrayList,Collections.reverseOrder());
                            //adapter = new MyListAdapter(getParent());
                            listView=(ListView)findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                        }else if(item.getTitle().equals("Time Added")){
                            Collections.sort(ListElementsArrayList,
                                    new Comparator<Audio>()
                                    {
                                        public int compare(Audio f1, Audio f2)
                                        {
                                            return f1.getData().compareTo(f2.getData());
                                        }
                                    });
                            listView=(ListView)findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                        }else{

                        }

                        Toasty.info(Media_list_activity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        //registerReceiver(broadcastReceiver, new IntentFilter("Prev"));
    }


    public  void nxtsong(){

        // startService(view);

        System.out.println(pro);

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

        mp.stop();
        mp = new MediaPlayer();

        if (position + 1 >= Media_list_activity.ListElementsArrayList.size()) {
            Media_list_activity.position = -1;
        }
        Media_list_activity.position++;
        {

            // playBtn = (Button) findViewById(R.id.playBtn);
            // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
            //  remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

            // Media Player


            title = ListElementsArrayList.get(position).getTitle();
            artist = ListElementsArrayList.get(position).getArtist();
            album = ListElementsArrayList.get(position).getAlbum();


            textView.setText(title);
            textView1.setText(artist);

            titleq = ListElementsArrayList.get(position).getImagepath();
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
            ContentResolver res = getContentResolver();
            InputStream in;
            bm = null;
            try {
                in = res.openInputStream(uri);

                bm = BitmapFactory.decodeStream(in);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                InputStream is = getResources().openRawResource(R.raw.image);
                bm = BitmapFactory.decodeStream(is);
            }


            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    // Use generated instance
                    //work with the palette here
                    int defaultValue = 0x000000;
                    int vibrant = getDominantColor(bm);
                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                    int muted = palette.getMutedColor(defaultValue);
                    int mutedLight = palette.getLightMutedColor(defaultValue);
                    int mutedDark = getComplimentColor(vibrant);


                    if (vibrant == 0) {
                        mutedDark = palette.getDarkVibrantColor(vibrant);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(vibrant);
                        setTitleColor(vibrant);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                        if (actionBarToolbar != null)
                            actionBarToolbar.setTitleTextColor(mutedDark);
                    }

                    play = (mutedDark);
                    pause = (mutedDark);
                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));


                    fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                    toolbar.getNavigationIcon().setTint(mutedDark);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                    icon.setTint(mutedDark);
                    button.setBackground(icon);

                    if(isClicked){

                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        fabtint.setTint(getComplimentColor(mutedDark));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        //isClicked = true;
                    }else{

                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        fabtint.setTint(getComplimentColor(mutedDark));
                        fab.setBackground(fabtint);
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        //isClicked = false;
                    }


                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                    myIcon3.setTint(play);
                    myIcon.setTint(play);
                    myIcon1.setTint(play);
                    myIcon2.setTint(play);
                    nxtBtn.setBackground(myIcon3);
                    pauseBtn.setBackground(myIcon);
                    playBtn.setBackground(myIcon2);

                    textView.setTextColor(mutedDark);
                    textView1.setTextColor(mutedDark);

                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                    linearLayout.setBackgroundColor(vibrant);

                }
            });


            image = (ImageView) myView.findViewById(R.id.albumart);
            image.setImageBitmap(bm);


            String[] abspath = getAudioPath(title);

            try {
                if (!mp.isPlaying()) {
                    mp.setDataSource(abspath[0]);
                    mp.prepare();

                } else {


                }
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }


            //   if(! other.equals("yes") ) {
            mp.setLooping(true);
            mp.seekTo(0);

            mp.start();
            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
            myIcon2.setTint(play);
            playBtn.setBackground(myIcon2);
            // myIcon3.setTint(play);
            mp.setVolume(0.5f, 0.5f);
            totalTime = mp.getDuration();

            // Position Bar
            positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
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
            //volumeBar = (SeekBar) myView.findViewById(R.id.volumeBar);
            // volumeBar.setProgress(pro);
            volumeBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            System.out.println("peogress"+pro);
                            float volumeNum = progress / 100f;
                            mp.setVolume(volumeNum, volumeNum);
                            pro = progress;
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
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();

            startService();
            //myButton.setText("Slide up");
        }







    }

    public  void prevsong(){





                mp.stop();
                mp = new MediaPlayer();

                if (position - 1 < 0) {
                    position = ListElementsArrayList.size();
                }
                position--;

                {


                    title = ListElementsArrayList.get(position).getTitle();
                    artist = ListElementsArrayList.get(position).getArtist();
                    album = ListElementsArrayList.get(position).getAlbum();


                    textView.setText(title);
                    textView1.setText(artist);

                    titleq = ListElementsArrayList.get(position).getImagepath();
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                    Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                    ContentResolver res = getContentResolver();
                    InputStream in;
                    bm = null;
                    try {
                        in = res.openInputStream(uri);

                        bm = BitmapFactory.decodeStream(in);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        InputStream is = getResources().openRawResource(R.raw.image);
                        bm = BitmapFactory.decodeStream(is);
                    }


                    Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            // Use generated instance
                            //work with the palette here
                            int defaultValue = 0x0000FF;
                            int vibrant = getDominantColor(bm);
                            int vibrantLight = palette.getLightVibrantColor(defaultValue);
                            int vibrantDark = palette.getDarkVibrantColor(vibrant);
                            int muted = palette.getMutedColor(defaultValue);
                            int mutedLight = palette.getLightMutedColor(defaultValue);
                            int mutedDark = getComplimentColor(vibrant);


                            if (vibrant == 0) {
                                mutedDark = palette.getDarkVibrantColor(vibrant);
                                // vibrant++;

                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrant);
                                setTitleColor(vibrant);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                                if (actionBarToolbar != null)
                                    actionBarToolbar.setTitleTextColor(mutedDark);

                            }


                            play = (mutedDark);
                            pause = (mutedDark);

                            //fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));


                            fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);


                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);

                            if(isClicked){

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                //isClicked = true;
                            }else{

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));
                                fab.setBackground(fabtint);
                                fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                //isClicked = false;
                            }

                            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_skip_next_black_24dp);
                            Drawable myIcon = getResources().getDrawable(R.drawable.ic_skip_previous_black_24dp);
                            Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                            myIcon3.setTint(play);
                            myIcon.setTint(play);
                            myIcon1.setTint(play);
                            myIcon2.setTint(play);
                            nxtBtn.setBackground(myIcon3);
                            pauseBtn.setBackground(myIcon);
                            playBtn.setBackground(myIcon2);

                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);

                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                        }
                    });


                    image = (ImageView) myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);


                    String[] abspath = getAudioPath(title);

                    try {
                        if (!mp.isPlaying()) {
                            mp.setDataSource(abspath[0]);
                            mp.prepare();

                        } else {


                        }
                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }


                    //   if(! other.equals("yes") ) {
                    mp.setLooping(true);
                    mp.seekTo(0);

                    mp.start();
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon2.setTint(play);
                    playBtn.setBackground(myIcon2);
                    // playBtn.setBackgroundColor(play);
                    mp.setVolume(0.5f, 0.5f);
                    totalTime = mp.getDuration();

                    // Position Bar
                    positionBar = (SeekBar) myView.findViewById(R.id.positionBar);
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
                    //volumeBar = (SeekBar) myView.findViewById(R.id.volumeBar);
                    // volumeBar.setProgress(pro);
                    volumeBar.setOnSeekBarChangeListener(
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    float volumeNum = progress / 100f;
                                    mp.setVolume(volumeNum, volumeNum);
                                    pro = progress;
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
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }).start();

                    startService();
                    //myButton.setText("Slide up");
                }




    }
    //



    public void GetAllMediaMp3Files() {

        contentResolver = context.getContentResolver();


        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            Toast.makeText(Media_list_activity.this, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(Media_list_activity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);

        } else {

            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int date = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);


            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                Audio audio = new Audio();
                String SongTitle = cursor.getString(Title);
                String SongAlbum = cursor.getString(album);
                String SongArtist = cursor.getString(artist);
                String SongDate = cursor.getString(date);

                String albumid = cursor.getString(albumId);

                //path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                audio.setTitle(SongTitle);
                audio.setAlbum(SongAlbum);
                audio.setArtist(SongArtist);
                audio.setData(SongDate);
                audio.setImagepath(albumid);


                // Adding Media File Names to ListElementsArrayList.
                ListElementsArrayList.add(audio);

            } while (cursor.moveToNext());

            System.out.println(ListElementsArrayList.size());
            //System.out.println(Albumid.size());
        }
    }

    // Creating Runtime permission function.
    public void AndroidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(Media_list_activity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    Media_list_activity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                } else {

                    ActivityCompat.requestPermissions(
                            Media_list_activity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);

        listView.setEnabled(false);
        myView.setEnabled(true);
        volumeBar.setEnabled(true);
        playBtn.setEnabled(true);
        pauseBtn.setEnabled(true);
        nxtBtn.setEnabled(true);


    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        listView.setEnabled(true);
        myView.setEnabled(false);
        volumeBar.setEnabled(false);
        playBtn.setEnabled(false);
        pauseBtn.setEnabled(false);
        nxtBtn.setEnabled(false);

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

            String remainingTime = createTimeLabel(totalTime - currentPosition);
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



    private String[] getAudioPath(String songTitle) {

        final Cursor mInternalCursor = getContentResolver().query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.TITLE + "=?",
                new String[]{songTitle},
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        final Cursor mExternalCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.TITLE + "=?",
                new String[]{songTitle},
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
        serviceIntent.putExtra("p", (position));
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    public void stopService() {
        serviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(serviceIntent);
    }


    public Audio getNest() {
        return Media_list_activity.ListElementsArrayList.get((position + 1) % Media_list_activity.ListElementsArrayList.size());
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


    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }



}