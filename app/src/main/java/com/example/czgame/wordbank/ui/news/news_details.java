package com.example.czgame.wordbank.ui.news;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.ui.words.MainActivity;
import com.example.czgame.wordbank.R;
import com.itextpdf.text.Document;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class news_details extends AppCompatActivity {

    private static final int TRANSLATE = 1;
    private static final String EXTRA_IS_CUSTOM = "is_custom_overflow_menu";
    TextView news_details;
    Intent intent;
    Document doc = new Document();
    String selectedText;
    SharedPreferences prefs;
    boolean isDark;
    TextView textView;
    private Toolbar toolbar;
    private boolean isCustomOverflowMenu;
    private DBNewsHelper mDBHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        final Button button = findViewById(R.id.opt);
        registerForContextMenu(button);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        isDark = prefs.getBoolean("isDark", false);

        mDBHelper = new DBNewsHelper(this);
        intent = getIntent();

        news_details = findViewById(R.id.news_detail_des);

        try {
            RetrieveFeedTask asyncTask = new RetrieveFeedTask();
            String s = intent.getStringExtra("body");
            asyncTask.execute(s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        news_details.setTextIsSelectable(true);

        getSupportActionBar().setTitle(intent.getStringExtra("title"));
        CoordinatorLayout additem = findViewById(R.id.content_detail);
        textView = findViewById(R.id.words);


        if (isDark) {


            additem.setBackgroundColor(Color.BLACK);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            news_details.setTextColor(Color.WHITE);

        } else {


            additem.setBackgroundColor(Color.WHITE);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            news_details.setTextColor(Color.BLACK);
        }


        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab, R.drawable.ic_content_copy_black_24dp).setLabel("Copy")
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab21, R.drawable.ic_format_underlined_black_24dp).setLabel("Highlight")
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab32, R.drawable.ic_format_underlined_black_24dp).setLabel("Highlight All")
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab34, R.drawable.ic_unhide).setLabel("Unhighlight All")
                        .create()
        );


        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab:
                        copyText();
                        speedDialView.close(); // To close the Speed Dial with animation
                        return true; // false will close it without animation
                    case R.id.fab21:
                        setHighLightedText();
                        speedDialView.close(); // To close the Speed Dial with animation
                        return true;
                    case R.id.fab32:
                        setHighLightedAllText();
                        speedDialView.close(); // To close the Speed Dial with animation
                        return true;
                    case R.id.fab34:
                        setUnHighLightedText();
                        speedDialView.close(); // To close the Speed Dial with animation
                        return true;
                    default:
                        return false;
                }
            }
        });


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
                                    myIntent.putExtra("url", intent.getStringExtra("url"));
                                    myIntent.putExtra("id", intent.getExtras().getInt("id"));
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivityForResult(myIntent, 0);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                return true;
                            case R.id.open:

                                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                                boolean isDark = prefs.getBoolean("isDark", false);
                                if (isDark) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(news_details.this, R.style.DialogurDark);
                                    builder.setTitle(R.string.nn);
                                    builder.setMessage(R.string.deletethis);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DBNewsHelper mDBHelper;

                                            mDBHelper = new DBNewsHelper(news_details.this);
                                            int id = intent.getExtras().getInt("id");
                                            id++;
                                            int b;
                                            ActivityInfo activityInfo = null;
                                            try {
                                                activityInfo = getPackageManager().getActivityInfo(
                                                        getComponentName(), PackageManager.GET_META_DATA);
                                            } catch (PackageManager.NameNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            String title = activityInfo.loadLabel(getPackageManager())
                                                    .toString();

                                            if (intent.getStringExtra("url").equals("empty")) {


                                                b = mDBHelper.deleteDatau(String.valueOf(id), intent.getStringExtra("title"), title
                                                        , intent.getStringExtra("body"));
                                                System.out.println("called");
                                            } else {
                                                b = mDBHelper.deleteData(String.valueOf(id), intent.getStringExtra("title"), title
                                                        , intent.getStringExtra("body"), intent.getStringExtra("url"));
                                            }

                                            if (b == 1) {
                                                Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                                                Intent myIntent = new Intent(news_details.this, news_activity.class);
                                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivityForResult(myIntent, 0);
                                            } else {
                                                Toasty.success(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    builder.setNegativeButton("NO", null);
                                    builder.show();

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(news_details.this, R.style.DialogueLight);
                                    builder.setTitle(R.string.nn);
                                    builder.setMessage(R.string.deletethis);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DBNewsHelper mDBHelper;

                                            mDBHelper = new DBNewsHelper(news_details.this);
                                            int id = intent.getExtras().getInt("id");
                                            id++;
                                            int b;
                                            ActivityInfo activityInfo = null;
                                            try {
                                                activityInfo = getPackageManager().getActivityInfo(
                                                        getComponentName(), PackageManager.GET_META_DATA);
                                            } catch (PackageManager.NameNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            String title = activityInfo.loadLabel(getPackageManager())
                                                    .toString();

                                            if (intent.getStringExtra("url").equals("empty")) {


                                                b = mDBHelper.deleteDatau(String.valueOf(id), intent.getStringExtra("title"), title
                                                        , intent.getStringExtra("body"));
                                                System.out.println("called");
                                            } else {
                                                b = mDBHelper.deleteData(String.valueOf(id), intent.getStringExtra("title"), title
                                                        , intent.getStringExtra("body"), intent.getStringExtra("url"));
                                            }

                                            if (b == 1) {
                                                Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                                                Intent myIntent = new Intent(news_details.this, news_activity.class);
                                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivityForResult(myIntent, 0);
                                            } else {
                                                Toasty.success(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    builder.setNegativeButton("NO", null);
                                    builder.show();
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
                                                Toasty.error(news_details.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onSuccess(@NotNull String s) {
                                                // do your stuff here
                                                Toasty.success(news_details.this, "Success", Toast.LENGTH_SHORT).show();

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


    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder;
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            builder = new AlertDialog.Builder(this, R.style.DialogurDark);
        } else {
            builder = new AlertDialog.Builder(this, R.style.DialogueLight);
        }

        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void setHighLightedAllText() {
        copyText();
        if (!selectedText.isEmpty()) {
            String tvt = news_details.getText().toString();
            int ofe = tvt.indexOf(selectedText);
            Spannable wordToSpan = new SpannableStringBuilder(news_details.getText());
            for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
                ofe = tvt.indexOf(selectedText, ofs);
                if (ofe == -1)
                    break;
                else {
                    // set color here
                    if (isDark) {
                        wordToSpan.setSpan(new BackgroundColorSpan(0xFF4500), ofe, ofe + selectedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        wordToSpan.setSpan(new BackgroundColorSpan(0xFFFF00), ofe, ofe + selectedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    String html = Html.toHtml(wordToSpan);
                    news_details.setText(Html.fromHtml(html));
                    boolean b;
                    int id = intent.getExtras().getInt("id");
                    id++;
                    if (intent.getStringExtra("url").isEmpty()) {
                        b = mDBHelper.updateDatau(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html);
                    } else
                        b = mDBHelper.updateData(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html, intent.getStringExtra("url"));
                    if (b == true) {
                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
            // unregisterForContextMenu(news_details);
        }
    }

    public void setUnHighLightedText() {
        copyText();
        if (!selectedText.isEmpty()) {
            Spannable str = (Spannable) news_details.getText();
            String tvt = news_details.getText().toString();
            int ofe = tvt.indexOf(selectedText);
            Object[] spansToRemove = str.getSpans(ofe, ofe + selectedText.length(), Object.class);
            for (Object span : spansToRemove) {
                if (span instanceof CharacterStyle)
                    str.removeSpan(span);
            }
            String html = Html.toHtml(str);
            news_details.setText(Html.fromHtml(html));
            boolean b;
            int id = intent.getExtras().getInt("id");
            id++;
            if (intent.getStringExtra("url").isEmpty()) {
                b = mDBHelper.updateDatau(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html);
            } else
                b = mDBHelper.updateData(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html, intent.getStringExtra("url"));

            if (b == true) {

                Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            }
        }
        // unregisterForContextMenu(news_details);
    }

    public void setHighLightedText() {
        copyText();
        if (!selectedText.isEmpty()) {
            String tvt = news_details.getText().toString();
            int ofe = tvt.indexOf(selectedText);
            Spannable wordToSpan = new SpannableStringBuilder(news_details.getText());
            if (isDark) {
                wordToSpan.setSpan(new BackgroundColorSpan(0xFF4500), ofe, ofe + selectedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFF00), ofe, ofe + selectedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            String html = Html.toHtml(wordToSpan);
            news_details.setText(Html.fromHtml(html));
            boolean b;
            int id = intent.getExtras().getInt("id");
            id++;
            if (intent.getStringExtra("url").isEmpty()) {
                b = mDBHelper.updateDatau(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html);
            } else
                b = mDBHelper.updateData(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html, intent.getStringExtra("url"));

            if (b == true) {
                Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            }
        }
        // unregisterForContextMenu(news_details);
    }

    private void copyText() {
        selectedText = news_details.getText().toString();
        int startIndex = news_details.getSelectionStart();
        int endIndex = news_details.getSelectionEnd();
        selectedText = selectedText.substring(startIndex, endIndex);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(selectedText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", selectedText);
            clipboard.setPrimaryClip(clip);
        }

        //cm.setText(news_details.getText());
        // unregisterForContextMenu(news_details);
        Toasty.success(news_details.this, selectedText, Toasty.LENGTH_LONG).show();
    }


    class RetrieveFeedTask extends AsyncTask<String, Void, Spanned> {

        private Exception exception;

        protected Spanned doInBackground(String... data) {

            SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);
            String replacedStr = "";

            if (isDark) {

                try {
                    replacedStr = data[0].replaceAll("#FFFF00", "#FF4500");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            } else {

                replacedStr = data[0].replaceAll("#FF4500", "#FFFF00");

            }


            return Html.fromHtml(replacedStr.replace("\n", "<br>"));

        }

        protected void onPostExecute(Spanned text) {


            news_details.setText(text);
            textView.setText("Total Letters : " + news_details.getText().toString().length());


        }
    }

}
