package com.anie.dara.trackmonbus_supir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anie.dara.trackmonbus_supir.model.Halte;

import java.util.LinkedList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "transPadang";
    private static final String TABLE_NAME = "haltes";
    private static final String KEY_ID = "halte_id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String[] COLUMNS = { KEY_ID, KEY_NAMA, KEY_LAT,
            KEY_LNG };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE haltes ( "
                + "halte_id string PRIMARY KEY, " + "nama string, "
                + "lat string, " + "lng string )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public List<Halte> allHaltes() {

        List<Halte> Haltes = new LinkedList<Halte>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Halte halte = null;

        if (cursor.moveToFirst()) {
            do {
                halte = new Halte();
                halte.setHalte_id(cursor.getString(0));
                halte.setNama(cursor.getString(1));
                halte.setLat(cursor.getString(2));
                halte.setLng(cursor.getString(3));
                Haltes.add(halte);
            } while (cursor.moveToNext());
        }

        return Haltes;
    }

    public void addHalte(Halte halte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put(KEY_ID, halte.getHalte_id());
            values.put(KEY_NAMA, halte.getNama());
            values.put(KEY_LAT, halte.getLat());
            values.put(KEY_LNG, halte.getLng());
            // insert
            db.replace(TABLE_NAME, null, values);
            db.close();
    }




}
