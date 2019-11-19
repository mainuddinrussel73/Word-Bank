package com.example.czgame.wordbank.ui.media;


import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;
import com.github.siyamed.shapeimageview.RoundedImageView;

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

import static com.example.czgame.wordbank.ui.media.NotificationService.mOnAudioFocusChangeListener;

public class Media_list_activity extends AppCompatActivity  {

    public static final int RUNTIME_PERMISSION_CODE = 7;
    private static final String TAG = "Audio";
    public static List<Audio> ListElementsArrayList = new ArrayList<Audio>();
    public static int position;
    public static Button playBtn;
    public static MediaPlayer mp = new MediaPlayer();
    public static ImageView image;
    public static int totalTime;
    public static Button pauseBtn, nxtBtn;
    Context context;
    private ListView listView;
    private GridView gridView;
    Audiogrid_adapter adapterG;
    Audiolist_adapter adapter;
    ContentResolver contentResolver;
    Cursor cursor;
    Button sort;
    Uri uri;
    boolean isUp;
    LinearLayout myView;
    public static  Button nxt,pre,ply;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
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
    int mutedColor;
    boolean toogle1 = true;
    MediaController mediaController;
    Toolbar toolbar;
    public static  Intent serviceIntent;
    Button button, loop;
    private int currentViewMode = 0;
    private AudioManager mAudioManager;
    public boolean mFocusGranted;
    public boolean mFocusChanged;
    private AudioFocusChangeListenerImpl mAudioFocusChangeListener;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    public  static  WaveVisualizer mVisualizer;
    private Toast mToastToShow;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    RelativeLayout add_phone;
    TextView titleS,desS;
    RoundedImageView songI;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    private boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            //App is closed
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            } else {
                //App is closed
            }
        }
        return false;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {



            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            //
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);


            remainingTimeLabel.setText("- " + remainingTime);
            if (toogle1 == false && (remainingTime.equals("0:00"))) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                boolean requestGranted = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
                nxtsong();
            }
        }
    };
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            if(intent.getAction()==Constants.ACTION.NEXT_ACTION){
                nxtsong();
            }
            else if(intent.getAction()==Constants.ACTION.PREV_ACTION){
                prevsong();
            }

        }
    };

    public int getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }

        });
        return swatches.size() > 0 ? swatches.get(0).getRgb() : Color.WHITE;
    }

    private void switchView() {

        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            //Display listview
            listView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            //Hide listview
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if(VIEW_MODE_LISTVIEW == currentViewMode) {
            adapter = new Audiolist_adapter(this);
            listView.setAdapter(adapter);
        } else {
            adapterG = new Audiogrid_adapter(this);
            gridView.setAdapter(adapterG);
        }
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
            int audioSessionId = mp.getAudioSessionId();
            if (audioSessionId != -1){
                mVisualizer.setEnabled(false);
                mVisualizer.setAudioSessionId(audioSessionId);
            }
        }
    }

    @Override
    public  void onPause(){
        if(!mp.isPlaying()){
            int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            switch (result) {
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    mFocusGranted = true;
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    mFocusGranted = false;
                    break;
            }
        }

        String message = "Focus request " + (mFocusGranted ? "granted" : "failed");
        System.out.println(message);
        super.onPause();
    }

    @Override
    public  void onResume(){
        if(!mp.isPlaying()){
            int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            switch (result) {
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    mFocusGranted = true;
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    mFocusGranted = false;
                    break;
            }
        }

        String message = "Focus request " + (mFocusGranted ? "granted" : "failed");
        System.out.println(message);
        super.onResume();
    }
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);
        View view  = findViewById(R.id.content_music);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        RelativeLayout relativeLayout = findViewById(R.id.content_music);
        LinearLayout linearLayout = relativeLayout.findViewById(R.id.listview);
        listView = relativeLayout.findViewById(R.id.listviews);
        gridView = relativeLayout.findViewById(R.id.gridviews);


        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview

        if(currentViewMode==0){
           /// stubList.setVisibility(View.VISIBLE);
            switchView();
        }else{
          //  stubGrid.setVisibility(View.VISIBLE);
            switchView();
        }



        //barVisualizer = findViewById(R.id.visualizer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor("#3a9ebe"));
                    setTitleColor(Color.WHITE);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3a9ebe")));
                    Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                    if (actionBarToolbar != null)
                        actionBarToolbar.setTitleTextColor(Color.WHITE);
                }


                // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                toolbar.getNavigationIcon().setTint(Color.WHITE);

                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                icon.setTint(Color.WHITE);
                button.setBackground(icon);
                finish();
            }
        });





        myView = findViewById(R.id.medic_base);
        myView.setVisibility(View.INVISIBLE);
        //showhide = myView.findViewById(R.id.music_hide_show);
        isUp = false;

        playBtn = myView.findViewById(R.id.playBtn);
        elapsedTimeLabel = myView.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = myView.findViewById(R.id.remainingTimeLabel);


        textView = myView.findViewById(R.id.song_title);
        textView1 = myView.findViewById(R.id.song_artist);



        pauseBtn = myView.findViewById(R.id.prevBtn);
        nxtBtn = myView.findViewById(R.id.nxtBtn);


        volumeBar = myView.findViewById(R.id.volumeBar);


        context = getApplicationContext();


        AndroidRuntimePermission();
        serviceIntent = new Intent(Media_list_activity.this, NotificationService.class);

        if (ListElementsArrayList.isEmpty())
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


        Palette palette = Palette.from(bitmap).generate();
        int defaultValue = 0x0000FF;
        int vibrant = getDominantColor(bitmap);
        int muted = palette.getMutedColor(defaultValue);
        int mutedLight = getComplimentColor(palette.getDominantColor(palette.getDominantColor(defaultValue)));
        int mutedDark = getComplimentColor(vibrant);

        loop = findViewById(R.id.loops);
        SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        int type = prefs1.getInt("cond", 0);
        if (type == 0) {

        } else if (type == 1) {
            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
            myIcon3.setTint(mutedDark);
            loop.setBackground(myIcon3);
        } else if (type == 2) {
            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
            myIcon3.setTint(mutedDark);
            loop.setBackground(myIcon3);
        }



        //Register item lick


        add_phone = findViewById(R.id.relativeLayoutMain);
        if(!mp.isPlaying())
            add_phone.setVisibility(View.INVISIBLE);
        else
            add_phone.setVisibility(View.VISIBLE);

        titleS = add_phone.findViewById(R.id.textViewSongTitle);
        desS = add_phone.findViewById(R.id.textViewArtistName);
        songI = add_phone.findViewById(R.id.play_pause);
        ply = add_phone.findViewById(R.id.buttonPlayAndStop);
        nxt = add_phone.findViewById(R.id.buttonForward);
        pre = add_phone.findViewById(R.id.buttonBackward);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusChangeListener = new AudioFocusChangeListenerImpl();


        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.NEXT_ACTION));
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.PREV_ACTION));
        mVisualizer = findViewById(R.id.blast);

        //TODO: init MediaPlayer and play the audio

        //get the AudioSessionId from your MediaPlayer and pass it to the visualizer

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogle1 = !toogle1;
                if (toogle1 == true) {
                    if (mp.isPlaying()) {
                        mp.setLooping(true);
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                        myIcon3.setTint(mutedDark);
                        loop.setBackground(myIcon3);

                        SharedPreferences.Editor editor = prefs1.edit();
                        editor.putInt("cond", 2);
                        editor.commit();

                    }
                } else {
                    if (mp.isPlaying()) {
                        mp.setLooping(false);
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                        myIcon3.setTint(mutedDark);
                        loop.setBackground(myIcon3);

                        SharedPreferences.Editor editor = prefs1.edit();
                        editor.putInt("cond", 1);
                        editor.commit();

                    }
                }
            }
        });




        button = toolbar.findViewById(R.id.sortsong);


        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        slideDown(myView);
                        slideUp1(add_phone);
                        isUp = !isUp;
                        break;
                    case MotionEvent.ACTION_UP:
                        slideUp(myView);
                        slideDown1(add_phone);
                        isUp = !isUp;
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
        add_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(pro);
                if (isClicked || mp.isPlaying()) {
                    if (!isUp) {
                        slideUp(myView);
                        slideDown1(add_phone);
                        isClicked = true;

                        {


                            title = ListElementsArrayList.get(position).getTitle();
                            artist = ListElementsArrayList.get(position).getArtist();
                            album = ListElementsArrayList.get(position).getAlbum();


                            textView.setText(title);
                            textView1.setText(artist);
                            titleS.setText(title);
                            desS.setText(artist);


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
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }

                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

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

                                    textView.setTextColor(mutedDark);
                                    textView1.setTextColor(mutedDark);

                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);


                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);

                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());

                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);

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


                    }
                    else {
                        slideDown(myView);
                        slideUp1(add_phone);
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
                                        window.setStatusBarColor(Color.parseColor("#3a9ebe"));
                                        setTitleColor(Color.WHITE);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3a9ebe")));
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(Color.WHITE);
                                    }

                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);

                                   // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(Color.WHITE);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(Color.WHITE);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

                                    if (isClicked) {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        //fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    } else {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        //fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
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

                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);


                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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
                else if (isClicked || !mp.isPlaying()) {
                    if (!isUp) {
                        slideUp(myView);
                        slideDown1(add_phone);
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
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }

                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);

                                   // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }
                                    if (isClicked) {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                       // fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    } else {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        //fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
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
                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);

                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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
                    else {
                        slideDown(myView);
                        slideUp1(add_phone);
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
                                        window.setStatusBarColor(Color.parseColor("#3a9ebe"));
                                        setTitleColor(Color.WHITE);
                                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3a9ebe")));
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(Color.WHITE);
                                    }

                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);

                                   // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(Color.WHITE);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(Color.WHITE);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

                                    if (isClicked) {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                       // fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    } else {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        //fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
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
                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);

                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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
                else if (!isClicked || mp.isPlaying()) {
                    if (!isUp) {
                        slideUp(myView);
                        slideDown1(add_phone);
                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        fabtint.setTint(getComplimentColor(play));
                       // fab.setBackground(fabtint);
                        //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
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
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }
                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);

                                    //fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

                                    if (isClicked) {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                       // fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    } else {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                        //fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
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
                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);

                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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
                    else {
                        slideDown(myView);
                        slideUp1(add_phone);
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
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }
                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);

                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

                                    if (isClicked) {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                       // fab.setBackground(fabtint);
                                        //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                        //isClicked = true;
                                    } else {

                                        Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        fabtint.setTint(getComplimentColor(mutedDark));
                                      //  fab.setBackground(fabtint);
                                       // fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
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
                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);

                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);

                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));

                            //  positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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
                else if (!isClicked || !mp.isPlaying()) {
                    if (!isUp) {
                        slideUp(myView);
                        slideDown1(add_phone);
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
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }
                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

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
                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);

                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);
                            //myIcon3.setTint(play);
                            // myIcon2.setTint(play);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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
                    else {
                        slideDown(myView);
                        slideUp1(add_phone);


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
                                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                        if (actionBarToolbar != null)
                                            actionBarToolbar.setTitleTextColor(mutedDark);
                                    }
                                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                    remainingTimeLabel.setTextColor(mutedDark);
                                    elapsedTimeLabel.setTextColor(mutedDark);
                                    toolbar.getNavigationIcon().setTint(mutedDark);

                                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                    icon.setTint(mutedDark);
                                    button.setBackground(icon);

                                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                                    int type = prefs1.getInt("cond", 0);
                                    if (type == 0) {

                                    } else if (type == 1) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    } else if (type == 2) {
                                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                        myIcon3.setTint(mutedDark);
                                        loop.setBackground(myIcon3);
                                    }

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
                                    titleS.setText(title);
                                    desS.setText(artist);
                                    titleS.setTextColor(mutedDark);
                                    desS.setTextColor(mutedDark);
                                    add_phone.setBackgroundColor(vibrant);
                                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                                    linearLayout.setBackgroundColor(vibrant);

                                }
                            });


                            image = myView.findViewById(R.id.albumart);
                            image.setImageBitmap(bm);
                            songI.setImageBitmap(bm);

                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            myIcon2.setTint(play);
                            playBtn.setBackground(myIcon2);


                            // Position Bar
                            totalTime = mp.getDuration();
                            positionBar = myView.findViewById(R.id.positionBar);
                            int vibrant = getDominantColor(bm);
                            int mutedDark = getComplimentColor(vibrant);
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setMax(totalTime);
                            positionBar.setProgress(mp.getCurrentPosition());
                            remainingTimeLabel.setTextColor(mutedColor);
                            elapsedTimeLabel.setTextColor(mutedColor);
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



            SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);
            if (isDark && ListElementsArrayList.size() != 0) {

                relativeLayout.setBackgroundColor(Color.BLACK);
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_viewdark));
                listView.setAdapter(adapter);
                listView.setBackgroundColor(Color.BLACK);
                gridView.setAdapter(adapterG);
                gridView.setBackgroundColor(Color.BLACK);
            } else if (!isDark && ListElementsArrayList.size() != 0) {

                relativeLayout.setBackgroundColor(Color.WHITE);
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.listview_border));
                listView.setBackgroundColor(Color.WHITE);
                gridView.setBackgroundColor(Color.WHITE);
                listView.setAdapter(adapter);
                gridView.setAdapter(adapterG);

            } else if (isDark && ListElementsArrayList.size() == 0) {

                listView.setBackgroundColor(Color.BLACK);
                gridView.setBackgroundColor(Color.BLACK);
                relativeLayout.setBackgroundColor(Color.BLACK);
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_viewdark));

            } else if (!isDark && ListElementsArrayList.size() == 0) {


                listView.setBackgroundColor(Color.WHITE);
                gridView.setBackgroundColor(Color.WHITE);
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
                    slideDown1(add_phone);


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

                            mutedColor = mutedDark;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrant);
                                setTitleColor(vibrant);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                if (actionBarToolbar != null)
                                    actionBarToolbar.setTitleTextColor(mutedDark);
                            }
                            volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            remainingTimeLabel.setTextColor(mutedDark);
                            elapsedTimeLabel.setTextColor(mutedDark);

                           // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);
                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);

                            SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                            int type = prefs1.getInt("cond", 0);
                            if (type == 0) {

                            } else if (type == 1) {
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            } else if (type == 2) {
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            }

                            if (isClicked) {

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));

                                //isClicked = true;
                            } else {

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                fabtint.setTint(getComplimentColor(mutedDark));


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
                            ply.setBackground(myIcon2);
                            pre.setBackground(myIcon);
                            nxt.setBackground(myIcon3);


                            //myIcon3.setTint(mutedDark);
                            //myIcon2.setTint(mutedDark);

                            //barVisualizer.setColor(ContextCompat.getColor(Media_list_activity.this,vibrant));

                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);
                            titleS.setText(title);
                            desS.setText(artist);
                            titleS.setTextColor(mutedDark);
                            desS.setTextColor(mutedDark);
                            add_phone.setBackgroundColor(vibrant);

                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                        }
                    });


                    image = myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);
                    songI.setImageBitmap(bm);

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
                    mp.setLooping(false);
                    mp.seekTo(0);

                    mp.start();

                    // barVisualizer.setDensity(70);
                   // barVisualizer.setPlayer(mp.getAudioSessionId());


                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon2.setTint(play);
                    playBtn.setBackground(myIcon2);
                    //myIcon3.setTint(play);
                    // myIcon2.setTint(play);
                    mp.setVolume(2.5f, 2.5f);
                    totalTime = mp.getDuration();

                    requestAudioPermissions();

                    // Position Bar
                    positionBar = myView.findViewById(R.id.positionBar);
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
                                    System.out.println("PROGRESS" + pro);
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    slideDown1(add_phone);

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

                            mutedColor = mutedDark;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrant);
                                setTitleColor(vibrant);
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                                if (actionBarToolbar != null)
                                    actionBarToolbar.setTitleTextColor(mutedDark);
                            }
                            volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            remainingTimeLabel.setTextColor(mutedDark);
                            elapsedTimeLabel.setTextColor(mutedDark);

                           // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);
                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);

                            SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                            int type = prefs1.getInt("cond", 0);
                            if (type == 0) {

                            } else if (type == 1) {
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            } else if (type == 2) {
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
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
                            ply.setBackground(myIcon2);
                            pre.setBackground(myIcon);
                            nxt.setBackground(myIcon3);

                            //myIcon3.setTint(mutedDark);
                            //myIcon2.setTint(mutedDark);


                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);
                            titleS.setText(title);
                            desS.setText(artist);
                            titleS.setTextColor(mutedDark);
                            desS.setTextColor(mutedDark);
                            add_phone.setBackgroundColor(vibrant);
                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                        }
                    });


                    image = myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);
                    songI.setImageBitmap(bm);


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
                    mp.setLooping(false);
                    mp.seekTo(0);

                    mp.start();


                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon2.setTint(play);
                    playBtn.setBackground(myIcon2);
                    //myIcon3.setTint(play);
                    // myIcon2.setTint(play);
                    mp.setVolume(2.5f, 2.5f);
                    totalTime = mp.getDuration();
                    requestAudioPermissions();
                    // Position Bar
                    positionBar = myView.findViewById(R.id.positionBar);
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
                                    System.out.println("PROGRESS" + pro);
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


        nxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                nxtsong();

            }
        });
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nxtsong();
                    //myButton.setText("Slide up");



            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevsong();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startService(view);
                //startService(view);


              prevsong();
                    //myButton.setText("Slide up");

            }
        });

        ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
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
                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                boolean isDark = prefs.getBoolean("isDark", false);
                if (isDark) {
                    wrapper = new ContextThemeWrapper(Media_list_activity.this, R.style.YOURSTYLE);

                } else {
                    wrapper = new ContextThemeWrapper(Media_list_activity.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, sort);
                popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Ascending")) {
                            Collections.sort(ListElementsArrayList);
                            //  adapter = new MyListAdapter(getParent());
                            listView = findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                            gridView = findViewById(R.id.gridviews);
                            gridView.setAdapter(adapterG);
                        } else if (item.getTitle().equals("Descending")) {
                            Collections.sort(ListElementsArrayList, Collections.reverseOrder());
                            //adapter = new MyListAdapter(getParent());
                            listView = findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                            gridView = findViewById(R.id.gridviews);
                            gridView.setAdapter(adapterG);
                        } else if (item.getTitle().equals("Time Added Asc")) {
                            Collections.sort(ListElementsArrayList,
                                    new Comparator<Audio>() {
                                        public int compare(Audio f1, Audio f2) {
                                            return f1.getData().compareTo(f2.getData());
                                        }
                                    });
                            listView = findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                            gridView = findViewById(R.id.gridviews);
                            gridView.setAdapter(adapterG);
                        }else if (item.getTitle().equals("Time Added Des")) {
                            Collections.sort(ListElementsArrayList,
                                    new Comparator<Audio>() {
                                        public int compare(Audio f1, Audio f2) {
                                            return f2.getData().compareTo(f1.getData());
                                        }
                                    });

                            listView = findViewById(R.id.listviews);
                            listView.setAdapter(adapter);
                            gridView = findViewById(R.id.gridviews);
                            gridView.setAdapter(adapterG);
                        } else  if(item.getTitle().equals("View Switch")){
                            if(VIEW_MODE_LISTVIEW == currentViewMode) {
                                currentViewMode = VIEW_MODE_GRIDVIEW;
                            } else {
                                currentViewMode = VIEW_MODE_LISTVIEW;
                            }
                            //Switch view
                            switchView();
                            //Save view mode in share reference
                            SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("currentViewMode", currentViewMode);
                            editor.commit();
                        }

                        Toasty.info(Media_list_activity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        //registerReceiver(broadcastReceiver, new IntentFilter("Prev"));
    }
    public void play(){

            if (!mp.isPlaying()) {
                // Stopping
                mp.start();
                playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                ply.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                myIcon3.setTint(play);
                Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                ply.setBackground(myIcon33);

                NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView1);
                // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());


            } else{
                // Playing
                mp.pause();

                playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                myIcon2.setTint(play);
                Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                ply.setBackground(myIcon33);

                NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView1);

                // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());

            }

    }
    public void pause(){

        if (mp.isPlaying()) {
            // Stopping
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
            myIcon2.setTint(play);


        } else{
            // Playing
           // mp.pause();



        }

    }

    public void nxtsong() {

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

            titleS.setText(title);
            desS.setText(artist);

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
                    mutedColor = mutedDark;


                    if (vibrant == 0) {
                        mutedDark = palette.getDarkVibrantColor(vibrant);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(vibrant);
                        setTitleColor(vibrant);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                        if (actionBarToolbar != null)
                            actionBarToolbar.setTitleTextColor(mutedDark);
                    }

                    play = (mutedDark);
                    pause = (mutedDark);
                    // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));

                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    remainingTimeLabel.setTextColor(mutedColor);
                    elapsedTimeLabel.setTextColor(mutedColor);

                  //  fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                    toolbar.getNavigationIcon().setTint(mutedDark);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                    icon.setTint(mutedDark);
                    button.setBackground(icon);


                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                    int type = prefs1.getInt("cond", 0);
                    if (type == 0) {

                    } else if (type == 1) {
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                        myIcon3.setTint(mutedDark);
                        loop.setBackground(myIcon3);
                    } else if (type == 2) {
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                        myIcon3.setTint(mutedDark);
                        loop.setBackground(myIcon3);
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

                    ply.setBackground(myIcon2);
                    pre.setBackground(myIcon);
                    nxt.setBackground(myIcon3);
                    textView.setTextColor(mutedDark);
                    textView1.setTextColor(mutedDark);
                    titleS.setTextColor(mutedColor);
                    desS.setTextColor(mutedColor);

                    add_phone.setBackgroundColor(vibrant);
                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                    linearLayout.setBackgroundColor(vibrant);

                }
            });


            image = myView.findViewById(R.id.albumart);
            image.setImageBitmap(bm);
            songI.setImageBitmap(bm);

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
            mp.setLooping(false);
            mp.seekTo(0);

            mp.start();
            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
            myIcon2.setTint(play);
            playBtn.setBackground(myIcon2);
            // myIcon3.setTint(play);
            mp.setVolume(2.5f, 2.5f);
            totalTime = mp.getDuration();
            requestAudioPermissions();
            // Position Bar
            positionBar = myView.findViewById(R.id.positionBar);

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
                            System.out.println("peogress" + pro);
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

    public void prevsong() {
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

            titleS.setText(title);
            desS.setText(artist);

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

                    mutedColor = mutedDark;

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
                        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
                        if (actionBarToolbar != null)
                            actionBarToolbar.setTitleTextColor(mutedDark);

                    }


                    play = (mutedDark);
                    pause = (mutedDark);

                    //fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));

                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    remainingTimeLabel.setTextColor(mutedColor);
                    elapsedTimeLabel.setTextColor(mutedColor);

                    //fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                    toolbar.getNavigationIcon().setTint(mutedDark);


                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                    icon.setTint(mutedDark);
                    button.setBackground(icon);

                    SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                    int type = prefs1.getInt("cond", 0);
                    if (type == 0) {

                    } else if (type == 1) {
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                        myIcon3.setTint(mutedDark);
                        loop.setBackground(myIcon3);
                    } else if (type == 2) {
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                        myIcon3.setTint(mutedDark);
                        loop.setBackground(myIcon3);
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

                    ply.setBackground(myIcon2);
                    pre.setBackground(myIcon);
                    nxt.setBackground(myIcon3);
                    textView.setTextColor(mutedDark);
                    textView1.setTextColor(mutedDark);
                    titleS.setTextColor(mutedColor);
                    desS.setTextColor(mutedColor);
                    add_phone.setBackgroundColor(vibrant);
                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                    linearLayout.setBackgroundColor(vibrant);

                }
            });


            image = myView.findViewById(R.id.albumart);
            image.setImageBitmap(bm);
            songI.setImageBitmap(bm);

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
            mp.setLooping(false);
            mp.seekTo(0);

            mp.start();
            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
            myIcon2.setTint(play);
            playBtn.setBackground(myIcon2);
            // playBtn.setBackgroundColor(play);
            mp.setVolume(2.5f, 2.5f);
            totalTime = mp.getDuration();

            requestAudioPermissions();
            // Position Bar
            positionBar = myView.findViewById(R.id.positionBar);

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
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                Audio audio = new Audio();
                String SongTitle = cursor.getString(Title);
                String SongAlbum = cursor.getString(album);
                String SongArtist = cursor.getString(artist);
                String SongDate = cursor.getString(date);
                String SongDuration = cursor.getString(duration);

                String albumid = cursor.getString(albumId);

                //path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                audio.setTitle(SongTitle);
                audio.setAlbum(SongAlbum);
                audio.setArtist(SongArtist);
                audio.setData(SongDate);
                audio.setImagepath(albumid);
                audio.setDuration(SongDuration);


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

                    int audioSessionId = mp.getAudioSessionId();
                    if (audioSessionId != -1){
                        mVisualizer.setAudioSessionId(audioSessionId);
                    }
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
        gridView.setEnabled(false);
        myView.setEnabled(true);
        volumeBar.setEnabled(true);
        try {
            positionBar.setEnabled(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
        gridView.setEnabled(true);
        myView.setEnabled(false);
        volumeBar.setEnabled(false);
        try {
            positionBar.setEnabled(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        playBtn.setEnabled(false);
        pauseBtn.setEnabled(false);
        nxtBtn.setEnabled(false);

    }

    public void slideUp1(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        add_phone.setEnabled(true);
        ply.setEnabled(true);
        pre.setEnabled(true);
        nxt.setEnabled(true);



    }

    // slide the view from its current position to below itself
    public void slideDown1(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        add_phone.setEnabled(false);
        ply.setEnabled(false);
        pre.setEnabled(false);
        nxt.setEnabled(false);

    }

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
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.putExtra("p", (position));
        startService(serviceIntent);
    }

    public void stopService() {
        serviceIntent.setClass(Media_list_activity.this, NotificationService.class);
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



    @Override
    public void onDestroy(){
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        if (mVisualizer != null)
            mVisualizer.release();

    }

    class AudioFocusChangeListenerImpl implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {
            mFocusChanged = true;
            Log.i(TAG, "Focus changed");

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.i(TAG, "AUDIOFOCUS_GAIN");
                    play();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.i(TAG, "AUDIOFOCUS_LOSS");
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.i(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.i(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
            }
        }
    }

    @Override
    public  void onBackPressed(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3a9ebe"));
            setTitleColor(Color.WHITE);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3a9ebe")));
            Toolbar actionBarToolbar = findViewById(R.id.toolbar);
            if (actionBarToolbar != null)
                actionBarToolbar.setTitleTextColor(Color.WHITE);
        }


        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
        toolbar.getNavigationIcon().setTint(Color.WHITE);

        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
        icon.setTint(Color.WHITE);
        button.setBackground(icon);
        super.onBackPressed();
    }

}

