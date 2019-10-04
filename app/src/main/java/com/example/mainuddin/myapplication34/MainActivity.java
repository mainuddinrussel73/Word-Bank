package com.example.mainuddin.myapplication34;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.mainuddin.myapplication34.ui.BackupAndRestore.backup_restore;
import com.example.mainuddin.myapplication34.ui.Quiz.quiz_page;
import com.example.mainuddin.myapplication34.ui.data.DatabaseHelper;
import com.example.mainuddin.myapplication34.ui.data.word;
import com.example.mainuddin.myapplication34.ui.data.word_details;
import com.example.mainuddin.myapplication34.ui.insert.add_page;
import com.example.mainuddin.myapplication34.ui.tools.MyListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.suke.widget.SwitchButton;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    com.suke.widget.SwitchButton switchButton;
    Button sort ;
    ListView list;
    public static boolean isDark=false;
    private Context mContext;
    private Activity mActivity;
    public  static  int score = 0;
    private PopupWindow mPopupWindow;
    public static  boolean isChecked = false;
    public static int size = 0;
    public static List<word> contactList = new ArrayList<word>();
    DrawerLayout drawer;
    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    SearchView searchView;
    MyListAdapter adapter;
    private DrawerLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), add_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.scores);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        MainActivity.score = prefs.getInt("key", 0);
        navUsername.setText("Highest Score is : "+Integer.toString(MainActivity.score));

        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    // TODO: switch on menuItem.getItemId()
                    return false;
                }
            });
        }





        DatabaseHelper mDBHelper = new DatabaseHelper(this);


        contactList.clear();








        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if(cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                word word = new word();
                word.setID( Integer.parseInt(cursor.getString(0)));
                word.setWORD( cursor.getString(1));
                word.setMEANING(cursor.getString(2));

                contactList.add(word);

                // maintitle.add(word.WORD);
                // subtitle.add(word.MEANING);

            }

           // size = contactList.size();
            adapter = new MyListAdapter(this);
            list=(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);

            //textView = findViewById(R.id.board);
            //final Intent intent = getIntent();
            //bestscore = (intent.getIntExtra("sb",0)> bestscore)?intent.getIntExtra("sb",0) :bestscore;
//            textView.setText("Total Words : \n"+String.valueOf(contactList.size())+"\nCurrent score : \n"+String.valueOf(bestscore));




            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    // TODO Auto-generated method stub
                   // System.out.println(position);
                    Intent myIntent = new Intent(view.getContext(), word_details.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.putExtra("message",contactList.get(position).getWORD());
                    myIntent.putExtra("meaning",contactList.get(position).getMEANING());
                    myIntent.putExtra("id",position);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                }
            });
        }
        else {

            showMessage("Error","Nothing found");
        }
        size = contactList.size();




        sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(MainActivity.this, sort);
                //Inflating the Popup using xml file
                Context wrapper = new ContextThemeWrapper(mContext, R.style.YOURSTYLE1);
                if(isDark){
                    wrapper = new ContextThemeWrapper(MainActivity.this, R.style.YOURSTYLE);

                }else{
                    wrapper = new ContextThemeWrapper(MainActivity.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, sort);
                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());



                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Ascending")){
                            Collections.sort(contactList);
                          //  adapter = new MyListAdapter(getParent());
                            list=(ListView)findViewById(R.id.list);
                            list.setAdapter(adapter);
                        }else if(item.getTitle().equals("Descending")){
                            Collections.sort(contactList,Collections.reverseOrder());
                            //adapter = new MyListAdapter(getParent());
                            list=(ListView)findViewById(R.id.list);
                            list.setAdapter(adapter);
                        }else if(item.getTitle().equals("Alphabetically")){
                            Collections.sort(contactList,
                                    new Comparator<word>()
                                    {
                                        public int compare(word f1, word f2)
                                        {
                                            return f1.getWORD().compareTo(f2.getWORD());
                                        }
                                    });
                            list=(ListView)findViewById(R.id.list);
                            list.setAdapter(adapter);
                        }else{

                        }

                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method




        ConstraintLayout constraintLayout1  = findViewById(R.id.content_main);
        LinearLayout linearLayout1 =  findViewById(R.id.listview);

        NavigationView navigationView1 = findViewById(R.id.nav_view);

        if(isDark){

            constraintLayout1.setBackgroundColor(Color.rgb(64,64,64));
            // linearLayout.setBackgroundColor(Color.BLACK);
            navigationView1.setBackgroundColor(Color.rgb(64,64,64));
            navigationView1.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
            linearLayout1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            if(contactList.size()!=0)list.setAdapter(adapter);

        }else {
            constraintLayout1.setBackgroundColor(Color.WHITE);
            navigationView1.setBackgroundColor(Color.WHITE);
            navigationView1.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            // linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.listview_border));

            if(contactList.size()!=0)list.setAdapter(adapter);
        }


    }






    public void showMessage(String title ,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            // Handle the camera action
            //Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(MainActivity.this, backup_restore.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);

        } else if (id == R.id.nav_home) {

            Intent myIntent = new Intent(MainActivity.this, quiz_page.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);

        } else if (id == R.id.nav_tools) {
            mContext = getApplicationContext();
            mActivity = MainActivity.this;





            if(!isDark){

                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogueLight);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.yes);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout  = findViewById(R.id.content_main);
                    LinearLayout linearLayout =  findViewById(R.id.listview);

                    NavigationView navigationView = findViewById(R.id.nav_view);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDark = true;
                        constraintLayout.setBackgroundColor(Color.rgb(64,64,64));
                        // linearLayout.setBackgroundColor(Color.BLACK);
                        navigationView.setBackgroundColor(Color.rgb(64,64,64));
                        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.list_viewdark));
                        list.setAdapter(adapter);


                        Toast.makeText(mContext, "Enabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogurDark);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.no);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout  = findViewById(R.id.content_main);
                    LinearLayout linearLayout =  findViewById(R.id.listview);

                    NavigationView navigationView = findViewById(R.id.nav_view);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDark = false;
                        constraintLayout.setBackgroundColor(Color.WHITE);
                        navigationView.setBackgroundColor(Color.WHITE);
                        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
                        // linearLayout.setBackgroundColor(Color.WHITE);
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.listview_border));
                        list.setAdapter(adapter);


                        Toast.makeText(mContext, "Disabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();


            }




        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
