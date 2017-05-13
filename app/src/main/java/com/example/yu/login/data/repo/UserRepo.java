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


    public static boolean insertUser(User user) {

        // Get a database manager
        DatabaseManager databaseManager = new DatabaseManager();

        // Open the database to write
        SQLiteDatabase writable = databaseManager.openDatabase();

        // Create a content values instance (kind of like a map)
        ContentValues values = new ContentValues();
        //values.put(User.KEY_UserId, user.getUserId()); ISSUE WITH AUTOINCREMETNATION
        values.put(User.KEY_LoginName, user.getLoginName());
        values.put(User.KEY_LoginPW, user.getLoginPW());
        values.put(User.KEY_LastName, user.getLastName());
        values.put(User.KEY_FirstName, user.getFirstName());
        values.put(User.KEY_DOB, user.getDOB());
        values.put(User.KEY_Email, user.getEmail());

        // Insert the content values into the chosen table (User)
        long result = writable.insert("User", null, values);

        // If an error occured during insertion of row
        if (result == -1) {
            System.out.println("COULD NOT REGISTER USER!");
            return false;
        }
        return true;
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(User.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
