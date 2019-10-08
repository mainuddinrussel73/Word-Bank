package com.example.mainuddin.myapplication34.ui.media;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.data.word_details;
import com.example.mainuddin.myapplication34.ui.insert.add_news;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class Media_list_activity extends AppCompatActivity {

    Context context;

    public static final int RUNTIME_PERMISSION_CODE = 7;


    ListView listView;

    public static  List<Audio>  ListElementsArrayList = new ArrayList<Audio>(); ;

    Audiolist_adapter adapter ;

    ContentResolver contentResolver;

    Cursor cursor;

    int position;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), add_news.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });






        context = getApplicationContext();




        AndroidRuntimePermission();


        GetAllMediaMp3Files();
        for (int i = 0; i < ListElementsArrayList.size(); i++) {
           // System.out.println("list : "+ListElementsArrayList.get(i).getTitle());
        }


        adapter = new Audiolist_adapter(this);
        listView = (ListView) findViewById(R.id.listviews);
        listView.setAdapter(adapter);


        // ListView on item selected listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //startService(view);
                System.out.println("click");
                position = position;
                // TODO Auto-generated me
                //  thod stub
                // Showing ListView Item Click Value using Toast.
                MediaActivity.mp.stop();
                MediaActivity.mp = new MediaPlayer();
                Intent myIntent = new Intent(view.getContext(), MediaActivity.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("p",position);
                myIntent.putExtra("pos",ListElementsArrayList.get(position).getTitle());
                myIntent.putExtra("art",ListElementsArrayList.get(position).getArtist());
                myIntent.putExtra("alb",ListElementsArrayList.get(position).getAlbum());
                myIntent.putExtra("title",ListElementsArrayList.get(position).getImagepath());
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);


                Toast.makeText(Media_list_activity.this,parent.getAdapter().getItem(position).toString(),Toast.LENGTH_LONG).show();

            }
        });


    }

    //
    public void GetAllMediaMp3Files(){

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

            Toast.makeText(Media_list_activity.this,"Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(Media_list_activity.this,"No Music Found on SD Card.", Toast.LENGTH_LONG);

        }
        else {

            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int album  = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int date  = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);







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
    public void AndroidRuntimePermission(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

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

                    alert_builder.setNeutralButton("Cancel",null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                }
                else {

                    ActivityCompat.requestPermissions(
                            Media_list_activity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch(requestCode){

            case RUNTIME_PERMISSION_CODE:{

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else {

                }
            }
        }
    }

}