package com.example.czgame.wordbank.ui.backup_scheudle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.czgame.wordbank.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBDaily extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "daily.db";
    public static final String TABLE_NAME = "daily_table";
    public static final String ID = "ID";
    public static final String MONTH = "MONTH";
    public static final String WEEK = "WEEK";
    public static final String DAY = "DAY";
    public static final String YEAR = "YEAR";
    public static final String TIME = "TIME";
    static String DB_PATH = "";
    private SQLiteDatabase mDataBase;
    private Context mContext;
    private boolean mNeedUpdate = false;

    public DBDaily(Context context) {
        super(context, DATABASE_NAME, null, 2);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DATABASE_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        //InputStream mInput = mContext.getAssets().open(DB_NAME);
        InputStream mInput = mContext.getResources().openRawResource(R.raw.word);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + DAY + " TEXT,"
                + WEEK + " TEXT,"
                + MONTH + " TEXT,"
                + YEAR + " TEXT,"
                + TIME + " INTEGER "  + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public Cursor getAllWeek(String week,String year) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor res = db1.rawQuery("SELECT * FROM daily_table WHERE WEEK = ? AND YEAR = ? ", new String[]{week,year});
        return res;
    }

    public Cursor getAllMonth(String month,String year) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor res = db1.rawQuery("SELECT * FROM daily_table WHERE  MONTH = ? AND YEAR = ? ", new String[]{month,year});
        return res;
    }

    public boolean insertAll(String day, String  week,String  month, String year, String time) {
        SQLiteDatabase db1 = this.getReadableDatabase();

        SQLiteDatabase db = this.getWritableDatabase();
        String id = "m";
        try {
            Cursor re = db1.rawQuery("SELECT * FROM  daily_table  WHERE DAY = ? AND WEEK = ? AND MONTH = ? AND YEAR = ? ", new String[]{day,week,month,year});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(1));
                    id = re.getString(1);
                } while (re.moveToNext());
            }

            re.close();
            System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(id.equals("m")){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DAY, day);
            contentValues.put(WEEK, week);
            contentValues.put(MONTH, month);
            contentValues.put(YEAR, year);
            contentValues.put(TIME, time);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }else return false;
    }
    public boolean insertAll1(String day, String  week,String  month, String year, String time) {
        SQLiteDatabase db1 = this.getReadableDatabase();


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DAY, day);
            contentValues.put(WEEK, week);
            contentValues.put(MONTH, month);
            contentValues.put(YEAR, year);
            contentValues.put(TIME, time);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        //else return false;
    }

    public Integer deleteData(String day, String  week,String  month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        String id = "m";
        try {
            Cursor re = db1.rawQuery("SELECT * FROM  daily_table  WHERE DAY = ? AND WEEK = ? AND MONTH = ? AND YEAR = ? ", new String[]{day,week,month,year});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(1));
                    id = re.getString(1);
                } while (re.moveToNext());
            }

            re.close();
            System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }



    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }
    public int deleteDay(String day, String  week,String  month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        String id = "m";
        try {
            Cursor re = db1.rawQuery("SELECT * FROM  daily_table  WHERE DAY = ? AND WEEK = ? AND MONTH = ? AND YEAR = ? ", new String[]{day,week,month,year});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(0));
                    id = re.getString(0);
                } while (re.moveToNext());
            }

            re.close();
            System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
       // db.execSQL("delete from " + TABLE_NAME);
    }



}
