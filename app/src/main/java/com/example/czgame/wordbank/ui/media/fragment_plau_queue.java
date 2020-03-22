package com.example.czgame.wordbank.ui.media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.utill.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.isShowlrc;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.nxt;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.nxtBtn;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.pauseBtn;
import static com.example.czgame.wordbank.ui.media.fragment_music_list.pre;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_plau_queue.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_plau_queue#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_plau_queue extends Fragment implements music_base.OnBackPressedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static  List<Audio> playqueue = new ArrayList<>();
    RecyclerView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public fragment_plau_queue() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_plau_queue.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_plau_queue newInstance(String param1, String param2) {
        fragment_plau_queue fragment = new fragment_plau_queue();
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
        return inflater.inflate(R.layout.fragment_fragment_plau_queue, container, false);
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

        listView = linearLayout.findViewById(R.id.gridviews);


        Adapter_queue artist_adapter = new Adapter_queue(getActivity());

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(artist_adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(listView);
        listView.setAdapter(artist_adapter);


        SharedPreferences prefs = getContext().getSharedPreferences("myPrefsKey", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);




        if (isDark && playqueue.size() != 0) {

            linearLayout.setBackgroundColor(Color.BLACK);


            listView.setAdapter(artist_adapter);
            listView.setBackgroundColor(Color.BLACK);


            _rootLayout.setBackgroundColor(Color.BLACK);

            relativeLayout.setBackgroundColor(Color.BLACK);
        } else if (!isDark && playqueue.size() != 0) {

            linearLayout.setBackgroundColor(Color.WHITE);


            _rootLayout.setBackgroundColor(Color.WHITE);

            listView.setBackgroundColor(Color.WHITE);
            listView.setAdapter(artist_adapter);
            relativeLayout.setBackgroundColor(Color.WHITE);

        } else if (isDark && playqueue.size() == 0) {



            _rootLayout.setBackgroundColor(Color.BLACK);
            listView.setBackgroundColor(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundColor(Color.BLACK);


        } else if (!isDark && playqueue.size() == 0) {


            _rootLayout.setBackgroundColor(Color.WHITE);
            listView.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.WHITE);



        }
        nxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                isShowlrc = false;
                try {
                    System.out.println("nxr");
                    getContext().sendBroadcast(new Intent(Constants.ACTION.NEXT_ACTION));
                }catch (Exception e){

                }

            }
        });
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowlrc = false;
                try {
                    System.out.println("nxr");
                    getContext().sendBroadcast(new Intent(Constants.ACTION.NEXT_ACTION));
                }catch (Exception e){

                }

            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowlrc = false;
                try {
                    getContext().sendBroadcast(new Intent(Constants.ACTION.PREV_ACTION));
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
                    getContext().sendBroadcast(new Intent(Constants.ACTION.PREV_ACTION));
                }catch (Exception e){

                }

                //myButton.setText("Slide up");

            }
        });



    }

    @Override
    public void doBack() {


       // music_base.activity.onBackPressed();


        // fab.setBackgroundTintList(ColorStateList.valueOf(mutedDark));
       // music_base.toolbar.getNavigationIcon().setTint(Color.WHITE);
       // getContext().unregisterReceiver(broadcastReceiver);

    }



    private String[] getAudioPath(Context context, String songTitle) {

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




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
