package com.example.mainuddin.myapplication34.ui.tools;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.news_activity;
import com.example.mainuddin.myapplication34.ui.data.News;
import com.example.mainuddin.myapplication34.ui.data.word;

import java.util.ArrayList;
import java.util.List;

public class News_adapter extends BaseAdapter {

    private final Activity context;
    List<News> newsList;

    // private final Integer[] imgid;

    public News_adapter(Activity context) {

        this.context=context;
        this.newsList = new ArrayList<News>();
        this.newsList.addAll(news_activity.newsList);


    }

    @Override
    public int getCount() {
        return news_activity.newsList.size();
    }

    @Override
    public News getItem(int i) {
        return news_activity.newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.news_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.news_title);
        TextView body = (TextView) rowView.findViewById(R.id.news_detail);


        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(news_activity.newsList.get(position).getTITLE());
        body.setText(news_activity.newsList.get(position).getBODY());

        //LinearLayout listitem = rowView.findViewById(R.id.list_item);

        if(MainActivity.isDark){

            //System.out.println("klklkl");
            // listitem.setBackgroundColor(Color.BLACK);



            titleText.setTextColor(Color.WHITE);
            body.setTextColor(Color.WHITE);



        }else {

            titleText.setTextColor(Color.BLACK);
            body.setTextColor(Color.BLACK);
        }


        return rowView;

    };


    /*public void filter(String charText) {
        charText = charText.toLowerCase();
        MainActivity.contactList.clear();
        // System.out.println(this.contactList.size());
        if (charText.length() == 0) {
            MainActivity.contactList.addAll(this.contactList);
        } else {
            for (word wp : this.contactList) {
                if (wp.getWORD().toLowerCase().contains(charText)) {
                    MainActivity.contactList.add(wp);
                }
            }
        }
        // System.out.println(MainActivity.contactList.size());
        notifyDataSetChanged();*
    }*/
}