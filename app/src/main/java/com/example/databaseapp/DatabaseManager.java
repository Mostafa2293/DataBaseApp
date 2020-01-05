package com.example.databaseapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath()+"/recordsdb.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "records";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "id";
    public SQLiteDatabase db;

    public DatabaseManager(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.d("TAG################","path: "+ DATABASE_NAME);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " ( "+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_ADDRESS + " TEXT NOT NULL, " + COLUMN_NAME + " TEXT NOT NULL )" ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);

    }

    //create a  new record

    public boolean addNewRecord(String name,String address){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_ADDRESS,address);

        db = getWritableDatabase();

        return db.insert(TABLE_NAME,null,contentValues) != -1;

    }

    //read all values
    public Cursor getAllRecords(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    //update record
    public  boolean updateRecord(int id,String name, String address){

         db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_ADDRESS,address);

        return db.update(TABLE_NAME, contentValues, COLUMN_ID + " = " + id , null) == 1;



    }

    //delete record
    public boolean deleteRecord(int id){

        db = getWritableDatabase();
       return db.delete(TABLE_NAME,COLUMN_ID + "=" + id , null) == 1;

    }

}

