package com.example.mypath;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFrag extends DialogFragment {
    TextView tvName, tvAddress, tvPhoneNumber, tvNumber;
    Button btnEdit, btnDelete;
    CheckBox cbDone;


    public DetailsFrag() {
        // Required empty public constructor
    }

    public static DetailsFrag newInstance(Address model) {

        Bundle args = new Bundle();
        args.putParcelable("model", model);
        DetailsFrag fragment = new DetailsFrag();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvNumber = view.findViewById(R.id.tvNumber);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);
        cbDone = view.findViewById(R.id.cbDone);
        PackageManager packageManager = tvPhoneNumber.getContext().getPackageManager();

        Address model = getArguments().getParcelable("model");

        tvName.setText(model.getName());
        tvAddress.setText(model.getAddress());
        tvPhoneNumber.setText(model.getPhoneNumber());
        tvNumber.setText(String.valueOf(model.getNumber()));
        cbDone.setChecked(model.isDone());

        tvAddress.setOnClickListener(v->{
            String uri = null;
            try {
                uri = "waze://?ll=" + model.getLatitude() + "," + model.getLongitude() + "&navigate=yes&z=10";
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
                Log.d("tt","go");
            } } catch (Exception e) {
                Toast.makeText(getContext(), "The adress didn't found. ", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        tvPhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + model.getPhoneNumber()));
            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent);
            dismiss();
        });

        btnDelete.setOnClickListener(v->{
            FirebaseDatabase.getInstance().getReference("address").
                    child(model.getKey()).setValue(null);
            dismiss();
        });

        btnDelete.setOnLongClickListener(v->{
            dismiss();
            new AlertDialog.Builder(getActivity()).setMessage("delete all list? ").
                    setPositiveButton("Ok", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("address").
                                setValue(null);
                    }).show();

            return true;
        });

        btnEdit.setOnClickListener(v->{
            AddAddressFrag.newInstance(model).show(getFragmentManager(),"edit");
            dismiss();
        });

        cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
           model.setDone(isChecked);
           FirebaseDatabase.getInstance().getReference("address").child(model.getKey()).setValue(model);

           Marker marker = Markers.getInstance().get(model.getKey());
            if (isChecked) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            }else {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        });
    }
}
