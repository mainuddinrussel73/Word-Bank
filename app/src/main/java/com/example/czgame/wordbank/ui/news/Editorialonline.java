package com.example.czgame.wordbank.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.czgame.wordbank.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class Editorialonline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_editorialonline);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button p,i,in,k,ds,dsun,j,jo,ny,al,fp,wp,bdp;

        p = findViewById(R.id.prothomalo);
        i = findViewById(R.id.itterfaq);
        in = findViewById(R.id.inquilab);
        k = findViewById(R.id.kalerkantho);
        ny = findViewById(R.id.nayadiganta);
        j = findViewById(R.id.jugantor);
        jo = findViewById(R.id.janakantho);

        ds = findViewById(R.id.dailystar);
        dsun = findViewById(R.id.dailysun);

        al = findViewById(R.id.aljazeera);
        fp = findViewById(R.id.foreignpolicy);
        wp = findViewById(R.id.washpost);
        bdp = findViewById(R.id.bdpratidin);


        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "p");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "i");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "inquilab");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "kalerkantho");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        ny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "nayadiganta");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });



        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "jugantor");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });


        jo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "janakantho");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });


        ds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "dailystar");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        dsun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "dailysun");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "aljazeera");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "foreignpolicy");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "washpost");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        bdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Editorialonline.this, news_online.class);

                myIntent.putExtra("tag", "bdpratidin");
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        RelativeLayout relativeLayout = findViewById(R.id.newsoptions);
        RelativeLayout baselayout = findViewById(R.id.baselayout);
        if(isDark) {
            baselayout.setBackgroundColor(Color.BLACK);
            toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
        }else {
            baselayout.setBackgroundColor(Color.WHITE);
            toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
        }
    }
}
