package com.levrite.danetki.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.levrite.danetki.R;
import com.levrite.danetki.database.DataBaseHelper;
import com.levrite.danetki.model.Category;
import com.levrite.danetki.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private SQLiteDatabase mDataBase;
    private DataBaseHelper mDataBaseHelper;
    private Category mCategory;
    private Question mQquestion;
    private List<Question> questionList = new ArrayList<Question>();

    public DataSource(Context context){
        mDataBaseHelper = DataBaseHelper.getInstance(context);
        try{
            mDataBaseHelper.createDataBase();
            mDataBaseHelper.openDataBase();
        }catch (IOException|SQLException e){
            e.printStackTrace();
        }
    }

    public void open() throws SQLException{
        mDataBase = mDataBaseHelper.getWritableDatabase();
    }

    public void close(){
        mDataBaseHelper.close();
        mDataBase.close();
    }

    public List<Category> getAllCategory(){

        List<Category> category = new ArrayList<Category>();
        Cursor cursor = mDataBase.query("category", null,null,null,null,null,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            mCategory = new Category();
            mCategory.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            mCategory.setName(cursor.getString(cursor.getColumnIndex("name")));
            mCategory.setImage(cursor.getString(cursor.getColumnIndex("img")));
            category.add(mCategory);
            cursor.moveToNext();
        }

        cursor.close();

        return category;
    }

    public List<Question> getAllQuestion(String idCategory){


        Cursor cursor = mDataBase.query("danetki", null, "category == " + idCategory, null, null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            mQquestion = new Question();
            mQquestion.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            mQquestion.setArticle(cursor.getString(cursor.getColumnIndex("name")));
            mQquestion.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
            mQquestion.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            mQquestion.setDone(cursor.getString(cursor.getColumnIndex("done")));
            mQquestion.setFavorite(cursor.getString(cursor.getColumnIndex("favorit")));
            questionList.add(mQquestion);
            cursor.moveToNext();
        }

        cursor.close();

        return questionList;
    }

    public List<Question> getFavorite(String favorite){
        Cursor cursor = mDataBase.query("danetki", null, "favorit == " + favorite, null, null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            mQquestion = new Question();
            mQquestion.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            mQquestion.setArticle(cursor.getString(cursor.getColumnIndex("name")));
            mQquestion.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
            mQquestion.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            mQquestion.setDone(cursor.getString(cursor.getColumnIndex("done")));
            mQquestion.setFavorite(cursor.getString(cursor.getColumnIndex("favorit")));
            questionList.add(mQquestion);
            cursor.moveToNext();
        }

        cursor.close();

        return questionList;
    }

    public int getCount(String id){
        int count = 0;
        Cursor cursor;
        if(id.equals("3")){
            cursor = mDataBase.query("danetki", null, "favorit == 1", null,null,null,null);
        }else{
           cursor = mDataBase.query("danetki", null, "category == " + id, null,null,null,null);
        }
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            count++;
            cursor.moveToNext();
        }
        cursor.close();

        return count;
    }

    public void writeDone(ContentValues contentValues, String id){
        mDataBase.update("danetki", contentValues, "_id = ?", new String[]{id});
    }

    public void writeFavorite(ContentValues contentValues, String id){
        mDataBase.update("danetki", contentValues, "_id = ?", new String[]{id});
    }



}
