package com.example.mainuddin.myapplication34.ui.promotodo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yydcdut.sdlv.Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;

public class Promotodo_activity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener,
        SlideAndDragListView.OnDragDropListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {


    public static List<promotododata> promotododataList = new ArrayList<>();
    private Menu mMenu;
    public static SlideAndDragListView mListView;
    private Toast mToast;
    private promotododata mDraggedEntity;
    ExtendedEditText tfb;
    Button button;
    public static DBproHandle mDBHelper;
    promotododata currenttask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotodo_activity);

        FloatingActionButton fab = findViewById(R.id.fab);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tfb = findViewById(R.id.text_field_boxes2);
        button = findViewById(R.id.dark_button);


        mDBHelper = new DBproHandle(this);

        initList();
        initMenu();
        try {
            initUiAndListener();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        mToast = Toast.makeText(Promotodo_activity.this, "", Toast.LENGTH_SHORT);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tfb.getText().toString().isEmpty() || tfb.getText().toString().trim().length()<=0){
                    Toasty.error(getApplicationContext(),"No input.", Toast.LENGTH_SHORT).show();
                }
                else{
                boolean b = mDBHelper.insertData(tfb.getText().toString());
                if (b == true) {
                    Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                    initList();
                    initMenu();
                    initUiAndListener();
                } else {
                    Toasty.error(getApplicationContext(), "opps.", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });

        final LinearLayout additem = findViewById(R.id.promotodo_activity);
        RelativeLayout relativeLayout = findViewById(R.id.main_view);
        RelativeLayout relativeLayout1 = relativeLayout.findViewById(R.id.borderss);
        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark",false);
        if(isDark){
            mListView.setBackgroundColor(Color.BLACK);
            relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            additem.setBackgroundColor(Color.BLACK);
            relativeLayout.setBackgroundColor(Color.BLACK);
            tfb.setBackgroundColor(Color.BLACK);
            tfb.setTextColor(Color.WHITE);
            tfb.setHintTextColor(Color.rgb(185,185,185));
        }else{
            mListView.setBackgroundColor(Color.WHITE);
            relativeLayout1.setBackground(ContextCompat.getDrawable(this, R.drawable.listview_border));
            additem.setBackgroundColor(Color.WHITE);
            relativeLayout.setBackgroundColor(Color.WHITE);
            tfb.setBackgroundColor(Color.WHITE);
            tfb.setTextColor(Color.BLACK);
            tfb.setHintTextColor(Color.BLACK);
        }



    }


    public void initList() {

        promotododataList.clear();

        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                promotododata word = new promotododata();
                word.setID(Integer.parseInt(cursor.getString(0)+1));
                word.setTitle(cursor.getString(1));
                word.setNum_of_promotodo(cursor.getInt(2));
                word.setCompleted_promotodo(cursor.getInt(3));
                word.setIsrepeat(cursor.getInt(4));
                word.setDue_date(cursor.getString(5));

                promotododataList.add(word);
                System.out.println(word.NUM);


            }


        } else {

            Toasty.info(Promotodo_activity.this, "Nothing to show.", Toasty.LENGTH_LONG).show();
        }

        // mAdapter = new proadapter(promotododataList,this);


    }

    public void initMenu() {
        mMenu = new Menu(true);
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width) * 2)
                .setBackground(Utils.getDrawable(this, R.drawable.btn_left0))
                .setText("One")
                .setTextColor(Color.GRAY)
                .setTextSize(14)
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_left1))
                .setText("Two")
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width) + 30)
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.ic_cancel_black_24dp))
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.ic_timer_black_24dp))
                .build());
    }


    public void initUiAndListener() {
        mListView = (SlideAndDragListView) findViewById(R.id.lv_edit);
        mListView.setMenu(mMenu);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        mListView.setOnDragDropListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnSlideListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemLongClickListener(this);
        //mListView.setOnItemScrollBackListener((SlideAndDragListView.OnItemScrollBackListener) this);
    }

    @Override
    public void onDragViewStart(int beginPosition) {
        mDraggedEntity = promotododataList.get(beginPosition);
        toast("onDragViewStart   beginPosition--->" + beginPosition);
    }

    @Override
    public void onDragDropViewMoved(int fromPosition, int toPosition) {

        promotododata applicationInfo = promotododataList.remove(fromPosition);
        applicationInfo.ID=toPosition;
        promotododataList.add(toPosition, applicationInfo);
        toast("onDragDropViewMoved   fromPosition--->" + fromPosition + "  toPosition-->" + toPosition);
    }

    @Override
    public void onDragViewDown(int finalPosition) {
        promotododataList.set(finalPosition, mDraggedEntity);
        mDraggedEntity.ID = finalPosition;
        toast("onDragViewDown   finalPosition--->" + finalPosition);
    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {
        toast("onSlideOpen   position--->" + position + "  direction--->" + direction);
        //cvh.findViewById().imgLogo2.setImageDrawable(Utils.getDrawable(Promotodo_activity.this, R.drawable.ic_keyboard_arrow_left_black_24dp));
    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {
        toast("onSlideClose   position--->" + position + "  direction--->" + direction);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        //toast("onMenuItemClick   itemPosition--->" + itemPosition + "  buttonPosition-->" + buttonPosition + "  direction-->" + direction);
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_NOTHING;
                    case 1:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 1:
                        Intent myIntent = new Intent(v.getContext(), promodetail.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("ID", itemPosition);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        return Menu.ITEM_SCROLL_BACK;
                    case 0:
                        return ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {
        //mDBHelper = new DBproHandle(Promotodo_activity.this);
        int k = promotododataList.get(position - mListView.getHeaderViewsCount()).ISREPEAT;
        String s = promotododataList.get(position - mListView.getHeaderViewsCount()).TITLE;
        int n = promotododataList.get(position - mListView.getHeaderViewsCount()).NUM;
        String d =  promotododataList.get(position - Promotodo_activity.mListView.getHeaderViewsCount()).getDue_date();

        System.out.println(k + "isrepeat");
        promotododata promotododata = promotododataList.get(position - mListView.getHeaderViewsCount());
        int i = mDBHelper.deleteData(promotododataList.get(position - mListView.getHeaderViewsCount()).getTitle());
        promotododataList.remove(position - mListView.getHeaderViewsCount());
        if (k == 1) {
            LocalDate parsedDate = LocalDate.parse(d); //Parse date from String
            LocalDate addedDate = parsedDate.plusDays(1);
            String str = addedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            boolean b = mDBHelper.insertIsreat(s, k, n,str);
            if (b == true) {
                Toasty.success(getApplicationContext(), "Repeated.", Toast.LENGTH_SHORT).show();
                Toasty.success(view.getContext(), "Repeated.", Toast.LENGTH_SHORT).show();
                promotododata.setDue_date(str);
                promotododata.setNum_of_promotodo(0);
                promotododataList.add(promotododataList.size(), promotododata);
                mAdapter.notifyDataSetChanged();
            } else {
                Toasty.error(getApplicationContext(), "opps.", Toast.LENGTH_SHORT).show();
            }
        }
        //mAdapter = new proadapter(promotododataList,this);
        mListView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        if (i == 1) {
            Toasty.success(view.getContext(), "Task Completed!", Toasty.LENGTH_LONG).show();
        } else {
            Toasty.error(view.getContext(), "Task not Completed!", Toasty.LENGTH_LONG).show();
        }
        //toast("onItemDeleteAnimationFinished   position--->" + position);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toast("onItemClick   position--->" + position);

        Intent myIntent = new Intent(view.getContext(), promodetail.class);
        //String s = view.findViewById(R.id.subtitle).toString();
        //String s = (String) parent.getI;
        myIntent.putExtra("ID", position);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);


    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        toast("onItemLongClick   position--->" + position);
        Object o = view.getTag();
        if (o != null && o instanceof Integer) {
            mListView.startDrag(((Integer) o).intValue());

        }
        return false;
        //onDragViewStart(position);
        //return false;
    }

    private void toast(String toast) {
        mToast.setText(toast);
        mToast.show();
    }

    private BaseAdapter mAdapter = new BaseAdapter() {


        CustomViewHolder cvh;


        @Override
        public int getCount() {
            return promotododataList.size();
        }

        @Override
        public Object getItem(int position) {
            return promotododataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return promotododataList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View rowView, ViewGroup parent) {

            promotododata item = (promotododata) this.getItem(position);

            if (rowView == null) {
                cvh = new CustomViewHolder();
                rowView = LayoutInflater.from(Promotodo_activity.this).inflate(R.layout.item_custom, null);
                //cvh = new CustomViewHolder();
                // rowView = inflater.inflate(R.layout.item_custom, null,true);
                cvh.imgLogo = (CheckBox) rowView.findViewById(R.id.img_item_edit);
                cvh.imgLogo3 =  rowView.findViewById(R.id.loopp);
                cvh.txtName = (TextView) rowView.findViewById(R.id.txt_item_edit);
                cvh.date = rowView.findViewById(R.id.dates);
                cvh.imgLogo2 = (ImageView) rowView.findViewById(R.id.img_item_edit2);
                cvh.circularProgressBar = rowView.findViewById(R.id.circularProgressBar);
                cvh.circularProgressBar.setProgressMax(18000);
                cvh.circularProgressBar1 = rowView.findViewById(R.id.circularProgressBar1);
                cvh.circularProgressBar1.setProgressMax(18000);
                cvh.circularProgressBar2 = rowView.findViewById(R.id.circularProgressBar2);
                cvh.circularProgressBar2.setProgressMax(18000);
                cvh.circularProgressBar3 = rowView.findViewById(R.id.circularProgressBar3);
                cvh.circularProgressBar3.setProgressMax(18000);
                cvh.imgLogo2.setOnTouchListener(mOnTouchListener);
                cvh.imgLogo.setOnClickListener(mOnTouchListener1);
                rowView.setTag(cvh);
            } else {
                cvh = (CustomViewHolder) rowView.getTag();
            }

            cvh.txtName.setText(item.getTitle());
            cvh.date.setText(item.getDue_date());
            Drawable d ;
            SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("isDark",false);
            cvh.imgLogo.setButtonDrawable(Promotodo_activity.this.getResources().getDrawable(
                    R.drawable.radio_btn));
            if(isDark){
                cvh.imgLogo2.setImageDrawable(Utils.getDrawable(rowView.getContext(), R.drawable.ic_keyboard_arrow_left_white_24dp));
                cvh.txtName.setTextColor(Color.WHITE);
                cvh.date.setTextColor(Color.WHITE);
                //cvh.imgLogo.setB(Color.WHITE);
                if(item.isIsrepeat()==1){

                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_white_24dp, null);
                    //d = DrawableCompat.wrap(d);
                    System.out.println("jkjkjkk");
                    //DrawableCompat.setTint(d, Color.BLACK);
                    //cvh.imgLogo3.setImageDrawable(d);
                }else {
                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_black_24dp, null);
                   // d = DrawableCompat.wrap(d);
                   // DrawableCompat.setTint(d, Color.WHITE);

                }
            }else{
                cvh.imgLogo2.setImageDrawable(Utils.getDrawable(rowView.getContext(), R.drawable.ic_keyboard_arrow_left_black_24dp));
                cvh.txtName.setTextColor(Color.BLACK);
                cvh.date.setTextColor(Color.BLACK);
               // cvh.imgLogo.setBackgroundColor(Color.BLACK);
                if(item.isIsrepeat()==1){

                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_black_24dp, null);
                    //d = DrawableCompat.wrap(d);
                    System.out.println("jkjkjkk");
                    //DrawableCompat.setTint(d, Color.BLACK);
                    //cvh.imgLogo3.setImageDrawable(d);
                }else {
                    d = VectorDrawableCompat.create(getResources(), R.drawable.ic_loop_white_24dp, null);
                    // d = DrawableCompat.wrap(d);
                    // DrawableCompat.setTint(d, Color.WHITE);

                }

            }
            cvh.imgLogo3.setImageDrawable(d);

           // this.notifyDataSetChanged();
            // cvh.imgLogo.setImageDrawable(Utils.getDrawable(context, R.drawable.ic_timer_white_24dp));

            cvh.imgLogo2.setTag(Integer.parseInt(Integer.toString(position)));
            cvh.imgLogo.setTag(Integer.parseInt(Integer.toString(position)));
            cvh.circularProgressBar.setProgressBarColor(Color.RED);
            cvh.circularProgressBar1.setProgressBarColor(Color.RED);
            cvh.circularProgressBar2.setProgressBarColor(Color.RED);
            cvh.circularProgressBar3.setProgressBarColor(Color.RED);
            if (promotododataList.get(position).getNum_of_promotodo() == 1) {
                cvh.circularProgressBar.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(0, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
            } else if (promotododataList.get(position).getNum_of_promotodo() == 2) {
                cvh.circularProgressBar.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(0, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
            } else if (promotododataList.get(position).getNum_of_promotodo() == 3) {
                cvh.circularProgressBar.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(0, (long) 1000);
            } else if (promotododataList.get(position).getNum_of_promotodo() == 4) {
                cvh.circularProgressBar.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar1.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar2.setProgressWithAnimation(18000, (long) 1000);
                cvh.circularProgressBar3.setProgressWithAnimation(18000, (long) 1000);
            } else {

            }


            return rowView;
        }


        class CustomViewHolder {
            public CheckBox imgLogo;
            public TextView txtName;
            public TextView date;
            public ImageView imgLogo2;
            public ImageView imgLogo3;
            public CircularProgressBar circularProgressBar;
            public CircularProgressBar circularProgressBar1;
            public CircularProgressBar circularProgressBar2;
            public CircularProgressBar circularProgressBar3;

        }

        private View.OnClickListener mOnTouchListener1 = new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Object o = v.getTag();
                if (o != null && o instanceof Integer) {
                    if (((CheckBox) v).isChecked()) {

                        DBproHandle mDBHelper = new DBproHandle(v.getContext());
                        int k = promotododataList.get(((Integer) o).intValue() - Promotodo_activity.mListView.getHeaderViewsCount()).ISREPEAT;
                        String s = promotododataList.get(((Integer) o).intValue() - Promotodo_activity.mListView.getHeaderViewsCount()).TITLE;
                        int n = promotododataList.get(((Integer) o).intValue() - Promotodo_activity.mListView.getHeaderViewsCount()).NUM;

                        String d =  promotododataList.get(((Integer) o).intValue() - Promotodo_activity.mListView.getHeaderViewsCount()).getDue_date();

                        System.out.println(d);
                        promotododata item = (promotododata) mAdapter.getItem(((Integer) o).intValue());
                        //mDBHelper = new DBproHandle(Promotodo_activity.this);
                        int i = mDBHelper.deleteData(item.getTitle());
                        if (i == 1) {
                            Toasty.success(v.getContext(), "Task Completed!", Toasty.LENGTH_LONG).show();
                            //wait 1 second
                            ((CheckBox) v).postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    ((CheckBox) v).toggle();
                                }
                            }, 1000);


                            promotododataList.remove(((Integer) o).intValue() - Promotodo_activity.mListView.getHeaderViewsCount());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toasty.error(v.getContext(), "Task not Completed!", Toasty.LENGTH_LONG).show();
                        }

                        if (k == 1) {
                            try {
                                LocalDate parsedDate = LocalDate.parse(d); //Parse date from String
                                LocalDate addedDate = parsedDate.plusDays(1);
                                String str = addedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                boolean b = mDBHelper.insertIsreat(s, k, n,str);

                                if (b == true) {
                                    Toasty.success(v.getContext(), "Repeated.", Toast.LENGTH_SHORT).show();
                                    item.setDue_date(str);
                                    item.setNum_of_promotodo(0);
                                    promotododataList.add(promotododataList.size(), item);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    Toasty.error(v.getContext(), "opps.", Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception e){
                                System.out.println(e.getMessage());
                            }

                        }


                    }
                }

            }


        };

        public View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Object o = v.getTag();
                if (o != null && o instanceof Integer) {
                    mListView.startDrag(((Integer) o).intValue());

                }
                return false;
            }
        };

    };


}

