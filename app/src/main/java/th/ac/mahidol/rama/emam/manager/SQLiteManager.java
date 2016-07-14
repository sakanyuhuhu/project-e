package th.ac.mahidol.rama.emam.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mi- on 5/7/2559.
 */
public class SQLiteManager extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String TABLE_REG = "user";
    private static final String USER_ID = "id";
    private static final String USER_NFC = "nfc";
    private static final String DB_NAME = "register";
    private static final int DB_VERSION = 1;

    public SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REG + "( " + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NFC + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("check","Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destory all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_REG);
        onCreate(sqLiteDatabase);
    }

    public void addNFCRegister(String nfcTagId){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NFC, nfcTagId);
        db.insert(TABLE_REG, null, values);
        db.close();
    }

    public boolean getNFCRegister(String nfcTagId){
        db = this.getWritableDatabase();
        String strSQL = "SELECT * FROM " + TABLE_REG;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    if(cursor.getString(1).equals(nfcTagId)){
                        return true;
                    }
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    public String getHNRegister(String nfcTagId){
        String HN = null;
        db = this.getWritableDatabase();
        String strSQL = "SELECT * FROM " + TABLE_REG;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    if(cursor.getString(1).equals(nfcTagId)){
                        HN = cursor.getString(1);
                    }
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return HN;
    }
}
