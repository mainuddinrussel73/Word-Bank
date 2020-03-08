package com.example.czgame.wordbank.ui.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import es.dmoral.toasty.Toasty;

public class news_activity extends AppCompatActivity {

    public static List<News> newsList = new ArrayList<>();
    ListView list;
    News_adapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("news");
    private ShimmerFrameLayout mShimmerViewContainer;
    int i = 0,c=5;
    //int k = 0;
    int tiken = 0;
    private View footer;
    private boolean moreData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(true);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), add_news.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });

        DBNewsHelper mDBHelper = new DBNewsHelper(this);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        newsList.clear();
        footer = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress, null, false);


        SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        String type = prefs1.getString("sort", "asc");
        if (type.equals("asc")) {
            tiken = 0;
        } else if (type.equals("des")) {
            tiken = 1;
        } else if (type.equals("alp")) {
            tiken = 2;
        }


        final Cursor[] cursor = {mDBHelper.getAllData12(tiken)};

        // looping through all rows and adding to list
        if (cursor[0].getCount() != 0) {
            // show message
            while (cursor[0].moveToNext()) {


                if(i<5){
                News word = new News();
                word.setID(Integer.parseInt(cursor[0].getString(0)));
                word.setTITLE(cursor[0].getString(1));
                word.setBODY(cursor[0].getString(2));
                word.ISREAD =  cursor[0].getInt(3);
                word.setURL(cursor[0].getString(4));

                newsList.add(word);

                    i++;
                }else{
                    break;
                }


            }

            // size = contactList.size();



            adapter = new News_adapter(this);

            list = findViewById(R.id.news_list);

            list.addFooterView(footer);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(view.getContext(), news_details.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.putExtra("title", newsList.get(position).getTITLE());
                    myIntent.putExtra("body", newsList.get(position).getBODY());
                    myIntent.putExtra("url", newsList.get(position).getURL());
                    myIntent.putExtra("id", position);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                }
            });


