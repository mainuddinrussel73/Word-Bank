package com.example.mainuddin.myapplication34.ui.media;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.data.word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

        //ImageButton titleText = (ImageButton) rowView.findViewById(R.id.play_pause);
        TextView tt = (TextView) rowView.findViewById(R.id.title5);


        //System.out.println("klkl"+MainActivity.contactList.size());
       /* File imgFile = new File(Media_list_activity.ListElementsArrayList.get(position).getImagepath());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


            titleText.setImageBitmap(myBitmap);

        }*/

        tt.setText((Media_list_activity.ListElementsArrayList.get(position).getTitle()));

        //LinearLayout listitem = rowView.findViewById(R.id.list_item);

        if(MainActivity.isDark){

            //System.out.println("klklkl");
            // listitem.setBackgroundColor(Color.BLACK);

            tt.setTextColor(Color.WHITE);



        }else {

            tt.setTextColor(Color.BLACK);
        }


        return rowView;

    };



}