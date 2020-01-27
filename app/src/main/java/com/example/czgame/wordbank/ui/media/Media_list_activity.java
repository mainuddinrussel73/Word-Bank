package com.example.czgame.wordbank.ui.media;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.czgame.wordbank.R;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;
import com.lauzy.freedom.library.LrcView;
import com.ohoussein.playpause.PlayPauseView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.BlurTransformation;
import mkaflowski.mediastylepalette.MediaNotificationProcessor;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.czgame.wordbank.ui.words.MainActivity.isDark;

public class Media_list_activity extends AppCompatActivity  {

    public static final int RUNTIME_PERMISSION_CODE = 7;
    private static final String TAG = "Audio";
    public static List<Audio> ListElementsArrayList = new ArrayList<Audio>();
    public static int position = 0;
    public static PlayPauseView playBtn;
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
    public  static  int posit = 0;
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
    public boolean mFocusGranted;
    public boolean mFocusChanged;
    public static LrcView mLrcView;
    SlidingUpPanelLayout myView;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    public  static  WaveVisualizer mVisualizer;
    private Toast mToastToShow;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    TextView titleS,desS;
    RoundedImageView songI;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    boolean serviceBound = false;
    ImageButton showlrc;
RelativeLayout add_phone;
LinearLayout media_base;
    boolean isShowlrc = false;
    private NotificationService player;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(Media_list_activity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
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


            mLrcView.updateTime(currentPosition);

            mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
                @Override
                public void onPlay(long time, String content) {
                    mp.seekTo((int) time);
                }
            });


            remainingTimeLabel.setText("- " + remainingTime);
            if (toogle1 == false && (remainingTime.equals("0:00"))) {

                Media_list_activity.this.sendBroadcast(new Intent(Constants.ACTION.NEXT_ACTION));
                nxtsong();
                System.out.println("next");

            }
        }
    };

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
                mVisualizer.setAudioSessionId(audioSessionId);
            }
        }
    }



    public void save(EditText mEditText,String name) {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Lyrics");
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir();
        }
        if (isPresent) {
            File file = new File(docsFolder.getAbsolutePath(), name);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(mEditText.getText().toString().getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Failure
        }


        mEditText.getText().clear();
        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + name,
                Toast.LENGTH_LONG).show();
    }

    public int getDominantColor(Bitmap bitmap) {

        MediaNotificationProcessor processor = new MediaNotificationProcessor(this, bitmap); // can use drawable

        int backgroundColor = processor.getBackgroundColor();
        int primaryTextColor = processor.getPrimaryTextColor();
        int secondaryTextColor = processor.getSecondaryTextColor();



        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).maximumColorCount(32).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }

        });
        return backgroundColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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



        myView = findViewById(R.id.sliding_layout);


        add_phone = myView.findViewById(R.id.relativeLayoutMain);
        media_base = myView.findViewById(R.id.medic_base);



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














        playBtn = myView.findViewById(R.id.playBtn);
        elapsedTimeLabel = myView.findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = myView.findViewById(R.id.remainingTimeLabel);


        textView = myView.findViewById(R.id.song_title);
        textView1 = myView.findViewById(R.id.song_artist);

        textView.setSelected(true);
        textView1.setSelected(true);


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
        Bitmap bitmap ;
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
        int mutedLight = getComplimentColor(bitmap);
        int mutedDark = getComplimentColor(bitmap);

        loop = findViewById(R.id.loops);
        SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        title = ListElementsArrayList.get(position).getTitle();
        artist = ListElementsArrayList.get(position).getArtist();
        album = ListElementsArrayList.get(position).getAlbum();


        textView.setText(title);
        textView1.setText(artist);


        titleq = ListElementsArrayList.get(position).getImagepath();
        sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
        res = getContentResolver();

        try {
            in = res.openInputStream(uri);

            bitmap = BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            InputStream is = getResources().openRawResource(R.raw.image);
            bitmap = BitmapFactory.decodeStream(is);
            bm = bitmap;
        }


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



        titleS = myView.findViewById(R.id.textViewSongTitle);
        titleS.setSelected(true);

        desS = myView.findViewById(R.id.textViewArtistName);
        desS.setSelected(true);
        songI = myView.findViewById(R.id.play_pause);
        ply = myView.findViewById(R.id.buttonPlayAndStop);
        nxt = myView.findViewById(R.id.buttonForward);
        pre = myView.findViewById(R.id.buttonBackward);



        if(mp!=null) {
            if (mp.isPlaying()) {
                // mLrcView.setVisibility(View.GONE);
                if (!isUp) {

                    {

                        try {
                            in = res.openInputStream(uri);

                            bitmap = BitmapFactory.decodeStream(in);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            InputStream is = getResources().openRawResource(R.raw.image);
                            bitmap = BitmapFactory.decodeStream(is);
                        }


                        Bitmap finalBitmap1 = bitmap;
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Use generated instance
                                //work with the palette here
                                int defaultValue = 0x000000;
                                int vibrant = getDominantColor(finalBitmap1);
                                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                int muted = palette.getMutedColor(defaultValue);
                                int mutedLight = palette.getLightMutedColor(defaultValue);
                                int mutedDark = getComplimentColor(finalBitmap1);


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
                                volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                                remainingTimeLabel.setTextColor(mutedDark);
                                elapsedTimeLabel.setTextColor(mutedDark);
                                toolbar.getNavigationIcon().setTint(mutedDark);

                                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                icon.setTint(mutedDark);
                                button.setBackground(icon);
                                mVisualizer.setColor(mutedColor);
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
                                playBtn.setColor(play);
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                myIcon3.setTint(play);
                                myIcon.setTint(play);
                                myIcon1.setTint(play);
                                myIcon2.setTint(play);
                                nxtBtn.setBackground(myIcon3);
                                if (mp.isPlaying()) {
                                    playBtn.change(false);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                } else {
                                    playBtn.change(true);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                }
                                pauseBtn.setBackground(myIcon);
                                playBtn.setBackground(myIcon2);

                                playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon22 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                                Drawable myIcon21 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                myIcon33.setTint(play);
                                myIcon31.setTint(play);
                                myIcon22.setTint(play);
                                myIcon21.setTint(play);

                                ply.setBackground(myIcon22);
                                pre.setBackground(myIcon31);
                                nxt.setBackground(myIcon33);


                                textView.setTextColor(mutedDark);
                                textView1.setTextColor(mutedDark);

                                titleS.setTextColor(mutedDark);
                                desS.setTextColor(mutedDark);
                              //  add_phone.setAlpha(1.0f);


                                LinearLayout linearLayout = findViewById(R.id.medic_base);

                                linearLayout.setBackgroundColor(vibrant);
                                //linearLayout.setAlpha(1.0f);

                                LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                                linearLayout1.setBackgroundColor(vibrant);

                            }
                        });


                        image = myView.findViewById(R.id.albumart);
                        image.setImageBitmap(bitmap);
                        bm = bitmap;
                        if(isShowlrc){
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            bitmap = drawable.getBitmap();
                            Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                            image.setImageBitmap(blurred);
                        }
                        songI.setImageBitmap(bitmap);

                        media_base.setVisibility(View.VISIBLE);

                        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                        myIcon2.setTint(play);
                        playBtn.setBackground(myIcon2);

                        totalTime = mp.getDuration();
                        positionBar = myView.findViewById(R.id.positionBar);
                        vibrant = getDominantColor(bitmap);
                        mutedDark = getComplimentColor(bitmap);
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


                } else {

                    {



                        try {
                            in = res.openInputStream(uri);

                            bitmap = BitmapFactory.decodeStream(in);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            InputStream is = getResources().openRawResource(R.raw.image);
                            bitmap = BitmapFactory.decodeStream(is);
                        }


                        Bitmap finalBitmap2 = bitmap;
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Use generated instance
                                //work with the palette here
                                int defaultValue = 0x000000;
                                int vibrant = getDominantColor(finalBitmap2);
                                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                int muted = palette.getMutedColor(defaultValue);
                                int mutedLight = palette.getLightMutedColor(defaultValue);
                                int mutedDark = getComplimentColor(finalBitmap2);


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
                                volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                                remainingTimeLabel.setTextColor(mutedDark);
                                elapsedTimeLabel.setTextColor(mutedDark);

                                // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                toolbar.getNavigationIcon().setTint(Color.WHITE);

                                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                icon.setTint(Color.WHITE);
                                button.setBackground(icon);
                                mVisualizer.setColor(mutedColor);
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
                                    fabtint.setTint(getComplimentColor(finalBitmap2));
                                    //fab.setBackground(fabtint);
                                    //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                    //isClicked = true;
                                } else {

                                    Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                    fabtint.setTint(getComplimentColor(finalBitmap2));
                                    //fab.setBackground(fabtint);
                                    //fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                    //isClicked = false;
                                }
                                // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                //fab.setBackgroundColor(mutedDark);
                                play = (mutedDark);
                                pause = (mutedDark);
                                playBtn.setColor(play);
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                myIcon3.setTint(play);
                                myIcon.setTint(play);
                                myIcon1.setTint(play);
                                myIcon2.setTint(play);
                                nxtBtn.setBackground(myIcon3);
                                pauseBtn.setBackground(myIcon);
                                if (mp.isPlaying()) {
                                    playBtn.change(false);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                } else {
                                    playBtn.change(true);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                }
                                playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                                Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon22 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                                Drawable myIcon21 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                myIcon33.setTint(play);
                                myIcon31.setTint(play);
                                myIcon22.setTint(play);
                                myIcon21.setTint(play);

                                ply.setBackground(myIcon22);
                                pre.setBackground(myIcon31);
                                nxt.setBackground(myIcon33);


                                textView.setTextColor(mutedDark);
                                textView1.setTextColor(mutedDark);

                                titleS.setText(title);
                                desS.setText(artist);
                                titleS.setTextColor(mutedDark);
                                desS.setTextColor(mutedDark);

                               // add_phone.setBackgroundColor(vibrant);
                                LinearLayout linearLayout = findViewById(R.id.medic_base);

                                linearLayout.setBackgroundColor(vibrant);
                                LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                                linearLayout1.setBackgroundColor(vibrant);

                            }
                        });


                        image = myView.findViewById(R.id.albumart);
                        image.setImageBitmap(bitmap);
                        bm = bitmap;
                        if(isShowlrc){
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            bitmap = drawable.getBitmap();
                            Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                            image.setImageBitmap(blurred);
                        }
                        songI.setImageBitmap(bitmap);


                        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                        myIcon2.setTint(play);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));


                        // Position Bar
                        totalTime = mp.getDuration();
                        positionBar = myView.findViewById(R.id.positionBar);
                        vibrant = getDominantColor(bitmap);
                        mutedDark = getComplimentColor(bitmap);
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
            }
            else if (!mp.isPlaying()) {
                if (!isUp) {

                    {




                        try {
                            in = res.openInputStream(uri);

                            bitmap = BitmapFactory.decodeStream(in);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            InputStream is = getResources().openRawResource(R.raw.image);
                            bitmap = BitmapFactory.decodeStream(is);
                        }


                        Bitmap finalBitmap3 = bitmap;
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Use generated instance
                                //work with the palette here
                                int defaultValue = 0x000000;
                                int vibrant = getDominantColor(finalBitmap3);
                                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                int muted = palette.getMutedColor(defaultValue);
                                int mutedLight = palette.getLightMutedColor(defaultValue);
                                int mutedDark = getComplimentColor(finalBitmap3);


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
                                volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                                remainingTimeLabel.setTextColor(mutedDark);
                                elapsedTimeLabel.setTextColor(mutedDark);

                                // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                icon.setTint(mutedDark);
                                button.setBackground(icon);
                                mVisualizer.setColor(mutedColor);
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
                                    fabtint.setTint(getComplimentColor(finalBitmap3));
                                    // fab.setBackground(fabtint);
                                    //fab.setImageResource(R.drawable.ic_expand_more_black_24dp);
                                    //isClicked = true;
                                } else {

                                    Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                    fabtint.setTint(getComplimentColor(finalBitmap3));
                                    //fab.setBackground(fabtint);
                                    //fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                    //isClicked = false;
                                }
                                // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                                //fab.setBackgroundColor(mutedDark);
                                play = (mutedDark);
                                pause = (mutedDark);
                                playBtn.setColor(play);
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                myIcon3.setTint(play);
                                myIcon.setTint(play);
                                myIcon1.setTint(play);
                                myIcon2.setTint(play);
                                nxtBtn.setBackground(myIcon3);
                                pauseBtn.setBackground(myIcon);
                                if (mp.isPlaying()) {
                                    playBtn.change(false);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                } else {
                                    playBtn.change(true);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                }
                                playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                                Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon22 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                                Drawable myIcon21 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                myIcon33.setTint(play);
                                myIcon31.setTint(play);
                                myIcon22.setTint(play);
                                myIcon21.setTint(play);

                                ply.setBackground(myIcon21);
                                pre.setBackground(myIcon31);
                                nxt.setBackground(myIcon33);
                                //myIcon3.setTint(mutedDark);
                                //myIcon2.setTint(mutedDark);

                                media_base.setAlpha(1.0f);

                                textView.setTextColor(mutedDark);
                                textView1.setTextColor(mutedDark);
                                titleS.setText(title);
                                desS.setText(artist);
                                titleS.setTextColor(mutedDark);
                                desS.setTextColor(mutedDark);
                            //    add_phone.setBackgroundColor(vibrant);
                                LinearLayout linearLayout = findViewById(R.id.medic_base);

                                linearLayout.setBackgroundColor(vibrant);

                                LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                                linearLayout1.setBackgroundColor(vibrant);

                            }
                        });


                        image = myView.findViewById(R.id.albumart);
                        image.setImageBitmap(bitmap);
                        bm = bitmap;
                        if(isShowlrc){
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            bitmap = drawable.getBitmap();
                            Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                            image.setImageBitmap(blurred);
                        }
                        songI.setImageBitmap(bitmap);

                        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        myIcon2.setTint(play);
                        playBtn.setBackground(myIcon2);
                        //myIcon3.setTint(play);
                        // myIcon2.setTint(play);


                        // Position Bar
                        totalTime = mp.getDuration();
                        positionBar = myView.findViewById(R.id.positionBar);
                        vibrant = getDominantColor(bitmap);
                        mutedDark = getComplimentColor(bitmap);
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


                } else {

                    {


                        try {
                            in = res.openInputStream(uri);

                            bitmap = BitmapFactory.decodeStream(in);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            InputStream is = getResources().openRawResource(R.raw.image);
                            bitmap = BitmapFactory.decodeStream(is);
                        }


                        Bitmap finalBitmap4 = bitmap;
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                // Use generated instance
                                //work with the palette here
                                int defaultValue = 0x000000;
                                int vibrant = getDominantColor(finalBitmap4);
                                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                                int muted = palette.getMutedColor(defaultValue);
                                int mutedLight = palette.getLightMutedColor(defaultValue);
                                int mutedDark = getComplimentColor(finalBitmap4);


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
                                volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                                remainingTimeLabel.setTextColor(mutedDark);
                                elapsedTimeLabel.setTextColor(mutedDark);

                                // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                toolbar.getNavigationIcon().setTint(Color.WHITE);

                                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                icon.setTint(Color.WHITE);
                                button.setBackground(icon);
                                mVisualizer.setColor(mutedColor);
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
                                    fabtint.setTint(getComplimentColor(finalBitmap4));
                                } else {

                                    Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                    fabtint.setTint(getComplimentColor(finalBitmap4));
                                }

                                play = (mutedDark);
                                pause = (mutedDark);
                                playBtn.setColor(play);
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                                myIcon3.setTint(play);
                                myIcon.setTint(play);
                                myIcon1.setTint(play);
                                myIcon2.setTint(play);
                                nxtBtn.setBackground(myIcon3);
                                pauseBtn.setBackground(myIcon);
                                if (mp.isPlaying()) {
                                    playBtn.change(false);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                } else {
                                    playBtn.change(true);
                                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                }
                                playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                                Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                Drawable myIcon22 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                                Drawable myIcon21 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                myIcon33.setTint(play);
                                myIcon31.setTint(play);
                                myIcon22.setTint(play);
                                myIcon21.setTint(play);

                                ply.setBackground(myIcon21);
                                pre.setBackground(myIcon31);
                                nxt.setBackground(myIcon33);


                                textView.setTextColor(mutedDark);
                                textView1.setTextColor(mutedDark);
                                titleS.setText(title);
                                desS.setText(artist);
                                titleS.setTextColor(mutedDark);
                                desS.setTextColor(mutedDark);
                             //   add_phone.setBackgroundColor(vibrant);
                                LinearLayout linearLayout = findViewById(R.id.medic_base);

                                linearLayout.setBackgroundColor(vibrant);

                                LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                                linearLayout1.setBackgroundColor(vibrant);
                            }
                        });


                        image = myView.findViewById(R.id.albumart);
                        image.setImageBitmap(bitmap);

                        bm = bitmap;
                        if(isShowlrc){
                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            bitmap = drawable.getBitmap();
                            Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                            image.setImageBitmap(blurred);
                        }
                        songI.setImageBitmap(bitmap);

                        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        myIcon2.setTint(play);
                        playBtn.setBackground(myIcon2);


                        // Position Bar
                        totalTime = mp.getDuration();
                        positionBar = myView.findViewById(R.id.positionBar);
                        vibrant = getDominantColor(bitmap);
                        mutedDark = getComplimentColor(bitmap);
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
            }

        }else{
            startsong();
        }

        //add_phone.setVisibility(View.VISIBLE);

          //  myView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        //add_phone.setVisibility(View.VISIBLE);
        //media_base.setVisibility(View.INVISIBLE);

        add_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               myView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                listView.setEnabled(false);

                gridView.setEnabled(false);

                volumeBar.setEnabled(true);
                try {
                    positionBar.setEnabled(true);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                playBtn.setEnabled(true);
                pauseBtn.setEnabled(true);
                nxtBtn.setEnabled(true);
                showlrc.setEnabled(true);
                if(isShowlrc){
                    mLrcView.setVisibility(View.VISIBLE);

                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                    image.setImageBitmap(blurred);
                }else {
                    mLrcView.setVisibility(View.GONE);
                }

                //myView.setVisibility(View.VISIBLE);
               // add_phone.setVisibility(View.INVISIBLE);
                ply.setEnabled(false);
                pre.setEnabled(false);
                nxt.setEnabled(false);




                isUp = false;
            }
        });

        final float[] slideold = {0f};
        media_base.setAlpha(slideold[0]);
        myView.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {



            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                System.out.println("onPanelSlide, offset " + slideOffset);
                slideold[0] = slideOffset;


                    //    add_phone.setAlpha((1.0f-slideold[0]));
                media_base.setAlpha(slideold[0]);
                        add_phone.setAlpha(1.0f-slideold[0]);



            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){

                    listView.setEnabled(false);

                    gridView.setEnabled(false);

                    volumeBar.setEnabled(true);
                    try {
                        positionBar.setEnabled(true);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    playBtn.setEnabled(true);
                    pauseBtn.setEnabled(true);
                    nxtBtn.setEnabled(true);
                    showlrc.setEnabled(true);
                    if(isShowlrc){
                        mLrcView.setVisibility(View.VISIBLE);

                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                        image.setImageBitmap(blurred);
                    }else {
                        mLrcView.setVisibility(View.GONE);
                    }

                    //myView.setVisibility(View.VISIBLE);
                   // add_phone.setVisibility(View.INVISIBLE);
                    ply.setEnabled(false);
                    pre.setEnabled(false);
                    nxt.setEnabled(false);




                    isUp = false;

                    System.out.println("isside expand");


                }
                else if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){




                    listView.setEnabled(true);
                    gridView.setEnabled(true);
                    //     myView.setEnabled(false);
                    volumeBar.setEnabled(false);
                    try {
                        positionBar.setEnabled(false);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    playBtn.setEnabled(false);
                    pauseBtn.setEnabled(false);
                    nxtBtn.setEnabled(false);
                    showlrc.setEnabled(false);
                    mLrcView.setVisibility(View.INVISIBLE);
                    if(isShowlrc){

                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                        image.setImageBitmap(blurred);
                    }

                   // myView.setVisibility(View.GONE);
                  //  add_phone.setVisibility(View.VISIBLE);
                    ply.setEnabled(true);
                    pre.setEnabled(true);
                    nxt.setEnabled(true);

                    isUp = true;

                    System.out.println("isside collapse");
                }




            }
        });
        //myView.drag(false);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        myView.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myView.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });







        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.NEXT_ACTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.PREV_ACTION));
        mVisualizer = findViewById(R.id.blast);
        mVisualizer.show();
        //TODO: init MediaPlayer and play the audio

        //get the AudioSessionId from your MediaPlayer and pass it to the visualizer

        int finalMutedDark = mutedDark;
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogle1 = !toogle1;
                if (toogle1 == true) {
                    if (mp.isPlaying()) {
                        mp.setLooping(true);
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                        myIcon3.setTint(finalMutedDark);
                        loop.setBackground(myIcon3);

                        SharedPreferences.Editor editor = prefs1.edit();
                        editor.putInt("cond", 2);
                        editor.commit();

                    }
                } else {
                    if (mp.isPlaying()) {
                        mp.setLooping(false);
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                        myIcon3.setTint(finalMutedDark);
                        loop.setBackground(myIcon3);

                        SharedPreferences.Editor editor = prefs1.edit();
                        editor.putInt("cond", 1);
                        editor.commit();

                    }
                }
            }
        });




        button = toolbar.findViewById(R.id.sortsong);

        songI.setImageBitmap(bitmap);
        titleS.setText(ListElementsArrayList.get(position).getTitle());
        desS.setText(ListElementsArrayList.get(position).getArtist());
        titleS.setTextColor(mutedDark);
        desS.setTextColor(mutedDark);
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
        toolbar.getNavigationIcon().setTint(mutedDark);
        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
        icon.setTint(mutedDark);
        button.setBackground(icon);

       // slideUp1(add_phone);
        add_phone.setBackgroundColor(play);
        if(mp!=null){
            if(mp.isPlaying()){

                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                myIcon3.setTint(mutedDark);
                ply.setBackground(myIcon3);

            }else{
                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                myIcon3.setTint(mutedDark);
                ply.setBackground(myIcon3);
            }
        }

        mLrcView = findViewById(R.id.lrc_view);
       // mLrcView.setVisibility(View.GONE);


        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
        Bitmap finalBitmap = bitmap;

        enablelrc.setOnClickListener(new View.OnClickListener() {

            int oo = 0;

            @Override
            public void onClick(View view) {
                oo++;
                if(oo==1){
                    isShowlrc = true;
                    mLrcView.setVisibility(View.VISIBLE);
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Bitmap blurred = blurRenderScript(bitmap, 25);//second parametre is radius
                    image.setImageBitmap(blurred);
                }else if(oo==2){
                    oo=0;
                    isShowlrc = false;
                    mLrcView.setVisibility(View.INVISIBLE);


                    image.setImageBitmap(bm);

                }


            }
        });

        if(mp!=null){
            File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                    +Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));

            System.out.println(file);
            List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
            mLrcView.setLrcData(lrcs);
           if(isShowlrc) {mLrcView.setVisibility(View.VISIBLE);
               BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
               Bitmap bitmap1 = drawable.getBitmap();
               Bitmap blurred = blurRenderScript(bitmap1, 20);//second parametre is radius
               image.setImageBitmap(blurred);
           }
           else mLrcView.setVisibility(View.INVISIBLE);
        }



        showlrc = myView.findViewById(R.id.showlrc);

        showlrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog ;
                if(isDark){
                    alertDialog = new AlertDialog.Builder(Media_list_activity.this,R.style.DialogurDark);
                }else {
                    alertDialog = new AlertDialog.Builder(Media_list_activity.this,R.style.DialogueLight);
                }
                alertDialog.setMessage("Lyrics");

                final EditText input = new EditText(Media_list_activity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(15,15,15,15);
                input.setLayoutParams(lp);
                input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
                if(isDark){
                input.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittextstyledark));
                input.setTextColor(Color.WHITE);

                }else {
                    input.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.editextstyle));
                    input.setTextColor(Color.BLACK);
                }
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.common_google_signin_btn_icon_dark);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(!input.getText().equals("")){
                                    save(input, Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                alertDialog.show();
            }
        });






        myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
        myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
        myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
        myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);



        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if(isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }


        if (isDark && ListElementsArrayList.size() != 0) {

            relativeLayout.setBackgroundColor(Color.BLACK);


            listView.setAdapter(adapter);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            gridView.setAdapter(adapterG);
            gridView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
        } else if (!isDark && ListElementsArrayList.size() != 0) {

            relativeLayout.setBackgroundColor(Color.WHITE);


            listView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            gridView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            listView.setAdapter(adapter);
            gridView.setAdapter(adapterG);

        } else if (isDark && ListElementsArrayList.size() == 0) {

            listView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            gridView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            relativeLayout.setBackgroundColor(Color.BLACK);


        } else if (!isDark && ListElementsArrayList.size() == 0) {



            gridView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            relativeLayout.setBackgroundColor(Color.WHITE);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));


        }



        // ListView on item selected listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                System.out.println(pro);

                //startService(view);
                // System.out.println("click");
                Media_list_activity.position = position;


                if(mp!=null)mp.stop();
                mp = new MediaPlayer();

                isClicked = true;





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
                            int mutedDark = getComplimentColor(bm);

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
                            volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                            remainingTimeLabel.setTextColor(mutedDark);
                            elapsedTimeLabel.setTextColor(mutedDark);

                            // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            toolbar.getNavigationIcon().setTint(mutedDark);
                            Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                            icon.setTint(mutedDark);
                            button.setBackground(icon);

                            Glide.with(context).load(bm).apply(bitmapTransform(new BlurTransformation(context,25))).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        myView.setBackground(resource);
                                    }
                                }
                            });
                            mVisualizer.setColor(mutedColor);
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
                                fabtint.setTint(getComplimentColor(bm));

                                //isClicked = true;
                            } else {

                                Drawable fabtint = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                fabtint.setTint(getComplimentColor(bm));


                                //isClicked = false;
                            }

                            // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                            //fab.setBackgroundColor(mutedDark);
                            play = (mutedDark);
                            pause = (mutedDark);



                            Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                            Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                            Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                            myIcon3.setTint(play);
                            myIcon.setTint(play);
                            myIcon1.setTint(play);
                            myIcon2.setTint(play);
                            nxtBtn.setBackground(myIcon3);
                            pauseBtn.setBackground(myIcon);
                            playBtn.change(false);
                            playBtn.setColor(play);
                            int ব্রজে  = 10;
                            mLrcView.setIndicatorLineColor(mutedColor);
                            //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                            playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                            Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                            Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                            myIcon33.setTint(play);
                            myIcon31.setTint(play);
                            ply.setBackground(myIcon2);
                            pre.setBackground(myIcon31);
                            nxt.setBackground(myIcon33);


                            //myIcon3.setTint(mutedDark);
                            //myIcon2.setTint(mutedDark);

                            //barVisualizer.setColor(ContextCompat.getColor(Media_list_activity.this,vibrant));

                            textView.setTextColor(mutedDark);
                            textView1.setTextColor(mutedDark);
                            titleS.setText(title);
                            desS.setText(artist);
                            titleS.setTextColor(mutedDark);
                            desS.setTextColor(mutedDark);


                            LinearLayout linearLayout = findViewById(R.id.medic_base);

                            linearLayout.setBackgroundColor(vibrant);

                            LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                            linearLayout1.setBackgroundColor(vibrant);
                        }
                    });


                    image = myView.findViewById(R.id.albumart);
                    image.setImageBitmap(bm);
                    if(isShowlrc){
                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                        image.setImageBitmap(blurred);
                    }
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


                    File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                            +Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));

                    System.out.println(file);
                    List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
                    mLrcView.setLrcData(lrcs);

                    if(isShowlrc){ mLrcView.setVisibility(View.VISIBLE);
                        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                        image.setImageBitmap(blurred);
                    }
                    else mLrcView.setVisibility(View.INVISIBLE);



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




            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                System.out.println(pro);

                //startService(view);
                // System.out.println("click");
                Media_list_activity.position = position;


                if (mp != null) mp.stop();
                mp = new MediaPlayer();

                isClicked = true;


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


                Palette.from(bm).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {
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
                        int mutedDark = getComplimentColor(bm);

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
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                        positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                        remainingTimeLabel.setTextColor(mutedDark);
                        elapsedTimeLabel.setTextColor(mutedDark);

                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        toolbar.getNavigationIcon().setTint(mutedDark);
                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                        icon.setTint(mutedDark);
                        button.setBackground(icon);
                        mVisualizer.setColor(mutedColor);
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
                        playBtn.setColor(play);
                        Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                        Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        //myIcon3.setTint(mutedDark);
                        //myIcon2.setTint(mutedDark);

                        mLrcView.setIndicatorTextColor(play);
                        mLrcView.setCurrentIndicateLineTextColor(play);
                        //mLrcView.setLrcTextColor(play);
                        Drawable myIcons1 = getResources().getDrawable(R.drawable.play);
                        myIcons1.setTint(play);
                        mLrcView.setPlayDrawable(myIcons1);

                        textView.setTextColor(mutedDark);
                        textView1.setTextColor(mutedDark);
                        titleS.setText(title);
                        desS.setText(artist);
                        titleS.setTextColor(mutedDark);
                        desS.setTextColor(mutedDark);

                        LinearLayout linearLayout = findViewById(R.id.medic_base);

                        LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                        linearLayout1.setBackgroundColor(vibrant);

                        linearLayout.setBackgroundColor(vibrant);


                    }
                });


                image = myView.findViewById(R.id.albumart);
                image.setImageBitmap(bm);
                if (isShowlrc) {
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                    image.setImageBitmap(blurred);
                }
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

                File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                        + Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));

                System.out.println(file);
                List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
                mLrcView.setLrcData(lrcs);
                if (isShowlrc) {
                    mLrcView.setVisibility(View.VISIBLE);
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                    image.setImageBitmap(blurred);
                } else mLrcView.setVisibility(View.INVISIBLE);

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
            }



        });


        nxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                isShowlrc = false;
                nxtsong();

            }
        });
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowlrc = false;
                nxtsong();
                //myButton.setText("Slide up");



            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowlrc = false;
                prevsong();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startService(view);
                //startService(view);
                isShowlrc = false;

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

        //
        // registerReceiver(broadcastReceiver, new IntentFilter("Prev"));
    }

    public void play(){

        if (!serviceBound) {
            startService();

        } else {
            //Service is active
            //Send media with BroadcastReceiver
            if(mp==null){
                startsong();
            }
            else if (!mp.isPlaying()) {
                // Stopping
                mp.start();
                mLrcView.resume();
                playBtn.change(false);
                playBtn.setColor(play);
                //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                // playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                ply.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                myIcon3.setTint(play);

                NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView1);
                // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());


            } else{
                // Playing
                mp.pause();
                playBtn.change(true);
                playBtn.setColor(play);
                // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                //playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                ply.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                myIcon2.setTint(play);

                NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView1);

                // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());

            }
        }


    }

    public void pause(){

        if (mp.isPlaying()) {
            // Stopping
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            playBtn.setColor(play);
            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
            myIcon2.setTint(play);
            mLrcView.pause();


        } else{
            // Playing
            playBtn.setColor(play);
            // mp.pause();



        }

    }

    public void startsong() {

        // startService(view);
        System.out.println(pro);


        if(mp!=null) mp.stop();
        mp = new MediaPlayer();

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
                    int mutedDark = getComplimentColor(bm);
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
                    playBtn.setColor(play);
                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                    remainingTimeLabel.setTextColor(mutedColor);
                    elapsedTimeLabel.setTextColor(mutedColor);

                    //  fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                    toolbar.getNavigationIcon().setTint(mutedDark);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                    icon.setTint(mutedDark);
                    button.setBackground(icon);

                    mVisualizer.setColor(mutedColor);
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




                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                    myIcon3.setTint(play);
                    myIcon.setTint(play);
                    myIcon1.setTint(play);
                    myIcon2.setTint(play);
                    nxtBtn.setBackground(myIcon3);
                    pauseBtn.setBackground(myIcon);
                    playBtn.change(false);
                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                    playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                    Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                    Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                    myIcon33.setTint(play);
                    myIcon31.setTint(play);
                    ply.setBackground(myIcon2);
                    pre.setBackground(myIcon31);
                    nxt.setBackground(myIcon33);

                    textView.setTextColor(mutedDark);
                    textView1.setTextColor(mutedDark);
                    titleS.setTextColor(mutedColor);
                    desS.setTextColor(mutedColor);

                   // add_phone.setBackgroundColor(vibrant);
                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                    linearLayout.setBackgroundColor(vibrant);


                    LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                    linearLayout1.setBackgroundColor(vibrant);

                }
            });


            image = myView.findViewById(R.id.albumart);
            image.setImageBitmap(bm);
            if(isShowlrc){
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                image.setImageBitmap(blurred);
            }
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
            mp.seekTo(posit);

            mp.start();

            File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                    +Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));

            System.out.println(file);
            List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
            mLrcView.setLrcData(lrcs);
            if(isShowlrc){ mLrcView.setVisibility(View.VISIBLE);
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                image.setImageBitmap(blurred);
            }
            else mLrcView.setVisibility(View.INVISIBLE);

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

    public void nxtsong() {

        // startService(view);
        System.out.println(pro);


        if(mp!=null) mp.stop();
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
                    int mutedDark = getComplimentColor(bm);
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
                    playBtn.setColor(play);
                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                    remainingTimeLabel.setTextColor(mutedColor);
                    elapsedTimeLabel.setTextColor(mutedColor);

                    //  fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                    toolbar.getNavigationIcon().setTint(mutedDark);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                    icon.setTint(mutedDark);
                    button.setBackground(icon);

                    mVisualizer.setColor(mutedColor);
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




                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                    myIcon3.setTint(play);
                    myIcon.setTint(play);
                    myIcon1.setTint(play);
                    myIcon2.setTint(play);
                    nxtBtn.setBackground(myIcon3);
                    pauseBtn.setBackground(myIcon);
                    playBtn.change(false);
                    // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                    playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                    Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                    Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                    myIcon33.setTint(play);
                    myIcon31.setTint(play);
                    ply.setBackground(myIcon2);
                    pre.setBackground(myIcon31);
                    nxt.setBackground(myIcon33);

                    textView.setTextColor(mutedDark);
                    textView1.setTextColor(mutedDark);
                    titleS.setTextColor(mutedColor);
                    desS.setTextColor(mutedColor);

                   // add_phone.setBackgroundColor(vibrant);
                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                    linearLayout.setBackgroundColor(vibrant);
                    LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                    linearLayout1.setBackgroundColor(vibrant);
                }
            });


            image = myView.findViewById(R.id.albumart);
            image.setImageBitmap(bm);
            if(isShowlrc){
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                image.setImageBitmap(blurred);
            }
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

            File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                    +Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));

            System.out.println(file);
            List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
            mLrcView.setLrcData(lrcs);
            if(isShowlrc){ mLrcView.setVisibility(View.VISIBLE);
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                image.setImageBitmap(blurred);
            }
            else mLrcView.setVisibility(View.INVISIBLE);

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

    public void prevsong() {
        System.out.println(pro);

        // startService(view);


        if(mp!=null)mp.stop();
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
                    int mutedDark = getComplimentColor(bm);

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
                    playBtn.setColor(play);
                    //fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));

                    volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));
                    positionBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                    positionBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                    remainingTimeLabel.setTextColor(mutedColor);
                    elapsedTimeLabel.setTextColor(mutedColor);

                    //fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                    toolbar.getNavigationIcon().setTint(mutedDark);


                    Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                    icon.setTint(mutedDark);
                    button.setBackground(icon);
                    mVisualizer.setColor(mutedColor);
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



                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
                    Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                    myIcon3.setTint(play);
                    myIcon.setTint(play);
                    myIcon1.setTint(play);
                    myIcon2.setTint(play);
                    nxtBtn.setBackground(myIcon3);
                    pauseBtn.setBackground(myIcon);
                    playBtn.change(false);
                    //  playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                    playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                    Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                    Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                    myIcon33.setTint(play);
                    myIcon31.setTint(play);
                    ply.setBackground(myIcon2);
                    pre.setBackground(myIcon31);
                    nxt.setBackground(myIcon33);

                    textView.setTextColor(mutedDark);
                    textView1.setTextColor(mutedDark);
                    titleS.setTextColor(mutedColor);
                    desS.setTextColor(mutedColor);
                   // add_phone.setBackgroundColor(vibrant);
                    LinearLayout linearLayout = findViewById(R.id.medic_base);

                    linearLayout.setBackgroundColor(vibrant);

                    LinearLayout linearLayout1 = findViewById(R.id.medic_base1);
                    linearLayout1.setBackgroundColor(vibrant);

                }
            });


            image = myView.findViewById(R.id.albumart);
            image.setImageBitmap(bm);
            if(isShowlrc){
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                image.setImageBitmap(blurred);
            }
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

            File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                    +Media_list_activity.ListElementsArrayList.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));

            System.out.println(file);
            List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
            mLrcView.setLrcData(lrcs);

            if(isShowlrc){ mLrcView.setVisibility(View.VISIBLE);
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap blurred = blurRenderScript(bitmap, 20);//second parametre is radius
                image.setImageBitmap(blurred);
            }
            else mLrcView.setVisibility(View.INVISIBLE);
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


    public void stopService() {
        serviceIntent.setClass(Media_list_activity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    public Audio getNest() {
        return Media_list_activity.ListElementsArrayList.get((position + 1) % Media_list_activity.ListElementsArrayList.size());
    }

    public void startService() {
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.putExtra("p", (position));
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public int getComplimentColor(Bitmap bitmap) {

        MediaNotificationProcessor processor = new MediaNotificationProcessor(this, bitmap); // can use drawable

        int backgroundColor = processor.getBackgroundColor();
        int primaryTextColor = processor.getPrimaryTextColor();
        int secondaryTextColor = processor.getSecondaryTextColor();


        // get existing colors

        return primaryTextColor;
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
        icon.setTint(Color.WHITE);
        button.setBackground(icon);
        super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);


        if (mVisualizer != null)
            mVisualizer.release();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }
    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}

