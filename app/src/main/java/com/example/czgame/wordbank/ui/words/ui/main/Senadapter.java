package com.example.czgame.wordbank.ui.words.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;

public class Senadapter extends BaseAdapter {

    private final Activity context;
    List<sentence> sentenceList;
    HashMap<String, Integer> alphaIndexer;

    String[] selections;
    // private final Integer[] imgid;

    public Senadapter(Activity context) {

        this.context = context;
        this.sentenceList = new ArrayList<sentence>();
        this.sentenceList.addAll(TabFragment2.sentenceList);


    }

    @Override
    public int getCount() {
        return TabFragment2.sentenceList.size();
    }

    @Override
    public sentence getItem(int i) {
        return TabFragment2.sentenceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.senlist_item, null, true);

        TextView titleText = rowView.findViewById(R.id.title);
        TextView number = rowView.findViewById(R.id.num);


        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(TabFragment2.sentenceList.get(position).getSENTENCE());
        number.setText(Integer.toString(TabFragment2.sentenceList.get(position).getID()));

        LinearLayout listitem = rowView.findViewById(R.id.list_item);


        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {

            //System.out.println("klklkl");
            // listitem.setBackgroundColor(Color.BLACK);


            titleText.setTextColor(Color.WHITE);
            number.setTextColor(Color.WHITE);

            listitem.setBackground(ContextCompat.getDrawable(context,R.drawable.card_background_dark));


        } else {

            titleText.setTextColor(Color.BLACK);
            number.setTextColor(Color.BLACK);
            listitem.setBackground(ContextCompat.getDrawable(context,R.drawable.card_background));
        }


        return rowView;

    }
}