package com.example.mainuddin.myapplication34.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class news_activity extends AppCompatActivity {

    ListView list;
    News_adapter adapter;
    public static List<News> newsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), add_news.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        DBNewsHelper mDBHelper = new DBNewsHelper(this);


        newsList.clear();

        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if(cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                News  word = new News();
                word.setID( Integer.parseInt(cursor.getString(0)));
                word.setTITLE( cursor.getString(1));
                word.setBODY(cursor.getString(2));
                word.setURL(cursor.getString(3));

                newsList.add(word);

            }

            // size = contactList.size();
            adapter = new News_adapter(this);
            list=(ListView)findViewById(R.id.news_list);
            list.setAdapter(adapter);



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(view.getContext(), news_details.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.putExtra("title",newsList.get(position).getTITLE());
                    myIntent.putExtra("body",newsList.get(position).getBODY());
                    myIntent.putExtra("url",newsList.get(position).getURL());
                    myIntent.putExtra("id",position);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                }
            });
        }
        else {

            Toasty.info(news_activity.this,"Nothing to show.",Toasty.LENGTH_LONG).show();
        }
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark && newsList.size()!=0){

            ConstraintLayout constraintLayout  = findViewById(R.id.content_newsre);
            LinearLayout linearLayout =  findViewById(R.id.newslistview);
            constraintLayout.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_viewdark));
            list.setAdapter(adapter);
        }
        else if(!isDark && newsList.size()!=0){

            ConstraintLayout constraintLayout  = findViewById(R.id.content_newsre);
            LinearLayout linearLayout =  findViewById(R.id.newslistview);
            constraintLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.listview_border));
            list.setAdapter(adapter);

        }
        else if(isDark && newsList.size()==0){

            ConstraintLayout constraintLayout  = findViewById(R.id.content_newsre);
            LinearLayout linearLayout =  findViewById(R.id.newslistview);

            constraintLayout.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_viewdark));

        }
        else if(!isDark && newsList.size()==0){
            ConstraintLayout constraintLayout  = findViewById(R.id.content_newsre);
            LinearLayout linearLayout =  findViewById(R.id.newslistview);

            constraintLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.listview_border));


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);


        SearchView searchView;

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }


}
