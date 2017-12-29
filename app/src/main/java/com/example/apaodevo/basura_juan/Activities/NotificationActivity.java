package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.apaodevo.basura_juan.R;

public class NotificationActivity extends NavigationDrawerActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Put navigation drawer on this layout
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);

    }
}
