package com.example.czgame.wordbank.ui.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.prefm;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.recentlyList;

public class fragment_music_home  extends Fragment implements music_base.OnBackPressedListener ,recent_adapter.EventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;
    List<AlbumModel> topsongs = new ArrayList<>();
    ArrayList<ArtistModel> allsongs = new ArrayList<>();
    List<Genre> audioList = new ArrayList<>();
    List<Audio> items = new ArrayList<>();
    DatabaseHelper_artist_image  databaseHelper_artist_image;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView groceryRecyclerView;
    private RecyclerView groceryRecyclerView2;
    private adapter_recent_songs groceryAdapter;
    private  adapter_new_artist adapter_new_artist;
    private RecyclerView groceryRecyclerView3;
    private Genera_adapter genera_adapter;
    private RecyclerView groceryRecyclerView5;
    private recent_adapter recent_adapter;
    public fragment_music_home() {
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
    public static fragment_music_home newInstance(String param1, String param2) {
        fragment_music_home fragment = new fragment_music_home();
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
        return inflater.inflate(R.layout.fragment_fragment_music_home, container, false);
    }

    @Override
    public void onViewCreated(View viewq, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewq, savedInstanceState);

        ((music_base) getActivity()).setOnBackPressedListener(this);
        groceryRecyclerView = getActivity().findViewById(R.id.gridviews);
        topsongs = getListOfAlbums(getContext());

        groceryAdapter = new adapter_recent_songs(topsongs,getContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        groceryRecyclerView.setLayoutManager(horizontalLayoutManager);
        groceryRecyclerView.setAdapter(groceryAdapter);

        allsongs = getListOfArtist(getContext());
        groceryRecyclerView2 = getActivity().findViewById(R.id.gridviews2);
        adapter_new_artist = new adapter_new_artist(allsongs,getContext());
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        groceryRecyclerView2.setLayoutManager(horizontalLayoutManager1);
        groceryRecyclerView2.setAdapter(adapter_new_artist);

        getGenraAll();
        groceryRecyclerView3 = getActivity().findViewById(R.id.gridviews0);
        genera_adapter = new Genera_adapter(audioList,getContext());
        LinearLayoutManager horizontalLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        groceryRecyclerView3.setLayoutManager(horizontalLayoutManager3);
        groceryRecyclerView3.setAdapter(genera_adapter);


        groceryRecyclerView5 = getActivity().findViewById(R.id.gridviews4);
        recent_adapter = new recent_adapter(getActivity(),this,recentlyList);
        LinearLayoutManager horizontalLayoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        groceryRecyclerView5.setLayoutManager(horizontalLayoutManager5);
        groceryRecyclerView5.setAdapter(recent_adapter);

        SharedPreferences prefs = getContext().getSharedPreferences("myPrefsKey", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        FrameLayout _rootLayout = getActivity().findViewById(R.id._rootLayout);
        FrameLayout.LayoutParams _rootLayoutParams = new FrameLayout.LayoutParams(_rootLayout.getWidth(), _rootLayout.getHeight());
        //_rootLayoutParams.setMargins(300, 0, 300, 0);
        //_rootLayout.setLayoutParams(_rootLayoutParams);

        RelativeLayout relativeLayout = _rootLayout.findViewById(R.id.container);
        RelativeLayout linearLayout = relativeLayout.findViewById(R.id.inner);
        RelativeLayout linearLayout1 = relativeLayout.findViewById(R.id.inner2);
        RelativeLayout linearLayout2 = relativeLayout.findViewById(R.id.inner0);
        RelativeLayout linearLayout4 = relativeLayout.findViewById(R.id.inner4);
        RelativeLayout userspace = relativeLayout.findViewById(R.id.userspace);
        RelativeLayout options = relativeLayout.findViewById(R.id.options);

        TextView userfild = getActivity().findViewById(R.id.userfield);
        TextView username = getActivity().findViewById(R.id.username);
        if (isDark && allsongs.size() != 0) {


            _rootLayout.setBackgroundColor(Color.BLACK);

            linearLayout.setBackgroundColor(Color.BLACK);
            linearLayout1.setBackgroundColor(Color.BLACK);
            linearLayout2.setBackgroundColor(Color.BLACK);
            linearLayout4.setBackgroundColor(Color.BLACK);
            groceryRecyclerView.setAdapter(groceryAdapter);
            groceryRecyclerView2.setAdapter(adapter_new_artist);
            groceryRecyclerView3.setAdapter(genera_adapter);
            groceryRecyclerView5.setAdapter(recent_adapter);
            groceryRecyclerView.setBackgroundColor(Color.BLACK);
            groceryRecyclerView2.setBackgroundColor(Color.BLACK);
            groceryRecyclerView3.setBackgroundColor(Color.BLACK);
            groceryRecyclerView5.setBackgroundColor(Color.BLACK);

            relativeLayout.setBackgroundColor(Color.BLACK);
            userspace.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card_dark));
            options.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card_dark));
            userfild.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            username.setTextColor(ContextCompat.getColor(getContext(),R.color.soft_light));
        } else if (!isDark && allsongs.size() != 0) {

            _rootLayout.setBackgroundColor(Color.WHITE);

            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout1.setBackgroundColor(Color.WHITE);
            linearLayout2.setBackgroundColor(Color.WHITE);
            linearLayout4.setBackgroundColor(Color.WHITE);
            groceryRecyclerView.setAdapter(groceryAdapter);
            groceryRecyclerView2.setAdapter(adapter_new_artist);
            groceryRecyclerView3.setAdapter(genera_adapter);
            groceryRecyclerView5.setAdapter(recent_adapter);
            groceryRecyclerView.setBackgroundColor(Color.WHITE);
            groceryRecyclerView2.setBackgroundColor(Color.WHITE);
            groceryRecyclerView3.setBackgroundColor(Color.WHITE);
            groceryRecyclerView5.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.WHITE);
            userspace.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card));
            options.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card));

            userfild.setTextColor(ContextCompat.getColor(getContext(),R.color.bbb));
            username.setTextColor(ContextCompat.getColor(getContext(),R.color.soft_dark));
        } else if (isDark && allsongs.size() == 0) {

            _rootLayout.setBackgroundColor(Color.BLACK);

            linearLayout.setBackgroundColor(Color.BLACK);
            linearLayout1.setBackgroundColor(Color.BLACK);
            linearLayout2.setBackgroundColor(Color.BLACK);
            linearLayout4.setBackgroundColor(Color.BLACK);
            groceryRecyclerView.setBackgroundColor(Color.BLACK);
            groceryRecyclerView2.setBackgroundColor(Color.BLACK);
            groceryRecyclerView3.setBackgroundColor(Color.BLACK);
            groceryRecyclerView5.setBackgroundColor(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.BLACK);
            userspace.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card_dark));
            options.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card_dark));

            userfild.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
            username.setTextColor(ContextCompat.getColor(getContext(),R.color.soft_light));


        } else if (!isDark && allsongs.size() == 0) {

            _rootLayout.setBackgroundColor(Color.WHITE);

            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout1.setBackgroundColor(Color.WHITE);
            linearLayout2.setBackgroundColor(Color.WHITE);
            linearLayout4.setBackgroundColor(Color.WHITE);
            groceryRecyclerView.setBackgroundColor(Color.WHITE);
            groceryRecyclerView2.setBackgroundColor(Color.WHITE);
            groceryRecyclerView3.setBackgroundColor(Color.WHITE);
            groceryRecyclerView5.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.WHITE);
            userspace.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card));
            options.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_card));

            userfild.setTextColor(ContextCompat.getColor(getContext(),R.color.bbb));
            username.setTextColor(ContextCompat.getColor(getContext(),R.color.soft_dark));

        }

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
        final String firstYear  = MediaStore.Audio.Albums.FIRST_YEAR;

        final String[] columns = { _id, album_name, artist, albumart, tracks , firstYear };
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

               albumData.setFirstYear(cursor.getString(cursor
                       .getColumnIndex(firstYear)));
          //      System.out.println(albumData);

                list.add(albumData);

            } while (cursor.moveToNext());
        }

        cursor.close();
        try {
            Collections.sort(list,
                    new Comparator<AlbumModel>() {
                        public int compare(AlbumModel f1, AlbumModel f2) {
                            return Integer.parseInt(f1.getFirstYear()) == Integer.parseInt(f2.getFirstYear()) ? 0 : 1;
                        }
                    });
        }catch (Exception e){}
        return list;
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
                ///  DatabaseHelper_artist_image databaseHelper;
                //databaseHelper = new DatabaseHelper_artist_image(getContext());

                //System.out.println( databaseHelper.getImage(Long.toString(albumData.getID())).getImageByteArray().toString());


            } while (cursor.moveToNext());
        }

        cursor.close();





        return list;
    }
    public void getGenraAll(){
        int index;
        long genreId;
        int count;
        Uri uri;
        Cursor genrecursor;
        Cursor tempcursor;
        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        String[] proj2={MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID};

        genrecursor=getActivity().getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,proj1,null, null, null);
        if(genrecursor.moveToFirst())
        {
            do{
                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                //System.out.println("GENRE NAME: "+genrecursor.getString(index));
              //  System.out.println("======================================");
                Genre genre = new Genre();
                genre.setName(genrecursor.getString(index));


                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                genreId=Long.parseLong(genrecursor.getString(index));
                uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);


                tempcursor = getActivity().getContentResolver().query(uri, proj2, null,null,null);
              //  System.out.println("Total Songs: "+tempcursor.getCount());
                genre.setNumberSongs(tempcursor.getCount());
                audioList.add(genre);

                if(tempcursor.moveToFirst())
                {
                    do{
                        index=tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        int album = tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                        int artist = tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                        int date = tempcursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
                        int duration = tempcursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                        int albumId = tempcursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                       // System.out.println("    Song Name: "+tempcursor.getString(index));
                        Audio audio = new Audio();
                        audio.setTitle(tempcursor.getString(index));
                        audio.setAlbum(tempcursor.getString(album));
                        audio.setArtist(tempcursor.getString(artist));
                        audio.setData(tempcursor.getString(date));
                        audio.setDuration(tempcursor.getString(duration));
                        audio.setImagepath(tempcursor.getString(albumId));

                        items.add(audio);

                    }while(tempcursor.moveToNext());
                }
              //  System.out.println("======================================");
            }while(genrecursor.moveToNext());
        }


    }

    @Override
    public void loadsong(int position) {
        System.out.println("event");
        PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefm.edit();
        editor.putInt("positionnow",position);
        editor.commit();

        getContext().sendBroadcast(new Intent(Constants.ACTION.LOAD_ACTION));
    }
}
