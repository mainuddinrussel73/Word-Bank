package com.example.czgame.wordbank.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.Home.HomeActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import cdflynn.android.library.checkview.CheckView;

public class News_adapter extends  RecyclerView.Adapter<News_adapter.ViewHolder> {

    private final Activity context;
    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_HEADER1 = 2;
    final  int VIEW_TYPE_ITEM = 1;
    List<News> newsList;

    // private final Integer[] imgid;

    public News_adapter(Activity context,List<News> news) {

        this.context = context;
        this.newsList = new ArrayList<News>();
        this.newsList.addAll(news);


    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
int c = newsList.get(position).TITLE.trim().length() % 3;
        if(c==0){
            return  VIEW_TYPE_ITEM;
        }else if(c==1){
            return VIEW_TYPE_HEADER;
        }else if(c==2){
            return  VIEW_TYPE_HEADER1;
        }
        return c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());


        if (viewType == VIEW_TYPE_HEADER) {
            View listItem= layoutInflater.inflate(R.layout.news_item_header, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return  viewHolder;
        } else if (viewType == VIEW_TYPE_ITEM) {
            View listItem= layoutInflater.inflate(R.layout.news_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }else if(viewType == VIEW_TYPE_HEADER1){
            View listItem= layoutInflater.inflate(R.layout.news_list_header1, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final News myListData = newsList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                holder.titleText.setText(myListData.getTITLE());
                new RetrieveFeedTask(holder).execute(myListData.getBODY());
                if( newsList.get(position).ISREAD==1){
                    holder.mCheckView.check();
                }else{
                    holder.mCheckView.uncheck();
                }
                if ( newsList.get(position).getURL().isEmpty()) {
                    holder.imageView.setImageResource(R.drawable.news);
                } else{
                    Picasso.with(context)
                            .load( newsList.get(position).getURL())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(holder.imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    //Try again online if cache failed
                                    Picasso.with(context)
                                            .load( newsList.get(position).getURL())
                                            .error(R.drawable.news)
                                            .into(holder.imageView, new Callback() {
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
                }

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(view.getContext(), news_details.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("title", myListData.getTITLE());
                        myIntent.putExtra("body", myListData.getBODY());
                        myIntent.putExtra("url", myListData.getURL());
                        myIntent.putExtra("id", position);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),view.findViewById(R.id.topnews), "imageShared");
                        context.startActivity(myIntent, activityOptionsCompat.toBundle());
                    }
                });

                SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                boolean isDark = prefs.getBoolean("isDark", false);
                if (isDark) {

                    //System.out.println("klklkl");
                    holder.linearLayout.setBackgroundColor(Color.BLACK);


                    holder.titleText.setTextColor(Color.WHITE);
                    holder.body.setTextColor(Color.parseColor("#99ffffff"));
                    holder.listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));



                } else {

                    holder.linearLayout.setBackgroundColor(Color.WHITE);
                    holder.titleText.setTextColor(Color.BLACK);
                    holder.body.setTextColor(Color.parseColor("#99000000"));
                    holder.listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));

                }
                break;
            case VIEW_TYPE_HEADER:
                holder.titleText.setText(myListData.getTITLE());
                new RetrieveFeedTask(holder).execute(myListData.getBODY());
                if( newsList.get(position).ISREAD==1){
                    holder.mCheckView.check();
                }else{
                    holder.mCheckView.uncheck();
                }
                if ( newsList.get(position).getURL().isEmpty()) {
                    holder.imageView.setImageResource(R.drawable.news);
                } else{
                    Picasso.with(context)
                            .load( newsList.get(position).getURL())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(holder.imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    //Try again online if cache failed
                                    Picasso.with(context)
                                            .load( newsList.get(position).getURL())
                                            .error(R.drawable.news)
                                            .into(holder.imageView, new Callback() {
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
                }

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(view.getContext(), news_details.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("title", myListData.getTITLE());
                        myIntent.putExtra("body", myListData.getBODY());
                        myIntent.putExtra("url", myListData.getURL());
                        myIntent.putExtra("id", position);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),view.findViewById(R.id.topnews), "imageShared");
                        context.startActivity(myIntent, activityOptionsCompat.toBundle());
                    }
                });

                prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                isDark = prefs.getBoolean("isDark", false);
                if (isDark) {

                    //System.out.println("klklkl");
                    holder.linearLayout.setBackgroundColor(Color.BLACK);


                    holder.titleText.setTextColor(Color.WHITE);
                    holder.body.setTextColor(Color.parseColor("#99ffffff"));
                    holder.listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));



                } else {

                    holder.linearLayout.setBackgroundColor(Color.WHITE);
                    holder.titleText.setTextColor(Color.BLACK);
                    holder.body.setTextColor(Color.parseColor("#99000000"));
                    holder.listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));

                }
                break;
            case VIEW_TYPE_HEADER1:
                holder.titleText.setText(myListData.getTITLE());
                new RetrieveFeedTask(holder).execute(myListData.getBODY());
                if( newsList.get(position).ISREAD==1){
                    holder.mCheckView.check();
                }else{
                    holder.mCheckView.uncheck();
                }
                if ( newsList.get(position).getURL().isEmpty()) {
                    holder.imageView.setImageResource(R.drawable.news);
                } else{
                    Picasso.with(context)
                            .load( newsList.get(position).getURL())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(holder.imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    //Try again online if cache failed
                                    Picasso.with(context)
                                            .load( newsList.get(position).getURL())
                                            .error(R.drawable.news)
                                            .into(holder.imageView, new Callback() {
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
                }

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(view.getContext(), news_details.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("title", myListData.getTITLE());
                        myIntent.putExtra("body", myListData.getBODY());
                        myIntent.putExtra("url", myListData.getURL());
                        myIntent.putExtra("id", position);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),view.findViewById(R.id.topnews), "imageShared");
                        context.startActivity(myIntent, activityOptionsCompat.toBundle());
                    }
                });

                prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                isDark = prefs.getBoolean("isDark", false);
                if (isDark) {

                    //System.out.println("klklkl");
                    holder.linearLayout.setBackgroundColor(Color.BLACK);


                    holder.titleText.setTextColor(Color.WHITE);
                    holder.body.setTextColor(Color.parseColor("#99ffffff"));
                    holder.listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));



                } else {

                    holder.linearLayout.setBackgroundColor(Color.WHITE);
                    holder.titleText.setTextColor(Color.BLACK);
                    holder.body.setTextColor(Color.parseColor("#99000000"));
                    holder.listitm.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));

                }
                break;
        }



        // holder.duration.setText(s);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return  newsList.size();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase();
        newsList.clear();
        // System.out.println(this.contactList.size());
        if (charText.length() == 0) {
            newsList.addAll(newsList);
        } else {
            for (News wp : newsList) {
                if (wp.getTITLE().toLowerCase().contains(charText)) {
                    newsList.add(wp);
                }
            }
        }

        String type = HomeActivity.prefs.getString("sort", "asc");
        if (type.equals("asc")) {
            Collections.sort(newsList);
        } else if (type.equals("des")) {
            Collections.sort( newsList, Collections.reverseOrder());
        } else if (type.equals("alp")) {
            Collections.sort( newsList,
                    new Comparator<News>() {
                        public int compare(News f1, News f2) {
                            return f1.getTITLE().compareTo(f2.getTITLE());
                        }
                    });
        }
        // System.out.println(MainActivity.contactList.size());
        notifyDataSetChanged();

    }

    int getrand(){
        final int min = 0;
        final int max = 2;
        final int random = new Random().nextInt((max - min) + 1) + min;
        return random;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, body;
        CheckView mCheckView;
        ImageView imageView;
        FrameLayout linearLayout;
        LinearLayout listitm;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.topnews);
            titleText = itemView.findViewById(R.id.news_title);
            body = itemView.findViewById(R.id.news_detail);
            mCheckView = itemView.findViewById(R.id.check);
            linearLayout = itemView.findViewById(R.id.baselayout);
            listitm = itemView.findViewById(R.id.list_item);

        }

    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView titleText, body;
        CheckView mCheckView;
        ImageView imageView;
        FrameLayout linearLayout;
        LinearLayout listitm;

        public ViewHolder1(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.topnews);
            titleText = itemView.findViewById(R.id.news_title);
            body = itemView.findViewById(R.id.news_detail);
            mCheckView = itemView.findViewById(R.id.check);
            linearLayout = itemView.findViewById(R.id.baselayout);
            listitm = itemView.findViewById(R.id.list_item);

        }

    }
    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView titleText, body;
        CheckView mCheckView;
        ImageView imageView;
        FrameLayout linearLayout;
        LinearLayout listitm;

        public ViewHolder2(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.topnews);
            titleText = itemView.findViewById(R.id.news_title);
            body = itemView.findViewById(R.id.news_detail);
            mCheckView = itemView.findViewById(R.id.check);
            linearLayout = itemView.findViewById(R.id.baselayout);
            listitm = itemView.findViewById(R.id.list_item);

        }

    }

    class RetrieveFeedTask extends AsyncTask<String, Spanned, Spanned> {

        private Exception exception;
        ViewHolder holder;

        RetrieveFeedTask(ViewHolder holder){
            this.holder = holder;

        }
        protected Spanned doInBackground(String... data) {

            return Html.fromHtml(data[0].replace("\n", "<br>"));

        }

        protected void onPostExecute(Spanned text) {
            holder.body.setText(text);

        }
    }

}

