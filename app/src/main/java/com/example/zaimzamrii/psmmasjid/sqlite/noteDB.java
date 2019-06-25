package com.example.zaimzamrii.psmmasjid.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class noteDB extends SQLiteOpenHelper {

    public static final String dbName="dbNotes";
    public static final String tblNameNote = "notes";
    public static final String colNotesTitle = "notes_title";
    public static final String colNotesDesc = "notes_desc";
    public static final String colNotesID = "notes_id";

    public static final String strCreateTblExp = "CREATE TABLE "+ tblNameNote + " ("+ colNotesID +" INTEGER PRIMARY KEY, "+ colNotesTitle +" TEXT, "+ colNotesDesc + " TEXT)";

    public static final String strDropTblExpenses = " DROP TABLE IF EXISTS "+ tblNameNote;

    public noteDB(Context context){
        super(context,dbName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(strCreateTblExp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(strDropTblExpenses);

    }

    public float fnInsertExpenses (NotesDBModel jb){

        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colNotesTitle,jb.getTitle());
        values.put(colNotesDesc,jb.getDesc());

        retResult = db.insert(tblNameNote,null,values);
        Log.e("tengok",String.valueOf(retResult));
        return retResult;

    }

    public NotesDBModel fnGetExpenses (int intExpId){
        NotesDBModel jb = new NotesDBModel();
        String strSelQuery = "Select * from "+ tblNameNote + " where "+ colNotesID +" = "+intExpId;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelQuery,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        jb.setTitle(cursor.getString(1));
        jb.setDesc(cursor.getString(2));
        Log.e("SAJA",jb.getTitle());

        return jb;
    }


    public List<NotesDBModel> fnGetAllExpenses(){
        List<NotesDBModel> list = new ArrayList<>();
        String strSelAll = "Select * from "+ tblNameNote;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelAll,null);

        if (cursor.moveToFirst()){
            do {
                NotesDBModel model = new NotesDBModel();
                model.setId(cursor.getString(cursor.getColumnIndex(colNotesID)));
                model.setDesc(cursor.getString(cursor.getColumnIndex(colNotesDesc)));
                model.setTitle(cursor.getString(cursor.getColumnIndex(colNotesTitle)));
                list.add(model);
            }while (cursor.moveToNext());

        }
        Log.e("tengok1",String.valueOf(list.size()));
        return list;

    }

    public float fnUpdateExpenses (NotesDBModel jb){

        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colNotesTitle,jb.getTitle());
        values.put(colNotesDesc,jb.getDesc());


        retResult = db.update(tblNameNote,values, colNotesID +" = "+jb.getId(),null);
        return retResult;

    }

    public float fnDeleteExpenses (int id){
        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        retResult = db.delete(tblNameNote, colNotesID +" = "+id,null);
        return retResult;

    }
}
