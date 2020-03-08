package com.example.czgame.wordbank.ui.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.root.WalkThrough;
import com.example.czgame.wordbank.ui.words.DatabaseHelper;
import com.example.czgame.wordbank.ui.words.MyListAdapter;
import com.example.czgame.wordbank.ui.words.WordDetail;
import com.example.czgame.wordbank.ui.words.add_page;
import com.example.czgame.wordbank.ui.words.word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import es.dmoral.toasty.Toasty;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class HomeActivity extends AppCompatActivity {

    public static boolean isDark;
    public static SharedPreferences prefs;
    public static int size = 0;
    public static List<word> contactList = new ArrayList<word>();
    public static SharedPreferences sizee;
    MyListAdapter adapter;
    ListView list;
    Button sort;
    SearchView searchView;
    SearchView searchView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);


        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);


        isDark = prefs.getBoolean("isDark", false);





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelativeLayout homelay = findViewById(R.id.homelayoyt);
        if(isDark){
            homelay.setBackgroundColor(Color.BLACK);
        }else{
            homelay.setBackgroundColor(Color.WHITE);
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

        DatabaseHelper mDBHelper = new DatabaseHelper(this);


        contactList.clear();

        int i =0;

        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                word word = new word();

                if(!cursor.getString(3).isEmpty() && !cursor.getString(5).isEmpty()
                        && !cursor.getString(6).isEmpty()){
                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE(cursor.getString(3));
                    word.setSYNONYMS(cursor.getString(5));
                    word.setANTONYMS(cursor.getString(6));
                }
                else if(cursor.getString(3).isEmpty()){

                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE("None");
                    word.setSYNONYMS(cursor.getString(5));
                    word.setANTONYMS(cursor.getString(6));

                }else if(cursor.getString(5).isEmpty()){

                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE(cursor.getString(3));
                    word.setSYNONYMS("None");
                    word.setANTONYMS(cursor.getString(6));

                }else if(cursor.getString(6).isEmpty()){

                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE(cursor.getString(3));
                    word.setSYNONYMS(cursor.getString(5));
                    word.setANTONYMS("None");
                }




                contactList.add(word);
                //   System.out.println(word.ANTONYMS);

                // maintitle.add(word.WORD);
                // subtitle.add(word.MEANING);

                i++;
            }


            // size = contactList.size();
            adapter = new MyListAdapter(this);
            list = findViewById(R.id.list);

            String type = prefs.getString("sort", "asc");
            if (type.equals("asc")) {
                Collections.sort(contactList);
            } else if (type.equals("des")) {
                Collections.sort(contactList, Collections.reverseOrder());
            } else if (type.equals("alp")) {
                Collections.sort(contactList,
                        new Comparator<word>() {
                            public int compare(word f1, word f2) {
                                return f1.getWORD().compareTo(f2.getWORD());
                            }
                        });
            }

            //list.setFastScrollEnabled(true);
            list.setAdapter(adapter);

            //textView = findViewById(R.id.board);
            //final Intent intent = getIntent();
            //bestscore = (intent.getIntExtra("sb",0)> bestscore)?intent.getIntExtra("sb",0) :bestscore;
//            textView.setText("Total Words : \n"+String.valueOf(contactList.size())+"\nCurrent score : \n"+String.valueOf(bestscore));


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    // System.out.println(position);
                    //list.getChildAt(position).setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.card_state_pressed));
                    Intent myIntent = new Intent(view.getContext(), WordDetail.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.putExtra("message", contactList.get(position).getWORD());
                    myIntent.putExtra("meaningb", contactList.get(position).getMEANINGB());
                    myIntent.putExtra("meaninge", contactList.get(position).getMEANINGE());
                    myIntent.putExtra("syn", contactList.get(position).getSYNONYMS());
                    myIntent.putExtra("ant", contactList.get(position).getANTONYMS());
                    myIntent.putExtra("id", position);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                }
            });
        } else {

            Toasty.info(HomeActivity.this,"Error Nothing found");
        }

        size = contactList.size();

        sizee = this.getSharedPreferences("ok", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sizee.edit();
        editor.putInt("size", size);
        editor.commit();



        System.out.println(HomeActivity.sizee.getInt("size", 0));

        sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(MainActivity.this, sort);
                //Inflating the Popup using xml file
                Context wrapper = new ContextThemeWrapper(HomeActivity.this, R.style.YOURSTYLE1);
                if (isDark) {
                    wrapper = new ContextThemeWrapper(HomeActivity.this, R.style.YOURSTYLE);

                } else {
                    wrapper = new ContextThemeWrapper(HomeActivity.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, sort);
                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Ascending")) {
                            Collections.sort(contactList);
                            //  adapter = new MyListAdapter(getParent());
                            list = findViewById(R.id.list);
                            list.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("sort", "asc");
                            editor.commit();

                        } else if (item.getTitle().equals("Descending")) {
                            Collections.sort(contactList, Collections.reverseOrder());
                            //adapter = new MyListAdapter(getParent());
                            list = findViewById(R.id.list);
                            list.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("sort", "des");
                            editor.commit();

                        } else if (item.getTitle().equals("Alphabetically")) {
                            Collections.sort(contactList,
                                    new Comparator<word>() {
                                        public int compare(word f1, word f2) {
                                            return f1.getWORD().compareTo(f2.getWORD());
                                        }
                                    });
                            list = findViewById(R.id.list);
                            list.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("sort", "alp");
                            editor.commit();

                        } else {

                        }

                        Toasty.info(HomeActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        RelativeLayout linearLayout1 = findViewById(R.id.listview);

        if (isDark) {
            linearLayout1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background_dark));
            if (contactList.size() != 0) list.setAdapter(adapter);

        } else {
            linearLayout1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background));
            if (contactList.size() != 0) list.setAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_searchw);

        searchView1 = (SearchView) menu.findItem(R.id.app_bar_search1).getActionView();
        searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        v = searchView1.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_searchm);

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
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView1.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter1(newText);
                return false;
            }

        });


        if (WalkThrough.isfirst) {
            WalkThrough.isfirst = false;
            new MaterialTapTargetSequence()
                    .addPrompt(new MaterialTapTargetPrompt.Builder(HomeActivity.this)
                            .setTarget(findViewById(R.id.fab))
                            .setPrimaryText("Step 1")
                            .setSecondaryText("Add Word")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setFocalPadding(R.dimen.dp40)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(this)
                            .setPrimaryText("Step 2")
                            .setSecondaryText("Sort Words")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new FastOutSlowInInterpolator())
                            .setMaxTextWidth(R.dimen.dp40)
                            .setIcon(R.drawable.ic_sort_black_24dp)
                            .setTarget(R.id.sort)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(this)
                            .setPrimaryText("Step 3")
                            .setSecondaryText("Search by word")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new FastOutSlowInInterpolator())
                            .setMaxTextWidth(R.dimen.dp40)
                            .setIcon(R.drawable.ic_searchw)
                            .setTarget(searchView)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(HomeActivity.this)
                            .setTarget(findViewById(R.id.app_bar_search1))
                            .setPrimaryText("Step 4")
                            .setSecondaryText("Search by meaning")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new LinearOutSlowInInterpolator())
                            .setFocalPadding(R.dimen.dp40)
                            .setTarget(searchView1)
                            .setIcon(R.drawable.ic_searchm)
                            .create(), 4000)

                    .show();
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
        }
        return super.onCreateOptionsMenu(menu);
    }
}
