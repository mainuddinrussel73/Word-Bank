package com.example.czgame.wordbank.ui.media;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class detail_album extends AppCompatActivity {

    public static ArrayList<Audio> songs = new ArrayList<>();
    long albumid;
    RecyclerView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);



        ImageView imageView = findViewById(R.id.image);

        final Intent intent = getIntent();
        albumid = intent.getLongExtra("albumid",new Long(0));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                songs.clear();
                finish();
            }
        });

        listView  = findViewById(R.id.listviews);

        System.out.println(albumid);

        getAll((int) albumid);

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        try{
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf( songs.get(0).getImagepath()));


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image);
            Glide.with(this).load(uri).apply(options).into(imageView);
        } catch(NumberFormatException ex){ // handle your exception

        }

        getSupportActionBar().setTitle(intent.getStringExtra("album"));

        System.out.println(songs.size());

        Album_list_adapter album_list_adapter  = new Album_list_adapter(this,songs);

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(album_list_adapter);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        NestedScrollView relativeLayout1 = findViewById(R.id.basealbum);
        RelativeLayout relativeLayout2 = findViewById(R.id.container);
        TextView textView = findViewById(R.id.artist_info);

        if (isDark && songs.size() != 0) {


            listView.setAdapter(album_list_adapter);
            listView.setBackgroundColor(Color.BLACK);
            relativeLayout1.setBackgroundColor(Color.BLACK);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded));
        } else if (!isDark && songs.size() != 0) {

            //     relativeLayout.setBackgroundColor(Color.WHITE);

            relativeLayout1.setBackgroundColor(Color.WHITE);

            listView.setBackgroundColor(Color.WHITE);
            // listView.setAdapter(adapter);
            listView.setAdapter(album_list_adapter);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded_white));

        } else if (isDark && songs.size() == 0) {

            //listView.setBackgroundColor(Color.BLACK);
            listView.setBackgroundColor(Color.BLACK);
            relativeLayout1.setBackgroundColor(Color.BLACK);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded));
            //relativeLayout.setBackgroundColor(Color.BLACK);


        } else if (!isDark && songs.size() == 0) {

            relativeLayout1.setBackgroundColor(Color.WHITE);

            //  listView.setBackgroundColor(Color.WHITE);
            listView.setBackgroundColor(Color.WHITE);
            //  relativeLayout.setBackgroundColor(Color.WHITE);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded_white));



        }

    }


    public  void getAll(int albumId){

        String selection = "is_music != 0";

        if (albumId > 0) {
            selection = selection + " and album_id = " + albumId;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                int position = 1;
                while (!cursor.isAfterLast()) {
                    Audio song = new Audio();
                    song.setTitle(cursor.getString(0));
                    song.setDuration(cursor.getString(4));
                    song.setArtist(cursor.getString(1));

                    song.setImagepath(cursor.getString(6));
                    songs.add(song);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            Log.e("Media", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    public  void onBackPressed(){
        songs.clear();

        super.onBackPressed();
    }

}
