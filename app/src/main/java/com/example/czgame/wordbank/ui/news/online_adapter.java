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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.core.content.ContextCompat;

public class online_adapter extends BaseAdapter {

    private final Activity context;
    List<News> newsList;
    TextView titleText, body;

    // private final Integer[] imgid;

    public online_adapter(Activity context) {

        this.context = context;
        this.newsList = new ArrayList<News>();
        this.newsList.addAll(news_online.newsList);


    }

    @Override
    public int getCount() {
        return news_online.newsList.size();
    }

    @Override
    public News getItem(int i) {
        return news_online.newsList.get(i);
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

        LinearLayout listitm = rowView.findViewById(R.id.list_item);
        ImageView imageView = rowView.findViewById(R.id.topnews);

        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(newsList.get(position).getTITLE());


        online_adapter.RetrieveFeedTask asyncTask = new online_adapter.RetrieveFeedTask();
        String s = newsList.get(position).getBODY();
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
            listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background_dark));



        } else {

            titleText.setTextColor(Color.BLACK);
            body.setTextColor(Color.BLACK);
            listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.card_background));

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

        String type = MainActivity.prefs.getString("sort", "asc");
        if (type.equals("asc")) {
            Collections.sort(news_activity.newsList);
        } else if (type.equals("des")) {
            Collections.sort(news_activity.newsList, Collections.reverseOrder());
        } else if (type.equals("alp")) {
            Collections.sort(news_activity.newsList,
                    new Comparator<News>() {
                        public int compare(News f1, News f2) {
                            return f1.getTITLE().compareTo(f2.getTITLE());
                        }
                    });
        }
        // System.out.println(MainActivity.contactList.size());
        notifyDataSetChanged();

    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Spanned> {

        private Exception exception;

        protected Spanned doInBackground(String... data) {

            //System.out.println(data);
            return Html.fromHtml(data[0].replace("\n", "<br>"));

        }

        protected void onPostExecute(Spanned text) {
            body.setText(text);

        }
    }
}