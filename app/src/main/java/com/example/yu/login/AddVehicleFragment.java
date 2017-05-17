package com.example.yu.login;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVehicleFragment extends Fragment {

    private Button addButton;

    private EditText vehicleModel;
    private EditText vehicleMake;
    private EditText vehicleYear;
    private EditText vehicleVIN;

    private OnAddVehicleListener callback;

    public static AddVehicleFragment newInstance() {

        Bundle args = new Bundle();

        AddVehicleFragment fragment = new AddVehicleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddVehicleFragment() {
        // Required empty public constructor
    }

    // Interface for adding vechicles
    public interface OnAddVehicleListener {
        public void onAddVehicle(String model, String make, String year, String vin, int userId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnAddVehicleListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnAddVehicleListener!");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        addButton = (Button) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Get input fields
                vehicleModel = (EditText) rootView.findViewById(R.id.addModel);
                vehicleMake = (EditText) rootView.findViewById(R.id.addMake);
                vehicleYear = (EditText) rootView.findViewById(R.id.addYear);
                vehicleVIN = (EditText) rootView.findViewById(R.id.addVIN);

                // Store vehicle information
                String model = vehicleModel.getText().toString();
                String make = vehicleMake.getText().toString();
                String year = vehicleYear.getText().toString();
                String vin = vehicleVIN.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                int currUserId = sharedPreferences.getInt("userId", -2);
                System.out.println("AddVehicleFragment" + Integer.toString(currUserId));
                // Simple validation. Only checks empty fields.
                if (model.equals("") || make.equals("") || year.equals("") || vin.equals("")) {
                    Toast.makeText(getActivity(), "INPUTS MUST BE FILLED.", Toast.LENGTH_LONG).show();
                } else {
                    // Add into db
                    callback.onAddVehicle(model, make, year, vin, currUserId);
                }
            }
        });
        return rootView;
    }
}