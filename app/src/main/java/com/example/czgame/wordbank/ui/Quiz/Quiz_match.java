package com.example.czgame.wordbank.ui.Quiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.DatabaseHelper;
import com.example.czgame.wordbank.ui.words.word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import androidx.core.content.ContextCompat;

public class Quiz_match extends Activity {
    public static int a, b;
    public static word[] words = new word[4];
    public static String[] s2 = new String[4];
    public static String[] s3 = new String[4];
    private static ArrayList<String> usedNames = new ArrayList<String>();
    ArrayAdapter<String> listadapter;
    ArrayAdapter<String> listadapter2;
    DrawView draw;
    word word = null;
    String[] s1 = new String[4];
    boolean toogle = true;
    Random Dice = new Random();
    float x1;
    float y1;
    float x2;
    float y2;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    public static TextView scoress;
    public  static  int score1 = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_match);

        final Intent intent = getIntent();
        score1 = intent.getIntExtra("ss", 0);
        scoress = findViewById(R.id.scores);
        scoress.setText("Current score : " + score1);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        for (int i = 0; i < 4; i++) {

            double randomDouble = Math.random();
            randomDouble = randomDouble * 1991 + 0;
            int randomInt = (int) randomDouble;

            System.out.println(randomInt);

            while (word == null) {
                word = randomword(randomInt);
            }

            System.out.println(word.getWORD());

            s1[i] = word.getWORD();
            words[i] = word;
            s2[i] = word.getMEANINGB();
            s3[i] = word.getMEANINGB();

            word = null;
        }


        Fighters();

        for (int i = 0; i < usedNames.size(); i++) {
            System.out.println("----------------------");
            System.out.println(usedNames.get(i));
            System.out.println(s2[i]);
            System.out.println("----------------------");
            //s2[i] = usedNames.get(i);
        }

        for (int i = 0; i < usedNames.size(); i++) {
            s2[i] = usedNames.get(i);
        }

        usedNames.clear();

        ListView lv = findViewById(R.id.text_list);
        ListView lv2 = findViewById(R.id.text_list1);


        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(s1));
        listadapter = new ArrayAdapter<String>(this, R.layout.rowtext, s1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setMaxLines(3);
                if (isDark) {

                    textView.setTextColor(Color.WHITE);
                } else {

                    textView.setTextColor(Color.BLACK);
                }

                return textView;
            }
        };
        lv.setAdapter(listadapter);


        ArrayList<String> list2 = new ArrayList<String>();
        list2.addAll(Arrays.asList(s2));
        listadapter2 = new ArrayAdapter<String>(this, R.layout.rowtext, s2) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setMaxLines(3);
                if (isDark) {

                    textView.setTextColor(Color.WHITE);
                } else {

                    textView.setTextColor(Color.BLACK);
                }

                return textView;
            }
        };
        lv2.setAdapter(listadapter2);


        TextView scoress = findViewById(R.id.scores);

        TextView textView = findViewById(R.id.quiz_question);

        LinearLayout ll = findViewById(R.id.draw_line);
        draw = new DrawView(this);
        ll.addView(draw);

        lv2.setOnItemClickListener(null);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                if (lv.getChildAt(arg2).isEnabled()) {
                    float x1 = v.getX();
                    float y1 = 60 + v.getHeight() / 2 + v.getY();
                    a = arg2;
                    draw.addSourcePoint(x1, y1);
                    //lv.getChildAt(a).setEnabled(false);
                    v.setBackground(ContextCompat.getDrawable(Quiz_match.this, R.drawable.border2));
                    lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                            if (lv.getChildAt(a).isEnabled() && lv2.getChildAt(arg2).isEnabled()) {
                                lv.getChildAt(a).setEnabled(false);
                                lv2.getChildAt(arg2).setEnabled(false);
                                float x2 = ll.getWidth() + v.getX();
                                float y2 = 60 + v.getHeight() / 2 + v.getY();
                                b = arg2;
                                draw.addDestinationPoint(x2, y2);
                                v.setBackground(ContextCompat.getDrawable(Quiz_match.this, R.drawable.border2));

                                //a=-1;
                            }

                            Log.d("list", "image positions x2:" + x2 + " y2:" + y2);
                            lv2.setOnItemClickListener(null);
                        }
                    });

                }
                Log.d("list", "text positions x1:" + x1 + " y1:" + y1);
            }
        });

        Button button = findViewById(R.id.nextpage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(Quiz_match.this, Quiz_match.class);
                myIntent.putExtra("ss", score1);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });
        Button button2 = findViewById(R.id.extpage);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                if (score1 > prefs.getInt("highscore1", 0)) {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("highscore1", score1);
                    editor.commit();

                }

                Intent myIntent = new Intent(view.getContext(), quiz_result.class);

                myIntent.putExtra("score", score1);

                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

        RelativeLayout relativeLayout = findViewById(R.id.mmmmm);
        RelativeLayout relativeLayoutq = findViewById(R.id.buttonss);
        LinearLayout linearLayout = findViewById(R.id.llll);


        if (isDark) {
            relativeLayout.setBackgroundColor(Color.BLACK);
            relativeLayoutq.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundColor(Color.BLACK);
            lv.setBackgroundDrawable(ContextCompat.getDrawable(Quiz_match.this, R.drawable.list_viewdark));
            lv2.setBackgroundDrawable(ContextCompat.getDrawable(Quiz_match.this, R.drawable.list_viewdark));
            lv.setAdapter(listadapter);
            lv2.setAdapter(listadapter2);
            scoress.setTextColor(Color.WHITE);
            textView.setTextColor(Color.WHITE);
        } else {
            relativeLayout.setBackgroundColor(Color.WHITE);
            relativeLayoutq.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundColor(Color.WHITE);
            lv.setBackgroundDrawable(ContextCompat.getDrawable(Quiz_match.this, R.drawable.listview_border));
            lv2.setBackgroundDrawable(ContextCompat.getDrawable(Quiz_match.this, R.drawable.listview_border));
            lv.setAdapter(listadapter);
            lv2.setAdapter(listadapter2);
            scoress.setTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
        }


    }

    public word randomword(int iii) {


        mDBHelper = new DatabaseHelper(this);


        //mDBHelper.getAllData();
        mDb = mDBHelper.getReadableDatabase();


        // Select All Query
        // String selectQuery = "SELECT  * FROM " + "Word_table";

        String query = "SELECT * FROM Word_table WHERE ID =" + iii;

        Cursor cursor = mDb.rawQuery(query, null);

        word word = new word();
        while (cursor.moveToNext()) {

            word.setID(Integer.parseInt(cursor.getString(0)));
            word.setWORD(cursor.getString(1));
            word.setMEANINGB(cursor.getString(2));

        }

        return word;
    }


    public void Fighters() {


        for (int i = 0; i < 4; i++) {
            String checkedName = null;
            do {
                checkedName = s2[(int) (Math.random() * 4)];
            } while (usedNames.contains(checkedName));
            usedNames.add(checkedName);
        }


    }

    public void onBackPressed() {

    }


}













