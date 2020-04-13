package com.example.omar.cnghiring.map1Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.omar.cnghiring.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomPlaceInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context context;

    public CustomPlaceInfoWindowAdapter(Context context) {
        this.context = context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.custom_place_info_window, null);

    }

    private void rendomWindowText(Marker marker, View view)
    {
        String title = marker.getTitle();
        TextView tv_title = (TextView) view.findViewById(R.id.cpiw_title);
        TextView tv_snippet = (TextView) view.findViewById(R.id.cpiw_snippet);
        String snippet = marker.getSnippet();


        try {
            if (!title.equals("")) {
                tv_title.setText(title);
            }

            if (!snippet.equals("")) {
                tv_snippet.setText(snippet);
            }
        }
        catch (NullPointerException e)
        {
            tv_title.setText(title);
            tv_snippet.setText(snippet);

        }


    }



    @Override
    public View getInfoWindow(Marker marker) {
        rendomWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendomWindowText(marker, mWindow);
        return mWindow;
    }
}
