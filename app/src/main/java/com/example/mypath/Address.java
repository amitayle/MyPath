package com.example.mypath;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class Address implements Parcelable {
    private String key;
    private String name;
    private String address;
    private String  phoneNumber;
    private int number;
    private double latitude;
    private double longitude;
    private boolean isDone;



    public Address() {}

    public Address(String key, Context context, String name, String address, String phoneNumber, int number) {
        this.key = key;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.number = number;

        toLatLng(address,context);
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address,Context context) {
        this.address = address;
        toLatLng(address,context);
        Marker marker = Markers.getInstance().get(key);
        marker.remove();
        Markers.getInstance().remove(key);
        isDone = false;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public boolean isDone() {
        return isDone;
    }
    public void setDone(boolean done) {
        isDone = done;
    }

    private void toLatLng(String address, Context context) {
        Geocoder geocoder = new Geocoder(context);

        try {
            if (geocoder.getFromLocationName(address, 1).size() != 0) {
                android.location.Address a = geocoder.getFromLocationName(address, 1).get(0);
//                LatLng mLatLng = new LatLng(a.getLatitude(), a.getLongitude());
                latitude = a.getLatitude();
                longitude = a.getLongitude();

            }
        } catch (IOException e) {
            Toast.makeText(context, "The adress didn't found. ", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public String toString() {
        return "Address{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", number=" + number +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isDone=" + isDone +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeInt(this.number);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeByte(this.isDone ? (byte) 1 : (byte) 0);
    }

    protected Address(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.number = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.isDone = in.readByte() != 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}