package com.example.czgame.wordbank.ui.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.DatabaseHelper;
import com.example.czgame.wordbank.ui.words.MainActivity;
import com.example.czgame.wordbank.ui.words.word;

import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static com.example.czgame.wordbank.ui.Quiz.quiz_result.wordBuck;

public class quiz_spelling extends AppCompatActivity {

    public static TextView scoress;
    public  static  int score2 = 0;
    RecyclerView listView;
    ExtendedEditText word;
    EditText texth;
    Button d,n;
    word curr;
    ImageButton listed;
    int pos = 0;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private TextToSpeech textToSpeech;

    public static String replaceCharAt(String s, int pos, char c) {
        return s.substring(0,pos) + c + s.substring(pos+1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_spelling);



        d = findViewById(R.id.btnDisplay);
        n = findViewById(R.id.nextquiz);
        word = findViewById(R.id.word);

        texth = findViewById(R.id.wordh);

        pos =getRandomNumberInRange(0,MainActivity.size-1);

        System.out.println(pos);
        curr = randomword(pos);
      //  mCheckView = findViewById(R.id.check);

        listView = findViewById(R.id.learnlist);

        int i = curr.getWORD().length();
        String s = curr.getWORD().trim();
        System.out.println(s);

        for (int j = 0; j <i ; j++) {
            int y = getRandomNumberInRange(0,i-1);
            System.out.println(y);
            s = replaceCharAt(s,y,'_');
        }

        texth.setText(s);

        spell_adapter spell_adapter = new spell_adapter(curr);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(spell_adapter);


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        RelativeLayout relativeLayout =findViewById(R.id.backup);
        if(isDark) {
           word.setHintTextColor(Color.rgb(185, 185, 185));
            word.setTextColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.BLACK);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background_dark));
        }else {

            word.setHintTextColor(Color.BLACK);
            word.setTextColor(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.WHITE);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.card_background));
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.UK);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                   // Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textToSpeech.setSpeechRate(0.5f);

        Button button2 = findViewById(R.id.extpage);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

                if (score2 > prefs.getInt("highscore2", 0)) {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("highscore1", score2);
                    editor.commit();

                }

                Intent myIntent = new Intent(view.getContext(), quiz_result.class);

                myIntent.putExtra("score2", score2);

                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

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

    public void onDisplay(View view) {
        if(word.getText().toString().trim().toLowerCase().equals(curr.getWORD().trim().toLowerCase())){
            score2++;
           // mCheckView.check();
            System.out.println("correct");
            Toasty.success(this,"Correct.", Toast.LENGTH_LONG).show();
            texth.setText(curr.getWORD());
            texth.setTextColor(Color.GREEN);

        }else{
            score2--;
           // mCheckView.uncheck();
            System.out.println("incorrect");
            Toasty.error(this,"Incorrect.", Toast.LENGTH_LONG).show();

            texth.setText(curr.getWORD());
            texth.setTextColor(Color.RED);
            wordBuck.add(curr.getWORD());
        }


    }

    public void onNxt(View view) {

        curr = randomword(getRandomNumberInRange(0,MainActivity.size-1));

        int i = curr.getWORD().length();
        String s = curr.getWORD().trim();

        for (int j = 0; j <i ; j++) {
            int y =getRandomNumberInRange(0,i-1);
            s = replaceCharAt(s,y,'_');
        }

        texth.setText(s);

        spell_adapter spell_adapter = new spell_adapter(curr);
        listView.setAdapter(spell_adapter);

    }

    public void onListen(View view) {
        int speechStatus = textToSpeech.speak(curr.getWORD(), TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }

    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
