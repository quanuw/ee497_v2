package com.example.yu.login;

/**
 * Created by amand on 3/3/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static final String TAG = "UserDAO";

    private SQLiteDatabase mDatabase;
    private DB_Controller mDB_Controller;
    private Context mContext;
    private String[] mAllColumns = { DB_Controller.COL_USER_ID,
            DB_Controller.COL_USER_FN, DB_Controller.COL_USER_LN,
            DB_Controller.COL_USER_DOB, DB_Controller.COL_USER_SSN,
            DB_Controller.COL_USER_STREET, DB_Controller.COL_USER_CITY,
            DB_Controller.COL_USER_STATE, DB_Controller.COL_USER_ZIPCODE,
            DB_Controller.COL_USER_UN, DB_Controller.COL_USER_PW };

    public UserDAO(Context context) {
        this.mContext = context;
        mDB_Controller = new DB_Controller(context);
        //open database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDB_Controller.getWritableDatabase();
    }

    public void close() {
        mDB_Controller.close();
    }

    public User createUser(String firstName, String lastName, String DOB, String SSN, String street, String city,
                           String state, String zipcode, String userName, String password) {
        ContentValues values = new ContentValues();
        values.put(DB_Controller.COL_USER_FN, firstName);
        values.put(DB_Controller.COL_USER_LN, lastName);
        values.put(DB_Controller.COL_USER_DOB, DOB);
        values.put(DB_Controller.COL_USER_SSN, SSN);
        values.put(DB_Controller.COL_USER_STREET, street);
        values.put(DB_Controller.COL_USER_CITY, city);
        values.put(DB_Controller.COL_USER_STATE, state);
        values.put(DB_Controller.COL_USER_ZIPCODE, zipcode);
        values.put(DB_Controller.COL_USER_UN, userName);
        values.put(DB_Controller.COL_USER_PW, password);
        long insertId = mDatabase.insert(DB_Controller.TABLE_USER, null, values);
        Cursor cursor = mDatabase.query(DB_Controller.TABLE_USER, mAllColumns, DB_Controller.COL_USER_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }
    public void deleteUser(User user) {
        long id = user.getId();
        System.out.println("the deleted user has the id: " + id);
        mDatabase.delete(DB_Controller.TABLE_USER, DB_Controller.COL_USER_ID + " = " + id, null);
    }

    public List<User> getAllUsers() {
        List<User> listUsers = new ArrayList<User>();
        Cursor cursor = mDatabase.query(DB_Controller.TABLE_USER, mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            listUsers.add(user);
            cursor.moveToNext();
        }
        //make sure to close the cursor
        cursor.close();
        return listUsers;
    }

    public User getUserById(long id) {
        Cursor cursor = mDatabase.query(DB_Controller.TABLE_USER, mAllColumns,
                DB_Controller.COL_USER_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        User user = cursorToUser(cursor);
        return user;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setLastName(cursor.getString(1));
        user.setFirstName(cursor.getString(2));
        user.setDOB(cursor.getInt(3));
        user.setSSN(cursor.getInt(4));
        user.setStreet(cursor.getString(5));
        user.setCity(cursor.getString(6));
        user.setState(cursor.getString(7));
        user.setZipcode(cursor.getInt(8));
        user.setUserName(cursor.getString(9));
        user.setPassword(cursor.getString(10));
        return user;

    }
}


