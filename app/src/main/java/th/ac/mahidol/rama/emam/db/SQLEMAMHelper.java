package th.ac.mahidol.rama.emam.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mi- on 5/7/2559.
 */
public class SQLEMAMHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String TABLE_REG = "user"; //table name.
    private static final String USER_ID = "id"; //column name.
    private static final String USER_HN = "hn";
    private static final String USER_NFC = "nfc";

    private static final String DB_NAME = "register"; //db name.
    private static final int DB_VERSION = 1;

    public SQLEMAMHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * method CREATE TABLE.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REG + "( " + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_HN + " TEXT," + USER_NFC + " TEXT);");

    }
    /**
     * method DROP TABLE if new install.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("check","Upgrading database from version " + oldVersion + "to " + newVersion + ", which will destory all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_REG);
        onCreate(sqLiteDatabase);
    }
    /**
     * method insert data into DB.
     */
    public void addNFCRegister(String hn, String nfcTagId){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_HN, hn);
        values.put(USER_NFC, nfcTagId);
        db.insert(TABLE_REG, null, values);
        db.close();
    }
    /**
     * get String NFC from DB.
     */
    public boolean getNFCRegister(String nfcTagId){
        db = this.getWritableDatabase();
        String strSQL = "SELECT * FROM " + TABLE_REG;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    if(cursor.getString(2).equals(nfcTagId)){
                        return true;
                    }
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return false;
    }
    /**
     * get HN by NFC search from DB.
     */
    public String getHNRegister(String nfcTagId){
        String HN = null;
        db = this.getWritableDatabase();
        String strSQL = "SELECT * FROM " + TABLE_REG;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    if(cursor.getString(2).equals(nfcTagId)){
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
