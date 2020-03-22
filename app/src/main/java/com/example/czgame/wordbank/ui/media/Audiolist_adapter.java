package com.example.czgame.wordbank.ui.media;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import static com.example.czgame.wordbank.ui.media.fragment_music_list.ListElementsArrayList;
import static com.example.czgame.wordbank.ui.media.fragment_plau_queue.playqueue;


public class Audiolist_adapter extends RecyclerView.Adapter<Audiolist_adapter.ViewHolder> {

    private final Activity context;
    List<Audio> contactList;
    EventListener listener;
    List<Audio> audioList ;

    public Audiolist_adapter(Activity context,EventListener listener,List<Audio>  queue) {

        this.context = context;
        this.contactList = queue;
        this.listener = listener;
        this.audioList = new ArrayList<>();
        GetAllMediaMp3Files();


    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    @NonNull
    @Override
    public Audiolist_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.media_item_layout, parent, false);
        Audiolist_adapter.ViewHolder viewHolder = new Audiolist_adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Audiolist_adapter.ViewHolder holder, int position) {
        final Audio myListData = contactList.get(position);

        long num = Long.parseLong(myListData.getDuration());

        holder.tt.setText((myListData.getTitle()));
        holder.tt1.setText((myListData.getArtist()));
        String s = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(num),
                TimeUnit.MILLISECONDS.toSeconds(num) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(num))
        );
        holder.duration.setText(s);

        holder.listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.loadsong(position);
            }
        });



        holder.options1.setOnClickListener(new View.OnClickListener() {
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
                            if(!playqueue.contains(myListData)){
                                playqueue.add(myListData);
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
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(myListData.getImagepath()));

        //in.close();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image)
                .error(R.drawable.image);
        Glide.with(context).load(uri).apply(options).into(holder.imageView);




        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            holder.listitem.setBackgroundColor(Color.BLACK);


            Drawable myIcon3 = context.getDrawable(R.drawable.ic_more_vert_black_24dp);
            myIcon3.setTint(Color.WHITE);
            holder.options1.setBackground(myIcon3);

            holder.tt.setTextColor(context.getResources().getColor(R.color.white));

            holder.tt1.setTextColor(Color.parseColor("#99ffffff"));

            holder.listitem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));

        } else {

            Drawable myIcon3 = context.getDrawable(R.drawable.ic_more_vert_black_24dp);
            myIcon3.setTint(Color.BLACK);
            holder.options1.setBackground(myIcon3);

            holder.listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            holder.tt.setTextColor(context.getResources().getColor(R.color.black));

            holder.tt1.setTextColor(Color.parseColor("#99000000"));
            holder.listitem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        ListElementsArrayList.clear();

        if (charText.length() == 0) {
            ListElementsArrayList.addAll(audioList);
        } else {
            for (Audio wp : audioList) {
                if (wp.getTitle().toLowerCase().contains(charText)) {
                    ListElementsArrayList.add(wp);
                }
            }
        }
        //SharedPreferences prefs = MyListAdapter.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences prefm = PreferenceManager.getDefaultSharedPreferences(context);
        String type = prefm.getString("sort_music","dsnd");
        System.out.println(type);
        if (type.equals("asnd")) {
            Collections.sort(ListElementsArrayList);
        } else if (type.equals("dsnd")) {
            Collections.sort(ListElementsArrayList, Collections.reverseOrder());
        } else if (type.equals("asnd_t")) {
            Collections.sort(ListElementsArrayList,
                    new Comparator<Audio>() {
                        public int compare(Audio f1, Audio f2) {
                            return f1.getData().compareTo(f2.getData());
                        }
                    });
        }
        else if (type.equals("dsnd_t")) {
            Collections.sort(ListElementsArrayList,
                    new Comparator<Audio>() {
                        public int compare(Audio f1, Audio f2) {
                            return f2.getData().compareTo(f1.getData());
                        }
                    });
        }
        // System.out.println(HomeActivity.contactList.size());
        this.notifyDataSetChanged();
    }

    public  void GetAllMediaMp3Files() {



        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG);

        } else {

            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int date = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                Audio audio = new Audio();
                String SongTitle = cursor.getString(Title);
                String SongAlbum = cursor.getString(album);
                String SongArtist = cursor.getString(artist);
                String SongDate = cursor.getString(date);
                String SongDuration = cursor.getString(duration);

                String albumid = cursor.getString(albumId);

                //path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                audio.setTitle(SongTitle);
                audio.setAlbum(SongAlbum);
                audio.setArtist(SongArtist);
                audio.setData(SongDate);
                audio.setImagepath(albumid);
                audio.setDuration(SongDuration);


                // Adding Media File Names to ListElementsArrayList.
                audioList.add(audio);

            } while (cursor.moveToNext());


            //System.out.println(Albumid.size());
        }
    }

    @Override
    public int getItemCount() {
        return ListElementsArrayList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tt, tt1, duration;
        RelativeLayout listitem;
        ImageButton options1;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.play_pause);
            this.duration = itemView.findViewById(R.id.duration);
            this.options1 = itemView.findViewById(R.id.options);
            this.tt = itemView.findViewById(R.id.title5);
            this.tt1 = itemView.findViewById(R.id.title6);
            listitem = itemView.findViewById(R.id.list_item);

        }
    }

    // private final Integer[] imgid;
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




}