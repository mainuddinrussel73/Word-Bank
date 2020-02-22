package com.example.czgame.wordbank.ui.media;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
import com.example.czgame.wordbank.utill.Lrc;
import com.example.czgame.wordbank.utill.LrcHelper;
import com.example.czgame.wordbank.utill.LrcView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.masoudss.lib.SeekBarOnProgressChanged;
import com.masoudss.lib.WaveformSeekBar;
import com.ohoussein.playpause.PlayPauseView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.BlurTransformation;
import mkaflowski.mediastylepalette.MediaNotificationProcessor;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.czgame.wordbank.ui.media.fragment_plau_queue.playqueue;
import static com.example.czgame.wordbank.ui.media.music_base.bottomTabLayout;
import static com.example.czgame.wordbank.ui.media.music_base.mVisualizer;
import static com.example.czgame.wordbank.ui.media.music_base.toolbar;
import static com.example.czgame.wordbank.ui.words.MainActivity.isDark;

public class fragment_music_list extends Fragment implements music_base.OnBackPressedListener,Audiolist_adapter.EventListener,Audiogrid_adapter.EventListener{
    public static final int RUNTIME_PERMISSION_CODE = 7;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
        private static final String ARG_COLOR = "color";
        private static final String ARG_TITLE = "title";
    private static final String TAG = "Audio";
    public static List<Audio> ListElementsArrayList = new ArrayList<Audio>();
    public static int position = 0;
    public static PlayPauseView playBtn;
    public static MediaPlayer mp = new MediaPlayer();
    public static ImageView image;
    public  static  int posit = 0;
    public static int totalTime;
    public static Button pauseBtn, nxtBtn;
    public static  Button nxt,pre,ply;
    public  static  boolean isClicked = false;
   // Toolbar toolbar;
    public static  Intent serviceIntent;
    public static LrcView mLrcView;
    public  static  boolean isShowlrc = false;
    public  static  boolean theme_changed = false;
    public  static  SharedPreferences prefm ;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    public  static Bitmap bm;
    public boolean mFocusGranted;
    public boolean mFocusChanged;
    Context context;
    public  static  SeekBar volumeBar;
    Audiogrid_adapter adapterG;
    Audiolist_adapter adapter;
    ContentResolver contentResolver;
    Cursor cursor;
    Button sort;
    Uri uri;
    boolean isUp;
    public  static  WaveformSeekBar waveformSeekBar;
    public  static  TextView elapsedTimeLabel;
    public  static  TextView remainingTimeLabel;
    public  static TextView textView;
    int pro = 0;
    int pro1 = 0;
    public  static TextView textView1;
    public  static String artist;
    public  static String album;
    public  static String titleq;
    public  static int play, pause;
    public  static  Drawable myIcon;
    Button showhide;
    public  static Drawable myIcon1;
    public  static Drawable myIcon2;
    public  static Drawable myIcon3;
    public  static  int mutedColor;
    public  static  boolean toogle1 = true;
    public  static  Button  loop;
    MediaController mediaController;
    public static SlidingUpPanelLayout myView;
    public  static TextView titleS,desS;
    public static RoundedImageView songI;
    public static ImageButton showlrc;
    public  static  int vibran = 0;
    public  static  int muted;
    RelativeLayout add_phone;
    LinearLayout media_base;
    public  static String title;
    public   boolean serviceBound = false;
    ImageButton enablelrc;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {



            int currentPosition = msg.what;
            // Update positionBar.
            ///positionBar.setProgress(currentPosition);
            try {
                Integer p = (100 * currentPosition) / mp.getDuration();
                waveformSeekBar.setProgress(p);
            }catch (Exception e){

            }

           // mVisualizer.animate();
          //  mVisualizer.show();

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


            try{
                context.sendBroadcast(new Intent(Constants.ACTION.NEXT_ACTION));
            }catch (Exception e){

            }
                //nxtsong();
                System.out.println("next");

            }
        }
    };
        private int color;
    private ListView listView;
    private GridView gridView;
    private int currentViewMode = 0;
    private Toast mToastToShow;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    private NotificationService player;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            //Toast.makeText(Media_list_activity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
    Activity activity;
    public   BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            if(intent.getAction()==Constants.ACTION.NEXT_ACTION){
                try{
                    System.out.println("nxtsong");
                nxtsong();}catch (Exception e){
                    System.out.println(e.getMessage());
                }

               // System.out.println("next");
            }
            else if(intent.getAction()==Constants.ACTION.PREV_ACTION){

                try{
                    prevsong();}catch (Exception e){}
            }
            else if(intent.getAction()==Constants.ACTION.LOAD_ACTION){

                try{
                    position = prefm.getInt("positionnow",0);
                    System.out.println("get"+position);
                    startsong();

                }catch (Exception e){}
            }

        }
    };
        public fragment_music_list() {
            // Required empty public constructor
        }

        public static fragment_music_list newInstance(int param1, String param2) {
            fragment_music_list fragment = new fragment_music_list();

            Bundle args = new Bundle();
            args.putInt(ARG_COLOR, param1);
            args.putString(ARG_TITLE, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                color = getArguments().getInt(ARG_COLOR);
                title = getArguments().getString(ARG_TITLE);
            }

            getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.NEXT_ACTION));
            getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.PREV_ACTION));
            getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.LOAD_ACTION));
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

            adapter = new Audiolist_adapter(getActivity(),this);
            listView.setAdapter(adapter);


        } else {
            adapterG = new Audiogrid_adapter(getActivity(),this);
            gridView.setAdapter(adapterG);
        }
    }

    public  void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(context, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(context,
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
        Toast.makeText(context, "Saved to " + context.getFilesDir() + "/" + name,
                Toast.LENGTH_LONG).show();
    }

    public int getDominantColor(Bitmap bitmap) {

        MediaNotificationProcessor processor = new MediaNotificationProcessor(context, bitmap); // can use drawable

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
            //    playBtn.setColor(play);
             //   playBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_pause_black_24dp));
                //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                // playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                try {
                    ply.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                    myIcon3.setTint(play);
                }catch (Exception e){

                }


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
            ///    playBtn.setColor(play);
//                playBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_play_arrow_black_24dp));
                // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                //playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                try {
                    ply.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                    myIcon2.setTint(play);
                }catch (Exception e){

                }

                NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                NotificationService.notification.setCustomContentView(NotificationService.notificationView1);

                // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());

            }
        }


    }


    public void startsong() {

        if(theme_changed == false){
            // startService(view);
            System.out.println(pro);


            if (mp != null) mp.stop();
            mp = new MediaPlayer();

            {

                // playBtn = (Button) findViewById(R.id.playBtn);
                // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
                //  remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

                // Media Player

                prefm = PreferenceManager.getDefaultSharedPreferences(context);
                position = prefm.getInt("positionnow", 0);

                title = playqueue.get(position).getTitle();
                artist = playqueue.get(position).getArtist();
                album = playqueue.get(position).getAlbum();


                textView.setText(title);
                textView1.setText(artist);

                titleS.setText(title);
                desS.setText(artist);

                titleq = playqueue.get(position).getImagepath();
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
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

                        vibran = vibrant;
                        muted = mutedDark;


                        if (vibrant == 0) {
                            mutedDark = palette.getDarkVibrantColor(vibrant);
                        }


                        play = (mutedDark);
                        pause = (mutedDark);
                        // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                        playBtn.setColor(play);
                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                        remainingTimeLabel.setTextColor(mutedColor);
                        elapsedTimeLabel.setTextColor(mutedColor);

                        //  fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        //toolbar.getNavigationIcon().setTint(mutedDark);

                    //    button.setBackground(icon);

                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                        int type = prefs1.getInt("cond", 0);
                        if (type == 0) {

                        } else if (type == 1) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_loop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        } else if (type == 2) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_unloop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        }


                        Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon  =  context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                        Drawable myIcon33 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        textView.setTextColor(mutedDark);
                        textView1.setTextColor(mutedDark);


                        waveformSeekBar.setWaveProgressColor(mutedDark);
                        // add_phone.setBackgroundColor(vibrant);
                        LinearLayout linearLayout = activity.findViewById(R.id.medic_base);

                        linearLayout.setBackgroundColor(vibrant);





                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);

              //          bottomTabLayout.setBackgroundColor(vibrant);


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
                mp.seekTo(posit);

                mp.start();

                File file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                        + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));

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
                // myIcon3.setTint(play);
                mp.setVolume(2.5f, 2.5f);
                totalTime = mp.getDuration();
                requestAudioPermissions();
                // Position Bar


                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                    @Override
                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                        if (b) {
                            System.out.println(i * mp.getDuration() / 100);
                            mp.seekTo(i * mp.getDuration() / 100);
                            //    positionBar.setProgress(i*mp.getDuration()/100);
                            waveformSeekBar.setProgress(i);
                            //pro = progress;
                        }
                    }
                });

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
        else{
            {

                System.out.println(pro);

                //startService(view);
                // System.out.println("click");
               // Media_list_activity.position = position;


                if (mp != null) mp.stop();
                mp = new MediaPlayer();

                isClicked = true;


                // Media Player

                prefm = PreferenceManager.getDefaultSharedPreferences(context);
                position = prefm.getInt("positionnow", 0);

                title = playqueue.get(position).getTitle();
                artist = playqueue.get(position).getArtist();
                album = playqueue.get(position).getAlbum();


                textView.setText(title);
                textView1.setText(artist);

                titleq = playqueue.get(position).getImagepath();
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
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
                        int vibrant = Color.WHITE;
                        int vibrantLight = palette.getLightVibrantColor(defaultValue);
                        int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                        int muted = palette.getMutedColor(defaultValue);
                        int mutedLight = palette.getLightMutedColor(defaultValue);
                        int mutedDark = Color.parseColor("#DF0974");

                        PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefm.edit();
                        editor.putInt("vibrant",vibrant);
                        editor.putInt("muted",mutedDark);
                        editor.commit();
                        mutedColor = Color.parseColor("#DF0974");



                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                        remainingTimeLabel.setTextColor(mutedDark);
                        elapsedTimeLabel.setTextColor(mutedDark);

                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                       // toolbar.getNavigationIcon().setTint(mutedDark);
                    //    button.setBackground(icon);
                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                        int type = prefs1.getInt("cond", 0);
                        if (type == 0) {

                        } else if (type == 1) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_loop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        } else if (type == 2) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_unloop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        }


                        // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                        //fab.setBackgroundColor(mutedDark);
                        play = (Color.parseColor("#DF0974"));
                        pause = (Color.parseColor("#DF0974"));
                        playBtn.setColor(play);
                        Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                        Drawable myIcon33 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        myIcon3.setTint(mutedDark);
                        myIcon2.setTint(mutedDark);

                        mLrcView.setIndicatorTextColor(play);
                        mLrcView.setCurrentIndicateLineTextColor(play);
                        mLrcView.setlrcCurrentTextColor(play);
                        //mLrcView.setLrcTextColor(play);
                        Drawable myIcons1 = context.getResources().getDrawable(R.drawable.play);
                        myIcons1.setTint(play);
                        mLrcView.setPlayDrawable(myIcons1);

                        textView.setTextColor(Color.parseColor("#DF0974"));
                        textView1.setTextColor(Color.parseColor("#DF0974"));
                        titleS.setText(title);
                        desS.setText(artist);
                        titleS.setTextColor(Color.parseColor("#DF0974"));
                        desS.setTextColor(Color.parseColor("#DF0974"));

                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                        waveformSeekBar.setWaveProgressColor(mutedDark);
                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);



                        linearLayout.setBackgroundColor(vibrant);



                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);

           //             bottomTabLayout.setBackgroundColor(vibrant);


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
                        + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));

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
                myIcon3.setTint(play);
                myIcon2.setTint(play);
                mp.setVolume(2.5f, 2.5f);
                totalTime = mp.getDuration();
                requestAudioPermissions();


                new YourAsyncTask(abspath[0]).execute();

                //   System.out.println(abspath[0]);

                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                    @Override
                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                        if (b) {
                            System.out.println(i * mp.getDuration() / 100);
                            mp.seekTo(i * mp.getDuration() / 100);
                            //    positionBar.setProgress(i*mp.getDuration()/100);
                            waveformSeekBar.setProgress(i);
                            //pro = progress;
                        }
                    }
                });
                // Position Bar


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
            }
        }
    }

    public void nxtsong() {

        if(theme_changed == false){
            // startService(view);
            System.out.println(pro);


            if (mp != null) mp.stop();
            mp = new MediaPlayer();


            position = prefm.getInt("positionnow",0);

            System.out.println(position);

                if (position + 1 >= playqueue.size()) {
                    position = -1;
                }
                position++;
                prefm = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefm.edit();
                editor.putInt("positionnow",position);
                editor.commit();



            {

                // playBtn = (Button) findViewById(R.id.playBtn);
                // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
                //  remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

                // Media Player


                    title = playqueue.get(position).getTitle();
                    artist = playqueue.get(position).getArtist();
                    album = playqueue.get(position).getAlbum();





                textView.setText(title);
                textView1.setText(artist);

                titleS.setText(title);
                desS.setText(artist);

                    titleq = playqueue.get(position).getImagepath();




                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
                InputStream in;
                bm = null;
                try {
                    in = res.openInputStream(uri);

                    bm = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    InputStream is = context.getResources().openRawResource(R.raw.image);
                    bm = BitmapFactory.decodeStream(is);
                }



                {
                        // Use generated instance
                        //work with the palette here
                        int defaultValue = 0x000000;
                        int vibrant = getDominantColor(bm);
                        int mutedDark = getComplimentColor(bm);
                        mutedColor = mutedDark;


                        PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor1 = prefm.edit();
                        editor1.putInt("vibrant",vibrant);
                        editor1.putInt("muted",mutedDark);
                        editor1.commit();

                        vibran = vibrant;
                        muted = mutedDark;



                        play = (mutedDark);
                        pause = (mutedDark);
                        // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                        playBtn.setColor(play);
                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                        remainingTimeLabel.setTextColor(mutedColor);
                        elapsedTimeLabel.setTextColor(mutedColor);

                        //  fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                       // toolbar.getNavigationIcon().setTint(mutedDark);


                    //    button.setBackground(icon);

                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                        int type = prefs1.getInt("cond", 0);
                        if (type == 0) {

                        } else if (type == 1) {
                            try {
                                Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_loop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            }catch (Exception e){

                            }
                        } else if (type == 2) {
                            try {
                                Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_unloop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            }catch (Exception e){

                            }
                        }


                        Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                        Drawable myIcon33 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        textView.setTextColor(mutedDark);
                        textView1.setTextColor(mutedDark);
                        waveformSeekBar.setWaveProgressColor(mutedDark);
                        // add_phone.setBackgroundColor(vibrant);
                        LinearLayout linearLayout = activity.findViewById(R.id.medic_base);

                        linearLayout.setBackgroundColor(vibrant);




                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);

                  //      bottomTabLayout.setBackgroundColor(vibrant);
//
                    }



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
                File file;

                file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                            + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));





                System.out.println(file);
                try {
                    List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);

                    mLrcView.setLrcData(lrcs);
                }catch (Exception e){

                }
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
                // myIcon3.setTint(play);
                mp.setVolume(2.5f, 2.5f);
                totalTime = mp.getDuration();
                requestAudioPermissions();
                // Position Bar


                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                    @Override
                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                        if (b) {
                            System.out.println(i * mp.getDuration() / 100);
                            mp.seekTo(i * mp.getDuration() / 100);
                            //    positionBar.setProgress(i*mp.getDuration()/100);
                            waveformSeekBar.setProgress(i);
                            //pro = progress;
                        }
                    }
                });
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
        }else{
            {

                System.out.println(pro);


                if (mp != null) mp.stop();
                mp = new MediaPlayer();

                position = prefm.getInt("positionnow",0);
                int choice = prefm.getInt("queue",0);


                    if (position + 1 >= playqueue.size()) {
                        position = -1;
                    }
                    position++;
                    PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefm.edit();
                    editor.putInt("positionnow",position);
                    editor.commit();

                isClicked = true;


                // Media Player


                    title = playqueue.get(position).getTitle();
                    artist = playqueue.get(position).getArtist();
                    album = playqueue.get(position).getAlbum();




                textView.setText(title);
                textView1.setText(artist);


                    titleq = playqueue.get(position).getImagepath();


                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
                InputStream in;
                bm = null;
                try {
                    in = res.openInputStream(uri);

                    bm = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    InputStream is = context.getResources().openRawResource(R.raw.image);
                    bm = BitmapFactory.decodeStream(is);
                }


                {
                        // Use generated instance
                        //work with the palette here
                        int defaultValue = 0x000000;
                        int vibrant = Color.WHITE;
                        int mutedDark = Color.parseColor("#DF0974");

                        mutedColor = Color.parseColor("#DF0974");

                        PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor1 = prefm.edit();
                        editor1.putInt("vibrant",vibrant);
                        editor1.putInt("muted",mutedDark);
                        editor1.commit();



                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                        remainingTimeLabel.setTextColor(mutedDark);
                        elapsedTimeLabel.setTextColor(mutedDark);

                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                     ///
                        // toolbar.getNavigationIcon().setTint(mutedDark);

                    //    button.setBackground(icon);
                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                        int type = prefs1.getInt("cond", 0);
                        if (type == 0) {

                        } else if (type == 1) {
                            try {
                                Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_loop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            }catch (Exception e){

                            }
                        } else if (type == 2) {
                            try {
                                Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_unloop);
                                myIcon3.setTint(mutedDark);
                                loop.setBackground(myIcon3);
                            }catch (Exception e){

                            }
                        }


                        // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                        //fab.setBackgroundColor(mutedDark);
                        play = (Color.parseColor("#DF0974"));
                        pause = (Color.parseColor("#DF0974"));
                        playBtn.setColor(play);
                        Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                        Drawable myIcon33 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        myIcon3.setTint(mutedDark);
                        myIcon2.setTint(mutedDark);

                    mLrcView.setIndicatorTextColor(play);
                    mLrcView.setCurrentIndicateLineTextColor(play);
                    mLrcView.setlrcCurrentTextColor(play);

                        //mLrcView.setLrcTextColor(play);
                        Drawable myIcons1 = context.getResources().getDrawable(R.drawable.play);
                        myIcons1.setTint(play);
                        mLrcView.setPlayDrawable(myIcons1);

                        textView.setTextColor(Color.parseColor("#DF0974"));
                        textView1.setTextColor(Color.parseColor("#DF0974"));
                        titleS.setText(title);
                        desS.setText(artist);
                        titleS.setTextColor(Color.parseColor("#DF0974"));
                        desS.setTextColor(Color.parseColor("#DF0974"));

                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                        waveformSeekBar.setWaveProgressColor(mutedDark);
                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                        LinearLayout linearLayout = activity.findViewById(R.id.medic_base);


                        linearLayout.setBackgroundColor(vibrant);



                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);

             //           bottomTabLayout.setBackgroundColor(vibrant);


                    }



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

                File file;

                     file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                            + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));



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

                Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                myIcon2.setTint(play);
                playBtn.setBackground(myIcon2);
                myIcon3.setTint(play);
                myIcon2.setTint(play);
                mp.setVolume(2.5f, 2.5f);
                totalTime = mp.getDuration();
                requestAudioPermissions();


                new YourAsyncTask(abspath[0]).execute();

                //   System.out.println(abspath[0]);

                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                    @Override
                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                        if (b) {
                            System.out.println(i * mp.getDuration() / 100);
                            mp.seekTo(i * mp.getDuration() / 100);
                            //    positionBar.setProgress(i*mp.getDuration()/100);
                            waveformSeekBar.setProgress(i);
                            //pro = progress;
                        }
                    }
                });
                // Position Bar


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
            }
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

            Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG);

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

    public void GetAllMediaMp3Files1() {

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

            Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG);

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
                playqueue.add(audio);


            } while (cursor.moveToNext());



            List<Audio> audioList = new ArrayList<>();

            prefm = PreferenceManager.getDefaultSharedPreferences(context);
            for (int i = 0; i < playqueue.size(); i++) {
                if(playqueue.get(i).getTitle().trim().equals(ListElementsArrayList.get(prefm.getInt("positionnow",position)).getTitle().trim())){
                    prefm = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefm.edit();
                    editor.putInt("positionnow",i);
                    editor.commit();
                }
            }

            audioList.add(playqueue.get(prefm.getInt("positionnow",position)));
            Collections.shuffle(playqueue);
            for (int i = 0; i < playqueue.size() ; i++) {
                if(!playqueue.get(i).getTitle().trim().equals(audioList.get(0).getTitle().trim())){
                    audioList.add(playqueue.get(i));
                }
            }

            playqueue.clear();
            playqueue.addAll(audioList);



            PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefm.edit();
            editor.putInt("positionnow",0);
            editor.commit();




            System.out.println(playqueue.get(prefm.getInt("positionnow",position)).getTitle());


            //System.out.println(ListElementsArrayList.size());
            //System.out.println(Albumid.size());
        }
    }

    // Creating Runtime permission function.
    public void AndroidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    getActivity(),
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
                            getActivity(),
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

        final Cursor mInternalCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.TITLE + "=?",
                new String[]{songTitle},
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        final Cursor mExternalCursor = context.getContentResolver().query(
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
        if(theme_changed == false){ System.out.println(pro);

            // startService(view);


            if (mp != null) mp.stop();
            mp = new MediaPlayer();

            position = prefm.getInt("positionnow",0);



                if (position - 1 < 0) {
                    position = playqueue.size();
                }
                position--;
                PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefm.edit();
                editor.putInt("positionnow",position);
                editor.commit();



            {

                    title = playqueue.get(position).getTitle();
                    artist = playqueue.get(position).getArtist();
                    album = playqueue.get(position).getAlbum();





                textView.setText(title);
                textView1.setText(artist);

                titleS.setText(title);
                desS.setText(artist);


                    titleq = playqueue.get(position).getImagepath();


                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
                InputStream in;
                bm = null;
                try {
                    in = res.openInputStream(uri);

                    bm = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    InputStream is = context.getResources().openRawResource(R.raw.image);
                    bm = BitmapFactory.decodeStream(is);
                }

                {
                        // Use generated instance
                        //work with the palette here
                        int defaultValue = 0x0000FF;
                        int vibrant = getDominantColor(bm);
                        int mutedDark = getComplimentColor(bm);

                        PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editorw = prefm.edit();
                        editorw.putInt("vibrant",vibrant);
                        editorw.putInt("muted",mutedDark);
                        editorw.commit();

                        mutedColor = mutedDark;





                        play = (mutedDark);
                        pause = (mutedDark);
                        playBtn.setColor(play);
                        //fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));

                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                        remainingTimeLabel.setTextColor(mutedColor);
                        elapsedTimeLabel.setTextColor(mutedColor);

                        //fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
              //          toolbar.getNavigationIcon().setTint(mutedDark);


                    //    button.setBackground(icon);
                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                        int type = prefs1.getInt("cond", 0);
                        if (type == 0) {

                        } else if (type == 1) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_loop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        } else if (type == 2) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_unloop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        }


                        Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        //  playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

                        Drawable myIcon33 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        textView.setTextColor(mutedDark);
                        textView1.setTextColor(mutedDark);
                        // add_phone.setBackgroundColor(vibrant);
                        LinearLayout linearLayout = activity.findViewById(R.id.medic_base);

                        waveformSeekBar.setWaveProgressColor(mutedDark);
                        linearLayout.setBackgroundColor(vibrant);





                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);

                  //      bottomTabLayout.setBackgroundColor(vibrant);


                    }



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
                File file;

                   file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                            + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));




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
                Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                myIcon2.setTint(play);
                playBtn.setBackground(myIcon2);
                // playBtn.setBackgroundColor(play);
                mp.setVolume(2.5f, 2.5f);
                totalTime = mp.getDuration();

                requestAudioPermissions();
                // Position Bar


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
        }else{

            {

                if (mp != null) mp.stop();
                mp = new MediaPlayer();

                position = prefm.getInt("positionnow",0);



                    if (position - 1 < 0) {
                        position = playqueue.size();
                    }
                    position--;
                    PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefm.edit();
                    editor.putInt("positionnow",position);
                    editor.commit();

                isClicked = true;


                // Media Player


                    title = playqueue.get(position).getTitle();
                    artist = playqueue.get(position).getArtist();
                    album = playqueue.get(position).getAlbum();




                textView.setText(title);
                textView1.setText(artist);

                    titleq = playqueue.get(position).getImagepath();




                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
                InputStream in;
                bm = null;
                try {
                    in = res.openInputStream(uri);

                    bm = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    InputStream is = context.getResources().openRawResource(R.raw.image);
                    bm = BitmapFactory.decodeStream(is);
                }


                {
                        // Use generated instance
                        //work with the palette here
                        int defaultValue = 0x000000;
                        int vibrant = Color.WHITE;
                        int mutedDark = Color.parseColor("#DF0974");

                        vibran = vibrant;
                        muted = mutedDark;

                        PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editora = prefm.edit();
                        editora.putInt("vibrant",vibrant);
                        editora.putInt("muted",mutedDark);
                        editora.commit();

                        mutedColor = Color.parseColor("#DF0974");


                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                        remainingTimeLabel.setTextColor(mutedDark);
                        elapsedTimeLabel.setTextColor(mutedDark);

                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
               //         toolbar.getNavigationIcon().setTint(mutedDark);

                   //     button.setBackground(icon);
                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                        int type = prefs1.getInt("cond", 0);
                        if (type == 0) {

                        } else if (type == 1) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_loop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        } else if (type == 2) {
                            Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_unloop);
                            myIcon3.setTint(mutedDark);
                            loop.setBackground(myIcon3);
                        }


                        // fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Media_list_activity.this,mutedDark)));
                        //fab.setBackgroundColor(mutedDark);
                        play = (Color.parseColor("#DF0974"));
                        pause = (Color.parseColor("#DF0974"));
                        playBtn.setColor(play);
                        Drawable myIcon3 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        Drawable myIcon1 = context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                        Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);

                        myIcon3.setTint(play);
                        myIcon.setTint(play);
                        myIcon1.setTint(play);
                        myIcon2.setTint(play);
                        nxtBtn.setBackground(myIcon3);
                        pauseBtn.setBackground(myIcon);
                        playBtn.change(false);
                        //playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                        Drawable myIcon33 = context.getResources().getDrawable(R.drawable.ic_next_fill);
                        Drawable myIcon31 = context.getResources().getDrawable(R.drawable.ic_prev_fill);
                        myIcon33.setTint(play);
                        myIcon31.setTint(play);
                        ply.setBackground(myIcon2);
                        pre.setBackground(myIcon31);
                        nxt.setBackground(myIcon33);

                        myIcon3.setTint(mutedDark);
                        myIcon2.setTint(mutedDark);

                    mLrcView.setIndicatorTextColor(play);
                    mLrcView.setCurrentIndicateLineTextColor(play);
                    mLrcView.setlrcCurrentTextColor(play);
                        //mLrcView.setLrcTextColor(play);
                        Drawable myIcons1 = context.getResources().getDrawable(R.drawable.play);
                        myIcons1.setTint(play);
                        mLrcView.setPlayDrawable(myIcons1);

                        textView.setTextColor(Color.parseColor("#DF0974"));
                        textView1.setTextColor(Color.parseColor("#DF0974"));
                        titleS.setText(title);
                        desS.setText(artist);
                        titleS.setTextColor(Color.parseColor("#DF0974"));
                        desS.setTextColor(Color.parseColor("#DF0974"));

                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                        waveformSeekBar.setWaveProgressColor(mutedDark);
                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                        LinearLayout linearLayout = activity.findViewById(R.id.medic_base);



                        linearLayout.setBackgroundColor(vibrant);


                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);

                  //      bottomTabLayout.setBackgroundColor(vibrant);


                    }



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

                File file;

                    file = new File(Environment.getExternalStorageDirectory() + "/Lyrics/"
                            + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));




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

                Drawable myIcon2 = context.getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                myIcon2.setTint(play);
                playBtn.setBackground(myIcon2);
                myIcon3.setTint(play);
                myIcon2.setTint(play);
                mp.setVolume(2.5f, 2.5f);
                totalTime = mp.getDuration();
                requestAudioPermissions();


                new YourAsyncTask(abspath[0]).execute();

                //   System.out.println(abspath[0]);

                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                    @Override
                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                        if (b) {
                            System.out.println(i * mp.getDuration() / 100);
                            mp.seekTo(i * mp.getDuration() / 100);
                            //    positionBar.setProgress(i*mp.getDuration()/100);
                            waveformSeekBar.setProgress(i);
                            //pro = progress;
                        }
                    }
                });
                // Position Bar


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
            }
        }


    }


    public void stopService() {
        serviceIntent.setClass(context, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        getActivity().startService(serviceIntent);
    }


    public void startService() {
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        serviceIntent.putExtra("p", (prefm.getInt("positionnow", 0)));
        getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public int getComplimentColor(Bitmap bitmap) {

        MediaNotificationProcessor processor = new MediaNotificationProcessor(context, bitmap); // can use drawable

        int backgroundColor = processor.getBackgroundColor();
        int primaryTextColor = processor.getPrimaryTextColor();
        int secondaryTextColor = processor.getSecondaryTextColor();


        // get existing colors

        return primaryTextColor;
    }


    @Override
    public void onDetach(){
        super.onDetach();




        //if (mVisualizer != null)
          //  mVisualizer.release();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
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

    public int[] convert(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[8192];

        for (int readNum; (readNum = fis.read(b)) != -1;) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        IntBuffer intBuf =
                ByteBuffer.wrap(bytes)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);

        return array;
    }

    @Override
    public void doBack() {





        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));

      //  context.unregisterReceiver(broadcastReceiver);

       // music_base.activity.onBackPressed();
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(50);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }


    @Override
    public void onResume(){
        super.onResume();
        context.registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.NEXT_ACTION));
        context.registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.PREV_ACTION));
        context.registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.LOAD_ACTION));

    }
    @Override
    public void onPause(){
        super.onPause();
       // context.unregisterReceiver(broadcastReceiver);
    }


        @Override
        public void onViewCreated(View viewq, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(viewq, savedInstanceState);

            ((music_base) getActivity()).setOnBackPressedListener(this);

            View view  = getActivity().findViewById(R.id.content_music);


            RelativeLayout relativeLayout = getActivity().findViewById(R.id.content_music);
            LinearLayout linearLayout = relativeLayout.findViewById(R.id.listview);
            listView = relativeLayout.findViewById(R.id.listviews);
            gridView = relativeLayout.findViewById(R.id.gridviews);


            SharedPreferences sharedPreferences = getContext().getSharedPreferences("ViewMode", MODE_PRIVATE);
            currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview

            if(currentViewMode==0){
                /// stubList.setVisibility(View.VISIBLE);
                switchView();
            }else{
                //  stubGrid.setVisibility(View.VISIBLE);
                switchView();
            }


            myView = getActivity().findViewById(R.id.sliding_layout);


            add_phone = myView.findViewById(R.id.relativeLayoutMain);
            media_base = myView.findViewById(R.id.medic_base);















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


            context = getActivity().getApplicationContext();
            activity = getActivity();


            AndroidRuntimePermission();
            serviceIntent = new Intent(context, NotificationService.class);

            if (ListElementsArrayList.isEmpty())
                GetAllMediaMp3Files();

            if (playqueue.isEmpty())
                GetAllMediaMp3Files1();



            prefm = PreferenceManager.getDefaultSharedPreferences(context);
            position = prefm.getInt("positionnow", 0);

            System.out.println(prefm.getInt("positionnow", 0));

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");


            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(playqueue.get(position).getImagepath()));
            ContentResolver res = getActivity().getContentResolver();
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

            loop = getActivity().findViewById(R.id.loops);
            SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

            title = playqueue.get(position).getTitle();
            artist = playqueue.get(position).getArtist();
            album = playqueue.get(position).getAlbum();


            textView.setText(title);
            textView1.setText(artist);


            titleq = playqueue.get(position).getImagepath();
            sArtworkUri = Uri.parse("content://media/external/audio/albumart");

            uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
            res = getActivity().getContentResolver();

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




            waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
            try {
                String[] audio = getAudioPath(title);
                waveformSeekBar.setSample(convert(audio[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //waveformSeekBar.setSample(data);
            waveformSeekBar.setWaveProgressColor(play);

            //Load animation
            Animation slide_down = AnimationUtils.loadAnimation(context.getApplicationContext(),
                    R.anim.slide_down);

            Animation slide_up = AnimationUtils.loadAnimation(context.getApplicationContext(),
                    R.anim.slide_up);

// Start animation
            if(isDark) {
                toolbar.setBackgroundColor(Color.BLACK);
                add_phone.setBackgroundColor(Color.BLACK);
            }
            else {
                toolbar.setBackgroundColor(Color.WHITE);
                //toolbar.getNavigationIcon().setTint(Color.BLACK);
                add_phone.setBackgroundColor(Color.WHITE);
            }


            if(mp!=null) {
                if(theme_changed==false) {
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





                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                       // music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, R.color.uou));
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                        music_base.button.setBackground(icon);

                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);
                                        //      toolbar.getNavigationIcon().setTint(mutedDark);

                                        //                button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);
                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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


                                        //  add_phone.setAlpha(1.0f);

                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);

                                        linearLayout.setBackgroundColor(vibrant);
                                        //linearLayout.setAlpha(1.0f);


                                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);

                                        //    bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);
                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);

                                media_base.setVisibility(View.VISIBLE);

                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                                myIcon2.setTint(play);
                                playBtn.setBackground(myIcon2);

                                totalTime = mp.getDuration();

                                vibrant = getDominantColor(bitmap);
                                mutedDark = getComplimentColor(bitmap);

                                remainingTimeLabel.setTextColor(mutedColor);
                                elapsedTimeLabel.setTextColor(mutedColor);


                                String[] abspath = getAudioPath(title);
                                new YourAsyncTask(abspath[0]).execute();


                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });


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



                                       // music_base.toolbar.getNavigationIcon().setTint(mutedDark);

                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(mutedDark);
                                        music_base.button.setBackground(icon);


                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //         toolbar.getNavigationIcon().setTint(Color.WHITE);


                                        //           button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);
                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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


                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        // add_phone.setBackgroundColor(vibrant);
                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);

                                        linearLayout.setBackgroundColor(vibrant);


                                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);

