package com.example.czgame.wordbank.ui.words;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class add_page extends AppCompatActivity {


    TextInputEditText word;
    TextInputLayout w;
    TextInputEditText meaningb;
    TextInputLayout mb;
    TextInputEditText meaninge;
    TextInputLayout me;
    TextInputEditText synonym;
    TextInputLayout sym;
    TextInputEditText antonym;
    TextInputLayout ant;
    Button done,load;
    private DatabaseHelper mDBHelper;
    String meaningbs="",meaninges="",syns="",ants="";
    String[] lines = new String[10000];

    boolean loadonline = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        word = findViewById(R.id.word1);
        w = findViewById(R.id.wordlay);
        word = findViewById(R.id.word1);
        w = findViewById(R.id.word);
        meaningb = findViewById(R.id.meaningB1);
        mb = findViewById(R.id.meaningB);
        meaninge = findViewById(R.id.meaningE1);
        me = findViewById(R.id.meaningE);
        synonym = findViewById(R.id.synonyme1);
        sym = findViewById(R.id.synonyme);
        antonym = findViewById(R.id.antonyme1);
        ant = findViewById(R.id.antonyme);

        mDBHelper = new DatabaseHelper(this);
        done = findViewById(R.id.done);
        load = findViewById(R.id.loadonline);
        final RelativeLayout additem = findViewById(R.id.add_item);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {
            additem.setBackgroundColor(Color.BLACK);
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            word.setHintTextColor(Color.rgb(185, 185, 185));
            word.setTextColor(Color.WHITE);
            w.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            meaningb.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            meaningb.setHintTextColor(Color.rgb(185, 185, 185));
            meaningb.setTextColor(Color.WHITE);
            mb.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            meaninge.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            meaninge.setHintTextColor(Color.rgb(185, 185, 185));
            meaninge.setTextColor(Color.WHITE);
            me.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            synonym.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            synonym.setHintTextColor(Color.rgb(185, 185, 185));
            synonym.setTextColor(Color.WHITE);
            sym.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));

            antonym.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            antonym.setHintTextColor(Color.rgb(185, 185, 185));
            antonym.setTextColor(Color.WHITE);
            ant.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.divider)));
        } else {
            additem.setBackgroundColor(Color.WHITE);
            word.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            word.setHintTextColor(Color.BLACK);
            word.setTextColor(Color.BLACK);
            w.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            meaningb.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            meaningb.setHintTextColor(Color.BLACK);
            meaningb.setTextColor(Color.BLACK);
            mb.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            meaninge.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            meaninge.setHintTextColor(Color.BLACK);
            meaninge.setTextColor(Color.BLACK);
            me.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            synonym.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            synonym.setHintTextColor(Color.BLACK);
            synonym.setTextColor(Color.BLACK);
            sym.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));

            antonym.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            antonym.setHintTextColor(Color.BLACK);
            antonym.setTextColor(Color.BLACK);
            ant.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkgray)));
        }


        word.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (!arg1) {
                    DatabaseHelper mDBHelper = new DatabaseHelper(add_page.this);
                    // SQLiteDatabase mDb;
                    String id = "-1";
                    //SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    SQLiteDatabase db1 = mDBHelper.getReadableDatabase();
                    try {
                        Cursor re = db1.rawQuery("SELECT * FROM Word_table WHERE WORD = ?; ", new String[]{word.getText().toString()});
                        if (re.moveToFirst()) {
                            do {
                                System.out.println(re.getString(0));
                                id = re.getString(0);
                            } while (re.moveToNext());
                        }

                        re.close();
                        // System.out.println(re.getString(0));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if (!id.equals("-1")) {
                        Toasty.warning(add_page.this, "Data already exist", Toasty.LENGTH_LONG).show();
                        done.setEnabled(false);
                    } else {
                        done.setEnabled(true);
                    }
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @Override
            public void onClick(View v) {
                if(!loadonline){
                    if (word.getText().toString().isEmpty() || word.getText().toString().trim().length() <= 0) {
                        Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                    } else if (meaningb.getText().toString().isEmpty() || meaningb.getText().toString().trim().length() <= 0) {
                        Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean b = mDBHelper.insertData(word.getText().toString(), meaningb.getText().toString(),meaninge.getText().toString(), "",
                                synonym.getText().toString(),antonym.getText().toString());
                        if (b == true) {
                            Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(myIntent, 0);
                        } else {
                            Toasty.error(getApplicationContext(), "opps.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    loadonline = false;
                    for (String s:
                            lines) {
                        mDBHelper.insertData1(word.getText().toString(),s);
                    }

                    boolean b = mDBHelper.insertData(word.getText().toString(), meaningb.getText().toString(),meaninge.getText().toString(), "",
                            synonym.getText().toString(),antonym.getText().toString());
                    if (b == true) {
                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(add_page.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    } else {
                        Toasty.error(getApplicationContext(), "opps.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        load.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (word.getText().toString().isEmpty() || word.getText().toString().trim().length() <= 0) {
                    Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                }else {

                    if (netInfo != null) {
                        if (netInfo.isConnected()) {
                            new RetrieveFeedTask().execute("http://dictionary.studysite.org/Bengali-meaning-of-".concat(word.getText().toString().trim()));
                        }
                    }else{
                        Toasty.error(add_page.this,"No internet connection.", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });
    }
    private void setUpperHintColor(int color) {
        try {
            Field field = w.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    color
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(w, myList);

            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(w, myList);

            Method method = w.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(w, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void setUpperHintColor1(int color) {
        try {
            Field field = mb.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    color
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(mb, myList);

            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(mb, myList);

            Method method = mb.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(mb, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //m.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(this, color)));
    }
    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;


        protected Void doInBackground(String... urls) {
            try {
                String url = (urls[0]);


                Document doc =  Jsoup.connect(url).get();
                Element table = doc.select("table").get(0); //select the first table.
                Elements rows = table.select("tr");
                System.out.println(rows.size());
                if(rows.size()==8){
                    meaningbs = rows.get(1).select("td").get(1).wholeText();
                    meaninges =  rows.get(2).select("td").get(1).wholeText();
                    System.out.println("Pics : " + rows.get(3).select("td").get(0).select("img").first().attr("abs:src"));
                    rows.get(4).select("td").select("br").after("\n");
                    lines = rows.get(4).select("td").get(1).wholeText().split("\\r?\\n");
                    syns = rows.get(5).select("td").get(1).wholeText();
                    ants = rows.get(6).select("td").get(1).wholeText();
                }else if(rows.size()==7){
                    meaningbs = rows.get(1).select("td").get(1).wholeText();
                    meaninges =  rows.get(2).select("td").get(1).wholeText();
                    rows.get(3).select("td").select("br").after("\n");
                    lines = rows.get(3).select("td").get(1).wholeText().split("\\r?\\n");
                    syns = rows.get(4).select("td").get(1).wholeText();
                    ants = rows.get(5).select("td").get(1).wholeText();
                }
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
            return null;
        }

        protected void onPostExecute(Void doc) {

            // In cas
            // e of any IO errors, we want the messages written to the console

            loadonline = true;
            meaningb.setText(meaningbs);
            meaninge.setText(meaninges);
            synonym.setText(syns);
            antonym.setText(ants);

            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}


