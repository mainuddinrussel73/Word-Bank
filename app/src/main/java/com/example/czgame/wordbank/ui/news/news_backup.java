package com.example.czgame.wordbank.ui.news;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.czgame.wordbank.ui.words.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class news_backup extends AppCompatActivity {

    public static final int PICKFILE_RESULT_CODE = 1;
    public static AtomicInteger number = new AtomicInteger(0);
    public boolean flag;
    public View v;
    RelativeLayout coordinatorLayout, mainlayout;
    TextView textView;
    private DBNewsHelper mDBHelper;
    private Button insert, backup, restore, ok, restore1;
    private EditText retext;
    private Uri fileUri;
    private String filePath;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("news");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {

            setContentView(R.layout.activity_news_backup);
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


        mDBHelper = new DBNewsHelper(this);

        backup = findViewById(R.id.backup);
        backup.setEnabled(true);

        backup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new MyTask().execute();


                JSONArray jsonArray = new JSONArray();
                final Cursor cursor = mDBHelper.getAllData();


                if (cursor.getCount() != 0) {


                    // show message
                    while (cursor.moveToNext()) {




                        JSONObject word = new JSONObject();
                        try {
                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("TITLE", cursor.getString(1));
                            word.put("BODY", cursor.getString(2));
                            word.put("ISREAD", cursor.getInt(3));
                            word.put("URL", cursor.getString(4));

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }



                        // myRef.setValue(new News(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3)));

                        jsonArray.put(word);
                    }

                    File root = android.os.Environment.getExternalStorageDirectory();
                    // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
                    File dir = new File(root.getAbsolutePath() + "/wordstore");
                    dir.mkdirs();
                    File file = new File(dir, "backup_news.json");
                    try {
                        FileOutputStream f = new FileOutputStream(file);
                        PrintWriter pw = new PrintWriter(f);
                        pw.write(jsonArray.toString());
                        pw.flush();
                        pw.close();
                        f.close();
                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                        backup.setEnabled(false);
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

                ProgressDialog dialog = ProgressDialog.show(news_backup.this, "",
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
        RelativeLayout relativeLayout = findViewById(R.id.coordinate_j);
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

            toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

        if (isDark) {
            retext.setBackgroundColor(Color.rgb(64, 64, 64));
            retext.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));

            relativeLayout.setBackgroundColor(Color.BLACK);
         //   //mainlayout.setBackgroundColor(Color.BLACK);
            mainlayout.setBackgroundColor(Color.BLACK);
            coordinatorLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background_dark));
            retext.setHintTextColor(Color.rgb(185, 185, 185));
            retext.setTextColor(Color.WHITE);
        } else {
            retext.setBackgroundColor(Color.WHITE);
            retext.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));

            relativeLayout.setBackgroundColor(Color.WHITE);
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
        String currentDB = getDatabasePath(DBNewsHelper.DATABASE_NAME).getPath();
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
                    if (!jsonObject.has("URL")) {
                        mDBHelper.insertData(jsonObject.get("TITLE").toString(), jsonObject.get("BODY").toString(), "empty");
                    } else
                        mDBHelper.insertData(jsonObject.get("TITLE").toString(), jsonObject.get("BODY").toString(), jsonObject.get("URL").toString());
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
    private class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo != null) {
                if (netInfo.isConnected()) {


                    myRef = database.getReference("news");
                    myRef.setValue(null);
                    return true;

                }
            }else{
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean c) {
            if(!c){
                Toasty.error(news_backup.this,"No internet connection.",Toast.LENGTH_LONG).show();
            }
            new MyTask2().execute();
            // do something with result
        }
    }
    private class MyTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            final Cursor cursor = mDBHelper.getAllData();


            if (cursor.getCount() != 0) {


                // show message
                while (cursor.moveToNext()) {


                    if (netInfo != null) {
                        if (netInfo.isConnected()) {


                            myRef.child(cursor.getString(0)).child("ID").setValue(cursor.getString(0));
                            myRef.child(cursor.getString(0)).child("TITLE").setValue(cursor.getString(1));
                            myRef.child(cursor.getString(0)).child("BODY").setValue(cursor.getString(2));
                            myRef.child(cursor.getString(0)).child("ISREAD").setValue(cursor.getInt(3));
                            myRef.child(cursor.getString(0)).child("URL").setValue(cursor.getString(4));


                        }
                    } else {
                    }

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void c) {

            // do something with result
        }
    }

}