//                                    bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);
                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);


                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
                                myIcon2.setTint(play);
                                playBtn.setBackgroundTintList(ColorStateList.valueOf(mutedDark));


                                // Position Bar
                                totalTime = mp.getDuration();

                                vibrant = getDominantColor(bitmap);
                                mutedDark = getComplimentColor(bitmap);

                                remainingTimeLabel.setTextColor(mutedColor);
                                elapsedTimeLabel.setTextColor(mutedColor);

                                String[] abspath = getAudioPath(album);
                                new YourAsyncTask(abspath[0]).execute();
                                // waveformSeekBar.setSampleFrom(abspath[0],false);
                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });


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
                    } else if (!mp.isPlaying()) {
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





                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                       // music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, R.color.uou));
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                        music_base.button.setBackground(icon);


                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));

                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));


//                                    button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);
                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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

                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        //    add_phone.setBackgroundColor(vibrant);
                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);

                                        linearLayout.setBackgroundColor(vibrant);


                                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);

                                        //       bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);
                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
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

                                vibrant = getDominantColor(bitmap);
                                mutedDark = getComplimentColor(bitmap);

                                remainingTimeLabel.setTextColor(mutedColor);
                                elapsedTimeLabel.setTextColor(mutedColor);

                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });
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
                                            Window window = getActivity().getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(vibrant);
                                            getActivity().setTitleColor(vibrant);
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrant));
                                            Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                            if (actionBarToolbar != null)
                                                actionBarToolbar.setTitleTextColor(mutedDark);
                                        }
                                      //  music_base.toolbar.getNavigationIcon().setTint(mutedDark);

                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(mutedDark);
                                        music_base.button.setBackground(icon);


                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //             toolbar.getNavigationIcon().setTint(Color.WHITE);

                                        //       button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);
                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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


                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        //   add_phone.setBackgroundColor(vibrant);
                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);

                                        linearLayout.setBackgroundColor(vibrant);


                                        ImageButton enablelrc = myView.findViewById(R.id.enablelrc);
                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);

                                        //   bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);

                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);

                                Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
                                myIcon2.setTint(play);
                                playBtn.setBackground(myIcon2);


                                // Position Bar
                                totalTime = mp.getDuration();

                                vibrant = getDominantColor(bitmap);
                                mutedDark = getComplimentColor(bitmap);
                                remainingTimeLabel.setTextColor(mutedColor);
                                elapsedTimeLabel.setTextColor(mutedColor);

                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });


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

                                        int vibrant = Color.WHITE;
                                        int mutedDark = Color.parseColor("#DF0974");



                                        mutedColor = Color.parseColor("#DF0974");

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getActivity().getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(ContextCompat.getColor(context, R.color.white));
                                            getActivity().setTitleColor(ContextCompat.getColor(context, R.color.uou));
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
                                            Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                            if (actionBarToolbar != null)
                                                actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.uou));
                                        }

                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //                toolbar.getNavigationIcon().setTint(mutedDark);

                                        //    button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                    //    music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, R.color.uou));
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                        music_base.button.setBackground(icon);




                                        //      toolbar.getNavigationIcon().setTint(mutedDark);

                                        //                button.setBackground(icon);

                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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



                                        if (mp.isPlaying()) {
                                            playBtn.change(false);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                        } else {
                                            playBtn.change(true);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                        }



                                        play = (Color.parseColor("#DF0974"));
                                        pause = (Color.parseColor("#DF0974"));
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
                                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                                        Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                        Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                        myIcon33.setTint(play);
                                        myIcon31.setTint(play);
                                        ply.setBackground(myIcon2);
                                        pre.setBackground(myIcon31);
                                        nxt.setBackground(myIcon33);

                                        myIcon3.setTint(mutedDark);
                                        myIcon2.setTint(mutedDark);

                                        mLrcView.setIndicatorTextColor(play);
                                        mLrcView.setCurrentIndicateLineTextColor(play);
                                        mLrcView.setlrcCurrentTextColor(play);
                                        //mLrcView.setLrcTextColor(play);
                                        Drawable myIcons1 = getResources().getDrawable(R.drawable.play);
                                        myIcons1.setTint(play);
                                        mLrcView.setPlayDrawable(myIcons1);

                                        textView.setTextColor(Color.parseColor("#DF0974"));
                                        textView1.setTextColor(Color.parseColor("#DF0974"));
                                        titleS.setText(title);
                                        desS.setText(artist);
                                        titleS.setTextColor(Color.parseColor("#DF0974"));
                                        desS.setTextColor(Color.parseColor("#DF0974"));

                                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);



                                        linearLayout.setBackgroundColor(vibrant);



                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);


                                        //  add_phone.setAlpha(1.0f);



                                        //    bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);
                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);

                                media_base.setVisibility(View.VISIBLE);



                                totalTime = mp.getDuration();




                                String[] abspath = getAudioPath(title);
                                new YourAsyncTask(abspath[0]).execute();


                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });


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
                                        int vibrant = Color.WHITE;
                                        int mutedDark = Color.parseColor("#DF0974");



                                        mutedColor = Color.parseColor("#DF0974");



                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getActivity().getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(ContextCompat.getColor(context, R.color.white));
                                            getActivity().setTitleColor(ContextCompat.getColor(context, R.color.uou));
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
                                            Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                            if (actionBarToolbar != null)
                                                actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.uou));
                                        }




                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                   //     music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, R.color.uou));
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                        music_base.button.setBackground(icon);



                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //                toolbar.getNavigationIcon().setTint(mutedDark);

                                        //    button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //         toolbar.getNavigationIcon().setTint(Color.WHITE);


                                        //           button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);
                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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
                                        if (mp.isPlaying()) {
                                            playBtn.change(false);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                        } else {
                                            playBtn.change(true);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                        }






                                        titleS.setText(title);
                                        desS.setText(artist);
                                        play = (Color.parseColor("#DF0974"));
                                        pause = (Color.parseColor("#DF0974"));
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
                                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                                        Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                        Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                        myIcon33.setTint(play);
                                        myIcon31.setTint(play);
                                        ply.setBackground(myIcon2);
                                        pre.setBackground(myIcon31);
                                        nxt.setBackground(myIcon33);

                                        myIcon3.setTint(mutedDark);
                                        myIcon2.setTint(mutedDark);

                                        mLrcView.setIndicatorTextColor(play);
                                        mLrcView.setCurrentIndicateLineTextColor(play);
                                        mLrcView.setlrcCurrentTextColor(play);
                                        //mLrcView.setLrcTextColor(play);
                                        Drawable myIcons1 = getResources().getDrawable(R.drawable.play);
                                        myIcons1.setTint(play);
                                        mLrcView.setPlayDrawable(myIcons1);

                                        textView.setTextColor(Color.parseColor("#DF0974"));
                                        textView1.setTextColor(Color.parseColor("#DF0974"));
                                        titleS.setText(title);
                                        desS.setText(artist);
                                        titleS.setTextColor(Color.parseColor("#DF0974"));
                                        desS.setTextColor(Color.parseColor("#DF0974"));

                                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);



                                        linearLayout.setBackgroundColor(vibrant);



                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);

