package com.example.czgame.wordbank.ui.Quiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.DatabaseHelper;
import com.example.czgame.wordbank.ui.words.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class Quiz_match extends Activity {
    public static int a, b;
    public static word[] words = new word[4];
    public static List<String> s2 = new ArrayList<>();
    public static List<String> s1 = new ArrayList<>();
    private static ArrayList<String> usedNames = new ArrayList<String>();
    ArrayAdapter<String> listadapter;
    ArrayAdapter<String> listadapter2;
    DrawView draw;
    word word = null;
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
    CardView c1,c2,c3,c4,c5,c6,c7,c8;
    Button b1,b2,b3,b4,b5,b6,b7,b8;
    List<Button> optionn =  new ArrayList<>();

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

        s1.clear();
        s2.clear();
        words = new word[4];
        for (int i = 0; i < 4; i++) {

            double randomDouble = Math.random();
            randomDouble = randomDouble * 1991 + 0;
            int randomInt = (int) randomDouble;

            System.out.println(randomInt);
            word = randomword(randomInt);
            System.out.println(word.getWORD());

            s1.add(i,word.getWORD());
            words[i] = word;
            s2.add(i,word.getMEANINGB());

        }


        c1  = findViewById(R.id.radio0);
        c2  = findViewById(R.id.radio1);
        c3  = findViewById(R.id.radio2);
        c4  = findViewById(R.id.radio3);
        c5  = findViewById(R.id.radio4);
        c6  = findViewById(R.id.radio5);
        c7  = findViewById(R.id.radio6);
        c8  = findViewById(R.id.radio7);

        b1 =  findViewById(R.id.op1);
        b2 =  findViewById(R.id.op2);
        b3 =  findViewById(R.id.op3);
        b4 =  findViewById(R.id.op4);
        b5 =  findViewById(R.id.op5);
        b6 =  findViewById(R.id.op6);
        b7 =  findViewById(R.id.op7);
        b8 =  findViewById(R.id.op8);

        optionn.add(b1);
        optionn.add(b2);
        optionn.add(b3);
        optionn.add(b4);
        optionn.add(b5);
        optionn.add(b6);
        optionn.add(b7);
        optionn.add(b8);

        b1.setText(s1.get(0));
        b2.setText(s1.get(1));
        b3.setText(s1.get(2));
        b4.setText(s1.get(3));


        ArrayList<Integer> list = new ArrayList<Integer>();
        Random randomGenerator = new Random();
        while (list.size() < 4) {

            int random = randomGenerator .nextInt(4);
            if (!list.contains(random)) {
                list.add(random);
            }
        }

        Collections.shuffle(s2);


        b5.setText(s2.get(0));
        b6.setText(s2.get(1));
        b7.setText(s2.get(2));
        b8.setText(s2.get(3));




        TextView scoress = findViewById(R.id.scores);

        TextView textView = findViewById(R.id.quiz_question);

        LinearLayout ll = findViewById(R.id.draw_line);
        draw = new DrawView(this);
        ll.addView(draw);
        b5.setClickable(false);
        b6.setClickable(false);
        b7.setClickable(false);
        b8.setClickable(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b5.setClickable(true);
                b6.setClickable(true);
                b7.setClickable(true);
                b8.setClickable(true);
                c1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                float x1 = c1.getX();
                float y1 =  c1.getHeight() / 2 + c1.getY();
                a = 0;
                draw.addSourcePoint(x1, y1);
                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c5.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c5.getX();
                        float y2 =  c5.getHeight() / 2 + c5.getY();
                        b = 0;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c6.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c6.getX();
                        float y2 =  c6.getHeight() / 2 + c6.getY();
                        b = 1;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c7.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c7.getX();
                        float y2 =  c7.getHeight() / 2 + c7.getY();
                        b = 2;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c8.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c8.getX();
                        float y2 =  c8.getHeight() / 2 + c8.getY();
                        b = 3;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b5.setClickable(true);
                b6.setClickable(true);
                b7.setClickable(true);
                b8.setClickable(true);
                c2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                float x1 = c2.getX();
                float y1 =  c2.getHeight() / 2 + c2.getY();
                a = 1;
                draw.addSourcePoint(x1, y1);
                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c5.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c5.getX();
                        float y2 =  c5.getHeight() / 2 + c5.getY();
                        b = 0;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c6.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c6.getX();
                        float y2 =  c6.getHeight() / 2 + c6.getY();
                        b = 1;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c7.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c7.getX();
                        float y2 =  c7.getHeight() / 2 + c7.getY();
                        b = 2;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c8.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c8.getX();
                        float y2 =  c8.getHeight() / 2 + c8.getY();
                        b = 3;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b5.setClickable(true);
                b6.setClickable(true);
                b7.setClickable(true);
                b8.setClickable(true);
                c3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                float x1 = c3.getX();
                float y1 =  c3.getHeight() / 2 + c3.getY();
                a = 2;
                draw.addSourcePoint(x1, y1);
                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c5.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c5.getX();
                        float y2 =  c5.getHeight() / 2 + c5.getY();
                        b = 0;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c6.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c6.getX();
                        float y2 =  c6.getHeight() / 2 + c6.getY();
                        b = 1;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c7.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c7.getX();
                        float y2 =  c7.getHeight() / 2 + c7.getY();
                        b = 2;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c8.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c8.getX();
                        float y2 =  c8.getHeight() / 2 + c8.getY();
                        b = 3;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b5.setClickable(true);
                b6.setClickable(true);
                b7.setClickable(true);
                b8.setClickable(true);
                c4.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                float x1 = c4.getX();
                float y1 =  c4.getHeight() / 2 + c4.getY();
                a = 3;
                draw.addSourcePoint(x1, y1);
                b5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c5.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c5.getX();
                        float y2 =  c5.getHeight() / 2 + c5.getY();
                        b = 0;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c6.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c6.getX();
                        float y2 =  c6.getHeight() / 2 + c6.getY();
                        b = 1;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c7.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c7.getX();
                        float y2 =  c7.getHeight() / 2 + c7.getY();
                        b = 2;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });
                b8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c8.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Quiz_match.this,R.color.uou)));
                        float x2 = ll.getWidth() + c8.getX();
                        float y2 =  c8.getHeight() / 2 + c8.getY();
                        b = 3;
                        draw.addDestinationPoint(x2, y2);
                        b5.setClickable(false);
                        b6.setClickable(false);
                        b7.setClickable(false);
                        b8.setClickable(false);
                    }
                });

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
        LinearLayout linearLayout = findViewById(R.id.draw_line);
        CardView cardView = findViewById(R.id.question);
        LinearLayout linearLayout1 = findViewById(R.id.radioGroup);

        if (isDark) {
            linearLayout1.setBackgroundColor(ContextCompat.getColor(this,R.color.soft_dark));
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.soft_dark));
            relativeLayoutq.setBackgroundColor(ContextCompat.getColor(this,R.color.soft_dark));
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(Quiz_match.this, R.drawable.background_card_dark));
            ll.setBackgroundColor(ContextCompat.getColor(Quiz_match.this, R.color.soft_dark));
            cardView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
           // cardView.setBackground(ContextCompat.getDrawable(Quiz_match.this, R.drawable.back_b));
            textView.setTextColor(Color.WHITE);
            c1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c4.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c5.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c6.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c7.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            c8.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.black)));
            b1.setTextColor(Color.WHITE);
            b2.setTextColor(Color.WHITE);
            b3.setTextColor(Color.WHITE);
            b4.setTextColor(Color.WHITE);
            b5.setTextColor(Color.WHITE);
            b6.setTextColor(Color.WHITE);
            b7.setTextColor(Color.WHITE);
            b8.setTextColor(Color.WHITE);
        } else {
            linearLayout1.setBackgroundColor(ContextCompat.getColor(this,R.color.soft_light));
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.soft_light));
            relativeLayoutq.setBackgroundColor(ContextCompat.getColor(this,R.color.soft_light));
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(Quiz_match.this, R.drawable.background_card));
            ll.setBackgroundColor(ContextCompat.getColor(Quiz_match.this, R.color.soft_light));
            cardView.setBackground(ContextCompat.getDrawable(Quiz_match.this, R.drawable.back_w));
            cardView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            textView.setTextColor(Color.BLACK);
            c1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c4.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c5.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c6.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c7.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            c8.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white)));
            b1.setTextColor(Color.BLACK);
            b2.setTextColor(Color.BLACK);
            b3.setTextColor(Color.BLACK);
            b4.setTextColor(Color.BLACK);
            b5.setTextColor(Color.BLACK);
            b6.setTextColor(Color.BLACK);
            b7.setTextColor(Color.BLACK);
            b8.setTextColor(Color.BLACK);
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



    public void onBackPressed() {

    }



}













