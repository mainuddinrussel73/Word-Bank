package com.example.czgame.wordbank.ui.diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDatabase  extends SQLiteOpenHelper {
    public static final String databaseName = "user.db";
    public static final String tableName = "userTable";
    public static final String col_1 = "id";
    public static final String col_2 = "subject";
    public static final String col_3 = "description";
    public static final String col_4 = "mediaPath";
    public static final String col_5 = "dateTime";
    public static final String col_6 = "type";



    //this constructor for creating the database
    public SqliteDatabase(Context context) {
        super(context, databaseName, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();

       // context.deleteDatabase(databaseName);
    }

    //creating table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


       // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + tableName + "'");
        sqLiteDatabase.execSQL("create table " + tableName +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT,description TEXT,mediaPath TEXT,dateTime Date,type INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + databaseName);//drop table if exist
        onCreate(sqLiteDatabase); //and create new table
    }

    //function for inserting on sqlite database
    public long insertData(String subject, String description,String mediaPath, String dateTime,int type) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();//for accessing database data
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,subject);
        contentValues.put(col_3, description);
        contentValues.put(col_4, mediaPath);
        contentValues.put(col_5, dateTime);
        contentValues.put(col_6, type);
        long id = sqLiteDatabase.insert(tableName, null, contentValues);
        return id;
    }


    public Cursor display(){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();//for accessing database data
        Cursor cursor = sqliteDatabase.rawQuery("select * from "+tableName, null);
        return cursor;
    }

    //for updating database data
    public boolean update(String subject,String description,String mediaPath,String dateTime,String id,int type){
        try{
            SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(col_1,id);
            contentValues.put(col_2,subject);
            contentValues.put(col_3, description);
            contentValues.put(col_4, mediaPath);
            contentValues.put(col_5, dateTime);
            contentValues.put(col_6, type);
            sqliteDatabase.update(tableName,contentValues,col_1+" =?", new String[]{id});
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


    //for deleting database data
    public boolean delete(String id){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.delete(tableName,col_1+" = ?",new String[]{id});
        return  true;
    }
}
