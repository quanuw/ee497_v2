package com.example.yu.login;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private OnLoginListener callback;

    // Interface for logging in
    public interface OnLoginListener {
        public void onLogin(String username, String password);
    }
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnLoginListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnLoginListener!");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText username = (EditText) rootView.findViewById(R.id.loginUsername);
        final EditText password = (EditText) rootView.findViewById(R.id.loginPassword);

        // Login button.
        Button button = (Button) rootView.findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                callback.onLogin(usernameStr, passwordStr);
            }
        });

        return rootView;
    }

    // TODO: 5/12/17
    // Sample check of user credentials
    public static boolean checkCredentials(String username, String password) {
        // Get a database manager
        DatabaseManager databaseManager = new DatabaseManager();

        // Open the database to write
        SQLiteDatabase readable = databaseManager.openDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = { User.KEY_UserId, User.KEY_LoginName, User.KEY_LoginPW };

        // TODO: 5/12/17
        // Not sure if this is the correct selection
        // Filter results WHERE "LoginName" = username AND "LoginPW" = password
        String selection = User.KEY_LoginName + " = ?" + " AND " + User.KEY_LoginPW + " = ?";
        String[] selectionArgs = { username, password };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = User.KEY_LoginName + " DESC";

        // Look through the rows
        Cursor cursor = readable.query(User.TABLE, projection, selection, selectionArgs,
                null, null, sortOrder);

        if (cursor.moveToFirst()) {
            return true; // There is at least one row returned from query
        }
        return false; // No rows returned from query
    }

}
