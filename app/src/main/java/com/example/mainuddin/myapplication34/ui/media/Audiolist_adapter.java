package com.example.mainuddin.myapplication34.ui.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.palette.graphics.Palette;

public class Audiolist_adapter extends BaseAdapter {

    private final Activity context;
    List<Audio> contactList;

    // private final Integer[] imgid;

    public Audiolist_adapter(Activity context) {

        this.context=context;
        this.contactList = new ArrayList<Audio>();
        this.contactList.addAll(Media_list_activity.ListElementsArrayList);


    }

    @Override
    public int getCount() {
        return Media_list_activity.ListElementsArrayList.size();
    }

    @Override
    public Audio getItem(int i) {
        return Media_list_activity.ListElementsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.media_item_layout, null,true);

        ImageView titleText = (ImageView) rowView.findViewById(R.id.play_pause);
        TextView tt = (TextView) rowView.findViewById(R.id.title5);
        TextView tt1 = (TextView) rowView.findViewById(R.id.title6);


        //System.out.println("klkl"+MainActivity.contactList.size());
       /* File imgFile = new File(Media_list_activity.ListElementsArrayList.get(position).getImagepath());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


            titleText.setImageBitmap(myBitmap);

        }*/

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(Media_list_activity.ListElementsArrayList.get(position).getImagepath()));
        ContentResolver res = context.getContentResolver();
        InputStream in;
        Bitmap bm = null;
        try {
            in = res.openInputStream(uri);

            bm = BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            InputStream is = context.getResources().openRawResource(R.raw.image);
            bm = BitmapFactory.decodeStream(is);
        }

        Palette palette = Palette.from(bm).generate();


        int defaultValue = 0x0000FF;
        int vibrant = getDominantColor(bm);
        int vibrantLight = palette.getLightVibrantColor(defaultValue);
        int vibrantDark = palette.getDarkVibrantColor(vibrant);
        int muted = palette.getMutedColor(defaultValue);
        int mutedLight = getComplimentColor(muted);
        int mutedDark = getComplimentColor(vibrant);



        tt.setText((Media_list_activity.ListElementsArrayList.get(position).getTitle()));
        tt.setTextColor(mutedDark);
        tt1.setText((Media_list_activity.ListElementsArrayList.get(position).getArtist()));
        tt1.setTextColor(mutedLight);


        LinearLayout listitem = rowView.findViewById(R.id.list_item);
        titleText.setImageBitmap(bm);
        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);

        if(isDark){

            //System.out.println("klklkl");
            listitem.setBackgroundColor(Color.BLACK);
           

            tt.setTextColor(getComplimentColor(tt.getCurrentTextColor()));

            tt1.setTextColor(getComplimentColor(tt1.getCurrentTextColor()));


        }else {

            listitem.setBackgroundColor(Color.WHITE);
           // tt.setTextColor(Color.BLACK);
            tt.setTextColor(getComplimentColor(tt.getCurrentTextColor()));

            tt1.setTextColor(getComplimentColor(tt1.getCurrentTextColor()));
        }


        return rowView;

    };
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


        public static int getDominantColor(Bitmap bitmap) {
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
            final int color = newBitmap.getPixel(0, 0);
            newBitmap.recycle();
            return color;
        }




    }