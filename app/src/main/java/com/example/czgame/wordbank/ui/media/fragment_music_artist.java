package com.example.czgame.wordbank.ui.media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.example.czgame.wordbank.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class fragment_music_artist extends Fragment implements music_base.OnBackPressedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  static  ArrayList<AlbumModel> allsongs = new ArrayList<>();
    GridView gridView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    public fragment_music_artist() {
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
    public static fragment_music_artist newInstance(String param1, String param2) {
        fragment_music_artist fragment = new fragment_music_artist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_fragment_music_artist, container, false);
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

        allsongs = getListOfAlbums(getContext());

        System.out.println(allsongs.size());
        Artist_adapter artist_adapter = new Artist_adapter(getActivity());
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

               Intent myIntent = new Intent(view.getContext(), detail_album.class);
               //String s = view.findViewById(R.id.subtitle).toString();
               //String s = (String) parent.getI;
               System.out.println(allsongs.get(position).getID());
               myIntent.putExtra("albumid", allsongs.get(position).getID());
               myIntent.putExtra("album", allsongs.get(position).getAlbumName());
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


    public ArrayList<AlbumModel> getListOfAlbums(Context context) {

        String where = null;

        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
        final String album_name = MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String albumart = MediaStore.Audio.Albums.ALBUM_ART;
        final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

        final String[] columns = { _id, album_name, artist, albumart, tracks };
        Cursor cursor = context.getContentResolver().query(uri, columns, where,
                null, null);

        ArrayList<AlbumModel> list = new ArrayList<AlbumModel>();

        // add playlsit to list

        if (cursor.moveToFirst()) {

            do {

                AlbumModel albumData = new AlbumModel();

                albumData
                        .setId(cursor.getLong(cursor.getColumnIndex(_id)));

                albumData.setAlbumName(cursor.getString(cursor
                        .getColumnIndex(album_name)));

                albumData.setArtistName(cursor.getString(cursor
                        .getColumnIndex(artist)));

                albumData.setAlbumImg(cursor.getString(cursor
                        .getColumnIndex(albumart)));

                albumData.setNr_of_songs(cursor.getString(cursor
                        .getColumnIndex(tracks)));

                list.add(albumData);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }
}
