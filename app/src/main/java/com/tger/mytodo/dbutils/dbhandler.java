package com.tger.mytodo.dbutils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tger.mytodo.MainActivity;
import com.tger.mytodo.model.taskmodel;

import java.util.ArrayList;
import java.util.List;

public class dbhandler extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "todo_db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_STATUS = "status";
    SQLiteDatabase db;


    private static final String CREATE_TABLE_TODO =
            "CREATE TABLE " + TABLE_TODO + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASK + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " INTEGER" +
                    ")";
    public dbhandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // Toast.makeText(context,"Hello Javatpoint",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }


    public void openDatabase()
    {
        db = this.getWritableDatabase();
    }
    public Boolean addTask(taskmodel task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task.getTask());
        values.put(COLUMN_STATUS, 0);
        long result=db.insert(TABLE_TODO, null, values);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
        //Toast.makeText(null,"Data Saved into Database",Toast.LENGTH_SHORT).show();
        //db.close();
    }

    //this method gets all the tasks from the-to-do table
    @SuppressLint("Range")
    public List<taskmodel> getAllTasks()
    {
        List<taskmodel> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.query(TABLE_TODO, null, null, null, null, null,null);
        String query = "SELECT * FROM " + TABLE_TODO + " ORDER BY " + COLUMN_ID + " DESC";

// Execute the query and get the cursor
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                taskmodel task = new taskmodel();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
        {
            cursor.close();
        }
        db.close();
        return taskList;
    }
   //this method updates the selected task that exists in the database
    public void updateTask(int id, String task, int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        values.put(COLUMN_STATUS, status);
        db.update(TABLE_TODO, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    //this method deletes the tasks from the database
    public void deleteTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public boolean isItemExists(int itemId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String tableName = "todo";
        String idColumnName = "id";
        String selection = idColumnName + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};

        Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
        boolean isExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isExists;
    }
}
