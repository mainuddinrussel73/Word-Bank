package com.example.czgame.wordbank.ui.promotodo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.backup_scheudle.DBDaily;
import com.example.czgame.wordbank.ui.words.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class pro_backup extends AppCompatActivity {

    public static final int PICKFILE_RESULT_CODE = 1;
    public static AtomicInteger number = new AtomicInteger(0);
    public boolean flag;
    public View v;
    RelativeLayout coordinatorLayout, mainlayout;
    TextView textView;
    private DBproHandle mDBHelper;
    private DBDaily dbDaily;
    private DbTaskDhandle dbTaskDhandle;
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
        try {

            setContentView(R.layout.activity_probackup);
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


        mDBHelper = new DBproHandle(this);
        dbDaily = new DBDaily(this);
        dbTaskDhandle = new DbTaskDhandle(this);

        backup = findViewById(R.id.backup);

        backup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                final Cursor cursor = mDBHelper.getAllData();

                // looping through all rows and adding to list
                if (cursor.getCount() != 0) {
                    // show message
                    while (cursor.moveToNext()) {

                        JSONObject word = new JSONObject();
                        try {
                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("TITLE", cursor.getString(1));
                            word.put("NUM", cursor.getInt(2));
                            word.put("COMPLETED", cursor.getInt(3));
                            word.put("ISREPEAT", cursor.getInt(4));
                            word.put("DUE_DATE", cursor.getString(5));
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
                    File file = new File(dir, "backup_promotodos.json");
                    try {
                        FileOutputStream f = new FileOutputStream(file);
                        PrintWriter pw = new PrintWriter(f);
                        pw.write(jsonArray.toString());
                        pw.flush();
                        pw.close();
                        f.close();
                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    showMessage("Error", "Nothing found");
                }


                JSONArray jsonArray1 = new JSONArray();
                final Cursor cursor1 = dbDaily.getAll();

                // looping through all rows and adding to list
                if (cursor1.getCount() != 0) {
                    // show message
                    while (cursor1.moveToNext()) {

                        JSONObject word = new JSONObject();
                        try {
                            word.put("ID", Integer.parseInt(cursor1.getString(0)));
                            word.put("DAY", cursor1.getString(1));
                            word.put("WEEK", cursor1.getString(2));
                            word.put("MONTH", cursor1.getString(3));
                            word.put("YEAR", cursor1.getString(4));
                            word.put("TIME", cursor1.getString(5));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        jsonArray1.put(word);
                    }

                    File root = android.os.Environment.getExternalStorageDirectory();
                    // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
                    File dir = new File(root.getAbsolutePath() + "/wordstore");
                    dir.mkdirs();
                    File file = new File(dir, "backup_daily.json");
                    try {
                        FileOutputStream f = new FileOutputStream(file);
                        PrintWriter pw = new PrintWriter(f);
                        pw.write(jsonArray1.toString());
                        pw.flush();
                        pw.close();
                        f.close();
                        //Toasty.success(context, "Done.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    // showMessage("Error", "Nothing found");
                }

                JSONArray jsonArray2 = new JSONArray();
                final Cursor cursor2 = dbTaskDhandle.getAllData();

                // looping through all rows and adding to list
                if (cursor2.getCount() != 0) {
                    // show message
                    while (cursor2.moveToNext()) {

                        JSONObject word = new JSONObject();


                                try {
                                    word.put("ID", Integer.parseInt(cursor2.getString(0)));
                                    word.put("TITLE", cursor2.getString(1));
                                    word.put("NUM", cursor2.getInt(2));
                                    word.put("COMPLETED", cursor2.getInt(3));
                                    word.put("ISREPEAT", cursor2.getInt(4));
                                    word.put("DUE_DATE", cursor2.getString(5));
                                    word.put("START_TIME", cursor2.getString(6));
                                    word.put("END_TIME", cursor2.getString(7));
                                    } catch (JSONException e) {
                                    // TODO Auto-generated catch block

                                    e.printStackTrace();
                                }





                        jsonArray2.put(word);
                    }

                    File root = android.os.Environment.getExternalStorageDirectory();
                    // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
                    File dir = new File(root.getAbsolutePath() + "/wordstore");
                    dir.mkdirs();
                    File file = new File(dir, "backup_timeline.json");
                    try {
                        FileOutputStream f = new FileOutputStream(file);
                        PrintWriter pw = new PrintWriter(f);
                        pw.write(jsonArray2.toString());
                        pw.flush();
                        pw.close();
                        f.close();
                        //Toasty.success(context, "Done.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    // showMessage("Error", "Nothing found");
                }



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
                try{dothings_daily();}catch (Exception e){

                }
                try{dothings_timeline();}catch (Exception e){

                }

                ProgressDialog dialog = ProgressDialog.show(pro_backup.this, "",
                        "Loading. Please wait...", true);


                if (number.get() == 1) {
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
        if (isDark) {
            retext.setBackgroundColor(Color.rgb(64, 64, 64));
            retext.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborderwhite));

            //coordinatorLayout.setBackgroundColor(Color.BLACK);
            //mainlayout.setBackgroundColor(Color.BLACK);
            mainlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborderwhite));
            coordinatorLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborderwhite));
            retext.setHintTextColor(Color.rgb(185, 185, 185));
            retext.setTextColor(Color.WHITE);
        } else {
            retext.setBackgroundColor(Color.WHITE);
            retext.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));

            //coordinatorLayout.setBackgroundColor(Color.WHITE);
            //mainlayout.setBackgroundColor(Color.WHITE);
            mainlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            coordinatorLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
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
        String currentDB = getDatabasePath(DBproHandle.DATABASE_NAME).getPath();
        String[] parts = fileUri.getLastPathSegment().split(":");
        String part2 = parts[1]; // 0


        if (part2.contains(".json")) {
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
                    mDBHelper.insertAll(jsonObject.get("TITLE").toString(), jsonObject.getInt("ISREPEAT"),
                            jsonObject.getInt("NUM"), jsonObject.get("DUE_DATE").toString(), jsonObject.getInt("COMPLETED"));
                }
                // Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        return 0;
    }
    private int dothings_daily() {
        FileChannel source = null;
        FileChannel destination = null;
        String currentDB = getDatabasePath(DBDaily.DATABASE_NAME).getPath();
        String[] parts = fileUri.getLastPathSegment().split(":");
        String part2 = "wordstore/backup_daily.json"; // 0


        if (part2.contains(".json")) {
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
                dbDaily.deleteAll();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.get(i).toString());
                    dbDaily.insertAll1(jsonObject.get("DAY").toString(), jsonObject.get("WEEK").toString(),
                            jsonObject.get("MONTH").toString(), jsonObject.get("YEAR").toString(), jsonObject.getString("TIME"));
                }
                // Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        return 0;
    }
    private int dothings_timeline() {
        FileChannel source = null;
        FileChannel destination = null;
        String currentDB = getDatabasePath(DBDaily.DATABASE_NAME).getPath();
        String[] parts = fileUri.getLastPathSegment().split(":");
        String part2 = "wordstore/backup_timeline.json"; // 0


        if (part2.contains(".json")) {
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
                dbTaskDhandle.deleteAll();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.get(i).toString());
                    dbTaskDhandle.insertAll(jsonObject.get("TITLE").toString(), jsonObject.getInt("ISREPEAT"),
                            jsonObject.getInt("NUM"), jsonObject.get("DUE_DATE").toString(),
                            jsonObject.getInt("COMPLETED"),jsonObject.get("START_TIME").toString(), jsonObject.get("END_TIME").toString());
                   // System.out.println(jsonObject.toString());
                }
                return 1;


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        return 0;
    }
}
