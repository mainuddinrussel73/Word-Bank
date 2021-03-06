package com.example.czgame.wordbank.ui.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.DatabaseHelper;
import com.example.czgame.wordbank.ui.words.word;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class Quiz_confirm extends AppCompatActivity {

    public static SharedPreferences prefs;

    public static int questioncount = 10;
    public  static  List<word> contactList = new ArrayList<>();
    private Animation mScaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                word word = new word();
                word.setID(Integer.parseInt(cursor.getString(0)));
                word.setWORD(cursor.getString(1));
                word.setMEANINGB(cursor.getString(2));

                contactList.add(word);

            }


        } else {

            Toasty.error(Quiz_confirm.this, "Error Nothing found", Toast.LENGTH_LONG).show();
        }

        RelativeLayout relativeLayout1 = findViewById(R.id.spinerlayout);
        MaterialSpinner spinner = relativeLayout1.findViewById(R.id.questions);
        spinner.setItems(new Integer(10), new Integer(25), new Integer(50), new Integer(100));
        switch (questioncount) {
            case 10:
                spinner.setSelectedIndex(0);
                break;
            case 25:
                spinner.setSelectedIndex(1);
                break;
            case 50:
                spinner.setSelectedIndex(2);
                break;
            case 100:
                spinner.setSelectedIndex(3);
                break;

        }

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Integer>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Integer item) {
                Snackbar.make(view, "Clicked " + item.intValue(), Snackbar.LENGTH_LONG).show();


                if (item.intValue() == 10) {
                    questioncount = 10;

                } else if (item.intValue() == 25) {
                    questioncount = 25;
                } else if (item.intValue() == 50) {
                    questioncount = 50;
                } else if (item.intValue() == 100) {
                    questioncount = 100;
                } else {
                    questioncount = contactList.size() - 1;
                }
            }


        });

        RelativeLayout relativeLayout = findViewById(R.id.confirm);


        mScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

            toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

        TextView opt1 = findViewById(R.id.option1);
        TextView opt2 = findViewById(R.id.option2);
        RelativeLayout match = findViewById(R.id.match);
        RelativeLayout multi = findViewById(R.id.multi);
        TextView opt3 = findViewById(R.id.option3);
        RelativeLayout spell = findViewById(R.id.spell);
        if (isDark) {
            relativeLayout.setBackgroundColor(Color.BLACK);
            opt1.setTextColor(Color.WHITE);
            opt2.setTextColor(Color.WHITE);
            opt3.setTextColor(Color.WHITE);
            spinner.setBackgroundColor(Color.rgb(31,39,41));
            spinner.setTextColor(Color.WHITE);
            match.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background_dark));
            multi.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background_dark));
            spell.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background_dark));

        } else {
            relativeLayout.setBackgroundColor(Color.WHITE);
            opt1.setTextColor(Color.BLACK);
            opt2.setTextColor(Color.BLACK);
            opt3.setTextColor(Color.BLACK);
            spinner.setBackgroundColor(Color.WHITE);
            spinner.setTextColor(Color.BLACK);
            match.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background));
            multi.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background));
            spell.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background));
        }

        Button button = findViewById(R.id.strtquiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.startAnimation(mScaleAnimation);
                if (contactList.size() >= 4) {
                    //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    // MainActivity.score = prefs.getInt("highscore", 0);
                    Intent myIntent = new Intent(Quiz_confirm.this, quiz_page.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 1);
                } else {
                    Toasty.error(Quiz_confirm.this, "Sorry Collect more then 4 words.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button button1 = findViewById(R.id.strtquizmatch);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.startAnimation(mScaleAnimation);
                if (contactList.size() >= 4) {
                    //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    // MainActivity.score = prefs.getInt("highscore", 0);
                    Intent myIntent = new Intent(Quiz_confirm.this, Quiz_match.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 1);
                } else {
                    Toasty.error(Quiz_confirm.this, "Sorry Collect more then 4 words.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button button3 = findViewById(R.id.spelling);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button3.startAnimation(mScaleAnimation);
                if (contactList.size() >= 2) {
                    //SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    // MainActivity.score = prefs.getInt("highscore", 0);
                    Intent myIntent = new Intent(Quiz_confirm.this, quiz_spelling.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 1);
                } else {
                    Toasty.error(Quiz_confirm.this, "Sorry Collect more then 4 words.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
