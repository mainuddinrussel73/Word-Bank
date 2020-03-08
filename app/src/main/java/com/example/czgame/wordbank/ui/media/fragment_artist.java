package com.example.czgame.wordbank.ui.media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.czgame.wordbank.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class fragment_artist extends Fragment implements music_base.OnBackPressedListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  static  ArrayList<ArtistModel> allsongs = new ArrayList<>();
    public  static  List<ArtistModel> imagepathm = new ArrayList<>();
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    ListView gridView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public fragment_artist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_music_artist.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_artist newInstance(String param1, String param2) {
        fragment_artist fragment = new fragment_artist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private static void SaveImage(Bitmap finalBitmap,String name) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = "Image-"+ name +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) return;
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_music_album, container, false);
    }

    @Override
    public void onViewCreated(View viewq, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewq, savedInstanceState);

        ((music_base) getActivity()).setOnBackPressedListener(this);


        FrameLayout _rootLayout = getActivity().findViewById(R.id._rootLayout);
        FrameLayout.LayoutParams _rootLayoutParams = new FrameLayout.LayoutParams(_rootLayout.getWidth(), _rootLayout.getHeight());
        //_rootLayoutParams.setMargins(300, 0, 300, 0);
        //_rootLayout.setLayoutParams(_rootLayoutParams);

        RelativeLayout relativeLayout = _rootLayout.findViewById(R.id.container);
        RelativeLayout linearLayout = relativeLayout.findViewById(R.id.inner);

        gridView = linearLayout.findViewById(R.id.gridviews);

        allsongs = getListOfArtist(getContext());


        Album_adapter artist_adapter = new Album_adapter(getActivity());
        gridView.setAdapter(artist_adapter);

        SharedPreferences prefs = getContext().getSharedPreferences("myPrefsKey", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);




        if (isDark && allsongs.size() != 0) {


            _rootLayout.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundColor(Color.BLACK);
            gridView.setAdapter(artist_adapter);
            gridView.setBackgroundColor(Color.BLACK);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.card_background_dark));
        } else if (!isDark && allsongs.size() != 0) {

            linearLayout.setBackgroundColor(Color.WHITE);

            _rootLayout.setBackgroundColor(Color.WHITE);

            gridView.setBackgroundColor(Color.WHITE);
            // listView.setAdapter(adapter);
            gridView.setAdapter(artist_adapter);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.card_background));

        } else if (isDark && allsongs.size() == 0) {

            _rootLayout.setBackgroundColor(Color.BLACK);
            //listView.setBackgroundColor(Color.BLACK);
            gridView.setBackgroundColor(Color.BLACK);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.card_background_dark));
            linearLayout.setBackgroundColor(Color.BLACK);


        } else if (!isDark && allsongs.size() == 0) {

            _rootLayout.setBackgroundColor(Color.WHITE);

            //  listView.setBackgroundColor(Color.WHITE);
            gridView.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.card_background));



        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent myIntent = new Intent(view.getContext(), detail_artist.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                System.out.println(allsongs.get(position).getID());
                myIntent.putExtra("artistid", allsongs.get(position).getID());
                myIntent.putExtra("artistname", allsongs.get(position).getArtistName());
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void doBack() {

        //LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        //music_base.activity.onBackPressed();
    }

    public ArrayList<ArtistModel> getListOfArtist(Context context) {

        String where = null;

        final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Artists._ID;
        final String artist = MediaStore.Audio.Artists.ARTIST;
        final String tracks = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;

        final String[] columns = { _id , artist , tracks };
        Cursor cursor = context.getContentResolver().query(uri, columns, where,
                null, null);

        ArrayList<ArtistModel> list = new ArrayList<ArtistModel>();

        // add playlsit to list

        if (cursor.moveToFirst()) {

            do {

                ArtistModel albumData = new ArtistModel();

                albumData
                        .setId(cursor.getLong(cursor.getColumnIndex(_id)));


                albumData.setArtistName(cursor.getString(cursor
                        .getColumnIndex(artist)));



                albumData.setNr_of_songs(cursor.getString(cursor
                        .getColumnIndex(tracks)));

                list.add(albumData);

            } while (cursor.moveToNext());
        }

        cursor.close();

        new TheTaskAdvance().execute();

        return list;
    }

    class TheTaskAdvance extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            for (ArtistModel a:
                    allsongs) {

                String kb = "https://www.theaudiodb.com/api/v1/json/1/search.php?s="+a.getArtistName();
                String json = "";
                try {
                    json  = Jsoup.connect(kb).ignoreContentType(true).execute().body();
                    // System.out.println(json);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // System.out.println(connection.request());

                try {
                    JSONObject jsnobject = new JSONObject(json);
                    //System.out.println(jsnobject.toString());

                    JSONArray jsonArray = jsnobject.getJSONArray("artists");
                    // System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject explrObject = jsonArray.getJSONObject(i);

                        System.out.println(explrObject.get("strArtistThumb").toString());
                        a.setUrl(explrObject.get("strArtistThumb").toString());

                        imagepathm.add(a);
                    }

                }catch (Exception e){

                }
            }

            allsongs.clear();
            allsongs.addAll(imagepathm);

            for (ArtistModel a:
                    allsongs) {
                String s = Long.toString(a.getID());

                SaveImage(getBitmapFromURL(a.getUrl()),s);
            }

            return null;
        }

        @Override
        protected void   onPostExecute(Void result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);



            Album_adapter artist_adapter = new Album_adapter(getActivity());
            gridView.setAdapter(artist_adapter);



            // System.out.println(result);
            //System.out.println(result);





        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }
}
