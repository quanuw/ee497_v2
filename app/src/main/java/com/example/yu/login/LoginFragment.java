package com.example.yu.login;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private OnLoginListener callback;

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

}
