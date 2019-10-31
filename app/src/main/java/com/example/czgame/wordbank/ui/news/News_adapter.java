package com.example.czgame.wordbank.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class News_adapter extends BaseAdapter {

    private final Activity context;
    List<News> newsList;
    TextView titleText, body;

    // private final Integer[] imgid;

    public News_adapter(Activity context) {

        this.context = context;
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

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.news_list_item, null, true);

        titleText = rowView.findViewById(R.id.news_title);
        body = rowView.findViewById(R.id.news_detail);

        ImageView imageView = rowView.findViewById(R.id.topnews);

        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(news_activity.newsList.get(position).getTITLE());


        RetrieveFeedTask asyncTask = new RetrieveFeedTask();
        String s = news_activity.newsList.get(position).getTITLE();
        asyncTask.execute(s);






        Picasso.with(context)
                .load(newsList.get(position).getURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(newsList.get(position).getURL())
                                .error(R.drawable.news)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

        //LinearLayout listitem = rowView.findViewById(R.id.list_item);

        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {

            //System.out.println("klklkl");
            // listitem.setBackgroundColor(Color.BLACK);


            titleText.setTextColor(Color.WHITE);
            body.setTextColor(Color.WHITE);


        } else {

            titleText.setTextColor(Color.BLACK);
            body.setTextColor(Color.BLACK);
        }


        return rowView;

    }


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

    class RetrieveFeedTask extends AsyncTask<String, Void, Spanned> {

        private Exception exception;

        protected Spanned doInBackground(String... data) {

            return Html.fromHtml(data[0].replace("\n", "<br>"));

        }

        protected void onPostExecute(Spanned text) {
            body.setText(text);

        }
    }

}

