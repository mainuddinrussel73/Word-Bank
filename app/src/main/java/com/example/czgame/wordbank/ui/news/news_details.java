package com.example.czgame.wordbank.ui.news;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.developer.kalert.KAlertDialog;
import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.backup_scheudle.NestedWebView;
import com.example.czgame.wordbank.ui.backup_scheudle.PicassoImageGetter;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.jaouan.revealator.Revealator;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
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
  //  ProgressDialog progressBar;
    KAlertDialog kAlertDialog;
    KAlertDialog pDialog;
    private View theAwesomeView;
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()){
            // Hide keyboard
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } else {
            // Show keyboard
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
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
    public  String insertString(
            String originalString,
            String stringToBeInserted,
            int index)
    {

        // Create a new string
        String newString = "";

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

        String dataq = html;

        String stringToAdd = "width=\"100%\" ";

        // Create a StringBuilder to insert string in the middle of content.
        StringBuilder sb = new StringBuilder(dataq);

        int i = 0;
        int cont = 0;

        // Check for the "src" substring, if it exists, take the index where
        // it appears and insert the stringToAdd there, then increment a counter
        // because the string gets altered and you should sum the length of the inserted substring
        while (i != -1) {
            i = dataq.indexOf("src", i + 1);
            if (i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd);
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
        String html = Html.toHtml(wordToSpan1);



        //this is to get the the cursor position


        int start = 0;

        System.out.println(news_details.getSelectionStart());

        for (int i = news_details.getSelectionStart(); (i = html.indexOf("@i#", i + 1)) != -1; i++) {
            System.out.println("iddd" + i);
            start = i + 2;
            break;
        }

        System.out.println(html.contains("img"));

        StringBuilder builder = new StringBuilder(html);
        builder.replace( start-2,start+1,selectedText);
        html = builder.toString();
        // html = html.replace("@i#", " ");


        //Spannable wordToSpan2 = new SpannableStringBuilder(news_details.getText());
        //html = Html.toHtml(wordToSpan2);
        System.out.println(html.contains("img"));


        PicassoImageGetter imageGetter = new PicassoImageGetter(webView,news_details.this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),
                    Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
        } else {

            news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
        }



        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        String dataq = html;

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

        // Spannable wordToSpan = new SpannableStringBuilder(news_details.getText());
        // html = Html.toHtml(wordToSpan);

        System.out.println(html.contains("img"));

        boolean b1;
        int id1 = intent.getExtras().getInt("id");
        id1++;
        if (intent.getStringExtra("url").isEmpty()) {
            b1 = mDBHelper.updateDatau(String.valueOf(id1), intent.getStringExtra("title"), intent.getStringExtra("title"), html);
        } else
            b1 = mDBHelper.updateData(String.valueOf(id1), intent.getStringExtra("title"), intent.getStringExtra("title"), html, intent.getStringExtra("url"));
        if (b1 == true) {
            Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
        } else {
            Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
        }
        Toast toast = Toast.makeText(news_details.getContext(), "Something", Toast.LENGTH_SHORT);





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


                    PicassoImageGetter imageGetter = new PicassoImageGetter(webView,news_details.this);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
                    } else {
                        news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
                    }


                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();

                    String dataq = "<html>" +
                            "<head>" +
                            "<meta name=\"viewport\"\"content=\"width=" + width + " height=" + width + ", initial-scale=1 \" />" +
                            "<style type=\"text/css\">" +
                            "@font-face {font-family: 'kont' ;src: url('fonts/belgrano.ttf');}" +
                            "body {font-family: 'kont' , Verdana ;font-size: medium;text-align: justify;}" +
                            "p { margin-bottom: 10em;  line-height: 32px;  "+
                            "  margin-bottom: 30px; }"+
                            "</style>" +
                            "</head>";

                    dataq = dataq  + "<body>"+"<h1>"+intent.getStringExtra("title")+"</h1></br>" +html+"</body></html>";

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
            Spannable str = news_details.getText();
            String tvt = news_details.getText().toString();
            int ofe = tvt.indexOf(selectedText);
            Object[] spansToRemove = str.getSpans(ofe, ofe + selectedText.length(), Object.class);
            for (Object span : spansToRemove) {
                if (span instanceof CharacterStyle)
                    str.removeSpan(span);
            }
            String html = Html.toHtml(str);



            PicassoImageGetter imageGetter = new PicassoImageGetter(webView,news_details.this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
            } else {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
            }

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            String dataq = "<html>" +
                    "<head>" +
                    "<meta name=\"viewport\"\"content=\"width=" + width + " height=" + width + ", initial-scale=1 \" />" +
                    "<style type=\"text/css\">" +
                    "@font-face {font-family: 'kont' ;src: url('fonts/belgrano.ttf');}" +
                    "body {font-family: 'kont' , Verdana ;font-size: medium;text-align: justify;}" +
                    "p { margin-bottom: 10em;  line-height: 32px;  "+
                    "  margin-bottom: 30px; }"+
                    "</style>" +
                    "</head>";

            dataq = dataq + "<body>"+"<h1>"+intent.getStringExtra("title")+"</h1></br>" +html+"</body></html>";

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



            PicassoImageGetter imageGetter = new PicassoImageGetter(webView,news_details.this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null));
            } else {
                news_details.setText(Html.fromHtml(html.replace("\n", "<br>"), imageGetter, null));
            }

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            String dataq = "<html>" +
                    "<head>" +
                    "<meta name=\"viewport\"\"content=\"width=" + width + " height=" + width + ", initial-scale=1 \" />" +
                    "<style type=\"text/css\">" +
                    "@font-face {font-family: 'kont' ;src: url('fonts/belgrano.ttf');}" +
                    "body {font-family: 'kont' , Verdana ;font-size: medium;text-align: justify;}" +
                    "p { margin-bottom: 10em;  line-height: 32px;  "+
                    "  margin-bottom: 30px; }"+
                    "</style>" +
                    "</head>";

            dataq = dataq + "<body>"+"<h1>"+intent.getStringExtra("title")+"</h1></br>" +html+"</body></html>";

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

    @Override
    public void onDestroy(){
        super.onDestroy();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news_details);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        RoundedImageView imageView = collapsingToolbarLayout.findViewById(R.id.image);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // collapsingToolbarLayout.setTitle(itemTitle);

     //   final Button button = findViewById(.id.opt);
       // registerForContextMenu(button);

        kAlertDialog  =  new KAlertDialog(this, KAlertDialog.WARNING_TYPE);


        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        isDark = prefs.getBoolean("isDark", false);

        mDBHelper = new DBNewsHelper(this);
        intent = getIntent();



        final boolean[] okkk = {false};

        vf = findViewById(R.id.viewFlipper);



        news_details = vf.findViewById(R.id.news_detail_des);
        RelativeLayout relativeLayout = vf.findViewById(R.id.some);
        webView = vf.findViewById(R.id.nestedView1);
        webView.setHorizontalScrollBarEnabled(false);
        //news_details.setTextIsSelectable(false);

        news_details.setFocusable(false);
        news_details.setClickable(true);
        news_details.clearFocus();

        news_activityD = this;



        try {
            // progressBar.setVisibility(View.VISIBLE);

            RetrieveFeedTask asyncTask = new RetrieveFeedTask();
            String s = intent.getStringExtra("body");
            asyncTask.execute(s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }





        FloatingActionButton fab = findViewById(R.id.fab16);
        theAwesomeView = findViewById(R.id.the_awesome_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                Revealator.reveal(theAwesomeView)
                        .from(fab)
                        .withCurvedTranslation()
                        .withChildsAnimation()
                        //.withDelayBetweenChildAnimation(...)
                        //.withChildAnimationDuration(...)
                        //.withTranslateDuration(...)
                        //.withRevealDuration(...)
                        //.withEndAction(...)
                        .start();

            }
        });

        TextView textViewa = findViewById(R.id.texbt);
        LinearLayout linearLayoutm = findViewById(R.id.mainlay);
        if(isDark){
            theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.bbbbb));
            textViewa.setTextColor(Color.WHITE);
            linearLayoutm.setBackground(ContextCompat.getDrawable(news_details.this,R.drawable.rounded_corner_ask));

        }else{
            theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.wwwww));
            textViewa.setTextColor(Color.BLACK);
            linearLayoutm.setBackground(ContextCompat.getDrawable(news_details.this,R.drawable.rounded_corner));
        }

        final View theWonderfulButton = findViewById(R.id.the_wonderful_button);
        theWonderfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .to(fab)
                        .withCurvedTranslation()
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //fab.show();

                                try {
                                    Intent myIntent = new Intent(news_details.this, news_update.class);
                                    //String s = view.findViewById(R.id.subtitle).toString();
                                    //String s = (String) parent.getI;
                                    toolbar.getNavigationIcon().setTint(Color.WHITE);
                                    myIntent.putExtra("title", intent.getStringExtra("title"));
                                    myIntent.putExtra("body", intent.getStringExtra("body"));
                                    myIntent.putExtra("url", intent.getStringExtra("url"));
                                    myIntent.putExtra("id", intent.getExtras().getInt("id"));
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivityForResult(myIntent, 0);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }

                                if(isDark){
                                    theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.bbb));
                                }else{
                                    theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.material_white));
                                }

                                fab.setVisibility(View.VISIBLE);
                                Snackbar.make(fab, "What a beautiful snackbar !", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .start();

            }
        });

        final View theWonderfulButton1 = findViewById(R.id.the_wonderful_button1);
        theWonderfulButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .to(fab)
                        .withCurvedTranslation()
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //fab.show();
                                SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                                boolean isDark = prefs.getBoolean("isDark", false);
                                if (isDark) {

                                    KAlertDialog.DARK_STYLE = true;
                                    kAlertDialog =  new KAlertDialog(news_details.this, KAlertDialog.WARNING_TYPE);
                                    kAlertDialog
                                            .setTitleText("Are you sure?")
                                            .setContentText("Won't be able to recover this file!")
                                            .setCancelText("No,cancel!")
                                            .setConfirmText("Yes,delete!")
                                            .showCancelButton(true)
                                            .confirmButtonColor(R.drawable.btn_style)
                                            .cancelButtonColor(R.drawable.btn_style)
                                            .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                }
                                            })
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog  sDialog) {

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

                                                        sDialog
                                                                .setTitleText("Deleted!")
                                                                .setContentText("Your item has been deleted!")
                                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                                                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(news_details.this, news_activity.class);
                                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivityForResult(myIntent, 0);
                                                    } else {
                                                        Toasty.success(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    //  kAlertDialog.setCustomView(popupView);

                                    kAlertDialog.show();


                                } else {
                                    KAlertDialog.DARK_STYLE = false;
                                    kAlertDialog =  new KAlertDialog(news_details.this, KAlertDialog.WARNING_TYPE);
                                    kAlertDialog
                                            .setTitleText("Are you sure?")
                                            .setContentText("Won't be able to recover this file!")
                                            .setCancelText("No,cancel!")
                                            .setConfirmText("Yes,delete!")
                                            .showCancelButton(true)
                                            .confirmButtonColor(R.drawable.btn_style)
                                            .cancelButtonColor(R.drawable.btn_style)
                                            .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                }
                                            })
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog  sDialog) {

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

                                                        sDialog
                                                                .setTitleText("Deleted!")
                                                                .setContentText("Your item has been deleted!")
                                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                                                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(news_details.this, news_activity.class);
                                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivityForResult(myIntent, 0);
                                                    } else {
                                                        Toasty.success(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    //  kAlertDialog.setCustomView(popupView);

                                    kAlertDialog.show();
                                }

                                if(isDark){
                                    theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.bbb));
                                }else{
                                    theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.material_white));
                                }
                                fab.setVisibility(View.VISIBLE);
                                Snackbar.make(fab, "What a beautiful snackbar !", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .start();

            }
        });


        final View theWonderfulButton2 = findViewById(R.id.the_wonderful_button2);
        theWonderfulButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .to(fab)
                        .withCurvedTranslation()
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
                        .withEndAction(new Runnable() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void run() {
                                //fab.show();

                                new CreatePdf(news_details.this)
                                        .setPdfName(intent.getStringExtra("title"))
                                        .openPrintDialog(true)
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

                                if(isDark){
                                    theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.bbb));
                                }else{
                                    theAwesomeView.setBackgroundColor(ContextCompat.getColor(news_details.this,R.color.material_white));
                                }
                                fab.setVisibility(View.VISIBLE);
                                Snackbar.make(fab, "What a beautiful snackbar !", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .start();

            }
        });

        final View theWonderfulButton3 = findViewById(R.id.the_wonderful_button3);
        theWonderfulButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .to(fab)
                        .withCurvedTranslation()
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
                        .withEndAction(new Runnable() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void run() {
                                //fab.show();




                                fab.setVisibility(View.VISIBLE);
                                Snackbar.make(fab, "What a beautiful snackbar !", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .start();

            }
        });

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
                      //  button.setBackground(icon);

                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                        toolbar.getNavigationIcon().setTint(getComplimentColor(getDominantColor(bitmap[0])));
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println("go back");
                                toolbar.getNavigationIcon().setTint(Color.WHITE);
                                Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                icon.setTint(Color.WHITE);
                          //      button.setBackground(icon);
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
                                                  //  button.setBackground(icon);

                                                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                                                    toolbar.getNavigationIcon().setTint(vibrantSwatch.getRgb());
                                                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            System.out.println("go back");
                                                            toolbar.getNavigationIcon().setTint(Color.WHITE);
                                                            Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                                            icon.setTint(Color.WHITE);
                                                        //    button.setBackground(icon);
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
                                     //   button.setBackground(icon);
                                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
                                        toolbar.getNavigationIcon().setTint(Color.WHITE);
                                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                System.out.println("go back");
                                                toolbar.getNavigationIcon().setTint(Color.WHITE);
                                                Drawable icon = getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);
                                                icon.setTint(Color.WHITE);
                                           //     button.setBackground(icon);
                                                Intent myIntent = new Intent(news_details.getContext(), news_activity.class);
                                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivityForResult(myIntent, 0);
                                            }
                                        });
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });



    //
        //    progressBar.setProgress(1);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
       // getSupportActionBar().setTitle(intent.getStringExtra("title"));






        CoordinatorLayout additem = findViewById(R.id.content_detail);



     //   pDialog = new KAlertDialog(news_details.this, KAlertDialog.PROGRESS_TYPE)
               // .setTitleText("Loading");

        if (isDark) {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setBackgroundResource(R.drawable.background_card_dark);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl(
                            "javascript:document.body.style.setProperty(\"color\", \"white\");"
                    );
                }
            });

            additem.setBackgroundColor(Color.BLACK);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_card_dark));
            news_details.setTextColor(Color.WHITE);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_card_dark));

        } else {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setBackgroundResource(R.drawable.background_card);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl(
                            "javascript:document.body.style.setProperty(\"color\", \"black\");"
                    );
                }
            });
            additem.setBackgroundColor(Color.WHITE);
            news_details.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_card));
            news_details.setTextColor(Color.BLACK);
            relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_card));
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
                        hideKeyboard(news_details.this);
                        news_details.setFocusable(false);
                        setHighLightedText();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab32:
                        hideKeyboard(news_details.this);
                        news_details.setFocusable(false);
                        setHighLightedAllText();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab34:
                        hideKeyboard(news_details.this);
                        news_details.setFocusable(false);
                        setUnHighLightedText();
                        speedDialView.close();

                        // To close the Speed Dial with animation
                        return true;
                    case R.id.fab35:
                        hideKeyboard(news_details.this);
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







        webView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if(webView!=null){
                            if (!webView.canScrollVertically(1)){
                                //scroll view is at bottom

                                boolean b;
                                int id = intent.getExtras().getInt("id");
                                id++;
                                if (intent.getStringExtra("url").isEmpty()) {
                                    b = mDBHelper.updateDataR(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"),1);
                                } else
                                    b = mDBHelper.updateDataR(String.valueOf(id), intent.getStringExtra("title"), intent.getStringExtra("title"), 1);

                                if (b == true) {
                                    Toasty.success(getApplicationContext(), "Done reading whole news.", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                //scroll view is not at bottom
                            }
                        }
                    }
                });




    }
    class RetrieveFeedTask extends AsyncTask<String, Integer, Spanned> {


        private Exception exception;
        int ii = 0;
        private int i = -1;

        RetrieveFeedTask(){
            KAlertDialog.DARK_STYLE = isDark;

            pDialog = new KAlertDialog(news_details.this, KAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading");



            pDialog.show();
            pDialog.setCancelable(false);







        }


        protected Spanned doInBackground(String... data) {



            SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark", false);
            String replacedStr = "";

            ii = 2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(ii);

            if (isDark) {

                try {
                    replacedStr = data[0].replaceAll("#FFFF00", "#FF4500");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            } else {

                replacedStr = data[0].replaceAll("#FF4500", "#FFFF00");

            }



            ii = 4;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(ii);


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

            ii = 8;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(ii);


            PicassoImageGetter imageGetter = new PicassoImageGetter(webView,news_details.this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ii = 10;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(ii);

                return Html.fromHtml(replacedStr.replace("\n", "<br>"),Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
            } else {
                return Html.fromHtml(replacedStr.replace("\n", "<br>"), imageGetter, null);
            }


        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            //txt.setText("Running..."+ values[0]);
         //   progressBar.setProgress(values[0]);

            i++;
            switch (i){
                case 0:
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                            (news_details.this,R.color.blue_btn_bg_color));
                    pDialog.setTitleText("Loading .. "+(values[0]*10)+"%");
                    break;
                case 1:
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                            (news_details.this,R.color.material_deep_teal_50));
                    pDialog.setTitleText("Loading .. "+(values[0]*10)+"%");
                    break;
                case 2:
                case 6:
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                            (news_details.this,R.color.success_stroke_color));
                    pDialog.setTitleText("Loading .. "+(values[0]*10)+"%");
                    break;
                case 3:
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                            (news_details.this,R.color.material_deep_teal_20));
                    pDialog.setTitleText("Loading .. "+(values[0]*10)+"%");
                    break;
                case 4:
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                            (news_details.this,R.color.material_blue_grey_80));
                    pDialog.setTitleText("Loading .. "+(values[0]*10)+"%");
                    break;
                case 5:
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                            (news_details.this,R.color.warning_stroke_color));
                    pDialog.setTitleText("Loading .. "+(values[0]*10)+"%");
                    break;
            }




        }

        protected void onPostExecute(Spanned text) {






            Spannable wordToSpan1 = new SpannableStringBuilder(text);
            String html = Html.toHtml(wordToSpan1);



            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            String dataq = "<html>" +
                    "<head>" +
                    "<meta name=\"viewport\"\"content=\"width=" + width + " height=" + width + ", initial-scale=1 \" />" +
                    "<style type=\"text/css\">" +
                    "@font-face {font-family: 'kont' ;src: url('fonts/belgrano.ttf');}" +
                    "body {font-family: 'kont' , Verdana ;font-size: medium;text-align: justify;}" +
                    "p { margin-bottom: 10em;  line-height: 32px;  "+
                    "  margin-bottom: 30px; }"+
                    "</style>" +
                    "</head>";

            dataq = dataq + "<body>"+"<h1>"+intent.getStringExtra("title")+"</h1></br>" +html+"</body></html>";

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

            System.out.println("tsa"+html);


            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.loadUrl("javascript:(function() {document.getElementsByTagName(\"body\")[0].style.background = \"red\";})()");
            webView.loadDataWithBaseURL("file:///android_asset/",html, "text/html","utf-8",null);


            // webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
            news_details.setMovementMethod(LinkMovementMethod.getInstance());
            // webView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText("Total Letters : " + news_details.getText().toString().length());

            vf.showNext();
//            progressBar.cancel();
            i = -1;
            pDialog.setTitleText("Success!")
                    .setConfirmText("OK")
                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);

        }
    }


    @Override
    public void onBackPressed(){
        toolbar.getNavigationIcon().setTint(Color.WHITE);
        Intent myIntent = new Intent(news_details.getContext(), news_activity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);

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
