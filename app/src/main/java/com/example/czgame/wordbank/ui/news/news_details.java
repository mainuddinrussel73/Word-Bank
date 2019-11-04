package com.example.czgame.wordbank.ui.news;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.czgame.wordbank.ui.backup_scheudle.NestedWebView;
import com.example.czgame.wordbank.ui.backup_scheudle.PicassoImageGetter;
import com.example.czgame.wordbank.ui.words.MainActivity;
import com.example.czgame.wordbank.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.itextpdf.text.Document;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jgabrielfreitas.core.BlurImageView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.palette.graphics.Palette;
import es.dmoral.toasty.Toasty;

public class news_details extends AppCompatActivity {

    private static final int TRANSLATE = 1;
    private static final String EXTRA_IS_CUSTOM = "is_custom_overflow_menu";
    EditText news_details;
    NestedWebView webView;
    public  static  Activity news_activityD;
    Intent intent;
    Document doc = new Document();
    String selectedText;
    SharedPreferences prefs;
    boolean isDark;
    ScrollView scrollview;
    TextView textView;
    private Toolbar toolbar;
    ViewFlipper vf;
    private boolean isCustomOverflowMenu;
    private DBNewsHelper mDBHelper;
    private void makeEditable(boolean isEditable,EditText et){
        if(isEditable){
           // et.setBackgroundDrawable("Give the textbox background here");//You can store it in some variable and use it over here while making non editable.
            et.setFocusable(true);
            et.setEnabled(true);
            et.setClickable(true);
            et.setFocusableInTouchMode(true);
 //           et.setKeyListener("Set edit text key listener here"); //You can store it in some variable and use it over here while making non editable.
        }else{
            et.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            et.setFocusable(false);
            et.setClickable(false);
            et.setFocusableInTouchMode(false);
            et.setEnabled(false);
            et.setKeyListener(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news_details);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ImageView imageView = collapsingToolbarLayout.findViewById(R.id.image);


        toolbar = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





       // collapsingToolbarLayout.setTitle(itemTitle);

        final Button button = findViewById(R.id.opt);
        registerForContextMenu(button);



        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        isDark = prefs.getBoolean("isDark", false);

        mDBHelper = new DBNewsHelper(this);
        intent = getIntent();


        final boolean[] okkk = {false};

        vf = (ViewFlipper) findViewById( R.id.viewFlipper );

        news_details = vf.findViewById(R.id.news_detail_des);
        RelativeLayout relativeLayout = vf.findViewById(R.id.some);
        webView = (NestedWebView) vf.findViewById(R.id.nestedView1);
        webView.setHorizontalScrollBarEnabled(false);
        //news_details.setTextIsSelectable(false);

        news_details.setFocusable(false);
        news_details.setClickable(true);
        news_details.clearFocus();

        news_activityD = this;


        try {
            RetrieveFeedTask asyncTask = new RetrieveFeedTask();
            String s = intent.getStringExtra("body");
            asyncTask.execute(s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }






        final Bitmap[] bitmap = {null};
        textView = findViewById(R.id.words);



        Picasso.with(this)
                .load(intent.getStringExtra("url"))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // imageView.setBlur(20);
                        okkk[0] = true;

                        imageView.invalidate();
                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                        bitmap[0] = drawable.getBitmap();
                        Bitmap blurredBitmap = BlurBuilder.blur( news_details.this, bitmap[0]);
                        imageView.setImageBitmap(blurredBitmap);
                        if(bitmap[0]==null){
                            System.out.println("null");
                        }
                        collapsingToolbarLayout.setExpandedTitleColor((getDominantColor(bitmap[0])));
                                    // Typeface typeface = ResourcesCompat.getFont(this, R.font.lobs_star);
                        collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_BOLD_ITALIC());


                        System.out.println("herere");
                        textView.setTextColor(getComplimentColor((getDominantColor(bitmap[0]))));


                        Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                        icon.setTint(getComplimentColor(getDominantColor(bitmap[0])));
                        button.setBackground(icon);

                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                        toolbar.getNavigationIcon().setTint(getComplimentColor(getDominantColor(bitmap[0])));
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            System.out.println("go back");
                                            toolbar.getNavigationIcon().setTint(Color.WHITE);
                                            Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                            icon.setTint(Color.WHITE);
                                            button.setBackground(icon);
                                            finish();
                                        }
                                    });

                    }

                    @Override
                    public void onError() {
                        okkk[0] =false;
                        System.out.println("failedddd");
                        //Try again online if cache failed
                        Picasso.with(news_details.this)
                                .load(intent.getStringExtra("url"))
                                .error(R.drawable.news)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        // imageView.setBlur(20);
                                        try {

                                            imageView.invalidate();
                                            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                                            bitmap[0] = drawable.getBitmap();
                                        }catch (Exception e){
                                            System.out.println(e.getMessage());
                                        }

                                        Bitmap blurredBitmap = BlurBuilder.blur( news_details.this, bitmap[0]);
                                        imageView.setImageBitmap(blurredBitmap);
                                        Palette.from(bitmap[0]).generate(new Palette.PaletteAsyncListener() {
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();
                                                if (vibrantSwatch != null) {
                                                    collapsingToolbarLayout.setExpandedTitleColor(((getComplimentColor(vibrantSwatch.getTitleTextColor()))));
                                                    // Typeface typeface = ResourcesCompat.getFont(this, R.font.lobs_star);
                                                    collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_BOLD_ITALIC());


                                                    textView.setTextColor(getComplimentColor(vibrantSwatch.getBodyTextColor()));


                                                    Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                                    icon.setTint(vibrantSwatch.getRgb());
                                                    button.setBackground(icon);

                                                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                                                    toolbar.getNavigationIcon().setTint(vibrantSwatch.getRgb());
                                                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            System.out.println("go back");
                                                            toolbar.getNavigationIcon().setTint(Color.WHITE);
                                                            Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                                            icon.setTint(Color.WHITE);
                                                            button.setBackground(icon);
                                                            finish();
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError() {
                                        collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_BOLD_ITALIC());
                                        Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                        icon.setTint(Color.WHITE);
                                        button.setBackground(icon);
                                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                                        toolbar.getNavigationIcon().setTint(Color.WHITE);
                                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                System.out.println("go back");
                                                toolbar.getNavigationIcon().setTint(Color.WHITE);
                                                Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                                icon.setTint(Color.WHITE);
                                                button.setBackground(icon);
                                                finish();
                                            }
                                        });
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });



        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        getSupportActionBar().setTitle(intent.getStringExtra("title"));






        CoordinatorLayout additem = findViewById(R.id.content_detail);



        if (isDark) {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.BLACK);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl(
                            "javascript:document.body.style.setProperty(\"color\", \"white\");"
                    );
                }
            });

            additem.setBackgroundColor(Color.BLACK);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            news_details.setTextColor(Color.WHITE);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));

        } else {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.WHITE);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl(
                            "javascript:document.body.style.setProperty(\"color\", \"black\");"
                    );
                }
            });
            additem.setBackgroundColor(Color.WHITE);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            news_details.setTextColor(Color.BLACK);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
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
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab35, R.drawable.ic_mode_edit_black_24dp).setLabel("Edit Mode")
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab36, R.drawable.ic_done_black_24dp).setLabel("Disable Edit Mode")
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab37, R.drawable.ic_save_black_24dp).setLabel("Save")
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab38, R.drawable.ic_image_black_24dp).setLabel("Add Image")
                        .create()
        );


        speedDialView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                vf.showNext();
                Toasty.success(news_details.getContext(),vf.getTransitionName(),Toasty.LENGTH_SHORT).show();
                switch (actionItem.getId()) {
                    case R.id.fab:
                        copyText();
                        speedDialView.close(); // To close the Speed Dial with animation
                        return true; // false will close it without animation
                    case R.id.fab21:

                        setHighLightedText();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab32:
                        setHighLightedAllText();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab34:
                        setUnHighLightedText();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab35:
                        news_details.setFocusable(true);
                        news_details.setEnabled(true);
                        news_details.setFocusableInTouchMode(true);
                        news_details.requestFocus();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;

                    case R.id.fab36:
                        //vf.showNext();
                        news_details.setFocusable(false);
                        news_details.setClickable(true);
                        news_details.clearFocus();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab37:
                       // vf.showNext();
                        updatetext();
                        news_details.setFocusable(false);
                        news_details.setClickable(true);
                        news_details.clearFocus();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab38:
                       // vf.showNext();
                        updatetextimage();

                        news_details.setFocusable(false);
                        news_details.setClickable(true);
                        news_details.clearFocus();
                        speedDialView.close();

                        // To close the Speed Dial with animation
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


    public void updatetextimage() {
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData abc = myClipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        selectedText = "<img src = \""+item.getText()+"\" alt=\"Smiley face\" height=\"72\" width=\"100%\">";

                    // Access your context here using YourActivityName.this

            //selectedText = "<img src=\"https://i1.wp.com/jmustafa.com/wp-content/uploads/2018/02/Digital-in-Bangladesh-2018-0000-TINY.png?resize=640%2C360&ssl=1\" alt=\"Smiley face\" height=\"42\" width=\"42\">";

        System.out.println(selectedText);

            Spannable wordToSpan1 = new SpannableStringBuilder(news_details.getText());
        final String[] html = {Html.toHtml(wordToSpan1)};



            boolean b;
            int id = intent.getExtras().getInt("id");
            id++;
            if (intent.getStringExtra("url").isEmpty()) {
                b = mDBHelper.updateDatau(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html[0]);
            } else
                b = mDBHelper.updateData(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), html[0], intent.getStringExtra("url"));
            if (b == true) {
                Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            }


           //this is to get the the cursor position





            int start = 0;

        System.out.println(news_details.getSelectionStart());

            for (int i = news_details.getSelectionStart(); (i = html[0].indexOf("@i#", i + 1)) != -1; i++) {
                System.out.println("iddd"+i);
                start = i+2;

                break;
            }

            System.out.println(html[0].contains("img"));

            html[0] = insertString(html[0],selectedText,start);
            html[0] = html[0].replace("@i#"," ");


            //Spannable wordToSpan2 = new SpannableStringBuilder(news_details.getText());
            //html = Html.toHtml(wordToSpan2);
            System.out.println(html[0].contains("img"));



            PicassoImageGetter imageGetter = new PicassoImageGetter(news_details);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                news_details.setText(Html.fromHtml(html[0].replace("\n", "<br>"),
                        Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
            } else {

                news_details.setText(Html.fromHtml(html[0].replace("\n", "<br>"), imageGetter, null));
            }

       new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();

                String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
                dataq = dataq + "<body>"+ html[0] +"</body></html>";

                String stringToAdd = "width=\"100%\" ";

                // Create a StringBuilder to insert string in the middle of content.
                StringBuilder sb = new StringBuilder(dataq);

                int i = 0;
                int cont = 0;

                // Check for the "src" substring, if it exists, take the index where
                // it appears and insert the stringToAdd there, then increment a counter
                // because the string gets altered and you should sum the length of the inserted substring
                while(i != -1){
                    i = dataq.indexOf("src", i + 1);
                    if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                    ++cont;
                }

                html[0] = sb.toString();


                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadData(html[0], "text/html; charset=utf-8", "UTF-8");

                // Spannable wordToSpan = new SpannableStringBuilder(news_details.getText());
                // html = Html.toHtml(wordToSpan);

                System.out.println(html[0].contains("img"));

                boolean b1;
                int id1 = intent.getExtras().getInt("id");
                id1++;
                if (intent.getStringExtra("url").isEmpty()) {
                    b1 = mDBHelper.updateDatau(String.valueOf(id1), intent.getStringExtra("title"), intent.getStringExtra("title"), html[0]);
                } else
                    b1 = mDBHelper.updateData(String.valueOf(id1), intent.getStringExtra("title"), intent.getStringExtra("title"), html[0], intent.getStringExtra("url"));
                if (b1 == true) {
                    Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                }
                Toast toast = Toast.makeText(news_details.getContext(), "Something", Toast.LENGTH_SHORT);
            }
        });




    }
    public  String insertString(
            String originalString,
            String stringToBeInserted,
            int index)
    {

        // Create a new string
        String newString = new String();

        for (int i = 0; i < originalString.length(); i++) {

            // Insert the original string character
            // into the new string
            newString += originalString.charAt(i);

            if (i == index) {

                // Insert the string to be inserted
                // into the new string
                newString += stringToBeInserted;
            }
        }

        // return the modified String
        return newString;
    }
    public void updatetext() {

           // String tvt = news_details.getText().toString();
            Spannable wordToSpan = new SpannableStringBuilder(news_details.getText());
            String html = Html.toHtml(wordToSpan);



            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

                               String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
            dataq = dataq + "<body>"+html+"</body></html>";

            String stringToAdd = "width=\"100%\" ";

            // Create a StringBuilder to insert string in the middle of content.
            StringBuilder sb = new StringBuilder(dataq);

            int i = 0;
            int cont = 0;

            // Check for the "src" substring, if it exists, take the index where
            // it appears and insert the stringToAdd there, then increment a counter
            // because the string gets altered and you should sum the length of the inserted substring
            while(i != -1){
                i = dataq.indexOf("src", i + 1);
                if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                ++cont;
            }

            html = sb.toString();


            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(html, "text/html; charset=utf-8", "UTF-8");

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


                    PicassoImageGetter imageGetter = new PicassoImageGetter(news_details);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
                    } else {
                        news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
                    }


                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();

                                       String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
                    dataq = dataq + "<body>"+html+"</body></html>";

                    String stringToAdd = "width=\"100%\" ";

                    // Create a StringBuilder to insert string in the middle of content.
                    StringBuilder sb = new StringBuilder(dataq);

                    int i = 0;
                    int cont = 0;

                    // Check for the "src" substring, if it exists, take the index where
                    // it appears and insert the stringToAdd there, then increment a counter
                    // because the string gets altered and you should sum the length of the inserted substring
                    while(i != -1){
                        i = dataq.indexOf("src", i + 1);
                        if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                        ++cont;
                    }

                    html = sb.toString();


                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadData(html, "text/html; charset=utf-8", "UTF-8");

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



            PicassoImageGetter imageGetter = new PicassoImageGetter(news_details);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
            } else {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
            }

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

                               String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
            dataq = dataq + "<body>"+html+"</body></html>";

            String stringToAdd = "width=\"100%\" ";

            // Create a StringBuilder to insert string in the middle of content.
            StringBuilder sb = new StringBuilder(dataq);

            int i = 0;
            int cont = 0;

            // Check for the "src" substring, if it exists, take the index where
            // it appears and insert the stringToAdd there, then increment a counter
            // because the string gets altered and you should sum the length of the inserted substring
            while(i != -1){
                i = dataq.indexOf("src", i + 1);
                if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                ++cont;
            }

            html = sb.toString();


            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(html, "text/html; charset=utf-8", "UTF-8");

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



            PicassoImageGetter imageGetter = new PicassoImageGetter(news_details);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
            } else {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
            }

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

                               String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
            dataq = dataq + "<body>"+html+"</body></html>";

            String stringToAdd = "width=\"100%\" ";

            // Create a StringBuilder to insert string in the middle of content.
            StringBuilder sb = new StringBuilder(dataq);

            int i = 0;
            int cont = 0;

            // Check for the "src" substring, if it exists, take the index where
            // it appears and insert the stringToAdd there, then increment a counter
            // because the string gets altered and you should sum the length of the inserted substring
            while(i != -1){
                i = dataq.indexOf("src", i + 1);
                if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                ++cont;
            }

            html = sb.toString();


            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(html, "text/html; charset=utf-8", "UTF-8");


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




            String stringToAdd = "width=\"100%\" ";

            // Create a StringBuilder to insert string in the middle of content.
            StringBuilder sb = new StringBuilder(replacedStr);

            int i = 0;
            int cont = 0;

            // Check for the "src" substring, if it exists, take the index where
            // it appears and insert the stringToAdd there, then increment a counter
            // because the string gets altered and you should sum the length of the inserted substring
            while(i != -1){
                i = replacedStr.indexOf("src", i + 1);
                if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                ++cont;
            }



            PicassoImageGetter imageGetter = new PicassoImageGetter(news_details);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return Html.fromHtml(replacedStr.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
            } else {
                return Html.fromHtml(replacedStr.replace("\n", "<br>"), imageGetter, null);
            }


        }

        protected void onPostExecute(Spanned text) {






            Spannable wordToSpan1 = new SpannableStringBuilder(text);
            String html = Html.toHtml(wordToSpan1);



            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

                               String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
            dataq = dataq + "<body>"+html+"</body></html>";

            String stringToAdd = "width=\"100%\" ";

            // Create a StringBuilder to insert string in the middle of content.
            StringBuilder sb = new StringBuilder(dataq);

            int i = 0;
            int cont = 0;

            // Check for the "src" substring, if it exists, take the index where
            // it appears and insert the stringToAdd there, then increment a counter
            // because the string gets altered and you should sum the length of the inserted substring
            while(i != -1){
                i = dataq.indexOf("src", i + 1);
                if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                ++cont;
            }

            html = sb.toString();


            news_details.setText(text);

            System.out.println(html);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.loadData(html, "text/html; charset=utf-8", "UTF-8");



           // webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
            news_details.setMovementMethod(LinkMovementMethod.getInstance());
           // webView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText("Total Letters : " + news_details.getText().toString().length());

            vf.showNext();

        }
    }




    class RetrieveSaveTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(Void... voids) {
            updatetextimage();
            return null;
        }

        protected void onPostExecute() {



        }


    }
    public static int getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }

        });
        return swatches.size() > 0 ? swatches.get(0).getRgb() : Color.WHITE;
    }
    public int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }

}

class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