//                                    bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);
                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);





                                // Position Bar
                                totalTime = mp.getDuration();



                                String[] abspath = getAudioPath(album);
                                new YourAsyncTask(abspath[0]).execute();
                                // waveformSeekBar.setSampleFrom(abspath[0],false);
                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });


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
                    } else if (!mp.isPlaying()) {
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
                                        int vibrant = Color.WHITE;
                                        int mutedDark = Color.parseColor("#DF0974");



                                        mutedColor = Color.parseColor("#DF0974");


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getActivity().getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(ContextCompat.getColor(context, R.color.white));
                                            getActivity().setTitleColor(ContextCompat.getColor(context, R.color.uou));
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
                                            Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                            if (actionBarToolbar != null)
                                                actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.uou));
                                        }


                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                 //       music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, R.color.uou));
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                        music_base.button.setBackground(icon);


                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));


                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //                toolbar.getNavigationIcon().setTint(mutedDark);

                                        //    button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);
//                                    button.setBackground(icon);

                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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

                                        if (mp.isPlaying()) {
                                            playBtn.change(false);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                        } else {
                                            playBtn.change(true);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                        }


                                        media_base.setAlpha(1.0f);


                                        titleS.setText(title);
                                        desS.setText(artist);

                                        play = (Color.parseColor("#DF0974"));
                                        pause = (Color.parseColor("#DF0974"));
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
                                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                                        Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                        Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                        myIcon33.setTint(play);
                                        myIcon31.setTint(play);
                                        ply.setBackground(myIcon2);
                                        pre.setBackground(myIcon31);
                                        nxt.setBackground(myIcon33);

                                        myIcon3.setTint(mutedDark);
                                        myIcon2.setTint(mutedDark);

                                        mLrcView.setIndicatorTextColor(play);
                                        mLrcView.setCurrentIndicateLineTextColor(play);
                                        mLrcView.setlrcCurrentTextColor(play);
                                        //mLrcView.setLrcTextColor(play);
                                        Drawable myIcons1 = getResources().getDrawable(R.drawable.play);
                                        myIcons1.setTint(play);
                                        mLrcView.setPlayDrawable(myIcons1);

                                        textView.setTextColor(Color.parseColor("#DF0974"));
                                        textView1.setTextColor(Color.parseColor("#DF0974"));
                                        titleS.setText(title);
                                        desS.setText(artist);
                                        titleS.setTextColor(Color.parseColor("#DF0974"));
                                        desS.setTextColor(Color.parseColor("#DF0974"));

                                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);



                                        linearLayout.setBackgroundColor(vibrant);



                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);


                                        //       bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);
                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);




                                // Position Bar
                                totalTime = mp.getDuration();


                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });
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
                                        int vibrant = Color.WHITE;
                                        int mutedDark = Color.parseColor("#DF0974");



                                        mutedColor = Color.parseColor("#DF0974");


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getActivity().getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(ContextCompat.getColor(context, R.color.white));
                                            getActivity().setTitleColor(ContextCompat.getColor(context, R.color.uou));
                                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
                                            Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                            if (actionBarToolbar != null)
                                                actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.uou));
                                        }


                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                      //  music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context, R.color.uou));
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                        icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                        music_base.button.setBackground(icon);

                                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                                        remainingTimeLabel.setTextColor(mutedDark);
                                        elapsedTimeLabel.setTextColor(mutedDark);

                                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        //                toolbar.getNavigationIcon().setTint(mutedDark);

                                        //    button.setBackground(icon);
                                        mVisualizer.setColor(mutedColor);

                                        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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


                                        if (mp.isPlaying()) {
                                            playBtn.change(false);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                                        } else {
                                            playBtn.change(true);
                                            // playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                                        }

                                        titleS.setText(title);
                                        desS.setText(artist);


                                        play = (Color.parseColor("#DF0974"));
                                        pause = (Color.parseColor("#DF0974"));
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
                                        playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                                        Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                                        Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                                        myIcon33.setTint(play);
                                        myIcon31.setTint(play);
                                        ply.setBackground(myIcon2);
                                        pre.setBackground(myIcon31);
                                        nxt.setBackground(myIcon33);

                                        myIcon3.setTint(mutedDark);
                                        myIcon2.setTint(mutedDark);

                                        mLrcView.setIndicatorTextColor(play);
                                        mLrcView.setCurrentIndicateLineTextColor(play);
                                        mLrcView.setlrcCurrentTextColor(play);
                                        //mLrcView.setLrcTextColor(play);
                                        Drawable myIcons1 = getResources().getDrawable(R.drawable.play);
                                        myIcons1.setTint(play);
                                        mLrcView.setPlayDrawable(myIcons1);

                                        textView.setTextColor(Color.parseColor("#DF0974"));
                                        textView1.setTextColor(Color.parseColor("#DF0974"));
                                        titleS.setText(title);
                                        desS.setText(artist);
                                        titleS.setTextColor(Color.parseColor("#DF0974"));
                                        desS.setTextColor(Color.parseColor("#DF0974"));

                                        waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                                        waveformSeekBar.setWaveProgressColor(mutedDark);
                                        waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);



                                        linearLayout.setBackgroundColor(vibrant);



                                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        myIcon3o.setTint(mutedDark);
                                        showlrc.setBackground(myIcon3o);

                                        //   bottomTabLayout.setBackgroundColor(vibrant);

                                    }
                                });


                                image = myView.findViewById(R.id.albumart);
                                image.setImageBitmap(bitmap);

                                bm = bitmap;
                                if (isShowlrc) {
                                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                                    image.setImageBitmap(blurred);
                                }
                                songI.setImageBitmap(bitmap);




                                // Position Bar
                                totalTime = mp.getDuration();


                                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                                    @Override
                                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                                        if (b) {
                                            System.out.println(i * mp.getDuration() / 100);
                                            mp.seekTo(i * mp.getDuration() / 100);
                                            //    positionBar.setProgress(i*mp.getDuration()/100);
                                            waveformSeekBar.setProgress(i);
                                            //pro = progress;
                                        }
                                    }
                                });


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


                    playBtn.setEnabled(true);
                    pauseBtn.setEnabled(true);
                    nxtBtn.setEnabled(true);
                    showlrc.setEnabled(true);
                    if(isShowlrc){
                        mLrcView.setVisibility(View.VISIBLE);

                        Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
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
                   // System.out.println("onPanelSlide, offset " + slideOffset);
                    slideold[0] = slideOffset;


                    //    add_phone.setAlpha((1.0f-slideold[0]));
                    media_base.setAlpha(slideold[0]);
                    add_phone.setAlpha(1.0f-slideold[0]);
                    toolbar.setAlpha(1.0f-slideold[0]);
                    //.setAlpha(1.0f-slideold[0]);



                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                  //  Log.i(TAG, "onPanelStateChanged " + newState);
                    if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){






                        listView.setEnabled(false);

                        gridView.setEnabled(false);

                        volumeBar.setEnabled(true);

                        slideDown(bottomTabLayout);

                        playBtn.setEnabled(true);
                        pauseBtn.setEnabled(true);
                        nxtBtn.setEnabled(true);
                        showlrc.setEnabled(true);
                        //toolbar.setBackground(null);
                        if(isShowlrc){
                            mLrcView.setVisibility(View.VISIBLE);


                            Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
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

                        slideUp(bottomTabLayout);

                        playBtn.setEnabled(false);
                        pauseBtn.setEnabled(false);
                        nxtBtn.setEnabled(false);
                        showlrc.setEnabled(false);
                        mLrcView.setVisibility(View.INVISIBLE);
                        if(isShowlrc){


                            Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
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





            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.NEXT_ACTION));
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.PREV_ACTION));
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.LOAD_ACTION));

           // mVisualizer = getActivity().findViewById(R.id.blast);

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
                            try {
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_unloop);
                                myIcon3.setTint(finalMutedDark);
                                loop.setBackground(myIcon3);
                            }catch (Exception e){

                            }

                            SharedPreferences.Editor editor = prefs1.edit();
                            editor.putInt("cond", 2);
                            editor.commit();

                        }
                    } else {
                        if (mp.isPlaying()) {
                            mp.setLooping(false);
                            try {
                                Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_loop);
                                myIcon3.setTint(finalMutedDark);
                                loop.setBackground(myIcon3);
                            }catch (Exception e){

                            }

                            SharedPreferences.Editor editor = prefs1.edit();
                            editor.putInt("cond", 1);
                            editor.commit();

                        }
                    }
                }
            });






            songI.setImageBitmap(bitmap);
            titleS.setText(playqueue.get(position).getTitle());
            desS.setText(playqueue.get(position).getArtist());


            // slideUp1(add_phone);
         //   add_phone.setBackgroundColor(play);
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

            mLrcView = getActivity().findViewById(R.id.lrc_view);
            // mLrcView.setVisibility(View.GONE);


            enablelrc = myView.findViewById(R.id.enablelrc);
            Bitmap finalBitmap = bitmap;

            enablelrc.setOnClickListener(new View.OnClickListener() {

                int oo = 0;

                @Override
                public void onClick(View view) {
                    oo++;
                    if(oo==1){
                        isShowlrc = true;
                        mLrcView.setVisibility(View.VISIBLE);

                        Bitmap blurred = blurRenderScript(bm, 25);//second parametre is radius
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
                        +playqueue.get(position).getTitle().toLowerCase().replace(" ","_").concat(".lrc"));

                System.out.println(file);
                List<Lrc> lrcs = LrcHelper.parseLrcFromFile(file);
                mLrcView.setLrcData(lrcs);
                if(isShowlrc) {mLrcView.setVisibility(View.VISIBLE);

                    Bitmap blurred = blurRenderScript(bm, 20);//second parametre is radius
                    image.setImageBitmap(blurred);
                }
                else mLrcView.setVisibility(View.INVISIBLE);
            }



            showlrc = myView.findViewById(R.id.showlrc);

            try {
                showlrc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog;
                        try {
                            if (isDark) {
                                alertDialog = new AlertDialog.Builder(getContext(), R.style.DialogurDark);
                            } else {
                                alertDialog = new AlertDialog.Builder(getContext(), R.style.DialogueLight);
                            }
                            alertDialog.setMessage("Lyrics");

                            final EditText input = new EditText(getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            lp.setMargins(45, 15, 45, 15);
                            input.setLayoutParams(lp);
                            input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
                            if (isDark) {


                                input.setBackground(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.edittextstyledark));
                                input.setTextColor(Color.WHITE);

                            } else {
                                input.setBackground(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.editextstyle));
                                input.setTextColor(Color.BLACK);
                            }
                            alertDialog.setView(input);
                            alertDialog.setIcon(R.drawable.common_google_signin_btn_icon_dark);

                            alertDialog.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!input.getText().equals("")) {
                                                save(input, playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));
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
                        }catch (Exception e){

                        }

                    }
                });
            }catch (Exception e){}





            myIcon3 = getResources().getDrawable(R.drawable.ic_next_fill);
            myIcon = getResources().getDrawable(R.drawable.ic_prev_fill);
            myIcon1 = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
            myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);



            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);



            if (isDark && ListElementsArrayList.size() != 0) {

                relativeLayout.setBackgroundColor(Color.BLACK);


                listView.setAdapter(adapter);
                listView.setBackgroundColor(Color.BLACK);
                gridView.setAdapter(adapterG);
                gridView.setBackgroundColor(Color.BLACK);


                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.card_background_dark));
            } else if (!isDark && ListElementsArrayList.size() != 0) {

                relativeLayout.setBackgroundColor(Color.WHITE);




                listView.setBackgroundColor(Color.WHITE);
                gridView.setBackgroundColor(Color.WHITE);
                listView.setAdapter(adapter);
                gridView.setAdapter(adapterG);
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.card_background));

            } else if (isDark && ListElementsArrayList.size() == 0) {




                listView.setBackgroundColor(Color.BLACK);
                gridView.setBackgroundColor(Color.BLACK);
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.card_background_dark));
                relativeLayout.setBackgroundColor(Color.BLACK);


            } else if (!isDark && ListElementsArrayList.size() == 0) {



                listView.setBackgroundColor(Color.WHITE);
                gridView.setBackgroundColor(Color.WHITE);
                relativeLayout.setBackgroundColor(Color.WHITE);
                linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.card_background));



            }


            nxt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    isShowlrc = false;
                    try {
                        nxtsong();
                    }catch (Exception e){

                    }

                }
            });
            nxtBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isShowlrc = false;
                    try {
                        nxtsong();
                    }catch (Exception e){

                    }
                    //myButton.setText("Slide up");



                }
            });

            pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isShowlrc = false;
                    try {
                        prevsong();
                    }catch (Exception e){

                    }

                }
            });
            pauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // startService(view);
                    //startService(view);
                    isShowlrc = false;

                    try {
                        prevsong();
                    }catch (Exception e){

                    }

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


            sort = getActivity().findViewById(R.id.sortsong);
            sort.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    //PopupMenu popup = new PopupMenu(MainActivity.this, sort);
                    //Inflating the Popup using xml file
                    Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                    SharedPreferences prefs =context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);
                    boolean isDark = prefs.getBoolean("isDark", false);
                    if (isDark) {
                        wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE);

                    } else {
                        wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                    }

                    PopupMenu popup = new PopupMenu(wrapper, sort);
                    popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());


                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("Ascending")) {
                                Collections.sort(ListElementsArrayList);
                                //  adapter = new MyListAdapter(getParent());
                                listView = getActivity().findViewById(R.id.listviews);
                                listView.setAdapter(adapter);
                                gridView =  getActivity().findViewById(R.id.gridviews);
                                gridView.setAdapter(adapterG);


                            } else if (item.getTitle().equals("Descending")) {
                                Collections.sort(ListElementsArrayList, Collections.reverseOrder());
                                //adapter = new MyListAdapter(getParent());
                                listView =  getActivity().findViewById(R.id.listviews);
                                listView.setAdapter(adapter);
                                gridView =  getActivity().findViewById(R.id.gridviews);
                                gridView.setAdapter(adapterG);


                            } else if (item.getTitle().equals("Time Added Asc")) {
                                Collections.sort(ListElementsArrayList,
                                        new Comparator<Audio>() {
                                            public int compare(Audio f1, Audio f2) {
                                                return f1.getData().compareTo(f2.getData());
                                            }
                                        });
                                listView =  getActivity().findViewById(R.id.listviews);
                                listView.setAdapter(adapter);
                                gridView =  getActivity().findViewById(R.id.gridviews);
                                gridView.setAdapter(adapterG);


                            }else if (item.getTitle().equals("Time Added Des")) {
                                Collections.sort(ListElementsArrayList,
                                        new Comparator<Audio>() {
                                            public int compare(Audio f1, Audio f2) {
                                                return f2.getData().compareTo(f1.getData());
                                            }
                                        });

                                listView =  getActivity().findViewById(R.id.listviews);
                                listView.setAdapter(adapter);
                                gridView =  getActivity().findViewById(R.id.gridviews);
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
                                SharedPreferences sharedPreferences =context.getSharedPreferences("ViewMode", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("currentViewMode", currentViewMode);
                                editor.commit();
                            }

                            else  if(item.getTitle().equals("Theme Change")){
                                theme_changed = !theme_changed;

                            }

                            Toasty.info(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

           getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.NEXT_ACTION));
           getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.PREV_ACTION));
            getContext().registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.LOAD_ACTION));
            return inflater.inflate(R.layout.fragment_music_list, container, false);
        }

        public void loadsong(int position){

            playqueue.clear();



            PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefm.edit();
            editor.putInt("positionnow",position);
            editor.commit();

            GetAllMediaMp3Files1();

            position = prefm.getInt("positionnow",position);



            if(theme_changed == false){
                System.out.println(pro);

                //startService(view);
                // System.out.println("click");
                //position = position;


                if (mp != null) mp.stop();
                mp = new MediaPlayer();

                isClicked = true;


                // Media Player



                title = playqueue.get(position).getTitle();
                artist = playqueue.get(position).getArtist();
                album = playqueue.get(position).getAlbum();


                textView.setText(title);
                textView1.setText(artist);


                titleq = playqueue.get(position).getImagepath();
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res =context.getContentResolver();
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




                    {
                        // Use generated instance
                        //work with the palette here
                        int defaultValue = 0x000000;
                        int vibrant = getDominantColor(bm);
                        int mutedDark = getComplimentColor(bm);

                        mutedColor = mutedDark;



                        PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editorq = prefm.edit();
                        editorq.putInt("vibrant",vibrant);
                        editorq.putInt("muted",mutedDark);
                        editorq.commit();

                        System.out.println("jkjkjkjkjk"+prefm.getInt("vibrant",0));






                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                     //   music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context,R.color.uou));
                        Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                        icon.setTint(ContextCompat.getColor(context,R.color.uou));
                        music_base.button.setBackground(icon);


                        volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                        volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                        remainingTimeLabel.setTextColor(mutedDark);
                        elapsedTimeLabel.setTextColor(mutedDark);

                        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        //              toolbar.getNavigationIcon().setTint(mutedDark);

                        //    button.setBackground(icon);

                        Glide.with(context).load(bm).apply(bitmapTransform(new BlurTransformation(context, 25))).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    myView.setBackground(resource);
                                }
                            }
                        });
                        mVisualizer.setColor(mutedColor);
                        SharedPreferences prefs1 =context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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
                        int ব্রজে = 10;

                        mLrcView.setIndicatorTextColor(play);
                        mLrcView.setCurrentIndicateLineTextColor(play);
                        mLrcView.setlrcCurrentTextColor(play);

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


                        waveformSeekBar.setWaveProgressColor(mutedDark);

                        LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);

                        linearLayout.setBackgroundColor(vibrant);





                        enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                        Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        myIcon3o.setTint(mutedDark);
                        showlrc.setBackground(myIcon3o);



                      //  bottomTabLayout.setBackgroundColor(vibrant);
                      //  bottomTabLayout.setIndicatorLineColor(mutedDark);


                    }



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
                        + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));

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

                waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                    @Override
                    public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                        if (b) {
                            System.out.println(i * mp.getDuration() / 100);
                            mp.seekTo(i * mp.getDuration() / 100);
                            //    positionBar.setProgress(i*mp.getDuration()/100);
                            waveformSeekBar.setProgress(i);
                            //pro = progress;
                        }
                    }
                });

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
            else{

                {

                    System.out.println(pro);

                    //startService(view);
                    // System.out.println("click");
                    //position = position;


                    if (mp != null) mp.stop();
                    mp = new MediaPlayer();

                    isClicked = true;



                    // Media Player


                    title = playqueue.get(position).getTitle();
                    artist = playqueue.get(position).getArtist();
                    album = playqueue.get(position).getAlbum();


                    textView.setText(title);
                    textView1.setText(artist);

                    titleq = playqueue.get(position).getImagepath();
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                    Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                    ContentResolver res =context.getContentResolver();
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



                    {
                            // Use generated instance
                            //work with the palette here
                            int defaultValue = 0x000000;
                            int vibrant = Color.WHITE;
                            int mutedDark = Color.parseColor("#DF0974");



                            mutedColor = Color.parseColor("#DF0974");


                            if(!isDark) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Window window = getActivity().getWindow();
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    window.setStatusBarColor(ContextCompat.getColor(context, R.color.white));
                                    getActivity().setTitleColor(ContextCompat.getColor(context, R.color.uou));
                                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
                                    Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                    if (actionBarToolbar != null)
                                        actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.uou));
                                }


                                // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                //   music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context,R.color.uou));
                                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                music_base.button.setBackground(icon);
                            }else{
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Window window = getActivity().getWindow();
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    window.setStatusBarColor(ContextCompat.getColor(context, R.color.material_black));
                                    getActivity().setTitleColor(ContextCompat.getColor(context, R.color.uou));
                                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.material_black)));
                                    Toolbar actionBarToolbar = getActivity().findViewById(R.id.toolbar);
                                    if (actionBarToolbar != null)
                                        actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.uou));
                                }


                                // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                                //   music_base.toolbar.getNavigationIcon().setTint(ContextCompat.getColor(context,R.color.uou));
                                Drawable icon = getResources().getDrawable(R.drawable.ic_sort_black_24dp);
                                icon.setTint(ContextCompat.getColor(context, R.color.uou));
                                music_base.button.setBackground(icon);
                            }

                            volumeBar.setProgressTintList(ColorStateList.valueOf(mutedDark));
                            volumeBar.setThumbTintList(ColorStateList.valueOf(mutedDark));


                            remainingTimeLabel.setTextColor(mutedDark);
                            elapsedTimeLabel.setTextColor(mutedDark);

                            // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            //                toolbar.getNavigationIcon().setTint(mutedDark);

                            //    button.setBackground(icon);
                            mVisualizer.setColor(mutedColor);
                            SharedPreferences prefs1 =context.getSharedPreferences("myPrefsKey", MODE_PRIVATE);

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
                            play = (Color.parseColor("#DF0974"));
                            pause = (Color.parseColor("#DF0974"));
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
                            playBtn.setBackgroundTintList(ColorStateList.valueOf(play));

                            Drawable myIcon33 = getResources().getDrawable(R.drawable.ic_next_fill);
                            Drawable myIcon31 = getResources().getDrawable(R.drawable.ic_prev_fill);
                            myIcon33.setTint(play);
                            myIcon31.setTint(play);
                            ply.setBackground(myIcon2);
                            pre.setBackground(myIcon31);
                            nxt.setBackground(myIcon33);

                            myIcon3.setTint(mutedDark);
                            myIcon2.setTint(mutedDark);

                            mLrcView.setIndicatorTextColor(play);
                            mLrcView.setCurrentIndicateLineTextColor(play);
                            mLrcView.setlrcCurrentTextColor(play);
                            //mLrcView.setLrcTextColor(play);
                            Drawable myIcons1 = getResources().getDrawable(R.drawable.play);
                            myIcons1.setTint(play);
                            mLrcView.setPlayDrawable(myIcons1);

                            textView.setTextColor(Color.parseColor("#DF0974"));
                            textView1.setTextColor(Color.parseColor("#DF0974"));
                            titleS.setText(title);
                            desS.setText(artist);
                            titleS.setTextColor(Color.parseColor("#DF0974"));
                            desS.setTextColor(Color.parseColor("#DF0974"));

                            waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);
                            waveformSeekBar.setWaveProgressColor(mutedDark);
                            waveformSeekBar.setWaveBackgroundColor(Color.parseColor("#F7CCBD"));

                            LinearLayout linearLayout = getActivity().findViewById(R.id.medic_base);



                            linearLayout.setBackgroundColor(vibrant);



                            enablelrc.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
                            Drawable myIcon3o = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                            myIcon3o.setTint(mutedDark);
                            showlrc.setBackground(myIcon3o);

                          //  bottomTabLayout.setBackgroundColor(vibrant);


                        }



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
                            + playqueue.get(position).getTitle().toLowerCase().replace(" ", "_").concat(".lrc"));

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
                    myIcon3.setTint(play);
                    myIcon2.setTint(play);
                    mp.setVolume(2.5f, 2.5f);
                    totalTime = mp.getDuration();
                    requestAudioPermissions();


                    new YourAsyncTask(abspath[0]).execute();

                    //   System.out.println(abspath[0]);

                    waveformSeekBar.setOnProgressChanged(new SeekBarOnProgressChanged() {
                        @Override
                        public void onProgressChanged(@NotNull WaveformSeekBar waveformSeekBar, int i, boolean b) {
                            if (b) {
                                System.out.println(i * mp.getDuration() / 100);
                                mp.seekTo(i * mp.getDuration() / 100);
                                //    positionBar.setProgress(i*mp.getDuration()/100);
                                waveformSeekBar.setProgress(i);
                                //pro = progress;
                            }
                        }
                    });
                    // Position Bar


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
                }
            }


        }

    private class YourAsyncTask extends AsyncTask<String, int[], int[]> {
        String abspath ;
        private ProgressDialog dialog;

        public YourAsyncTask(String bb) {
            dialog = new ProgressDialog(context);
            //abspath =new String[9];
            abspath = bb;

        }


        @Override
        protected void onPreExecute() {
            // waveformSeekBar.setWaveProgressColor(play);

        }
        @Override
        protected int[] doInBackground(String... args) {
            // do background work here
            int[] a = new int[0];
            try {
                a = convert(abspath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return a;
        }
        @Override
        protected void onPostExecute(int[] result) {
            // do UI work here
            System.out.println(result);




            waveformSeekBar = myView.findViewById(R.id.waveformSeekBar);

            waveformSeekBar.setSample(result);
            int i = waveformSeekBar.getSample().length;
            System.out.println("jkjkj"+i);



            try {
                ///waveformSeekBar.setSampleFrom(result,false);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


        }
    }
    }

