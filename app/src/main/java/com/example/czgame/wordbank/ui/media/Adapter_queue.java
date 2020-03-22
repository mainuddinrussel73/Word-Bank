package com.example.czgame.wordbank.ui.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.utill.ItemTouchHelperAdapter;
import com.example.czgame.wordbank.utill.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.czgame.wordbank.ui.media.fragment_music_list.prefm;
import static com.example.czgame.wordbank.ui.media.fragment_plau_queue.playqueue;

public class Adapter_queue  extends RecyclerView.Adapter<Adapter_queue.ViewHolder>  implements ItemTouchHelperAdapter {

    private final Activity context;
    List<Audio> contactList;

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


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.queue_music_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Audio myListData = contactList.get(position);
        holder.tt.setText(myListData.getTitle());
        holder.tt1.setText(myListData.getArtist());

        long num = Long.parseLong(myListData.getDuration());
        String s = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(num),
                TimeUnit.MILLISECONDS.toSeconds(num) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(num))
        );
        holder.duration.setText(s);



        holder.listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadsong(position);
                // Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });



        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            holder.listitem.setBackgroundColor(Color.BLACK);


            holder.tt.setTextColor(context.getResources().getColor(R.color.per50white));

            holder.tt1.setTextColor(context.getResources().getColor(R.color.material_white));

            holder.listitem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));

        } else {

            holder.listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            holder.tt.setTextColor(context.getResources().getColor(R.color.per54black));

            holder.tt1.setTextColor(context.getResources().getColor(R.color.darkgray));
            holder.listitem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return playqueue.size();
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

    public  void  loadsong(int position){
        //System.out.println(pro);
        System.out.println("event");
        PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefm.edit();
        editor.putInt("positionnow",position);
        editor.commit();

        context.sendBroadcast(new Intent(Constants.ACTION.LOAD_ACTION));
    }

    @Override
    public void onItemDismiss(int position) {
        contactList.remove(position);
        playqueue.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(contactList, i, i + 1);
                Collections.swap(playqueue, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(contactList, i, i - 1);
                Collections.swap(playqueue, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        ImageButton imageView;
        TextView tt, tt1, duration;
        RelativeLayout listitem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.play_pause);
            this.duration = itemView.findViewById(R.id.duration);
            this.tt = itemView.findViewById(R.id.title5);
            this.tt1 = itemView.findViewById(R.id.title6);
            listitem = itemView.findViewById(R.id.list_item);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }



}