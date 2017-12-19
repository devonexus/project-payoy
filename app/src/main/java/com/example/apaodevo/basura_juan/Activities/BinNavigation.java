package com.example.apaodevo.basura_juan.Activities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.example.apaodevo.basura_juan.R;

@SuppressWarnings("deprecation")
public class BinNavigation extends TabActivity  {

    TabHost TabHostWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.activity_bin_navigation, null, false);
//        drawer.addView(contentView, 0);
//         fab.setImageResource(R.drawable.activity_bin_navigation);

        setContentView(R.layout.activity_bin_navigation);

        //Assign id to Tabhost.
        TabHostWindow = (TabHost)findViewById(android.R.id.tabhost);

        //Creating tab menu.
        TabHost.TabSpec TabMenu1 = TabHostWindow.newTabSpec("Bin Location Tab");
        TabHost.TabSpec TabMenu2 = TabHostWindow.newTabSpec("Deploy Bin Tab");
        TabHost.TabSpec TabMenu3 = TabHostWindow.newTabSpec("Register Bin Tab");
        TabHost.TabSpec TabMenu4 = TabHostWindow.newTabSpec("Navigation Bin Tab");

        //Setting up tab 1 name.
        TabMenu1.setIndicator("Bin Location");
        //Set tab 1 activity to tab 1 menu.
        TabMenu1.setContent(new Intent(this,BinLocationActivity.class));

        //Setting up tab 2 name.
        TabMenu2.setIndicator("Deploy Bin");
        //Set tab 3 activity to tab 1 menu.
        TabMenu2.setContent(new Intent(this,DeployBinActivity.class));

        //Setting up tab 2 name.
        TabMenu3.setIndicator("Register Bin");
        //Set tab 3 activity to tab 3 menu.
        TabMenu3.setContent(new Intent(this,RegisterBin.class));

        //Setting up tab 2 name.
        TabMenu4.setIndicator("Navigate Bin");
        //Set tab 3 activity to tab 3 menu.
        TabMenu4.setContent(new Intent(this,DeviceList.class));

        //Adding tab1, tab2, tab3 to tabhost view.

        TabHostWindow.addTab(TabMenu1);
        TabHostWindow.addTab(TabMenu2);
        TabHostWindow.addTab(TabMenu3);
        TabHostWindow.addTab(TabMenu4);

    }
}