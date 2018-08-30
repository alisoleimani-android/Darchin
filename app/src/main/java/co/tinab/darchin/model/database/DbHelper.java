package co.tinab.darchin.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by A.S.R on 4/16/2018.
 */

public class DbHelper {

    private static DbHelper dbHelper = new DbHelper();
    public static DbHelper getInstance(){
        return dbHelper;
    }

    private String DBName = "Darchin";


    // DB Methods For Drawer Table
    // Select DB
    public String select(Context context,String name){
        if (context != null) {
            // Open Or Create DataBase :
            SQLiteDatabase database = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
            // Query for select and putting into Cursor Element :
            database.execSQL("create table if not exists MainTable (Name varchar,Value varchar)");
            Cursor cursor = database.rawQuery("select * from MainTable where Name=?", new String[]{name});
            String row;
            StringBuilder result = new StringBuilder();
            while (cursor.moveToNext()) {
                row = cursor.getString(cursor.getColumnIndex("Value"));
                result.append(row);
            }
            //Closing DataBase :
            cursor.close();
            database.close();
            return result.toString();
        }
        return "";
    }

    public void delete(Context context,String name){
        if (context != null) {
            // Open Or Create DataBase :
            SQLiteDatabase database = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
            // Query for select and putting into Cursor Element :
            database.execSQL("create table if not exists MainTable (Name varchar,Value varchar)");
            database.delete("MainTable","Name=?",new String[]{name});
            database.close();
        }
    }

    // insert and update DB
    public void insert(Context context,String name, String value){
        if (context != null) {
            boolean existence = this.checkExistence(context,name);
            if(existence){
                updateContentInDB(context,name,value);
            }else{
                insertContentToDB(context,name,value);
            }
        }
    }

    // insert DB
    private void insertContentToDB(Context context, String name,String value){
        if (context != null) {
            // Open Or Create DataBase :
            SQLiteDatabase database = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
            //Create DataBase if not Exists :
            database.execSQL("create table if not exists MainTable (Name varchar,Value varchar)");
            // Giving Query :
            SQLiteStatement statement = database.compileStatement("insert into MainTable values(?,?)");
            statement.bindString(1,name);
            statement.bindString(2,value);
            statement.execute();
            // Closing DataBase :
            database.close();
        }
    }

    // update DB
    private void updateContentInDB(Context context, String name,String value){
        if (context != null) {
            // open DataBase or create DataBase:
            SQLiteDatabase database = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);

            //Create DataBase if not Exists :
            database.execSQL("create table if not exists MainTable (Name varchar,Value varchar)");

            // Giving Query:
            SQLiteStatement statement = database.compileStatement("update MainTable set Name=? , Value=? where Name=?;");
            statement.bindString(1,name);
            statement.bindString(2,value);
            statement.bindString(3,name);
            statement.execute();

            // Closing DataBase :
            database.close();
        }
    }

    // checking value existence in DB
    private boolean checkExistence(Context context,String name){
        if (context != null) {
            // open DataBase or create DataBase:
            SQLiteDatabase database = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
            //Create DataBase if not Exists :
            database.execSQL("create table if not exists MainTable (Name varchar,Value varchar)");
            //Query
            //Cursor
            Cursor cursor= database.rawQuery("select Name from MainTable where Name=?"
                    ,new String[]{name});
            if(cursor.getCount()>0) {
                //Team Found
                cursor.close();
                database.close();
                return true;
            }else{
                //Team Not Found
                cursor.close();
                database.close();
                return false;
            }
        }
        return false;
    }
}