//            k = 0;
            list.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    moreData = true;


                }

                @Override
                public void onScroll(AbsListView absListView, int firstItem, int visibleItemCount, final int totalItems) {



                    final int lastItem = firstItem + visibleItemCount;

                    if (lastItem == totalItems) {


                            footer.setVisibility(View.VISIBLE);
                            footer.setPadding(0, 0, 0, 0);
                            //newsList.clear();


                        Handler handler = new Handler(); // hear is the handler for testing purpose
                        handler.postDelayed(new Runnable() { // make some delay for check load more view
                            @Override
                            public void run() {


                                if(moreData){

                                    loadmore();
                                    moreData=false;
                                }


                                adapter = new News_adapter(news_activity.this);
                                footer.setVisibility(View.GONE);
                                footer.setPadding(0, -1 * footer.getHeight(), 0, 0);
                                //here above tow line invisible footer after data added
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();

                                }

                            }
                        }, 2000);



                        }






                   // }

                }

            });





        } else {

            Toasty.info(news_activity.this, "Nothing to show.", Toasty.LENGTH_LONG).show();
        }



        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo != null) {
                    if (netInfo.isConnected()) {
                        //new LoadData().execute();

                    }
                }else{
                    Toasty.error(news_activity.this,"No internet connection.",Toast.LENGTH_LONG).show();
                }
                pullToRefresh.setRefreshing(false);
            }
        });


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);


            toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

        CoordinatorLayout cardView = findViewById(R.id.basic);
        if (isDark && newsList.size() != 0) {

            cardView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            RelativeLayout constraintLayout = findViewById(R.id.content_newsre);
            LinearLayout linearLayout = findViewById(R.id.newslistview);
            constraintLayout.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            list.setAdapter(adapter);
        } else if (!isDark && newsList.size() != 0) {
            cardView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.material_white));
            RelativeLayout constraintLayout = findViewById(R.id.content_newsre);
            LinearLayout linearLayout = findViewById(R.id.newslistview);
            constraintLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            list.setAdapter(adapter);

        } else if (isDark && newsList.size() == 0) {
            cardView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
            RelativeLayout constraintLayout = findViewById(R.id.content_newsre);
            LinearLayout linearLayout = findViewById(R.id.newslistview);

            constraintLayout.setBackgroundColor(Color.BLACK);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));

        } else if (!isDark && newsList.size() == 0) {
            cardView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.material_white));
            RelativeLayout constraintLayout = findViewById(R.id.content_newsre);
            LinearLayout linearLayout = findViewById(R.id.newslistview);

            constraintLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));


        }
    }

    public void loadmore(){
        int k = 0;
        DBNewsHelper mDBHelper = new DBNewsHelper(this);
        final Cursor cursor = mDBHelper.getAllData12(tiken);
        cursor.moveToPosition(c);
        c+=5;

        //newsList.clear();
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {


                if (k < 5) {
                    News word = new News();

                    word.setID(Integer.parseInt(cursor.getString(0)));
                    word.setTITLE(cursor.getString(1));
                    word.setBODY(cursor.getString(2));
                    word.ISREAD = cursor.getInt(3);
                    word.setURL(cursor.getString(4));

                    System.out.println(k);
                    newsList.add(word);
                    k++;

                } else if(k>=5) {

                    //moreData = false;

                   // k = 0;
                    break;


                }
                System.out.println("c"+c);
                System.out.println("k"+k);



            }


        }


    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);


        SearchView searchView;

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();


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

    class LoadData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private DBNewsHelper mDBHelper;


        public LoadData( ) {
            dialog = new ProgressDialog(news_activity.this);
        }

        @Override
        protected void onPostExecute(Void result) {

            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);

        }

        @Override
        protected void onPreExecute() {
            mShimmerViewContainer.startShimmer();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newsList.clear();
            mDBHelper = new DBNewsHelper(news_activity.this);
            mDBHelper.deleteAll();
            ref = database.getReference("news");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        newsList.add(new News(i,postSnapshot.child("TITLE").getValue().toString(),postSnapshot.child("BODY").getValue().toString(),postSnapshot.child("URL").getValue().toString()));

                        Display display = getWindowManager().getDefaultDisplay();
                        int width = display.getWidth();

                        String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
                        dataq = dataq + "<body>"+ postSnapshot.child("BODY").getValue().toString() +"</body></html>";

                        String stringToAdd = "width=\"100%\" ";

                        // Create a StringBuilder to insert string in the middle of content.
                        StringBuilder sb = new StringBuilder(dataq);

                        int j = 0;
                        int cont = 0;

                        // Check for the "src" substring, if it exists, take the index where
                        // it appears and insert the stringToAdd there, then increment a counter
                        // because the string gets altered and you should sum the length of the inserted substring
                        while(j != -1){
                            j = dataq.indexOf("src", j + 1);
                            if(j != -1) sb.insert(j + (cont * stringToAdd.length()), stringToAdd );
                            ++cont;
                        }


                        boolean b;
                        if (postSnapshot.child("URL").getValue().toString().isEmpty()) {
                            b = mDBHelper.insertData(postSnapshot.child("TITLE").getValue().toString(), dataq, "empty");
                        } else
                            b = mDBHelper.insertData(postSnapshot.child("TITLE").getValue().toString(), dataq, postSnapshot.child("URL").getValue().toString());

                    }

                    SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    String type = prefs.getString("sort", "asc");
                    if (type.equals("asc")) {
                        Collections.sort(newsList);
                    } else if (type.equals("des")) {
                        Collections.sort(newsList, Collections.reverseOrder());
                    } else if (type.equals("alp")) {
                        Collections.sort(newsList,
                                new Comparator<News>() {
                                    public int compare(News f1, News f2) {
                                        return f1.getTITLE().compareTo(f2.getTITLE());
                                    }
                                });
                    }


                    adapter = new News_adapter(news_activity.this);
                    list = findViewById(R.id.news_list);
                    list.setAdapter(adapter);


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Log.w("d", "Failed to read value.", error.toException());
                }
            });
            return null;
        }
    }


}
