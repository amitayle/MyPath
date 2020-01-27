package com.example.mypath;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAddressFrag extends AppCompatDialogFragment {
    private EditText etName;
    private EditText etAddress;
    private EditText etNumber;
    private EditText etPhoneNumber;
    private Button btnAdd;
    DatabaseReference database;

    public AddAddressFrag() {
        // Required empty public constructor
    }

    public static AddAddressFrag newInstance(Address model) {

        Bundle args = new Bundle();
        args.putParcelable("model", model);
        AddAddressFrag fragment = new AddAddressFrag();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etNumber = view.findViewById(R.id.etNumber);
        btnAdd = view.findViewById(R.id.btnAdd);
        etNumber.setError("Required");


        if (getArguments() == null) {

                btnAdd.setOnClickListener(v -> {

                    DatabaseReference db = FirebaseDatabase.getInstance().
                            getReference("address");

                    DatabaseReference newRowRef = db.push();

                    Address a = null;
                    try {
                        a = new Address(
                                newRowRef.getKey(),
                                getContext(),
                                etName.getText().toString(),
                                etAddress.getText().toString(),
                                etPhoneNumber.getText().toString(),
                                Integer.valueOf(etNumber.getText().toString())
                        );
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter number", Toast.LENGTH_SHORT).show();
                    }
                    newRowRef.setValue(a);

                    dismiss();
                });

        } else  {
            Address model = getArguments().getParcelable("model");
            etName.setText(model.getName());
            etAddress.setText(model.getAddress());
            etNumber.setText(String.valueOf(model.getNumber()));
            etPhoneNumber.setText(model.getPhoneNumber());

            btnAdd.setText("update");
            btnAdd.setOnClickListener(v -> {
                try {
                    model.setName(etName.getText().toString());
                    model.setAddress(etAddress.getText().toString(),getContext());
                    model.setNumber(Integer.valueOf(etNumber.getText().toString()));
                    model.setPhoneNumber(etPhoneNumber.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter number", Toast.LENGTH_SHORT).show();
                }

                DatabaseReference ref = FirebaseDatabase.getInstance().
                        getReference("address");

                DatabaseReference updateRef = ref.child(model.getKey());

                updateRef.setValue(model);
                dismiss();
            });
        }
    }
}
