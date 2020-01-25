package com.example.czgame.wordbank.ui.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.words.MainActivity;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.BasePieLegendsView;
import com.razerdp.widget.animatedpieview.DefaultCirclePieLegendsView;
import com.razerdp.widget.animatedpieview.DefaultPieLegendsView;
import com.razerdp.widget.animatedpieview.callback.OnPieLegendBindListener;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class quiz_result extends AppCompatActivity {

    public static List<String> wordBuck = new ArrayList<>();
    TextView result;
    TextView mainresult;
    RecyclerView listView;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_quiz_result);
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        AnimatedPieView pieChartView = findViewById(R.id.chart);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        desc = findViewById(R.id.tv_desc);

        List<SimplePieInfo> pieData = new ArrayList<>();
        result = findViewById(R.id.result);
        mainresult = findViewById(R.id.mainresult);
        float p_correct = 0, p_ignored = 0, p_wrong = 0;
        float total = (quiz_page.correct + quiz_page.wrong + quiz_page.ignored);
        System.out.println(quiz_page.correct + "," + quiz_page.wrong + "," + quiz_page.ignored);

        if (quiz_page.correct != 0 && quiz_page.ignored == 0 && quiz_page.wrong == 0) {
            p_correct = (quiz_page.correct / total) * 100;
        }
        if (quiz_page.ignored != 0 && quiz_page.correct == 0 && quiz_page.wrong == 0) {
            p_ignored = (quiz_page.ignored / total) * 100;
        }
        if (quiz_page.wrong != 0 && quiz_page.ignored == 0 && quiz_page.correct == 0) {
            p_wrong = (quiz_page.wrong / total) * 100;
        }
        if (quiz_page.ignored != 0 && quiz_page.correct != 0 && quiz_page.wrong == 0) {
            p_correct = (quiz_page.correct / total) * 100;
            p_ignored = (quiz_page.ignored / total) * 100;
        }
        if (quiz_page.ignored != 0 && quiz_page.wrong != 0 && quiz_page.correct == 0) {
            p_wrong = (quiz_page.wrong / total) * 100;
            p_ignored = (quiz_page.ignored / total) * 100;
        }
        if (quiz_page.correct != 0 && quiz_page.wrong != 0 && quiz_page.ignored == 0) {
            p_correct = (quiz_page.correct / total) * 100;
            p_wrong = (quiz_page.wrong / total) * 100;
        }
        if (quiz_page.correct != 0 && quiz_page.wrong != 0 && quiz_page.ignored != 0) {
            p_correct = (quiz_page.correct / total) * 100;
            p_ignored = (quiz_page.ignored / total) * 100;
            p_wrong = (quiz_page.wrong / total) * 100;
        }
        if (quiz_page.correct == 0 && quiz_page.wrong == 0 && quiz_page.ignored == 0) {
            p_ignored = 100;
            p_correct = 0;
            p_wrong = 0;
        }

        System.out.println(p_correct + "," + p_wrong + "," + p_ignored);

        System.out.println(wordBuck.size());


        HorizontalAdapter adapter = new HorizontalAdapter(wordBuck);

        listView = findViewById(R.id.learnlist);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);


        //listView.setAdapter(adapter);




        if (quiz_page.correct != 0 && quiz_page.ignored == 0 && quiz_page.wrong == 0) {
            pieData.add(new SimplePieInfo(p_correct, Color.GREEN,"Cor"));
        }
        if (quiz_page.ignored != 0 && quiz_page.correct == 0 && quiz_page.wrong == 0) {
            pieData.add(new SimplePieInfo(p_ignored, Color.GRAY,"Ign"));
        }
        if (quiz_page.wrong != 0 && quiz_page.ignored == 0 && quiz_page.correct == 0) {
            pieData.add(new SimplePieInfo(p_wrong, Color.RED,"Wro"));
        }
        if (quiz_page.ignored != 0 && quiz_page.correct != 0 && quiz_page.wrong == 0) {
            pieData.add(new SimplePieInfo(p_ignored, Color.GRAY,"Ign"));
            pieData.add(new SimplePieInfo(p_correct, Color.GREEN,"Cor"));
        }
        if (quiz_page.ignored != 0 && quiz_page.wrong != 0 && quiz_page.correct == 0) {
            pieData.add(new SimplePieInfo(p_ignored, Color.GRAY,"Ign"));
            pieData.add(new SimplePieInfo(p_wrong, Color.RED,"Cor"));
        }
        if (quiz_page.correct != 0 && quiz_page.wrong != 0 && quiz_page.ignored == 0) {
            pieData.add(new SimplePieInfo(p_correct, Color.GREEN,"Cor"));
            pieData.add(new SimplePieInfo(p_wrong, Color.RED , "Cor"));
        }
        if (quiz_page.correct == 0 && quiz_page.ignored == 0 && quiz_page.wrong == 0) {
            pieData.add(new SimplePieInfo(0, Color.GREEN,"Cor"));
            pieData.add(new SimplePieInfo(100, Color.GRAY,"Ign"));
            pieData.add(new SimplePieInfo(0, Color.RED,"Wro"));
        }
        if (quiz_page.correct != 0 && quiz_page.ignored != 0 && quiz_page.wrong != 0) {
            pieData.add(new SimplePieInfo(p_correct, Color.GREEN,"Cor"));
            pieData.add(new SimplePieInfo(p_ignored, Color.GRAY,"Ign"));
            pieData.add(new SimplePieInfo(p_wrong, Color.RED,"Wro"));
        }


       // PieChartData pieChartData = new PieChartData(pieData);
        float res = 0;
        if (quiz_page.correct != 0)
            res = (quiz_page.correct / (total)) * 100;


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);


        RelativeLayout relativeLayout = findViewById(R.id.quiz_res);
        SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs1.getBoolean("isDark", false);
        if (isDark) {

            result.setText("Correct : " + df.format(res) + "%");
                    result.setTextSize(10);
                    result.setTextColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.BLACK);
          //  result.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            mainresult.setTextColor(Color.WHITE);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.list_viewdark));

            if (wordBuck.size() != 0) listView.setAdapter(adapter);
        } else {

            result.setText("Correct : " + df.format(res) + "%");
            result.setTextSize(10);
            result.setTextColor(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.WHITE);
            //result.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.backgroundborder));
            mainresult.setTextColor(Color.BLACK);
            listView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.listview_border));

            if (wordBuck.size() != 0) listView.setAdapter(adapter);
        }


        mainresult.setText(
               "High Score : " + prefs.getInt(
                        "highscore", 0) + "\n"
                        + "Your Score: " + quiz_page.score + "\n"
                        + "Corect : " + quiz_page.correct + "\n"
                        + "Ignored :" + quiz_page.ignored + "\n"
                        + "Wrong : " + quiz_page.wrong
    );
