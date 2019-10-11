package com.example.mainuddin.myapplication34.ui.tools;

import android.app.Activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.data.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends BaseAdapter implements SectionIndexer {

    private final Activity context;
    List<word> contactList;
    HashMap<String,Integer> alphaIndexer;

    String[] selections;
    // private final Integer[] imgid;

    public MyListAdapter(Activity context) {

        this.context=context;
        this.contactList = new ArrayList<word>();
        this.contactList.addAll(MainActivity.contactList);
        this.alphaIndexer= new HashMap<String ,Integer>();

        int size = this.contactList.size();

        for (int i = 0; i < size; i++) {
            String w = contactList.get(i).getWORD();
            String ch = w.substring(0,1);
            ch = ch.toLowerCase();
            if(!alphaIndexer.containsKey(ch)){
                alphaIndexer.put(ch,i);
            }
        }
        Set<String> selectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<>(selectionLetters);
        Collections.sort(sectionList);
        selections = new String[sectionList.size()];
        sectionList.toArray(selections);

    }

    @Override
    public int getCount() {
        return MainActivity.contactList.size();
    }

    @Override
    public word getItem(int i) {
        return MainActivity.contactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView number = (TextView) rowView.findViewById(R.id.num);


        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(MainActivity.contactList.get(position).getWORD());
        number.setText(Integer.toString(MainActivity.contactList.get(position).getID()));

        //LinearLayout listitem = rowView.findViewById(R.id.list_item);

        if(MainActivity.isDark){

            //System.out.println("klklkl");
           // listitem.setBackgroundColor(Color.BLACK);



                titleText.setTextColor(Color.WHITE);
                number.setTextColor(Color.WHITE);



        }else {

                titleText.setTextColor(Color.BLACK);
                number.setTextColor(Color.BLACK);
            }


        return rowView;

    };


    public void filter(String charText) {
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
        notifyDataSetChanged();
    }

    public void filter1(String charText) {
        charText = charText.toLowerCase();
        MainActivity.contactList.clear();
        // System.out.println(this.contactList.size());
        if (charText.length() == 0) {
            MainActivity.contactList.addAll(this.contactList);
        } else {
            for (word wp : this.contactList) {
                if (wp.getMEANING().toLowerCase().contains(charText)) {
                    MainActivity.contactList.add(wp);
                }
            }
        }
        // System.out.println(MainActivity.contactList.size());
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return selections;
    }

    @Override
    public int getPositionForSection(int i) {
        return alphaIndexer.get(this.selections[i]);
    }

    @Override
    public int getSectionForPosition(int i) {
       return 0;
    }
}