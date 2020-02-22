package com.example.czgame.wordbank.ui.diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

class words{

    String word;
    int position;
    int length;

    public words(String word, int position,int length) {
        this.word = word;
        this.position = position;
        this.length = length;
    }

    public words() {

    }

    @Override
    public String toString() {
        return "words{" +
                "word='" + word + '\'' +
                ", position=" + position +
                ", length=" + length +
                '}';
    }


}

public class UpdateDiary  extends AppCompatActivity {
    TextInputEditText subjectEt;
    TextInputLayout s;
    TextInputEditText descriptionEt;
    TextInputLayout d;
    Button updateBt,shareBtOnUpdate,advance;
    SqliteDatabase dbUpdate;
    List<words> suggs = new ArrayList<>();
    List<words> suggs1 = new ArrayList<>();
    String suggestions = "";
    ImageButton warn;
    WebView mWebView;
    @SuppressLint("JavascriptInterface")
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


        updateBt = findViewById(R.id.saveButtonIdUpdate);
        shareBtOnUpdate = findViewById(R.id.shareButtonIdUpdate);
        advance = findViewById(R.id.advance);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("description");
        final String id = intent.getStringExtra("listId");


        subjectEt.setText(sub);
        descriptionEt.setText(des);



        String s1 = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <script src='https://www.scribens.com/scribens-integration.js'></script>\n" +
                "    <style>\n" +
                "  div{\n" +
                "  display:inline-block;\n" +
                "  position:relative;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "button {\n" +
                "\tbackground-color:#44c767;\n" +
                "\tborder-radius:28px;\n" +
                "\tborder:1px solid #18ab29;\n" +
                "\tdisplay:inline-block;\n" +
                "\tcursor:pointer;\n" +
                "\tcolor:#ffffff;\n" +
                "\tfont-family:Arial;\n" +
                "    \n" +
                "      position:absolute;\n" +
                "  bottom:10px;\n" +
                "  right:10px;\n" +
                "  \n" +
                "\tfont-size:7px;\n" +
                "\tpadding:6px 10px;\n" +
                "\ttext-decoration:none;\n" +
                "\ttext-shadow:0px 1px 0px #2f6627;\n" +
                "}\n" +
                "\n" +
                "        \n" +
                "textarea{\n" +
                "  display:block;\n" +
                "}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<div>\n" +
                "    <button id='ok' onclick=\"Scribens.Check('id1')\">Check</button>\n" +
                "    <textarea cols=\"20\" rows=\"5\" id='id1'>" + descriptionEt.getText().toString().trim() +
                "</textarea>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";


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




        advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new TheTaskAdvance(descriptionEt.getText().toString()).execute();

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setHighLightedText(EditText editText) {
        String s = editText.getText().toString();
        Spannable WordtoSpan = new SpannableString(s);
        for (int i = 0; i < suggs.size(); i++) {


             System.out.println(suggs.get(i).toString());

            // if(i==0) {
            // System.out.println(suggs.get(i));
            if(suggs.get(i).position + suggs.get(i).word.length()>=s.length()){
                WordtoSpan.setSpan(new ColoredUnderlineSpan(Color.MAGENTA), suggs.get(i).position, suggs.get(i).position + suggs.get(i).word.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                WordtoSpan.setSpan(new ColoredUnderlineSpan(Color.MAGENTA), suggs.get(i).position, suggs.get(i).position + suggs.get(i).word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //}
        }
        editText.setText(WordtoSpan, TextView.BufferType.SPANNABLE);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setHighLightedText1(EditText editText) {
        String s = editText.getText().toString();
        Spannable WordtoSpan = new SpannableString(s);
        for (int i = 0; i < suggs1.size(); i++) {


            System.out.println(suggs1.get(i).toString());

            // if(i==0) {
            // System.out.println(suggs.get(i));
            WordtoSpan.setSpan(new ColoredUnderlineSpan(Color.RED), suggs1.get(i).position,  suggs1.get(i).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //}
        }
        editText.setText(WordtoSpan, TextView.BufferType.SPANNABLE);

    }
    final class ColoredUnderlineSpan extends CharacterStyle
            implements UpdateAppearance {
        private final int mColor;

        public ColoredUnderlineSpan(final int color) {
            mColor = color;
        }

        @Override
        public void updateDrawState(final TextPaint tp) {
            try {
                final Method method = TextPaint.class.getMethod("setUnderlineText",
                        Integer.TYPE,
                        Float.TYPE);
                method.invoke(tp, mColor, 8.0f);
            } catch (final Exception e) {
                tp.setUnderlineText(true);
            }
        }
    }

    class TheTaskAdvance extends AsyncTask<Void,String,String>
    {
        String text;
        String ou = "";

        TheTaskAdvance(String text){
            this.text = text;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String kb = "https://services.gingersoftware.com/Ginger/correct/jsonSecured/GingerTheTextFull?" +
                    "callback=jQuery172015406464511272344_1490987331365&apiKey=GingerWebSite&lang=US&clientVersion=2.0&text="+ text +"&_=1490987518060&fbclid=IwAR2FdbEaWXHDm_0w0VYz5GxjZ_-SgAd1ht6nMJNk1SdHLQADw14KMKL2Dwo";
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

                //System.out.println(result);
                result  = result.replace("jQuery172015406464511272344_1490987331365(", "");
                result  = result.replace(");", "");
                JSONObject jsnobject = new JSONObject(result);
                //System.out.println(jsnobject);
                JSONArray jsonArray = jsnobject.getJSONArray("Corrections");
                //System.out.println(jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    //System.out.println("Mistake :" + (i+1));
                   ou += "Mistake :" + (i + 1) + "\n";
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = explrObject.getJSONArray("Suggestions");
                    words  words= new words();
                    ou += "Suggestions  :" + "\n {";
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        // System.out.println("Suggestions  :" + (j+1));

                        //  System.out.println(jsonArray1.get(j).toString());
                         ou += "\n";
                        JSONObject json = jsonArray1.getJSONObject(j);
                        Iterator<String> keys = json.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            //  System.out.println("Key :" + key + "  Value :" + json.get(key));
                            if(key.equals("Text")){
                                ou+= "\n Text  : " + json.get(key) + "\n";
                                words.word = json.get(key).toString();
                                System.out.println(words.toString());
                            }

                        }

                    }

                    ou += "\n}";
                    JSONObject json = jsonArray.getJSONObject(i);
                    Iterator<String> keys = json.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        // System.out.println("Key :" + key + "  Value :" + json.get(key));
                        if(key.equals("From")){
                            ou+= "\n Start at : " + json.get(key);
                            words.position = json.getInt(key);
                        }
                        if(key.equals("To")){
                            ou+= "\n End at : " + json.get(key);
                            words.length = json.getInt(key);
                        }

                    }


                    System.out.println(words.toString());


                    // System.out.println("\n\n");
                    ou += "\n\n";

                   suggs1.add(words);

                }


                suggestions+=ou;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!suggs.isEmpty()) {
                warn.setVisibility(View.VISIBLE);
                warn.setBackgroundTintList(ContextCompat.getColorStateList(UpdateDiary.this, R.color.red));
            }else{
                warn.setVisibility(View.VISIBLE);
                warn.setBackgroundTintList(ContextCompat.getColorStateList(UpdateDiary.this, R.color.green));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setHighLightedText1(descriptionEt);
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

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
                            words.length = json.getInt(key);
                        }

                    }


                    JSONObject jsnobjecta = jsonArray.getJSONObject(i);
                    JSONObject jsonArraya = jsnobjecta.getJSONObject("rule");
                    //System.out.println(jsonArraya.toString());
                    ou+= "\n Error type : " + jsonArraya.get("description");
                    //System.out.println(jsonArraya.get("description"));




                    // System.out.println("\n\n");
                    ou += "\n\n";

                    suggs.add(words);

                }
                suggestions+=ou;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!suggs.isEmpty()) {
                warn.setVisibility(View.VISIBLE);
                warn.setBackgroundTintList(ContextCompat.getColorStateList(UpdateDiary.this, R.color.red));
            }else{
                warn.setVisibility(View.VISIBLE);
                warn.setBackgroundTintList(ContextCompat.getColorStateList(UpdateDiary.this, R.color.green));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setHighLightedText(descriptionEt);
            }


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }


}
