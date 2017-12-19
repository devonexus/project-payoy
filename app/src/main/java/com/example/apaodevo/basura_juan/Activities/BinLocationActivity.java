package com.example.apaodevo.basura_juan.Activities;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;


import com.example.apaodevo.basura_juan.R;




public class BinLocationActivity extends NavigationDrawerActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Set up navigation drawer
//        setContentView(R.layout.activity_bin_location);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_bin_location, null, false);
        drawer.addView(contentView, 0);
        fab.setImageResource(R.drawable.bin_location_icon);



    }

}
