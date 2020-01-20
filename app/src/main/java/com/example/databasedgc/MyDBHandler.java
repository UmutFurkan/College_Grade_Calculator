package com.example.databasedgc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyDBHandler extends SQLiteOpenHelper {
    //Creating database and table.
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME ="classDB";
    private static final String TABLE_PRODUCTS ="classes6";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CLASSNAME = "_className";
    private static final String COLUMN_CLASSCREDIT = "_classCredit";
    private static final String COLUMN_CLASSNOTE = "_classNote";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME, factory,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS +"(" +
                COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CLASSNAME + " TEXT, " +
                COLUMN_CLASSCREDIT + " TEXT, " +
                COLUMN_CLASSNOTE + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            onCreate(db);
        }
    }

    public void addProduct (Product product){
        //Adds values that taken from product object and CalPage class global variable to insert in database table.
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASSNAME,product.get_className());
        values.put(COLUMN_CLASSCREDIT,product.get_classCredit());
        values.put(COLUMN_CLASSNOTE,CalPage.strB);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
    }

    public void deleteProduct (String productName){
        //Takes a class name from table end deletes it.
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_CLASSNAME + "=\'" + productName + "\';";
        db.execSQL(query);
    }

    public ArrayList<ArrayList<String>> dbToString(){
        ArrayList<ArrayList<String>> arrB = new ArrayList<ArrayList<String>>();

        ArrayList<String> arr1 = new ArrayList<>();
        ArrayList<String> arr2 = new ArrayList<>();
        ArrayList<String> arr3 = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS +  " WHERE 1" ;

        //Cursor acts like an iterator, it moves to first index in table if unless it is "null".
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c != null) {
            //Until it reaches to after last item which, takes the indexes to separated strings and adds them to arrays
            while (!c.isAfterLast()) {
                String s;
                String ss;
                String sss;

                s = c.getString(c.getColumnIndex(COLUMN_CLASSNAME));
                ss =  c.getString(c.getColumnIndex(COLUMN_CLASSCREDIT));
                sss = c.getString(c.getColumnIndex(COLUMN_CLASSNOTE));

                arr1.add(s);
                arr2.add(ss);
                arr3.add(sss);

                c.moveToNext();
                //To see and test which indexes are taken from database.
                System.out.println(s + "  XX " + ss + " XX " + sss );
            }
            c.close();
        }


        arrB.add(arr1);
        arrB.add(arr2);
        arrB.add(arr3);
        return arrB;
    }
    //This method gets called if there is an update on class note.
    public void dataUpdate (String productName, String someValue){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_CLASSNOTE +  "=" +
                someValue  + " WHERE " + COLUMN_CLASSNAME + "=\'" + productName + "\';";

        db.execSQL(query);
    }
}
