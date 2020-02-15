package com.example.czgame.wordbank.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.softcorporation.suggester.BasicSuggester;
import com.softcorporation.suggester.Suggestion;
import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.tools.SpellCheck;
import com.softcorporation.suggester.util.SpellCheckConfiguration;
import com.softcorporation.suggester.util.SuggesterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

class words{

    String word;
    int position;

    public words(String word, int position) {
        this.word = word;
        this.position = position;
    }
    public words() {

    }

    @Override
    public String toString() {
        return "words{" +
                "word='" + word + '\'' +
                ", position=" + position +
                '}';
    }
}

public class UpdateDiary  extends AppCompatActivity {
    TextInputEditText subjectEt;
    TextInputLayout s;
    TextInputEditText descriptionEt;
    TextInputLayout d;
    Button cancelBt,updateBt,shareBtOnUpdate;
    SqliteDatabase dbUpdate;
    List<words> suggs = new ArrayList<>();
    String suggestions = "";
    ImageButton warn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.update_diary);

        //passing Update Activity's context to database alass
        dbUpdate = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();

        subjectEt = findViewById(R.id.subjectEditTextIdUpdate1);
        s = findViewById(R.id.subjectEditTextIdUpdate);
        descriptionEt = findViewById(R.id.descriptionEditTextIdUpdate1);
        d = findViewById(R.id.descriptionEditTextIdUpdate);


        cancelBt = findViewById(R.id.cacelButtonIdUpdate);
        updateBt = findViewById(R.id.saveButtonIdUpdate);
        shareBtOnUpdate = findViewById(R.id.shareButtonIdUpdate);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("description");
        final String id = intent.getStringExtra("listId");


        subjectEt.setText(sub);
        descriptionEt.setText(des);


        warn = findViewById(R.id.warn);

        warn.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.dialogId);
        RelativeLayout lidtb = findViewById(R.id.two);

        if(isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (isDark) {

            laybase.setBackgroundColor(Color.BLACK);
            subjectEt.setHintTextColor(Color.rgb(185, 185, 185));
            subjectEt.setTextColor(Color.WHITE);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            descriptionEt.setHintTextColor(Color.rgb(185, 185, 185));
            descriptionEt.setTextColor(Color.WHITE);

            d.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
        } else if (!isDark) {

            laybase.setBackgroundColor(Color.WHITE);
            subjectEt.setHintTextColor(Color.BLACK);
            subjectEt.setTextColor(Color.BLACK);

            s.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            descriptionEt.setHintTextColor(Color.BLACK);
            descriptionEt.setTextColor(Color.BLACK);

            d.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));


            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));

        }

        //for sharing data to social media
        shareBtOnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new TheTask(descriptionEt.getText().toString()).execute();



            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //for updating database data
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String d = (String) DateFormat.format("dd/MM/yyyy  hh:mm: a",date);

                if(dbUpdate.update(subjectEt.getText().toString(),descriptionEt.getText().toString(),"",d,id,0)==true){
                    Toasty.success(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
        });

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float density= UpdateDiary.this.getResources().getDisplayMetrics().density;
                LayoutInflater inflater = (LayoutInflater) UpdateDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popupdetail,null);
                final PopupWindow pw = new PopupWindow(layout, (int)density*370, (int)density*685, true);


                ((TextView)layout.findViewById(R.id.goldName)).setText(suggestions);
                ((TextView)layout.findViewById(R.id.goldName)).setMovementMethod(new ScrollingMovementMethod());
                ((TextView)layout.findViewById(R.id.goldNamet)).setText("Mistakes.");
                ((TextView)layout.findViewById(R.id.goldNamet)).setMovementMethod(new ScrollingMovementMethod());

                pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.setOutsideTouchable(true);
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });


    }

    //this method to clearing top activity and starting new activity
    public void backToMain()
    {
        Intent intent = new Intent(UpdateDiary.this,DiaryMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
    public  String check(String s) throws SuggesterException {
        String command;
        String prevCommand = "";
        String word;
        String prevWord = "";
        SpellCheck spellCheck = null;

        String workingDir = "/Users/macbookair/Downloads/MyApplication34/app/libs/";
        String inputFileName = workingDir + "english.jar";
        //  File inFile = new File(workingDir + "input.txt");
        BasicDictionary dictionary = new BasicDictionary(inputFileName);
        SpellCheckConfiguration configuration = new SpellCheckConfiguration(
                "app/spellCheck.config");
        BasicSuggester suggester = new BasicSuggester(configuration);
        suggester.attach(dictionary);
        String text = s;
        System.out.println(text);
        spellCheck = new SpellCheck(configuration);
        spellCheck.setSuggester(suggester);
        spellCheck.setSuggestionLimit(5);

        while (true)
        {
            ArrayList suggestions = null;
            spellCheck.setText(text);
            spellCheck.check();
            if (spellCheck.hasMisspelt()) {

                String misspeltWord = spellCheck.getMisspelt();
                String misspeltText = spellCheck.getMisspeltText(5, "<b>", "</b>", 5);
                System.out.println("Misspelt text: " + misspeltText);
                System.out.println("Misspelt word: " + misspeltWord);
                suggestions = spellCheck.getSuggestions();
                System.out.println("Suggestions: ");
                if(suggestions.size()==0){
                    break;
                }
                for (int j = 0; j < suggestions.size(); j++) {
                    Suggestion suggestion = (Suggestion) suggestions.get(j);
                    //System.out.println(j + ": " + suggestion.word);
                    text = text.replace(misspeltWord,suggestion.word);
                //    suggs.add(suggestion.word);

                }
            }if (!spellCheck.hasMisspelt()) break;

        }
        return text;

    }
    public void setHighLightedText(EditText editText) {
        Spannable WordtoSpan = new SpannableString(editText.getText());
        for (int i = 0; i < suggs.size(); i++) {


          // if(i==0) {
               // System.out.println(suggs.get(i));
                WordtoSpan.setSpan(new UnderlineSpan(), suggs.get(i).position, suggs.get(i).position + suggs.get(i).word.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //}
        }
        editText.setText(WordtoSpan, TextView.BufferType.SPANNABLE);

    }

    class TheTask extends AsyncTask<Void,String,String>
    {
        String text;
        String ou = "";

        TheTask(String text){
            this.text = text;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String kb = "https://languagetool.org/api/v2/check?language=en-US&text=".concat(text);
            String json = "";
            try {
                json  = Jsoup.connect(kb).ignoreContentType(true).execute().body();
                // System.out.println(json);

            } catch (IOException e) {
                e.printStackTrace();
            }
            // System.out.println(connection.request());

            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);

            try {

                JSONObject jsnobject = new JSONObject(result);
                JSONArray jsonArray = jsnobject.getJSONArray("matches");
                for (int i = 0; i < jsonArray.length(); i++) {
                    //System.out.println("Mistake :" + (i+1));
                    ou += "Mistake :" + (i + 1) + "\n";
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = explrObject.getJSONArray("replacements");
                    words  words= new words();
                    ou += "Suggestions  :" + "\n {";
                    for (int j = 0; j < jsonArray1.length(); j++) {
                       // System.out.println("Suggestions  :" + (j+1));

                      //  System.out.println(jsonArray1.get(j).toString());
                       // ou += new String(jsonArray1.get(j).toString()+"\n");
                        JSONObject json = jsonArray1.getJSONObject(j);
                        Iterator<String> keys = json.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                          //  System.out.println("Key :" + key + "  Value :" + json.get(key));
                            if(key.equals("value")){
                                ou+= "\n Values  : " + json.get(key) + "\n";
                                words.word = json.get(key).toString();
                            }

                        }

                    }
                    ou += "\n}";
                    JSONObject json = jsonArray.getJSONObject(i);
                    Iterator<String> keys = json.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                       // System.out.println("Key :" + key + "  Value :" + json.get(key));
                        if(key.equals("offset")){
                            ou+= "\n Start at : " + json.get(key);
                            words.position = json.getInt(key);
                        }
                        if(key.equals("length")){
                            ou+= "\n End at : " + json.get(key);
                        }

                    }


                        JSONObject jsnobjecta = jsonArray.getJSONObject(i);
                        JSONObject jsonArraya = jsnobjecta.getJSONObject("rule");
                        System.out.println(jsonArraya.toString());
                        ou+= "\n Error type : " + jsonArraya.get("description");
                        System.out.println(jsonArraya.get("description"));




                   // System.out.println("\n\n");
                    ou += "\n\n";

                    suggs.add(words);

                }
                suggestions+=ou;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            warn.setVisibility(View.VISIBLE);

            setHighLightedText(descriptionEt);




        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }
}
