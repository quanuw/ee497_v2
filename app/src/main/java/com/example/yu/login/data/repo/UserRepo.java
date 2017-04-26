package com.example.yu.login.data.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.User;

/**
 * Created by amand on 3/31/2017.
 */

public class UserRepo {
    private User user;

    public UserRepo(){
        user = new User();
    }


    public static String createTable(){
        return "CREATE TABLE " + User.TABLE  + "("
                + User.KEY_UserId  + " INTERGER PRIMARY KEY  ,"
                + User.KEY_LoginName  + " TEXT  ,"
                + User.KEY_LoginPW  + " TEXT  ,"
                + User.KEY_LastName  + " TEXT  ,"
                + User.KEY_FirstName  + " TEXT  ,"
                + User.KEY_DOB  + " TEXT  ,"
                + User.KEY_Email  + " TEXT )";
    }



    public void insert(User user) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_UserId, user.getUserId());
        values.put(User.KEY_LoginName, user.getLoginName());
        values.put(User.KEY_LoginPW, user.getLoginPW());
        values.put(User.KEY_LastName, user.getLastName());
        values.put(User.KEY_FirstName, user.getFirstName());
        values.put(User.KEY_DOB, user.getDOB());
        values.put(User.KEY_Email, user.getEmail());


        // Inserting Row
        db.insert(User.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(User.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
