package com.example.czgame.wordbank.ui.media;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;

import static com.example.czgame.wordbank.ui.media.fragment_plau_queue.playqueue;

public class Adapter_queue  extends BaseAdapter {

    private final Activity context;
    List<Audio> contactList;
    ImageView titleText;
    TextView tt, tt1, duration;

    public Adapter_queue(Activity context) {

        this.context = context;
        this.contactList = new ArrayList<Audio>();
        this.contactList.addAll(playqueue);


    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    @Override
    public int getCount() {
        return playqueue.size();
    }

    @Override
    public Audio getItem(int i) {
        return  playqueue.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.queue_music_item, null, true);
        RelativeLayout listitm = rowView.findViewById(R.id.list_item);
        titleText = rowView.findViewById(R.id.play_pause);
        tt = rowView.findViewById(R.id.title5);
        tt1 = rowView.findViewById(R.id.title6);
        long num = Long.parseLong( playqueue.get(position).getDuration());
        duration = rowView.findViewById(R.id.duration); // duration

        tt.setText(( playqueue.get(position).getTitle()));
        tt1.setText(( playqueue.get(position).getArtist()));
        String s = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(num),
                TimeUnit.MILLISECONDS.toSeconds(num) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(num))
        );
        duration.setText(s);


        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(playqueue.get(position).getImagepath()));

        //in.close();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image)
                .error(R.drawable.image);
        Glide.with(context).load(uri).apply(options).into(titleText);



        RelativeLayout listitem = rowView.findViewById(R.id.list_item);

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            listitem.setBackgroundColor(Color.BLACK);


            tt.setTextColor(context.getResources().getColor(R.color.per50white));

            tt1.setTextColor(context.getResources().getColor(R.color.material_white));

            listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background_dark));

        } else {


            listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            tt.setTextColor(context.getResources().getColor(R.color.per54black));

            tt1.setTextColor(context.getResources().getColor(R.color.darkgray));
            listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background));
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

    // private final Integer[] imgid;
    public interface EventListener {
        void loadsong(int position);
    }




}