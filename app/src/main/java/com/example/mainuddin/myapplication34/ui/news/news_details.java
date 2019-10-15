package com.example.mainuddin.myapplication34.ui.news;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mainuddin.myapplication34.ui.words.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class news_details extends AppCompatActivity {

    TextView news_details;
    Intent intent;
    private Toolbar toolbar;
    Document doc = new Document();
    private boolean isCustomOverflowMenu;
    private static final String EXTRA_IS_CUSTOM = "is_custom_overflow_menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        final Button button = (Button) findViewById(R.id.opt);
        registerForContextMenu(button);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        intent = getIntent();

        news_details = findViewById(R.id.news_detail_des);
        news_details.setText(intent.getStringExtra("body"));

        getSupportActionBar().setTitle(intent.getStringExtra("title"));
        CoordinatorLayout additem = findViewById(R.id.content_detail);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if (isDark) {
            additem.setBackgroundColor(Color.BLACK);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            news_details.setTextColor(Color.WHITE);

        } else {
            additem.setBackgroundColor(Color.WHITE);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            news_details.setTextColor(Color.BLACK);
        }


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(MainActivity.this, sort);
                //Inflating the Popup using xml file
                Context wrapper = new ContextThemeWrapper(news_details.this, R.style.YOURSTYLE1);
                if (MainActivity.isDark) {
                    wrapper = new ContextThemeWrapper(news_details.this, R.style.YOURSTYLE);

                } else {
                    wrapper = new ContextThemeWrapper(news_details.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, button);
                popup.getMenuInflater().inflate(R.menu.top_menu, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.create_new:
                                try {
                                    Intent myIntent = new Intent(news_details.this, news_update.class);
                                    //String s = view.findViewById(R.id.subtitle).toString();
                                    //String s = (String) parent.getI;
                                    myIntent.putExtra("title", intent.getStringExtra("title"));
                                    myIntent.putExtra("body", intent.getStringExtra("body"));
                                    myIntent.putExtra("id", intent.getExtras().getInt("id"));
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivityForResult(myIntent, 0);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                return true;
                            case R.id.open:

                                DBNewsHelper mDBHelper;

                                mDBHelper = new DBNewsHelper(news_details.this);
                                int id = intent.getExtras().getInt("id");
                                id++;
                                int b = mDBHelper.deleteData(String.valueOf(id), intent.getStringExtra("message"), intent.getStringExtra("title")
                                        , intent.getStringExtra("body"));
                                if (b == 1) {
                                    Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(news_details.this, news_activity.class);
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivityForResult(myIntent, 0);
                                } else {
                                    Toasty.success(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                                }
                                return true;

                            case R.id.pdf:

                                new CreatePdf(news_details.this)
                                        .setPdfName(intent.getStringExtra("title"))
                                        .openPrintDialog(false)
                                        .setContentBaseUrl(null)
                                        .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                                        .setContent(intent.getStringExtra("body"))
                                        .setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir")
                                        .setCallbackListener(new CreatePdf.PdfCallbackListener() {
                                            @Override
                                            public void onFailure(@NotNull String s) {
                                                Toasty.error(news_details.this,"Failed",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onSuccess(@NotNull String s) {
                                                // do your stuff here
                                                Toasty.success(news_details.this,"Success",Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .create();


                            default:
                                return false;
                        }


                    }
                });
                popup.show();
            }
        });
    }



}