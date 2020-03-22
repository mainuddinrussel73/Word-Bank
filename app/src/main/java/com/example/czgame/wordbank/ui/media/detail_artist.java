package com.example.czgame.wordbank.ui.media;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class detail_artist extends AppCompatActivity {

    public static ArrayList<Audio> songs = new ArrayList<>();
    long albumid;
    private  final String clientId = "zyuxhfo1c51b5hxjk09x2uhv5n0svgd6g";
    private  final String clientSecret = "zudknyqbh3wunbhcvg9uyvo7uwzeu6nne";
    private final String  redirectUri = ("https://example.com/spotify-redirect");
    private  final String code = "";
    RecyclerView listView;
    TextView artist_info,artistname,songnum;
    ImageView imageView ;
    private DatabaseHelper_artist_image databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_artist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);



        /* Add callbacks to handle success and failure */


         imageView = findViewById(R.id.image);

        final Intent intent = getIntent();
        albumid = intent.getLongExtra("artistid",new Long(0));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                songs.clear();
                finish();
            }
        });

        listView  = findViewById(R.id.listviews);

      //  System.out.println(albumid);

        getAll((int) albumid);

        databaseHelper = new DatabaseHelper_artist_image(detail_artist.this);




        getSupportActionBar().setTitle(intent.getStringExtra("artistname"));

      //  System.out.println(songs.size());

        Artist_list_adapter album_list_adapter  = new Artist_list_adapter(this,songs);

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(album_list_adapter);


        RelativeLayout relativeLayout = findViewById(R.id.controll);
        artist_info = relativeLayout.findViewById(R.id.artist_info);
        artistname = relativeLayout.findViewById(R.id.artistname);
        songnum = relativeLayout.findViewById(R.id.otherinfo1);


        songnum.setText(songs.size() +" Songs");
        artistname.setText(intent.getStringExtra("artistname"));



        TheTaskAdvance theTaskAdvance =new TheTaskAdvance(intent.getStringExtra("artistname"));
        theTaskAdvance.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        LoadImageFromDatabaseTask loadImageFromDatabaseTask  =new LoadImageFromDatabaseTask(intent.getLongExtra("artistid",0));
        loadImageFromDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        NestedScrollView relativeLayout1 = findViewById(R.id.baseartist);
        RelativeLayout relativeLayout2 = findViewById(R.id.container);
        TextView textView = findViewById(R.id.artist_info);

        if (isDark && songs.size() != 0) {

            textView.setTextColor(Color.WHITE);

            listView.setAdapter(album_list_adapter);
            listView.setBackgroundColor(Color.BLACK);
            relativeLayout1.setBackgroundColor(Color.BLACK);
             relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded));
        } else if (!isDark && songs.size() != 0) {

                textView.setTextColor(Color.BLACK);

            relativeLayout1.setBackgroundColor(Color.WHITE);

            listView.setBackgroundColor(Color.WHITE);
            // listView.setAdapter(adapter);
            listView.setAdapter(album_list_adapter);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded_white));

        } else if (isDark && songs.size() == 0) {

            textView.setTextColor(Color.WHITE);
            relativeLayout1.setBackgroundColor(Color.BLACK);
            //listView.setBackgroundColor(Color.BLACK);
            listView.setBackgroundColor(Color.BLACK);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded));
            //relativeLayout.setBackgroundColor(Color.BLACK);


        } else if (!isDark && songs.size() == 0) {

            relativeLayout1.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
            //  listView.setBackgroundColor(Color.WHITE);
            listView.setBackgroundColor(Color.WHITE);
            //  relativeLayout.setBackgroundColor(Color.WHITE);
            relativeLayout2.setBackgroundDrawable(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.base_rounded_white));



        }





    }


    public  void getAll(int albumId){

        String selection = "is_music != 0";

        if (albumId > 0) {
            selection = selection + " and artist_id = " + albumId;
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
                 //   System.out.println(song.toString());
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

    class TheTaskAdvance extends AsyncTask<Void , String, String >
    {

        String s;
        String rt = "";

        TheTaskAdvance(String d){
            this.s = d;
            System.out.println(s);
        }




        @Override
        protected String doInBackground(Void ... arg0) {
            // TODO Auto-generated method stub

            if(s.trim().contains("&") ){
                s = s.trim().split("&")[0];
            }else if(s.trim().contains(",")){
                s = s.trim().split(",")[0];
            }
            System.out.println(s.trim());
            String kb = "https://www.theaudiodb.com/api/v1/json/1/search.php?s=".concat(s.trim());
            String json = "";
            try {
                json  = Jsoup.connect(kb).ignoreContentType(true).execute().body();
                System.out.println(json);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            // System.out.println(connection.request());

            try {
                JSONObject jsnobject = new JSONObject(json);
                //System.out.println(jsnobject.toString());

                JSONArray jsonArray = jsnobject.getJSONArray("artists");
                // System.out.println(jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject explrObject = jsonArray.getJSONObject(i);

                    // System.out.println(explrObject.get("strArtistThumb").toString());
                    rt = (explrObject.get("strBiographyEN").toString());
                    System.out.println(rt);

                }

            }catch (Exception e){

            }

            return rt;
        }

        @Override
        protected void   onPostExecute(String  result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);


            artist_info.setText(result);





        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }
    private class LoadImageFromDatabaseTask extends AsyncTask<Integer, Integer, ImageHelper> {


        long id;
        LoadImageFromDatabaseTask(long id){
            this.id = id;
        }

        protected void onPreExecute() {

        }

        @Override
        protected ImageHelper doInBackground(Integer... integers) {

            return databaseHelper.getImage(Long.toString(id));
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ImageHelper imageHelper) {

            if(imageHelper.getImageId()!=null) {

                Bitmap myBitmap = BitmapFactory.decodeByteArray(imageHelper.getImageByteArray(), 0, imageHelper.getImageByteArray().length);
                imageView.setImageBitmap(myBitmap);
                System.out.println("ok");
            }
        }

    }
}
