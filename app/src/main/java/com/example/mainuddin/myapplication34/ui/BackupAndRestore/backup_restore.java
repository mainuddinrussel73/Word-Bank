package com.example.mainuddin.myapplication34.ui.BackupAndRestore;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.ui.data.DatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class backup_restore extends AppCompatActivity {


    public static final int PICKFILE_RESULT_CODE = 1;
    private DatabaseHelper mDBHelper;
    private Button insert,backup,restore,ok,restore1;
    private EditText retext;

    TextView textView;
    private Uri fileUri;
    private String filePath;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_backup_restore);
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){
            Log.d("error",e.getMessage());
        }

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

         backup = (Button) findViewById(R.id.backup);

        backup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                final Cursor cursor = mDBHelper.getAllData();

                // looping through all rows and adding to list
                if(cursor.getCount() != 0) {
                    // show message
                    while (cursor.moveToNext()) {

                        JSONObject word = new JSONObject();
                        try {
                            word.put("ID", Integer.parseInt(cursor.getString(0)));
                            word.put("WORD", cursor.getString(1));
                            word.put("MEANING", cursor.getString(2));
                            word.put("SENTENCE", cursor.getString(3));
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
                        Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }

                }


                else {

                    showMessage("Error","Nothing found");
                }



            }


        });

         restore = (Button) findViewById(R.id.restore);
        restore1 = (Button) findViewById(R.id.restore1);
         ok = (Button) findViewById(R.id.click);
         retext = findViewById(R.id.retext);
        ok.setEnabled(false);
        restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
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
                FileChannel source=null;
                FileChannel destination=null;
                String currentDB = getDatabasePath(mDBHelper.DATABASE_NAME).getPath();
                String[] parts = fileUri.getLastPathSegment().split(":");
                String part2 = parts[1]; // 0

                if(!part2.contains(".json")){
                    List<String> total = new ArrayList<>();
                    String retreivedDBPAth= Environment.getExternalStorageDirectory()+"/"+part2;
                    File input = new File(retreivedDBPAth);
                    //BufferedWriter file = null;
                    File root = android.os.Environment.getExternalStorageDirectory();
                    // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
                    File dir = new File(root.getAbsolutePath() + "/wordstore");
                    File file ;
                    dir.mkdirs();
                    file = new File(dir, "backup.json");
                        //file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/macbookair/IdeaProjects/data/backup.html"), "UTF-8"));

                    Document document = null;
                    try {
                        document = Jsoup.parse(input, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Elements paragraphs = document.getElementsByTag("entry");
                    int count= 0 ;
                    for (Element paragraph : paragraphs) {
                        Elements w = paragraph.getElementsByTag("w");
                        Elements m = paragraph.getElementsByTag("m");
                        Elements ex = paragraph.getElementsByTag("ex");
                        total.add("{ \"ID\" : "+count+" , \"WORD\" : \"" + w.text() + "\" , \"MEANING\" : \"" + m.text() +"\" ,  \"SENTENCE\" : \"" +ex.text()+ "\" }");
                        count++;
                    }
                    int i=0;
                    FileOutputStream f = null;
                    PrintWriter pw = null;
                    try {
                       f =  new FileOutputStream(file);
                       pw = new PrintWriter(f);
                        pw.write("[");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    do{
                        String resultData = total.get(i);
                        if(i==count-1) {


                            pw.write(resultData + "\n");
                            pw.flush();

                        }
                        else {

                            pw.write(resultData + ", \n");
                            pw.flush();
                        }
                        i++;
                    }while(i<count);
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

                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
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
                        json = new String(buffer, "UTF-8");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }
                    try {
                        jsonArray = new JSONArray(json);
                        mDBHelper.deleteAll();
                        for( i=0;i<jsonArray.length();i++){
                            jsonObject = new JSONObject(jsonArray.get(i).toString());
                            mDBHelper.insertData(jsonObject.get("WORD").toString(),jsonObject.get("MEANING").toString(),jsonObject.get("SENTENCE").toString());
                        }
                        Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();

                }else{
                    String retreivedDBPAth= Environment.getExternalStorageDirectory()+"/"+part2;
                    File retrievedDB = new File(retreivedDBPAth);
                    InputStream is = null;


                    try {
                        is = new FileInputStream(retrievedDB);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
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
                        json = new String(buffer, "UTF-8");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }
                    try {
                        jsonArray = new JSONArray(json);
                        mDBHelper.deleteAll();
                        for(int i=0;i<jsonArray.length();i++){
                            jsonObject = new JSONObject(jsonArray.get(i).toString());
                            mDBHelper.insertData(jsonObject.get("WORD").toString(),jsonObject.get("MEANING").toString(),jsonObject.get("SENTENCE").toString());
                        }
                        Toast.makeText(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Opps.",Toast.LENGTH_SHORT).show();
                    }




                }





            }


        });
    }
    public void showMessage(String title ,String Message){
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

}
