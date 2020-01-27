package com.example.czgame.wordbank.ui.words;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class backup_restore extends AppCompatActivity {


    public static final int PICKFILE_RESULT_CODE = 1;
    public static AtomicInteger number = new AtomicInteger(0);
    public static AtomicInteger number1 = new AtomicInteger(0);
    public boolean flag;
    public View v;
    RelativeLayout coordinatorLayout, mainlayout;
    TextView textView;
    private DatabaseHelper mDBHelper;
    private Button insert, backup, restore, ok, restore1;
    private EditText retext;
    private Uri fileUri;
    private String filePath;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {

            setContentView(R.layout.activity_backup_restore);
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }



        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mDBHelper = new DatabaseHelper(this);

        backup = findViewById(R.id.backup);

        backup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                MyTask myTask = new MyTask();
                MyTask2 myTask2 = new MyTask2();
                myTask.execute();
                myTask2.execute();
            }


        });

        restore = findViewById(R.id.restore);
        //restore1 = (Button) findViewById(R.id.restore1);
        ok = findViewById(R.id.click);
        retext = findViewById(R.id.retext);
        ok.setEnabled(false);
        restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

                //  Toast.makeText(onNewIntent();)

                ok.setEnabled(true);


            }


        });


        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                number.set(dothings());
                number1.set(dothings1());

                ProgressDialog dialog = ProgressDialog.show(backup_restore.this, "",
                        "Loading. Please wait...", true);


                if (number.get() == 1  && number1.get() == 1) {
                    System.out.println("then");
                    Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);
                } else {
                    Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                }


            }


        });
        mainlayout = findViewById(R.id.coordinate);
        coordinatorLayout = mainlayout.findViewById(R.id.coordinate_backup);
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if(isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (isDark) {
            retext.setBackgroundColor(Color.rgb(64, 64, 64));
            retext.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));

            //coordinatorLayout.setBackgroundColor(Color.BLACK);
            //mainlayout.setBackgroundColor(Color.BLACK);
            mainlayout.setBackgroundColor(Color.BLACK);
            coordinatorLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background_dark));
            retext.setHintTextColor(Color.rgb(185, 185, 185));
            retext.setTextColor(Color.WHITE);
        } else {
            retext.setBackgroundColor(Color.WHITE);
            retext.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));

            //coordinatorLayout.setBackgroundColor(Color.WHITE);
            //mainlayout.setBackgroundColor(Color.WHITE);
            mainlayout.setBackgroundColor(Color.WHITE);
            coordinatorLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background));
            retext.setHintTextColor(Color.rgb(64, 64, 64));
            retext.setTextColor(Color.BLACK);
        }
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    retext.setText(filePath);

                }

                break;
        }

    }

    public void indeterminateCircularProgress() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("WAIT");
        dialog.setMessage("Loading.....");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                number.set(dothings());

                try {
                    Thread.sleep(1000);
                    // Here we can place our time consuming task
                    System.out.println(number.get());
                    dismissDialog(dialog);
                } catch (Exception e) {
                    dismissDialog(dialog);
                }

            }
        }).start();

    }

    public void dismissDialog(final ProgressDialog pd) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (pd != null) pd.dismiss();
                System.out.println("after" + number.get());
                flag = number.get() == 1;
            }
        });
    }

    private int dothings() {
        FileChannel source = null;
        FileChannel destination = null;
        String currentDB = getDatabasePath(DatabaseHelper.DATABASE_NAME).getPath();
        String[] parts = fileUri.getLastPathSegment().split(":");
        String part2 = parts[1]; // 0


        if (!part2.contains(".json")) {
            List<String> total = new ArrayList<>();
            String retreivedDBPAth = Environment.getExternalStorageDirectory() + "/" + part2;
            File input = new File(retreivedDBPAth);
            //BufferedWriter file = null;
            File root = android.os.Environment.getExternalStorageDirectory();
            // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
            File dir = new File(root.getAbsolutePath() + "/wordstore");
            File file,file1;
            dir.mkdirs();
            file = new File(dir, "backup.json");
            file1 = new File(dir, "backup_sentences.json");
            //file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/macbookair/IdeaProjects/data/backup.html"), "UTF-8"));

            Document document = null;
            try {
                document = Jsoup.parse(input, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements paragraphs = document.getElementsByTag("entry");
            int count = 0;
            for (Element paragraph : paragraphs) {
                Elements w = paragraph.getElementsByTag("w");
                Elements m = paragraph.getElementsByTag("m");
                Elements ex = paragraph.getElementsByTag("ex");
                total.add("{ \"ID\" : " + count + " , \"WORD\" : \"" + w.text() + "\" , \"MEANING\" : \"" + m.text() + "\" ,  \"SENTENCE\" : \"" + ex.text() + "\" }");
                count++;
            }
            int i = 0;
            FileOutputStream f = null;
            PrintWriter pw = null;
            try {
                f = new FileOutputStream(file);
                pw = new PrintWriter(f);
                pw.write("[");
            } catch (IOException e) {
                e.printStackTrace();
            }
            do {
                String resultData = total.get(i);
                if (i == count - 1) {


                    pw.write(resultData + "\n");
                    pw.flush();

                } else {

                    pw.write(resultData + ", \n");
                    pw.flush();
                }
                i++;
            } while (i < count);
            try {
                pw.write("]");
                pw.close();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream is = null;


            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            InputStreamReader isr = new InputStreamReader(is);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject reader = new JSONObject();
            String json = null;
            try {
                //is = getActivity().getAssets().open("yourfilename.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                //   Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            try {
                jsonArray = new JSONArray(json);
                mDBHelper.deleteAll();
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.get(i).toString());
                    mDBHelper.insertData(jsonObject.get("WORD").toString(), jsonObject.get("MEANINGB").toString(),jsonObject.get("MEANINGE").toString(), jsonObject.get("SENTENCE").toString(),jsonObject.get("SYNONYM").toString(),jsonObject.get("ANTONYM").toString());
                }
                //Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
            return 1;

        } else {
            String retreivedDBPAth = Environment.getExternalStorageDirectory() + "/" + part2;
            File retrievedDB = new File(retreivedDBPAth);
            InputStream is = null;


            try {
                is = new FileInputStream(retrievedDB);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            InputStreamReader isr = new InputStreamReader(is);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject reader = new JSONObject();
            String json = null;
            try {
                //is = getActivity().getAssets().open("yourfilename.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            try {
                jsonArray = new JSONArray(json);
                mDBHelper.deleteAll();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.get(i).toString());
                    if(jsonObject.has("MEANINGE") && jsonObject.has("MEANINGB") && jsonObject.has("SYNONYM") && jsonObject.has("ANTONYM")){
                        mDBHelper.insertData(jsonObject.get("WORD").toString(), jsonObject.get("MEANINGB").toString(),jsonObject.get("MEANINGE").toString(), "",jsonObject.get("SYNONYM").toString(),jsonObject.get("ANTONYM").toString());
                    }else{
                        mDBHelper.insertData(jsonObject.get("WORD").toString(), jsonObject.get("MEANING").toString(),"None", "","None","None");
                    }

                }
                // Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
    }

    private int dothings1() {
        FileChannel source = null;
        FileChannel destination = null;
        String currentDB = getDatabasePath(DatabaseHelper.DATABASE_NAME).getPath();
        String[] parts = fileUri.getLastPathSegment().split(":");
        String part2 = parts[1]; // 0


        if (!part2.contains(".json")) {
            List<String> total = new ArrayList<>();
            String retreivedDBPAth = Environment.getExternalStorageDirectory() + "/" + "backup_sentences.json";
            File input = new File(retreivedDBPAth);
            //BufferedWriter file = null;
            File root = android.os.Environment.getExternalStorageDirectory();
            // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
            File dir = new File(root.getAbsolutePath() + "/wordstore");
            File file,file1;
            dir.mkdirs();
            //file = new File(dir, "backup.json");
            file1 = new File(dir, "backup_sentences.json");
            //file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/macbookair/IdeaProjects/data/backup.html"), "UTF-8"));

            Document document = null;
            try {
                document = Jsoup.parse(input, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements paragraphs = document.getElementsByTag("entry");
            int count = 0;
            for (Element paragraph : paragraphs) {
                Elements w = paragraph.getElementsByTag("w");
                Elements m = paragraph.getElementsByTag("m");
                Elements ex = paragraph.getElementsByTag("ex");
                total.add("{ \"ID\" : " + count + " , \"WORD\" : \"" + w.text() + "\" , \"MEANING\" : \"" + m.text() + "\" ,  \"SENTENCE\" : \"" + ex.text() + "\" }");
                count++;
            }
            int i = 0;
            FileOutputStream f = null;
            PrintWriter pw = null;
            try {
                f = new FileOutputStream(file1);
                pw = new PrintWriter(f);
                pw.write("[");
            } catch (IOException e) {
                e.printStackTrace();
            }
            do {
                String resultData = total.get(i);
                if (i == count - 1) {


                    pw.write(resultData + "\n");
                    pw.flush();

                } else {

                    pw.write(resultData + ", \n");
                    pw.flush();
                }
                i++;
            } while (i < count);
            try {
                pw.write("]");
                pw.close();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream is = null;


            try {
                is = new FileInputStream(file1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            InputStreamReader isr = new InputStreamReader(is);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject reader = new JSONObject();
            String json = null;
            try {
                //is = getActivity().getAssets().open("yourfilename.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                //   Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            try {
                jsonArray = new JSONArray(json);
                mDBHelper.deleteAll1();
                for (i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.get(i).toString());
                    mDBHelper.insertData1(jsonObject.get("WORD").toString(), jsonObject.get("SENTENCE").toString());
                }
                //Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
            return 1;

        } else {
            String retreivedDBPAth = Environment.getExternalStorageDirectory() + "/" +part2.replace("backup","backup_sentences");
            File retrievedDB = new File(retreivedDBPAth);
            InputStream is = null;


            try {
                is = new FileInputStream(retrievedDB);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            InputStreamReader isr = new InputStreamReader(is);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject reader = new JSONObject();
            String json = null;
            try {
                //is = getActivity().getAssets().open("yourfilename.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
            try {
                jsonArray = new JSONArray(json);
                mDBHelper.deleteAll1();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.get(i).toString());
                    if(!jsonObject.has("SENTENCE")){
                        mDBHelper.insertData1(jsonObject.get("WORD").toString()," ");
                    }
                    else mDBHelper.insertData1(jsonObject.get("WORD").toString(), jsonObject.get("SENTENCE").toString());
                }
                // Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
    }

    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            JSONArray jsonArray = new JSONArray();
            final Cursor cursor = mDBHelper.getAllData();


            // looping through all rows and adding to list
            if (cursor.getCount() != 0) {
                // show message
                while (cursor.moveToNext()) {

                    JSONObject word = new JSONObject();
                    try {



                        if(!cursor.getString(3).isEmpty() && !cursor.getString(5).isEmpty()
                                && !cursor.getString(6).isEmpty()){
                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("WORD", cursor.getString(1));
                            word.put("MEANINGB", cursor.getString(2));
                            word.put("MEANINGE", cursor.getString(3));
                            word.put("SYNONYM", cursor.getString(5));
                            word.put("ANTONYM", cursor.getString(6));
                        }
                        else if(cursor.getString(3).isEmpty()){

                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("WORD", cursor.getString(1));
                            word.put("MEANINGB", cursor.getString(2));
                            word.put("MEANINGE", "None");
                            word.put("SYNONYM", cursor.getString(5));
                            word.put("ANTONYM", cursor.getString(6));

                        }else if(cursor.getString(5).isEmpty()){

                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("WORD", cursor.getString(1));
                            word.put("MEANINGB", cursor.getString(2));
                            word.put("MEANINGE", cursor.getString(3));
                            word.put("SYNONYM", "None");
                            word.put("ANTONYM", cursor.getString(6));
                        }else if(cursor.getString(6).isEmpty()){

                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("WORD", cursor.getString(1));
                            word.put("MEANINGB", cursor.getString(2));
                            word.put("MEANINGE", cursor.getString(3));
                            word.put("SYNONYM", cursor.getString(5));
                            word.put("ANTONYM", "None");
                        }

                        //System.out.println(word.toString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    jsonArray.put(word);



                }

                File root = android.os.Environment.getExternalStorageDirectory();
                // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
                File dir = new File(root.getAbsolutePath() + "/wordstore");
                dir.mkdirs();
                File file = new File(dir, "backup.json");
                try {
                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.write(jsonArray.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                    return true;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;

                }

            } else {

                return  false;
                //showMessage("Error", "Nothing found");
            }
        }
        @Override
        protected void onPostExecute(Boolean b){

            if(b){
                Toasty.success(backup_restore.this,"Done",Toast.LENGTH_LONG).show();
            }else{
                Toasty.success(backup_restore.this,"Failed.",Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyTask2 extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            JSONArray jsonArray = new JSONArray();
            final Cursor cursor1 = mDBHelper.getAllData2();
            jsonArray = new JSONArray();
            if (cursor1.getCount() != 0) {
                // show message
                while (cursor1.moveToNext()) {

                    JSONObject word = new JSONObject();
                    try {
                        word.put("ID", Integer.parseInt(cursor1.getString(0)));
                        word.put("WORD", cursor1.getString(1));
                        word.put("SENTENCE", cursor1.getString(2));

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    jsonArray.put(word);
                }

                File root = android.os.Environment.getExternalStorageDirectory();
                // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
                File dir = new File(root.getAbsolutePath() + "/wordstore");
                dir.mkdirs();
                File file = new File(dir, "backup_sentences.json");
                try {
                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.write(jsonArray.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            } else {


                return false;
            }

        }
        @Override
        protected void onPostExecute(Boolean b){

            if(b){
                Toasty.success(backup_restore.this,"Done",Toast.LENGTH_LONG).show();
            }else{
                Toasty.success(backup_restore.this,"Failed.",Toast.LENGTH_LONG).show();
            }
        }
    }

}


