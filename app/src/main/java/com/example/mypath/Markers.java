package com.example.mypath;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class Markers {
    private static  Markers ourInstance;

    private HashMap<String,Marker> markers = new HashMap<>();

    public static Markers getInstance() {
        if (ourInstance == null)
            ourInstance = new Markers();
        return ourInstance;
    }

    private Markers() {
    }

    public void put(String key, Marker marker){
        markers.put(key,marker);
    }
    public void remove(String key){
        markers.remove(key);
    }
    public Marker get(String key) {
        Marker marker = markers.get(key);
        return marker;
    }

}
