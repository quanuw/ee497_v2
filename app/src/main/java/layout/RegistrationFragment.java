package layout;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yu.login.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    private OnRegisterListener callback;
    private ToSignInListener toSignInCallback;

    //interface supported by anyone who can respond to this Fragment's clicks
    public interface OnRegisterListener {
        public void onRegistration(String firstName, String lastName, String email, String dob,
                                   String username, String password);
    }

    //interface supported by anyone who can respond to this Fragment's clicks
    public interface ToSignInListener {
        public void toSignIn();
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        
        Bundle args = new Bundle();
        
        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnRegisterListener) context;
            toSignInCallback = (ToSignInListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRegistrationListner and ToSignInListener!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_registration, container, false);
        Button registerButton = (Button) rootView.findViewById(R.id.registerButton);
        if (registerButton == null) {
            Log.e(TAG, "Button is null!");
        }

        final EditText firstName = (EditText) rootView.findViewById(R.id.firstName);
        final EditText lastName = (EditText) rootView.findViewById(R.id.lastName);
        final EditText email = (EditText) rootView.findViewById(R.id.email);
        final EditText dob = (EditText) rootView.findViewById(R.id.email);
        final EditText username = (EditText) rootView.findViewById(R.id.username);
        final EditText password = (EditText) rootView.findViewById(R.id.password);


        TextView signin = (TextView) rootView.findViewById(R.id.signin);

        // Register new user
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameStr= firstName.getText().toString();
                String lastNameStr = lastName.getText().toString();
                String emailStr = email.getText().toString();
                String dobStr = dob.getText().toString();
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                if (callback == null) {
                    Log.e(TAG, "callback is null");
                }
                callback.onRegistration(firstNameStr, lastNameStr, emailStr, dobStr, usernameStr,
                        passwordStr);
            }
        });

        // Go to login page
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toSignInCallback.toSignIn();
            }
        });

        return rootView;
    }

}
