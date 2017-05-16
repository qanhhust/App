package com.example.quanganh.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Quang Anh on 2/22/2017.
 */

public class DatabaseAdapter extends SQLiteOpenHelper {

    private Context context;
    private String DB_PATH = "data/data/com.example.quanganh.app/";
    private static String DB_NAME = "kimdung.sqlite";
    private SQLiteDatabase myDatabase;

    public DatabaseAdapter(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        boolean dbExist = checkDatabase();
        if (dbExist) {

        } else {
            Log.e("Message", "Database doesn't exist");
            createDatabse();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDatabase() {
        return myDatabase;
    }

    private boolean checkDatabase() {
        boolean check = false;

        try {
            String myPath = DB_PATH + DB_NAME;
            File dbFile = new File(myPath);
            check = dbFile.exists();
        } catch (Exception e) {

        }

        return check;
    }

    public void createDatabse() {
        this.getReadableDatabase();
        try {
            copyDatabase();
            Log.e("Message", "Database exist");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyDatabase() throws IOException {

        AssetManager dirPath = context.getAssets();

        InputStream myinput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = myinput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myinput.close();
    }


    public void open() {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        myDatabase.close();
        super.close();
    }
}
