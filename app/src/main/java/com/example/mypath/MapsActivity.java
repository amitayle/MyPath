package com.example.mypath;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.mypath.Move.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public  GoogleMap mMap;
    private FloatingActionButton fabAddAddress;
    private RecyclerView rvAddresses;
    private AdapterAddress adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map,mapFragment).commit();
        mapFragment.getMapAsync(this);

        rvAddresses = findViewById(R.id.rvAddresses);

        Query q = FirebaseDatabase.getInstance().getReference("address").orderByChild("number");
        FirebaseRecyclerOptions<Address> options = new FirebaseRecyclerOptions.
                Builder<Address>().
                setQuery(q, Address.class).build();
        adapter = new AdapterAddress(options,getSupportFragmentManager());
        rvAddresses.setAdapter(adapter);

        rvAddresses.setLayoutManager(new LinearLayoutManager(this));


        fabAddAddress = findViewById(R.id.fabAddAddress);
        fabAddAddress.setOnClickListener(v->{
            new AddAddressFrag().show(getSupportFragmentManager(),"add");
        });

       // readFromDataBase();
        adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        }

    private void readFromDataBase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().
                getReference("address");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Address adr = child.getValue(Address.class);
                    LatLng latLng = new LatLng(adr.getLatitude(),adr.getLongitude());

                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(adr.getName()).icon(icons(adr)));


                  //  Markers markers = Markers.getInstance();
                    Markers.getInstance().put(adr.getKey(),marker);

                }
            }

            private BitmapDescriptor icons(Address address) {
                if (address.isDone()) {
                    return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                }else {
                    return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    //how to make this workkkkkkk??????
    public void moveCamera(String key){
        Marker marker = Markers.getInstance().get(key);
      m = marker;
    }



    Marker m = null;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showLocation();
        readFromDataBase();
        if (m != null)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),15));

    }

    private final static int RC_LOCATION = 0;
    private void showLocation() {
        String location = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ActivityCompat.checkSelfPermission(this,location) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{location},RC_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            showLocation();
    }



}
