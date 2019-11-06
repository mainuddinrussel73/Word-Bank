package com.example.czgame.wordbank.ui.words;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.czgame.wordbank.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import android.support.v7.app.ActionBarActivity;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "word.db";
    public static final String TABLE_NAME = "Word_table";
    public static final String ID = "ID";
    public static final String WORD = "WORD";
    public static final String MEANING = "MEANING";
    public static final String SENTENCE = "SENTENCE";

    public static final String TABLE_NAME2 = "Sentence_table";
    public static final String ID1 = "ID1";
    public static final String WORD1 = "WORD1";
    public static final String SENTENCE1 = "SENTENCE1";

    static String DB_PATH = "";
    private SQLiteDatabase mDataBase;
    private Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
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
            if (this != null) {
                this.close();
                super.close();
            }
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
                + WORD + " TEXT,"
                + MEANING + " TEXT,"
                + SENTENCE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String TASK_TABLE_CREATE = "CREATE TABLE "
                + TABLE_NAME2 + " ("
                + ID1 + " integer primary key, "
                + WORD1 + " text, "
                + SENTENCE1 + " TEXT" + ")";

        db.execSQL(TASK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public boolean insertData(String word, String meaning, String sentence) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WORD, word);
        contentValues.put(MEANING, meaning);
        contentValues.put(SENTENCE, sentence);
        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean insertData1(String word, String sentence){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(WORD1, word);
        contentValues1.put(SENTENCE1, sentence);
        long result1 = db1.insert(TABLE_NAME2, null, contentValues1);
        return result1!=-1;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getAllData1(String old_word) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor res = db1.rawQuery("SELECT * FROM Sentence_table WHERE WORD1 = ?; ", new String[]{old_word});
        return res;
    }

    public Cursor getAllData2() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor res = db1.rawQuery("SELECT * FROM Sentence_table ",null);
        return res;
    }

    public boolean updateData(String id, String old_word, String word, String meaining, String sentence) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        try {
            Cursor re = db1.rawQuery("SELECT * FROM Word_table WHERE WORD = ?; ", new String[]{old_word});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(0));
                    id = re.getString(0);
                } while (re.moveToNext());
            }

            re.close();
            // System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(WORD, word);
        contentValues.put(MEANING, meaining);
        contentValues.put(SENTENCE, sentence);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id, String old_word, String word, String meaining, String sentence) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        try {
            Cursor re = db1.rawQuery("SELECT * FROM Word_table WHERE WORD = ?; ", new String[]{old_word});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(0));
                    id = re.getString(0);
                } while (re.moveToNext());
            }

            re.close();
            // System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(WORD, word);
        contentValues.put(MEANING, meaining);
        contentValues.put(SENTENCE, sentence);
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }


    public Integer deleteData2(String id,String word, String sentence) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        try {
            Cursor re = db1.rawQuery("SELECT * FROM Sentence_table WHERE SENTENCE1 = ?; ", new String[]{sentence});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(0));
                    id = re.getString(0);
                } while (re.moveToNext());
            }

            re.close();
            // System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID1, id);
        contentValues.put(WORD1, word);
        contentValues.put(SENTENCE1, sentence);
        return db.delete(TABLE_NAME2, "ID1 = ?", new String[]{id});
    }

    public Integer deleteData1(String id, String old_word, String word, String sentence) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();
        try {
            Cursor re = db1.rawQuery("SELECT * FROM Sentence_table WHERE WORD1 = ?; ", new String[]{old_word});
            if (re.moveToFirst()) {
                do {
                    System.out.println(re.getString(0));
                    id = re.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ID1, id);
                    contentValues.put(WORD1, word);
                    contentValues.put(SENTENCE1, sentence);
                    db.delete(TABLE_NAME2, "ID1 = ?", new String[]{id});
                } while (re.moveToNext());
            }

            re.close();
            return 1;
            // System.out.println(re.getString(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;

    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }
    public void deleteAll1() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME2);
    }

    public List<word> getSelectedDatas(String setId) {
        List<word> cardList = new ArrayList<word>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ID, WORD, MEANING, SENTENCE},
                WORD + "=?",
                new String[]{setId}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                word stats = new word();
                stats.setID(cursor.getInt(0));
                stats.setWORD(cursor.getString(1));
                stats.setMEANING(cursor.getString(2));
                stats.setSENTENCE(cursor.getString(3));
                // Adding card to list
                cardList.add(stats);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return contact list
        return cardList;
    }

}