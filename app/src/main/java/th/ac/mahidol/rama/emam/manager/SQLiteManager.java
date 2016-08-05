package th.ac.mahidol.rama.emam.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteManager extends SQLiteOpenHelper {

    private SQLiteDatabase db;
//    private static final String DB_NAME = "register";
    private static final String DB_NAME = "emam";
    private static final int DB_VERSION = 1;

//  first nfc for login table
    private static final String TABLE_REG = "user";
    private static final String USER_ID = "id";
    private static final String USER_NFC = "nfc";

//  preparation by ....table
    private static final String PREPARE_ID = "prepareid";
    private static final String PREPARE_BY_NAME = "preparebyname";
    private static final String TIMER = "timer";
    private static final String ADMIN_TIME = "admintime";
    private static final String STATUS = "status";
    private static final String REASON ="reason";

//    preparation for patient ....table
    private static final String TABLE_PREPAREFORPATIENT = "prepareforpatient";
    private static final String PREPAREFORPATIENT_ID = "prepareforpatientid";
    private static final String HN = "hn";
    private static final String ADMINTIMEHOUR = "admintimeHour";
    private static final String DRUGNAME = "drugname";
    private static final String DOSAGE = "dosage";
    private static final String UNIT = "unit";
    private static final String TYPE ="type";
    private static final String ROUTE = "route";
    private static final String FREQUENCY = "frequency";
    private static final String ADMINTIME ="admintime";

//    HN table
    private static final String HN_ID = "hnid";
    private static final String HN_CARD = "hncard";

    public SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REG + "( " + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NFC + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PREPAREFORPATIENT + "( " + PREPAREFORPATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HN + " TEXT," + ADMINTIMEHOUR + " TEXT," + DRUGNAME + " TEXT," + DOSAGE + " TEXT," + UNIT + " TEXT," + TYPE + " TEXT," +
                ROUTE + " TEXT," + FREQUENCY + " TEXT," + ADMINTIME + " TEXT);");

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


    public void addPrepareForPatient(String mRN, String strtimer, String listDrugName, String listDosage, String unit, String type, String route, String listFrequency, String adminTime){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HN, mRN);
        values.put(ADMINTIMEHOUR, strtimer);
        values.put(DRUGNAME, String.valueOf(listDrugName));
        values.put(DOSAGE, String.valueOf(listDosage));
        values.put(UNIT, String.valueOf(unit));
        values.put(TYPE, String.valueOf(type));
        values.put(ROUTE, String.valueOf(route));
        values.put(FREQUENCY, String.valueOf(listFrequency));
        values.put(ADMINTIME, String.valueOf(adminTime));
        db.insert(TABLE_PREPAREFORPATIENT, null, values);
        db.close();
    }

    public String getPrepareForPatient(String mRN){
        String HN = null;
        db = this.getWritableDatabase();
        String strSQL = "SELECT * FROM " + TABLE_PREPAREFORPATIENT;
        Cursor cursor = db.rawQuery(strSQL, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    if(cursor.getString(1).equals(mRN)){
                        HN = cursor.getString(1);
                        Log.d("check", "ID : "+ cursor.getString(0));
                        Log.d("check", "HN : "+ cursor.getString(1));
                        Log.d("check", "strtimer : "+ cursor.getString(2));
                        Log.d("check", "listDrugName : "+ cursor.getString(3));
                        Log.d("check", "listDosage : "+ cursor.getString(4));
                        Log.d("check", "unit : "+ cursor.getString(5));
                        Log.d("check", "type : "+ cursor.getString(6));
                        Log.d("check", "route : "+ cursor.getString(7));
                        Log.d("check", "listFrequency : "+ cursor.getString(8));
                        Log.d("check", "adminTime : "+ cursor.getString(9));
                    }
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return HN;
    }

    public void deletePrepareForPatient(String id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_PREPAREFORPATIENT, "prepareforpatientid =" + id, null);

        db.close();


    }
}
