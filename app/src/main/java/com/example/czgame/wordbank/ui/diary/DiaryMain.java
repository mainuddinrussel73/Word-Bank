package com.example.czgame.wordbank.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class DiaryMain extends AppCompatActivity {
    GridView listView;
    SqliteDatabase db;
    ArrayList<Info> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();

    ArrayAdapter arrayAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_diary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = findViewById(R.id.ListviewId);

        arrayList=new ArrayList<Info>();

        FloatingActionButton fab = findViewById(R.id.fab);

        FloatingActionButton fab2 = findViewById(R.id.fab2);

        // ClickListener for floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryMain.this,AddData.class);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryMain.this,AddDataVoice.class);
                startActivity(intent);
            }
        });

        view();//calling view method


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.laybase);
        RelativeLayout lidtb = findViewById(R.id.opp);

        if(isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (isDark && arrayAdapter.getCount() != 0) {

            laybase.setBackgroundColor(Color.BLACK);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            listView.setBackgroundColor(Color.BLACK);
            listView.setAdapter(arrayAdapter);
        } else if (!isDark && arrayAdapter.getCount()!= 0) {

            laybase.setBackgroundColor(Color.WHITE);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            listView.setBackgroundColor(Color.WHITE);
            listView.setAdapter(arrayAdapter);

        } else if (isDark && arrayAdapter.getCount() == 0) {


            laybase.setBackgroundColor(Color.BLACK);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            listView.setBackgroundColor(Color.BLACK);

        } else if (!isDark && arrayAdapter.getCount() == 0) {

            laybase.setBackgroundColor(Color.WHITE);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            listView.setBackgroundColor(Color.WHITE);


        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayList.get(i).getDescription().trim().isEmpty()==false){
                    Intent intent = new Intent(DiaryMain.this,UpdateDiary.class);
                    intent.putExtra("subject",arrayList.get(i).getSubject());
                    intent.putExtra("description",arrayList.get(i).getDescription());
                    intent.putExtra("listId",arrayList.get(i).getId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DiaryMain.this,UpdateDiary_voice.class);
                    intent.putExtra("subject",arrayList.get(i).getSubject());
                    intent.putExtra("medialink",arrayList.get(i).getMedialink());
                    intent.putExtra("listId",arrayList.get(i).getId());
                    startActivity(intent);
                }

            }
        });

    }



    public void view() {
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            Info information = new Info(cursor.getString(0),cursor.getString(1),
                    cursor.getString(2),cursor.getString(3),cursor.getString(4));
            arrayList.add(information);
            System.out.println(information.toString());
        }

        Collections.reverse(arrayList);//reversing arrayList for showing data in a proper way

        arrayAdapter = new InfoAdapter(this, arrayList);//passing context and arrayList to arrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//setting choice mode
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {//method for multiChoice option

            //checking state Item on Click mode or not
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                String id = arrayList.get(i).getId();//for getting database Id
                //if double click Item color will be white
                if(selectList.contains(id) && count>0){
                    listView.getChildAt(i).setBackground(ContextCompat.getDrawable(DiaryMain.this,R.drawable.card_background_green));
                    selectList.remove(id);
                    count--;
                }
                //else item color will be gray
                else{
                    selectList.add(arrayList.get(i).getId());
                    listView.getChildAt(i).setBackground(ContextCompat.getDrawable(DiaryMain.this,R.drawable.card_background_green));
                    unDeleteSelect.add(i);//item position storing on new arrayList
                    count++;
                }
                actionMode.setTitle(count+" item selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();//for connecting menu with main menu here
                inflater.inflate(R.menu.selector_layout,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            //this method for taking action like delete,share
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.deleteContextMenuId){
                    for(String i : selectList){
                        db.delete(i);
                        arrayAdapter.remove(i);
                        Toasty.success(getApplicationContext(), count + " item Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DiaryMain.this,DiaryMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    arrayAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    count = 0;
                }
                return true;
            }

            //this method for destroying actionMode
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                for(int i: unDeleteSelect){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);//reset all selected item with gray color
                }
                count = 0;//reset count here
                unDeleteSelect.clear();
                selectList.clear();
            }
        });
    }
}
