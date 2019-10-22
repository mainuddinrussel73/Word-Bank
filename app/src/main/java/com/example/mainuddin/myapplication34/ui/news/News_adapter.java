package com.example.mainuddin.myapplication34.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.news.news_activity;
import com.example.mainuddin.myapplication34.ui.news.News;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

        ImageView imageView = rowView.findViewById(R.id.topnews);

        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(news_activity.newsList.get(position).getTITLE());
        body.setText(news_activity.newsList.get(position).getBODY());



        if(newsList.get(position).getURL().equals("empty")){
            Picasso.with(context).load("https://www.albertadoctors.org/images/ama-master/feature/Stock%20photos/News.jpg").into(imageView);
        }
        else Picasso.with(context).load(newsList.get(position).getURL()).into(imageView);
        //imageView.setImageDrawable(LoadImageFromWebOperations(newsList.get(position).getURL()));

        //LinearLayout listitem = rowView.findViewById(R.id.list_item);

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark){

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


    public void filter(String charText) {
        charText = charText.toLowerCase();
        news_activity.newsList.clear();
        // System.out.println(this.contactList.size());
        if (charText.length() == 0) {
            news_activity.newsList.addAll(this.newsList);
        } else {
            for (News wp : this.newsList) {
                if (wp.getTITLE().toLowerCase().contains(charText)) {
                    news_activity.newsList.add(wp);
                }
            }
        }
        // System.out.println(MainActivity.contactList.size());
        notifyDataSetChanged();
    }

}

class RetrieveFeedTask extends AsyncTask<String, Void, Bitmap> {

    private Exception exception;

    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            return bitmap;
        } catch (Exception e) {
            this.exception = e;

            return null;
        }
    }

    protected void onPostExecute( ) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}