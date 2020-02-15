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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import es.dmoral.toasty.Toasty;

import static com.example.czgame.wordbank.ui.media.fragment_music_list.ListElementsArrayList;
import static com.example.czgame.wordbank.ui.media.fragment_plau_queue.playqueue;

public class Audiogrid_adapter  extends BaseAdapter {

    private final Activity context;
    List<Audio> contactList;
    ImageView titleText;
    TextView tt, tt1, duration;
    EventListener listener;
    // private final Integer[] imgid;

    public Audiogrid_adapter(Activity context,EventListener listener) {

        this.context = context;
        this.contactList = new ArrayList<Audio>();
        this.contactList.addAll(ListElementsArrayList);
        this.listener = listener;


    }

    @Override
    public int getCount() {
        return ListElementsArrayList.size();
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    @Override
    public Audio getItem(int i) {
        return ListElementsArrayList.get(i);
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.music_item_grid, null, true);

        RelativeLayout griditm = rowView.findViewById(R.id.grid_item);
        titleText = rowView.findViewById(R.id.play_pause);
        tt = rowView.findViewById(R.id.title5);
        tt1 = rowView.findViewById(R.id.title6);
        long num = Long.parseLong(ListElementsArrayList.get(position).getDuration());
        duration = rowView.findViewById(R.id.duration); // duration

        tt.setText((ListElementsArrayList.get(position).getTitle()));
        tt1.setText((ListElementsArrayList.get(position).getArtist()));
        String s = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(num),
                TimeUnit.MILLISECONDS.toSeconds(num) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(num))
        );
        duration.setText(s);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.loadsong(position);
            }
        });

        ImageButton options1 = rowView.findViewById(R.id.options);


        options1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                boolean isDark = prefs.getBoolean("isDark", false);
                if (isDark) {
                    wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE);

                } else {
                    wrapper = new ContextThemeWrapper(context, R.style.YOURSTYLE1);
                }

                androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(wrapper, view);
                popup.getMenuInflater().inflate(R.menu.music_menu, popup.getMenu());
                //
                popup.show();


                popup.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Add to Playlist")){
                            if(!playqueue.contains(ListElementsArrayList.get(position))){
                                playqueue.add(ListElementsArrayList.get(position));
                            Toasty.success(context,"Okk Added.",Toasty.LENGTH_LONG).show();
                            }else{
                                Toasty.success(context,"Added Already.",Toasty.LENGTH_LONG).show();
                            }
                        }
                        return true;
                    }
                });
            }
        });

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");


        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(ListElementsArrayList.get(position).getImagepath()));


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image)
                .error(R.drawable.image);
        Glide.with(context).load(uri).apply(options).into(titleText);


        RelativeLayout listitem = rowView.findViewById(R.id.grid_item);

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            listitem.setBackgroundColor(Color.BLACK);


            Drawable myIcon3 = context.getDrawable(R.drawable.ic_more_vert_black_24dp);
            myIcon3.setTint(Color.WHITE);
            options1.setBackground(myIcon3);

            tt.setTextColor(context.getResources().getColor(R.color.per50white));

            tt1.setTextColor(context.getResources().getColor(R.color.material_white));

            griditm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background_dark));

        } else {

            Drawable myIcon3 = context.getDrawable(R.drawable.ic_more_vert_black_24dp);
            myIcon3.setTint(Color.BLACK);
            options1.setBackground(myIcon3);

            listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            tt.setTextColor(context.getResources().getColor(R.color.per54black));

            tt1.setTextColor(context.getResources().getColor(R.color.darkgray));
            griditm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background));
        }


        return rowView;

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public interface EventListener {
        void loadsong(int position);
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