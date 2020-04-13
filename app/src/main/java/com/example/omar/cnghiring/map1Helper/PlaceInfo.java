package com.example.omar.cnghiring.map1Helper;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String name, address, phoneNumber, attributions;
    private Uri websiteUri;
    private LatLng latLng;

    public PlaceInfo(String name, String address, String phoneNumber, String attributions, Uri websiteUri, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.attributions = attributions;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
    }

    public PlaceInfo() {
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
