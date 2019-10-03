package com.example.mainuddin.myapplication34.ui.tools;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.data.word;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends BaseAdapter {

    private final Activity context;
    List<word> contactList;

    // private final Integer[] imgid;

    public MyListAdapter(Activity context) {

        this.context=context;
        this.contactList = new ArrayList<word>();
        this.contactList.addAll(MainActivity.contactList);


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
}