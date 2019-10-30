package com.example.czgame.wordbank.ui.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.palette.graphics.Palette;

public class Audiogrid_adapter  extends BaseAdapter {

    private final Activity context;
    List<Audio> contactList;
    ImageView titleText;
    TextView tt, tt1, duration;

    // private final Integer[] imgid;

    public Audiogrid_adapter(Activity context) {

        this.context = context;
        this.contactList = new ArrayList<Audio>();
        this.contactList.addAll(Media_list_activity.ListElementsArrayList);


    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
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

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.music_item_grid, null, true);

        titleText = rowView.findViewById(R.id.play_pause);
        tt = rowView.findViewById(R.id.title5);
        tt1 = rowView.findViewById(R.id.title6);
        long num = Long.parseLong(Media_list_activity.ListElementsArrayList.get(position).getDuration());
        duration = rowView.findViewById(R.id.duration); // duration

        tt.setText((Media_list_activity.ListElementsArrayList.get(position).getTitle()));
        tt1.setText((Media_list_activity.ListElementsArrayList.get(position).getArtist()));
        String s = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(num),
                TimeUnit.MILLISECONDS.toSeconds(num) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(num))
        );
        duration.setText(s);


        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(Media_list_activity.ListElementsArrayList.get(position).getImagepath()));


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image)
                .error(R.drawable.image);
        Glide.with(context).load(uri).apply(options).into(titleText);


        LinearLayout listitem = rowView.findViewById(R.id.grid_item);

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            listitem.setBackgroundColor(Color.BLACK);


            tt.setTextColor(context.getResources().getColor(R.color.per50white));

            tt1.setTextColor(context.getResources().getColor(R.color.material_white));


        } else {

            listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            tt.setTextColor(context.getResources().getColor(R.color.per54black));

            tt1.setTextColor(context.getResources().getColor(R.color.darkgray));
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

    class RetrieveFeedTask extends AsyncTask<Uri, Bitmap, Bitmap> {

        int defaultValue = 0x0000FF;
        Palette palette;
        int mutedDark;
        int mutedLight;
        ContentResolver res;
        InputStream in;
        Bitmap bm;
        private Exception exception;

        @Override
        protected void onPreExecute() {
            res = context.getContentResolver();

        }

        protected Bitmap doInBackground(Uri... data) {


            try {
                in = res.openInputStream(data[0]);
                bm = BitmapFactory.decodeStream(in);
                in.close();

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                InputStream is = context.getResources().openRawResource(R.raw.image);
                bm = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bm;

        }

        protected void onPostExecute(Bitmap bm) {

            try {
                palette = Palette.from(bm).generate();


                int vibrant = getDominantColor(bm);
                int muted = palette.getMutedColor(defaultValue);
                mutedLight = getComplimentColor(palette.getDominantColor(palette.getDominantColor(defaultValue)));
                mutedDark = getComplimentColor(vibrant);

                duration.setTextColor(mutedDark);
                titleText.setImageBitmap(bm);
                tt.setTextColor(mutedDark);
                tt1.setTextColor(mutedLight);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private String getCoverArtPath(long albumId, Context context) {

            Cursor albumCursor = context.getContentResolver().query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID + " = ?",
                    new String[]{Long.toString(albumId)},
                    null
            );
            boolean queryResult = albumCursor.moveToFirst();
            String result = null;
            if (queryResult) {
                result = albumCursor.getString(0);
            }
            albumCursor.close();
            return result;
        }
    }


}