package com.ctf.hauntedhotel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FlagDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hotel_secrets.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;
    private SQLiteDatabase database;

    public FlagDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabaseFromAssets();
    }

    /**
     * Copy database from assets folder to app's private storage
     * This runs only once when the app is first installed
     */
    private void copyDatabaseFromAssets() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        // If database already exists, don't copy again
        if (dbFile.exists()) {
            return;
        }

        // Create parent directories if needed
        dbFile.getParentFile().mkdirs();

        try {
            // Open the database file from assets
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(dbFile);

            // Copy the file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Database already exists from assets, so nothing to do here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades if needed
    }

    /**
     * Get the flag from the database
     */
    public String getFlag() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String flag = null;

        try {
            cursor = db.rawQuery("SELECT secret FROM flag108", null);
            if (cursor.moveToFirst()) {
                flag = cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return flag;
    }

    /**
     * Check if database exists
     */
    public boolean databaseExists() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }
}