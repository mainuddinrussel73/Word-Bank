package com.example.mainuddin.myapplication34.ui.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.words.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.dmoral.toasty.Toasty;

import static com.example.mainuddin.myapplication34.ui.Quiz.quiz_result.wordBuck;

public class quiz_page extends AppCompatActivity {

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay,nxt,previousquiz;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private TextView textView,scoress;
    public static int score = 0;
    SharedPreferences gamePrefs;
    ScrollView scrollView;
    CountDownTimer cTimer = null;
    List<String> word = new ArrayList<>();
    TextView timer;
    public static int correct = 0;
    public static int wrong = 0;
    public static int ignored = 0;
    public boolean isPause = false;
    public long total = 30000;
    public  long temp = 0;

    int size;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);



        final Intent intent = getIntent();
        score = intent.getIntExtra("s",0);
        scoress = findViewById(R.id.scores);
        scoress.setText("Current score : "+String.valueOf(score));
        btnDisplay = findViewById(R.id.btnDisplay);
        btnDisplay.setEnabled(true);
        textView = findViewById(R.id.quiz_question);
        textView.setMovementMethod(new ScrollingMovementMethod());

        scrollView = findViewById(R.id.scrollButtons);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);



        size = intent.getIntExtra("size",0);


        double randomDouble = Math.random();
        randomDouble = randomDouble * 3 + 0;
        int randomInt = (int) randomDouble;

        while (word.isEmpty()){
            word = randomword(randomInt);
        }
        textView.setText("What is the meaning of the word : "+word.get(0)+"?");

        RadioGroup radioGroup = (RadioGroup)scrollView.findViewById(R.id.radioGroup);

        for (int i = 0; i < radioGroup .getChildCount(); i++) {

            ((RadioButton) radioGroup.getChildAt(i)).setText(randommean(randomInt));
        }


        ((RadioButton) radioGroup.getChildAt(randomInt)).setText(word.get(1));


        addListenerOnButton(word);
        nxt = findViewById(R.id.nextquiz);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("wrrrr"+word.get(0));
                //index+=1;
                wordBuck.add(word.get(0));
                System.out.println("sssss"+wordBuck.toString());
                word.clear();
                cancelTimer();
                ignored++;
                score--;
                if(score<0)score = 0;
                Intent myIntent = new Intent(view.getContext(), quiz_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("s",score);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);

            }
        });


        RelativeLayout relativeLayout = findViewById(R.id.relative_quiz);

        timer = findViewById(R.id.timer);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark){
            textView.setBackgroundColor(Color.BLACK);
            scoress.setTextColor(Color.WHITE);
            textView.setTextColor(Color.WHITE);
            timer.setTextColor(Color.WHITE);
            for (int i = 0; i < radioGroup .getChildCount(); i++) {


                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(Color.WHITE);
            }
            relativeLayout.setBackgroundColor(Color.BLACK);
        }else{
            textView.setBackgroundColor(Color.WHITE);
            scoress.setTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
            timer.setTextColor(Color.BLACK);
            for (int i = 0; i < radioGroup .getChildCount(); i++) {


                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(Color.BLACK);
            }
            relativeLayout.setBackgroundColor(Color.WHITE);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                if(score>prefs.getInt("highscore", 0)){

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("highscore", score);
                    editor.commit();

                }

                cancelTimer();


                Intent myIntent = new Intent(view.getContext(), quiz_result.class);

                myIntent.putExtra("score",score);

                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });



        startTimer();



    }

    void startTimer() {
        cTimer = new CountDownTimer(total, 1000) {
            public void onTick(long millisUntilFinished) {
                if(!isPause){
                    total = millisUntilFinished;
                    timer.setText("Time remaining: " + millisUntilFinished / 1000);
                }
                else{
                    total = temp;
                    cancelTimer();
                    startTimer();
                }
            }
            public void onFinish() {
                timer.setText("Finished!");

                ignored++;
                score--;
                if(score<0)score = 0;
                wordBuck.add(word.get(0));

                Intent myIntent = new Intent(quiz_page.this, quiz_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.putExtra("s",score);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                cancelTimer();
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }



    @Override
    protected void onStart() {
        super.onStart();

        getWindow().setBackgroundDrawableResource(android.R.color.white);
    }

    public void addListenerOnButton(List<String> s) {

        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final List<String> sr = s;
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        //btnDisplay.setEnabled(true);
        System.out.println(selectedId);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                if(selectedId>0){
                    btnDisplay.setEnabled(true);
                    radioSexButton = (RadioButton) findViewById(selectedId);

                    if(sr.get(1).equals(radioSexButton.getText())){
                        Toasty.success(quiz_page.this,
                                "congrats", Toast.LENGTH_SHORT).show();



                        for (int i = 0; i < radioSexGroup .getChildCount(); i++) {


                            if(sr.get(1).equals(((RadioButton) radioSexGroup.getChildAt(i)).getText())){
                                Drawable marker;
                                marker = getResources().getDrawable(R.drawable.backgroundborder1);
                                radioSexGroup.getChildAt(i).setBackground(marker);

                                //rb_flash.setTextColor(Color.BLACK);
                            }


                        }
                        score++;


                        btnDisplay.setEnabled(false);



                        correct++;
                        scoress.setText("Current score : "+String.valueOf(score));
                        Intent myIntent = new Intent(quiz_page.this, quiz_page.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("s",score);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                        cancelTimer();
                    }else {
                        Toasty.error(quiz_page.this,
                                "Alas!", Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < radioSexGroup .getChildCount(); i++) {


                            if(sr.get(1).equals(((RadioButton) radioSexGroup.getChildAt(i)).getText())){
                                Drawable marker;
                                marker = getResources().getDrawable(R.drawable.backgroundborder1);
                                radioSexGroup.getChildAt(i).setBackground(marker);
                            }else if(((RadioButton) radioSexGroup.getChildAt(i)).getText().equals(radioSexButton.getText())){
                                Drawable marker;
                                marker = getResources().getDrawable(R.drawable.backgroundborder2);
                                radioSexGroup.getChildAt(i).setBackground(marker);
                            }

                        }
                        System.out.println("srrr"+sr.get(0));
                        // index++;
                        wordBuck.add(sr.get(0));
                        System.out.println("sssss"+wordBuck.toString());


                        score--;
                        wrong++;
                        if(score<0)score = 0;

                        scoress.setText("Current score : "+String.valueOf(score));
                        btnDisplay.setEnabled(false);
                        Intent myIntent = new Intent(quiz_page.this, quiz_page.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("s",score);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                        cancelTimer();
                    }
                }else {
                    Toasty.warning(quiz_page.this,
                            "Select an option.", Toast.LENGTH_SHORT).show();
                }
                // find the radiobutton by returned id



            }

        });


    }
    public List<String> randomword(int iii){
        List<String> str = new ArrayList<>();
        Random rnd = new Random();
        int randomInt;
        if(MainActivity.size==0){
            randomInt = size;
        }
        else {
            randomInt = 0 + rnd.nextInt(MainActivity.size + 1 - 0 + 1 - iii);
        }

        // randomDouble = randomDouble * MainActivity.size + 1;
        //int randomInt = (int) randomDouble;

        if(randomInt==iii){
            //return "";
            randommean(iii);
        }

        mDBHelper = new DatabaseHelper(quiz_page.this);


        //mDBHelper.getAllData();
        mDb = mDBHelper.getWritableDatabase();



        // Select All Query
        // String selectQuery = "SELECT  * FROM " + "Word_table";

        String query = "SELECT * FROM Word_table WHERE ID =" + randomInt;
        //System.out.println(randomInt);

        Cursor  cursor = mDb.rawQuery(query,null);

        if (cursor != null&& cursor.moveToFirst()) {
            str.add(cursor.getString(cursor.getColumnIndex("WORD")));
            str.add(cursor.getString(cursor.getColumnIndex("MEANING")));
            cursor.close();
        }
        mDb.close();
        return str;
    }

    public String randommean(int iii){
        String str = new String();
        Random rnd = new Random();
        int randomInt;
        if(MainActivity.size==0){
            randomInt = size;
        }
        else {
            randomInt = 0 + rnd.nextInt(MainActivity.size + 1 - 0 + 1 - iii);
        }

        // randomDouble = randomDouble * MainActivity.size + 1;
        //int randomInt = (int) randomDouble;

        if(randomInt==iii){
            //return "";
            randommean(iii);
        }
        mDBHelper = new DatabaseHelper(quiz_page.this);


        //mDBHelper.getAllData();
        mDb = mDBHelper.getWritableDatabase();



        // Select All Query
        // String selectQuery = "SELECT  * FROM " + "Word_table";

        String query = "SELECT * FROM Word_table WHERE ID =" + randomInt;
        //System.out.println(randomInt);

        Cursor  cursor = mDb.rawQuery(query,null);

        if (cursor != null&& cursor.moveToFirst()) {
            str = cursor.getString(cursor.getColumnIndex("MEANING"));
            cursor.close();
        }
        mDb.close();
        return str;
    }
    @Override
    public void onBackPressed(){
        cancelTimer();
    }
    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
        temp = total;


    }

    @Override
    public  void  onResume() {

        super.onResume();
        isPause = false;
        temp = 0;
    }
}
