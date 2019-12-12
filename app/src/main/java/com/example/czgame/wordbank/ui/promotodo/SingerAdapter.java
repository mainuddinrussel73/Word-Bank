package com.example.czgame.wordbank.ui.promotodo;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.backup_scheudle.Task_Detail;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SingerAdapter  extends RecyclerView.Adapter<SingerAdapter.ViewHolder>{
    private final Activity context;
    private List<Task_Detail> listdata;

    // RecyclerView recyclerView;
    public SingerAdapter(List<Task_Detail> listdata,Activity context) {
        this.listdata = listdata;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recycle_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            listItem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background_dark));


        } else {

            listItem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background));

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Task_Detail myListData = listdata.get(position);
        holder.textView.setText(listdata.get(position).getTitle());
        holder.num.setText(listdata.get(position).getCompleted_promotodo() / 2.0 +"h");
        holder.s.setText("Start : "+listdata.get(position).getSTART_TIME());
        holder.e.setText("End : "+listdata.get(position).getEND_TIME());

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {

            //System.out.println("klklkl");
            // listitem.setBackgroundColor(Color.BLACK);


            holder.textView.setTextColor(Color.WHITE);
            holder.num.setTextColor(Color.WHITE);
            //.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background_dark));



        } else {

            holder.textView.setTextColor(Color.BLACK);
            holder.textView.setTextColor(Color.BLACK);
           // listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background));

        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,num,s,e;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.full_name_tv);
            this.num = itemView.findViewById(R.id.num_of_pro);
            this.s = itemView.findViewById(R.id.time_s);
            this.e = itemView.findViewById(R.id.time_e);
        }
    }
}
