package com.example.movingAssistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_BARCODE_NUM = "BARCODE_NUM";
    public static final String COLUMN_PHOTO_URI = "PHOTO_URI";
    public static final String COLUMN_IN_STORAGE = "IN_STORAGE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_CUSTOMER_ID = "CUSTOMER_ID";
    public static final String ITEM_TABLE = "ITEM_TABLE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "holdingItems.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + ITEM_TABLE +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BARCODE_NUM + " TEXT NOT NULL, "
                + COLUMN_IN_STORAGE + " BOOL, "
                + COLUMN_PHOTO_URI + " TEXT, "
                + COLUMN_CUSTOMER_ID + " TEXT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i1 > i){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        }
    }

    public boolean addOne(MovingItemsControl movingItemsControl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BARCODE_NUM, movingItemsControl.getBarcodeNum());
        cv.put(COLUMN_IN_STORAGE, movingItemsControl.getInStorage());
        cv.put(COLUMN_PHOTO_URI, movingItemsControl.getPhotoUri());
        cv.put(COLUMN_CUSTOMER_ID, movingItemsControl.getCustomerId());

        long insert = db.insert(ITEM_TABLE, null, cv);
        return insert != -1;
    }

/*    public boolean deleteOne(MovingItemsControl movingItemsControl){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ITEM_TABLE + " WHERE" + COLUMN_ID + " = " + movingItemsControl.getId();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(queryString, null);

        return cursor.moveToFirst();
    }*/

    public void deleteOne(MovingItemsControl movingItemsControl){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM ITEM_TABLE WHERE ID = " + movingItemsControl.getId();

        db.execSQL(queryString);

        db.close();
    }

    public List<MovingItemsControl> getEveryone(){
        List<MovingItemsControl> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM ITEM_TABLE WHERE CUSTOMER_ID = " + MainActivity.customerID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do {
                int itemId = cursor.getInt(0);
                String barcodeNum = cursor.getString(1);
                boolean inStorage = cursor.getInt(2) == 1;
                String photoUri = cursor.getString(3);
                String customer = cursor.getString(4);

                MovingItemsControl movingItemsControl = new MovingItemsControl(itemId, barcodeNum, inStorage, photoUri, customer);
                returnList.add(movingItemsControl);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return  returnList;
    }

    public MovingItemsControl findTheItem(){

        String queryString = "SELECT * FROM ITEM_TABLE WHERE BARCODE_NUM = \"" + MainActivity2.codeNumber + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        MovingItemsControl result = null;

        if(cursor.moveToFirst()){
            do{
                int itemId = cursor.getInt(0);
                String barcodeNum = cursor.getString(1);
                boolean inStorage = cursor.getInt(2) == 1;
                String photoUri = cursor.getString(3);
                String customer = cursor.getString(4);

                result = new MovingItemsControl(itemId, barcodeNum, inStorage, photoUri, customer);
            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return result;
    }

    public List<String> getBarcodeNumbers(){
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT BARCODE_NUM FROM ITEM_TABLE";

        SQLiteDatabase db = this. getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do{
                String barcodeNum = cursor.getString(0);

                returnList.add(barcodeNum);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public int getTotalNumber(){
        int result = 0;
        String queryString = "SELECT count(*) FROM ITEM_TABLE WHERE CUSTOMER_ID = " + MainActivity.customerID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do{
                result = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return result;
    }
}
