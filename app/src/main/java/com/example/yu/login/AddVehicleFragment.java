package com.example.yu.login;


import android.os.Bundle;
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

    public static AddVehicleFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AddVehicleFragment fragment = new AddVehicleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddVehicleFragment() {
        // Required empty public constructor
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
                String VIN = vehicleVIN.getText().toString();

                // Simple validation. Only checks empty fields.
                if (model.equals("") || make.equals("") || year.equals("") || VIN.equals("")) {
                    Toast.makeText(getActivity(), "INPUTS MUST BE FILLED.", Toast.LENGTH_LONG).show();
                } else {
                    // Add into db
                }
            }
        });
        return rootView;
    }

}
