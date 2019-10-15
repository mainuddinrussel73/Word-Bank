package com.example.mainuddin.myapplication34.ui.promotodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;

import static com.yydcdut.sdlv.Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;

public class Promotodo_activity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener,
        SlideAndDragListView.OnDragDropListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener{


    public static  List<promotododata> promotododataList = new ArrayList<>();
    private Menu mMenu;
    public static SlideAndDragListView mListView;
    private Toast mToast;
    private promotododata mDraggedEntity;
    ExtendedEditText tfb;
    Button button;
    public static  DBproHandle mDBHelper;
    promotododata currenttask;
    proadapter mAdapter;

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
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        mToast = Toast.makeText(Promotodo_activity.this, "", Toast.LENGTH_SHORT);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = mDBHelper.insertData(tfb.getText().toString());
                if(b==true){
                    Toasty.success(getApplicationContext(),"Done.",Toast.LENGTH_SHORT).show();
                    initList();
                    initMenu();
                    initUiAndListener();
                }else{
                    Toasty.error(getApplicationContext(),"opps.",Toast.LENGTH_SHORT).show();
                }
            }
        });












    }





    public  void initList(){

        promotododataList.clear();

        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if(cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                promotododata  word = new promotododata();
                word.setID( Integer.parseInt(cursor.getString(0)));
                word.setTitle( cursor.getString(1));
                word.setNum_of_promotodo(cursor.getInt(2));
                word.setCompleted_promotodo(cursor.getInt(3));
                word.setIsrepeat(cursor.getInt(4));
                word.setDue_date(cursor.getString(5));

                promotododataList.add(word);
                System.out.println(word.NUM);


            }



        }
        else {

            Toasty.info(Promotodo_activity.this,"Nothing to show.",Toasty.LENGTH_LONG).show();
        }

        mAdapter = new proadapter(promotododataList,this);


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
                .setIcon(getResources().getDrawable(R.drawable.ic_timer_black_24dp))
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.ic_cancel_black_24dp))
                .build());
    }










    public void initUiAndListener() {
        mListView = (SlideAndDragListView) findViewById(R.id.lv_edit);
        mListView.setMenu(mMenu);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnDragDropListener(this);
//        mListView.setOnItemLongClickListener(this);
        mListView.setOnSlideListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onDragViewStart(int beginPosition) {
        mDraggedEntity = promotododataList.get(beginPosition);
        toast("onDragViewStart   beginPosition--->" + beginPosition);
    }

    @Override
    public void onDragDropViewMoved(int fromPosition, int toPosition) {
        promotododata applicationInfo = promotododataList.remove(fromPosition);
        promotododataList.add(toPosition, applicationInfo);
        toast("onDragDropViewMoved   fromPosition--->" + fromPosition + "  toPosition-->" + toPosition);
    }

    @Override
    public void onDragViewDown(int finalPosition) {
        promotododataList.set(finalPosition, mDraggedEntity);
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
                    case 0:
                        Intent myIntent = new Intent(v.getContext(), promodetail.class);
                        //String s = view.findViewById(R.id.subtitle).toString();
                        //String s = (String) parent.getI;
                        myIntent.putExtra("ID",itemPosition);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        return Menu.ITEM_SCROLL_BACK;
                    case 1:
                        return ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {
        //mDBHelper = new DBproHandle(Promotodo_activity.this);
        int i = mDBHelper.deleteData(promotododataList.get(position - mListView.getHeaderViewsCount()).getTitle());
        promotododataList.remove(position - mListView.getHeaderViewsCount());
        mAdapter = new proadapter(promotododataList,this);
        mListView.setAdapter(mAdapter);
       // mAdapter.notifyDataSetChanged();
        if(i==1){
            Toasty.success(view.getContext(),"Task Completed!",Toasty.LENGTH_LONG).show();
        }else {
            Toasty.error(view.getContext(),"Task not Completed!",Toasty.LENGTH_LONG).show();
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
        myIntent.putExtra("ID",position);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);





    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        toast("onItemLongClick   position--->" + position);
        return false;
    }

    private void toast(String toast) {
        mToast.setText(toast);
        mToast.show();
    }
}


