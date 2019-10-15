package com.example.mainuddin.myapplication34.ui.Quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.words.DatabaseHelper;
import com.example.mainuddin.myapplication34.ui.words.word;

import java.util.ArrayList;
import java.util.List;

public class Quiz_confirm extends AppCompatActivity {

    List<word> contactList = new ArrayList<>();
    private Animation mScaleAnimation;

    public static SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_confirm);

        DatabaseHelper mDBHelper = new DatabaseHelper(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Cursor cursor = mDBHelper.getAllData();

        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        // looping through all rows and adding to list
        if(cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                word word = new word();
                word.setID( Integer.parseInt(cursor.getString(0)));
                word.setWORD( cursor.getString(1));
                word.setMEANING(cursor.getString(2));

                contactList.add(word);

            }


        }
        else {

            Toasty.error(Quiz_confirm.this,"Error Nothing found", Toast.LENGTH_LONG).show();
        }

        RelativeLayout relativeLayout = findViewById(R.id.confirm);


        mScaleAnimation = AnimationUtils.loadAnimation(this,R.anim.scale_animation);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark){
            relativeLayout.setBackgroundColor(Color.BLACK);

        }else{
            relativeLayout.setBackgroundColor(Color.WHITE);
        }

        Button button = findViewById(R.id.strtquiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.startAnimation(mScaleAnimation);
                if(contactList.size()>=4){
                    //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                   // MainActivity.score = prefs.getInt("highscore", 0);
                    Intent myIntent = new Intent(Quiz_confirm.this, quiz_page.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 1);}
                else{
                    Toasty.error(Quiz_confirm.this,"Sorry Collect more then 4 words.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
