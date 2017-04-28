package layout;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yu.login.R;

import java.util.Calendar;
import java.util.regex.Matcher;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    private OnRegisterListener callback;
    private ToSignInListener toSignInCallback;
    private String dobStr;



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

        EditText dob = (EditText) rootView.findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        final EditText firstName = (EditText) rootView.findViewById(R.id.firstName);
        final EditText lastName = (EditText) rootView.findViewById(R.id.lastName);
        final EditText email = (EditText) rootView.findViewById(R.id.email);
        //final EditText dob = (EditText) rootView.findViewById(R.id.email);
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
                //String dobStr = dob.getText().toString();
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                // check fields
                if (firstNameStr.equals("") || lastNameStr.equals("") || emailStr.equals("") ||
                    usernameStr.equals("") || passwordStr.equals("")) {
                    Toast.makeText(getActivity(), "Please fill out all fields!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isValidEmailAddress(emailStr)) {
                    Toast.makeText(getActivity(), emailStr + " is not valid.", Toast.LENGTH_LONG).show();
                    return;
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

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public static boolean isValidEmailAddress(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{

        private OnDatePickListener callback;

        public interface OnDatePickListener {
            public void onDatePick(String dob);
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int monthIndexOne = month + 1;
            String monthStr = "" + monthIndexOne;
            String day = "" + dayOfMonth;
            if (monthIndexOne < 10) {
                monthStr = "0" + monthIndexOne;
            }
            if (dayOfMonth < 10) {
                day = "0" +dayOfMonth;
            }

            String dob = monthStr + "/" + day + "/" + year;

            callback.onDatePick(dob);
        }
    }



}
