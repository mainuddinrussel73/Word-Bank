package com.example.czgame.wordbank.ui.promotodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.backup_scheudle.DBDaily;
import com.example.czgame.wordbank.ui.backup_scheudle.Task;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class daily_details extends AppCompatActivity {

    LineChart mChart;
    BarChart barChart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;
    List<Task> taskList = new ArrayList<>();
    List<Task> taskList1 = new ArrayList<>();
    List<Float> taskList2 = new ArrayList<>();
    DBDaily dbDaily;

    String []monthss = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    String []days = {"MON", "TUR", "WED", "THU", "FRI","SAT","SUN"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_daily_details);



        SharedPreferences prefs1 = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);



        Calendar calendar = Calendar.getInstance();

        Date time = Calendar.getInstance().getTime();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        dbDaily = new DBDaily(this);
        //dbDaily.deleteAll();


      //  gen("2018");

        taskList.clear();
        taskList1.clear();
        taskList2.clear();

        int i =1;



        System.out.println(currentWeek +",,,,,"+monthss[currentMonth-1]+",,,,"+ currentYear);
        final Cursor cursor = dbDaily.getAllWeek(String.valueOf(currentWeek),monthss[currentMonth-1],String.valueOf(currentYear));

        System.out.println();
        for (int j = 0; j <7 ; j++) {
            taskList.add(j,new Task(days[j],String.valueOf(currentWeek),monthss[currentMonth-1],String.valueOf(currentYear),0,j));
        }
        System.out.println("task list"+taskList.size());
        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

               // System.out.println();
                Task word = new Task();
                word.setID(i);
                word.setDAY(cursor.getString(1));
                word.setWEEK(cursor.getString(2));
                word.setMONTH(cursor.getString(3));
                word.setYEAR(cursor.getString(4));
                word.setTIME(cursor.getInt(5));
                System.out.println(word.toString());


                if(word.getDAY().equals("MON")){
                    taskList.set(0,word);

                }
                if(word.getDAY().equals("TUE")){
                    taskList.set(1,word);

                }
                if(word.getDAY().equals("WED")){
                    taskList.set(2,word);

                }
                if(word.getDAY().equals("THU")){
                    taskList.set(3,word);

                }
                if(word.getDAY().equals("FRI")){
                    taskList.set(4,word);

                }
                if(word.getDAY().equals("SAT")){
                    taskList.set(5,word);

                }
                if(word.getDAY().equals("SUN")){
                    taskList.set(6,word);

                }



                // maintitle.add(word.WORD);
                // subtitle.add(word.MEANING);

                i++;
            }



        } else {


        }
        System.out.println("task list"+taskList.size());

        i = 1;
        for (int j = 0; j <12 ; j++) {
            taskList2.add(j,new Float(0));
        }
        for (String month:
             monthss) {
            //System.out.println(month);
            final Cursor cursor1 = dbDaily.getAllMonth(month,String.valueOf(currentYear));

            // looping through all rows and adding to list
            if (cursor1.getCount() != 0) {
                // show message
                while (cursor1.moveToNext()) {

                    Task word = new Task();
                    word.setID(i);
                    word.setDAY(cursor1.getString(1));
                    word.setWEEK(cursor1.getString(2));
                    word.setMONTH(cursor1.getString(3));
                    word.setYEAR(cursor1.getString(4));
                    word.setTIME(cursor1.getInt(5));
                    //System.out.println(word.toString());

                    taskList1.add(word);


                    // maintitle.add(word.WORD);
                    // subtitle.add(word.MEANING);

                    i++;
                }



            } else {


            }
           // System.out.println(taskList1.size());
            float totaltime = 0;
            for (int j = 0; j < taskList1.size(); j++) {
                totaltime+=taskList1.get(j).getTIME();
                //System.out.println((float) (totaltime/12.0));

            }
            //System.out.println(totaltime);

            if(taskList1.size()!=0) {
                if(month.equals("JAN")){
                    taskList2.set(0, (totaltime / taskList1.size()));
                }
                if(month.equals("FEB")){
                    taskList2.set(1, (totaltime / taskList1.size()));
                }
                if(month.equals("MAR")){
                    taskList2.set(2, (totaltime / taskList1.size()));
                }
                if(month.equals("APR")){
                    taskList2.set(3, (totaltime / taskList1.size()));
                }
                if(month.equals("MAY")){
                    taskList2.set(4, (totaltime / taskList1.size()));
                }
                if(month.equals("JUN")){
                    taskList2.set(5, (totaltime / taskList1.size()));
                }
                if(month.equals("JUL")){
                    taskList2.set(6, (totaltime / taskList1.size()));
                }
                if(month.equals("AUG")){
                    taskList2.set(7, (totaltime / taskList1.size()));
                }
                if(month.equals("SEP")){
                    taskList2.set(8, (totaltime / taskList1.size()));
                }
                if(month.equals("OCT")){
                    taskList2.set(9, (totaltime / taskList1.size()));
                }
                if(month.equals("NOV")){
                    taskList2.set(10, (totaltime / taskList1.size()));
                }
                if(month.equals("DEC")){
                    taskList2.set(11, (totaltime / taskList1.size()));
                }

            }
           // System.out.println(taskList2.get(10));



            taskList1.clear();
        }
        //System.out.println(taskList2.size());
        if(taskList.size()!=0) {
            barChart = findViewById(R.id.barchart);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(0f);
            final ArrayList<String> xAxisLabeln = new ArrayList<>();
            xAxisLabeln.add(" ");
            xAxisLabeln.add("MON");
            xAxisLabeln.add("TUE");
            xAxisLabeln.add("WEB");
            xAxisLabeln.add("THU");
            xAxisLabeln.add("FRI");
            xAxisLabeln.add("SAT");
            xAxisLabeln.add("SUN");
            xAxisLabeln.add(" ");

            xAxis.setGranularity(0f);
            try{
                xAxis.setValueFormatter(new IAxisValueFormatter(){
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return xAxisLabeln.get((int) value);
                    }
                });
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            xAxis.setAxisMaximum(8f);
            xAxis.setAxisMinimum(0f);

            xAxis.setLabelCount(9,true);


            BarDataSet barDataSet = new BarDataSet(getData(), "Day Activity");
            barDataSet.setBarBorderWidth(0.9f);
            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            BarData barData = new BarData(barDataSet);

            barChart.setDescription(new Description());
            barChart.setMinimumHeight(24);
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.animateXY(5000, 5000);
            barChart.invalidate();
        }
        if(taskList2.size()!=0){

            mChart = findViewById(R.id.chart);
            mChart.setTouchEnabled(true);
            mChart.setPinchZoom(true);
            MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
            mv.setChartView(mChart);
            mChart.setMarker(mv);
            mChart.animateXY(5000,5000);
            renderData();
        }
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout relativeLayout = findViewById(R.id.dailylayout);
        if (isDark) {
            mChart.setBackgroundColor(Color.BLACK);
            mChart.getXAxis().setTextColor(Color.WHITE);
            mChart.getAxisLeft().setTextColor(Color.WHITE);
            mChart.getLegend().setTextColor(Color.WHITE);
            barChart.getXAxis().setTextColor(Color.WHITE);
            barChart.getAxisLeft().setTextColor(Color.WHITE);
            barChart.getLegend().setTextColor(Color.WHITE);
            barChart.setBackgroundColor(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.BLACK);
        } else {
            mChart.setBackgroundColor(Color.WHITE);
            mChart.getXAxis().setTextColor(Color.BLACK);
            mChart.getAxisLeft().setTextColor(Color.BLACK);
            mChart.getLegend().setTextColor(Color.BLACK);
            barChart.getXAxis().setTextColor(Color.BLACK);
            barChart.getAxisLeft().setTextColor(Color.BLACK);
            barChart.getLegend().setTextColor(Color.BLACK);
            barChart.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.WHITE);
        }

    }

    private ArrayList getData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        float f = 0;

        entries.add(new BarEntry(f , 0));
        f++;

        for(Task t:taskList){

            System.out.println(t.toString());
            entries.add(new BarEntry(f,(float)t.getTIME()/2));
            f++;
        }

        entries.add(new BarEntry(f , 0));
        f++;
        return entries;
    }
    public void renderData() {

        XAxis xAxis = mChart.getXAxis();
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("JAN");
        xAxisLabel.add("FEB");
        xAxisLabel.add("MAR");
        xAxisLabel.add("APR");
        xAxisLabel.add("MAY");
        xAxisLabel.add("JUN");
        xAxisLabel.add("JUL");
        xAxisLabel.add("AUG");
        xAxisLabel.add("SEP");
        xAxisLabel.add("OCT");
        xAxisLabel.add("NOV");
        xAxisLabel.add("DEC");

        xAxis.setGranularity(0f);
        try{
        xAxis.setValueFormatter(new IAxisValueFormatter(){
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return xAxisLabel.get((int) value);
            }
        });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        xAxis.enableGridDashedLine(5f, 5f, 0f);
        xAxis.setAxisMaximum(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setLabelCount(12,true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(24f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(5f, 5f, 0f);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        setData();
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();
        float f = 0;
        for(float t:taskList2){
            values.add(new BarEntry(f, t));
            f++;
        }
        System.out.println(taskList2.size());

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Hours Of The Day");
            set1.setDrawIcons(false);
            set1.enableDashedLine(12f, 3f, 0f);
            set1.enableDashedHighlightLine(12f, 3f, 0f);


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout relativeLayout = findViewById(R.id.dailylayout);
        TextView week = findViewById(R.id.weekly);
        TextView month = findViewById(R.id.monthly);
        if (isDark) {
            set1.setColor(Color.RED);
            month.setTextColor(Color.WHITE);
            week.setTextColor(Color.WHITE);
            set1.setCircleColor(Color.RED);
        } else {
            set1.setColor(Color.RED);
            month.setTextColor(Color.BLACK);
            week.setTextColor(Color.BLACK);
            set1.setCircleColor(Color.RED);
        }

            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(4f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{12f, 3f}, 0f));
            set1.setFormSize(15.f);


                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.faded_red);
                set1.setFillDrawable(drawable);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }



    }

    public void  gen(String currentYear){
        Random random = new Random();

        int count=0;
        int jk = 1;
        int y=1;
        for (String month:
                monthss) {
            if(month.equals("JAN")){
                for (; jk <=31 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("FEB")){
                for (; jk <=59 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("MAR")){
                for (; jk <=90 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("APR")){
                for (; jk <=120 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("MAY")){
                for (; jk <=151 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("JUN")){
                for (; jk <=181 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("JUL")){
                for (; jk <=212 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("AUG")){
                for (; jk <=243 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("SEP")){
                for (; jk <=273 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("OCT")){
                for (; jk <=304 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("NOV")){
                for (; jk <=334 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
            if(month.equals("DEC")){
                for (; jk <=365 ; jk++) {
                    count++;
                    dbDaily.insertAll(days[(jk-1)%7],String.valueOf(y),month,String.valueOf(currentYear),random.nextInt(24));
                    if(count==7){
                        count=0;
                        y++;
                    }
                }
            }
        }
    }

}
