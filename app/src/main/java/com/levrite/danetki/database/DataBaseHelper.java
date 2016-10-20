package com.levrite.danetki.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.levrite.danetki/databases/";

    private static String DB_NAME = "danetki1.db";

    private SQLiteDatabase mDataBase;
    private static DataBaseHelper sDataBaseInstance;
    private Context mContext;

    /**
     * Singleton for Database
     * @param context
     * @return instance DataBaseHelper object
     */
    public static DataBaseHelper getInstance(Context context) {

        if (sDataBaseInstance == null) {
            sDataBaseInstance = new DataBaseHelper(context.getApplicationContext());
        }

        return sDataBaseInstance;
    }


    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    public void createDataBase() throws IOException {


        if(!checkDataBase()){

            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String path = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLException e){
            e.printStackTrace();
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;

    }

    private void copyDataBase() throws IOException{

        InputStream inputStream = mContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream outputStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        while((length = inputStream.read(buffer))>0){
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }

    public void openDataBase() throws SQLException{

        String path = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(mDataBase != null){
            mDataBase.close();
        }

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