//        pieChartView.setPieChartData(pieChartData);

        config.startAngle(0.9224089f)
                .addDatas(pieData)
                /**
                 * not done below!
                 */
                .selectListener(new OnPieSelectListener() {
                    @Override
                    public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {

                    }
                })
                .drawText(true)
                .duration(1200)
                .textSize(26)
                .focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA)
                .textGravity(AnimatedPieViewConfig.ABOVE)
                .interpolator(new DecelerateInterpolator())
                .legendsWith(findViewById(R.id.ll_legends), new OnPieLegendBindListener<BasePieLegendsView>() {
                    @Override
                    public BasePieLegendsView onCreateLegendView(int position, IPieInfo info) {
                        return position % 2 == 0 ?
                                DefaultPieLegendsView.newInstance(quiz_result.this)
                                : DefaultCirclePieLegendsView.newInstance(quiz_result.this);
                    }

                    @Override
                    public boolean onAddView(ViewGroup parent, BasePieLegendsView view) {
                        return false;
                    }
                });
        pieChartView.applyConfig(config);

        pieChartView.start();

        if (prefs.getInt("highscore", 0) > MainActivity.score) {


            Alerter.create(this)
                    .setTitle("CONGRATS!")
                    .setText("New highest score...")
                    .setIcon(R.drawable.ic_insert_emoticon_black_24dp)
                    .setBackgroundColorRes(R.color.colorPrimary)
                    .setDuration(5000)
                    .enableSwipeToDismiss() //seems to not work well with OnClickListener
                    .show();


        }







        Button button = findViewById(R.id.goback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordBuck = new ArrayList<>();
                quiz_page.ignored = 0;
                quiz_page.correct = 0;
                quiz_page.wrong = 0;
                Quiz_confirm.questioncount = 10;
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
    private Bitmap resourceToBitmap(int resid) {
        Drawable drawable = getResources().getDrawable(resid);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);

            return bitmap;
        }
    }

    private int getColor(String colorStr) {
        if (TextUtils.isEmpty(colorStr)) return Color.BLACK;
        if (!colorStr.startsWith("#")) colorStr = "#" + colorStr;
        return Color.parseColor(colorStr);
    }
}
