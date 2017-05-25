package com.example.yu.login.data.repo;

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
        return "CREATE TABLE IF NOT EXISTS " + User.TABLE  + " ("
                + User.KEY_UserId  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + User.KEY_LoginName  + " TEXT, "
                + User.KEY_LoginPW  + " TEXT, "
                + User.KEY_LastName  + " TEXT,"
                + User.KEY_FirstName  + " TEXT, "
                + User.KEY_DOB  + " TEXT, "
                + User.KEY_Email  + " TEXT );";
    }

    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(User.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
