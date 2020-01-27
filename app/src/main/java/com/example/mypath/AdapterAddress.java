package com.example.mypath;

import android.annotation.SuppressLint;   //??? get receorsess
import android.graphics.Color;  //??? get receorsess
import android.util.Log;  //??? get receorsess
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAddress extends FirebaseRecyclerAdapter<Address, AdapterAddress.ViewHolder> {
    private FragmentManager fragmentManager;


    public AdapterAddress(@NonNull FirebaseRecyclerOptions<Address> options, FragmentManager fragmentManager) {
        super(options);
        this.fragmentManager = fragmentManager;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull Address address) {
        holder.tvName.setText(address.getName());
        holder.tvAddress.setText(address.getAddress());
        holder.tvNumber.setText(String.valueOf(address.getNumber()));
        holder.model = address;


        if (address.isDone()){
            holder.addressItem.setBackgroundColor(R.color.gray);
        }else{
            holder.addressItem.setBackgroundColor(R.color.white);
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvAddress;
        TextView tvNumber;
        Address model;
        ConstraintLayout addressItem;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            addressItem = itemView.findViewById(R.id.addressItem);

//          if (model!= null && model.isDone())
//              itemView.setBackgroundColor(R.color.gray);

            itemView.setOnClickListener(v->{
                MapsActivity activity = new MapsActivity();
                activity.moveCamera(model.getKey());

            });


            itemView.setOnLongClickListener(v ->{
                DetailsFrag.newInstance(model).show(fragmentManager,"edit");
                return true;
            });

        }

        private int setColor() {
            if(model.isDone()){
                return R.color.gray;
            }else {
                return R.color.white;
            }
        }


    }
}
