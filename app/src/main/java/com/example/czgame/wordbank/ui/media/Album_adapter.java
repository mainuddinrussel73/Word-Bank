package com.example.czgame.wordbank.ui.media;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import java.io.File;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;

import static com.example.czgame.wordbank.ui.media.fragment_artist.allsongs;

public class Album_adapter  extends BaseAdapter {

    private final Activity context;
    ArrayList<ArtistModel> contactList;
    ImageView titleText;
    TextView tt,tt1;

    // private final Integer[] imgid;

    public Album_adapter(Activity context) {

        this.context = context;
        this.contactList = new ArrayList<ArtistModel>();
        this.contactList.addAll(allsongs);






    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    @Override
    public int getCount() {
        return allsongs.size();
    }

    @Override
    public ArtistModel getItem(int i) {
        return allsongs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.music_album_item, null, true);

        RelativeLayout griditm = rowView.findViewById(R.id.grid_item);
        titleText = rowView.findViewById(R.id.play_pause);
        tt = rowView.findViewById(R.id.title5);
        tt1 = rowView.findViewById(R.id.title6);

        tt.setText((allsongs.get(position).getArtistName()));
        tt1.setText("Total Tracks : "+(allsongs.get(position).getNr_of_songs()));





        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_images/"+"Image-"+ allsongs.get(position).getID() +".jpg";

        File imgFile = new  File(root);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            titleText.setImageBitmap(myBitmap);
            System.out.println("ok");

        }


        RelativeLayout listitem = rowView.findViewById(R.id.grid_item);

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            listitem.setBackgroundColor(Color.BLACK);


            tt.setTextColor(Color.parseColor("#10bcc9"));


            griditm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background_dark));

        } else {

            listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            tt.setTextColor(Color.parseColor("#10bcc9"));

            griditm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background));
        }


        return rowView;

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





}