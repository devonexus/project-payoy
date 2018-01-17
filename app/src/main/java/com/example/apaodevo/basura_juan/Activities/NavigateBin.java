package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;

import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class NavigateBin extends NavigationDrawerActivity {

    private Button                btnRight,btnLeft,btnAutomationpause,btnForward,btnDisconnect;
    GlobalData                    globalData;
    private String                auto          ="Pause Automation";
    Intent bluetooth,home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalData = (GlobalData) getApplicationContext();
        TextView binConnected = (TextView) findViewById(R.id.txtBinConnected);

        //binConnected.setText(globalData.name);
        //Intent newint = getIntent();
       // globalData.address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device
        if(globalData.address == null) {
            bluetooth = new Intent(NavigateBin.this, DeviceList.class);
            Toast.makeText(getApplicationContext(), "Please Connect First to a SPP bluetooth", Toast.LENGTH_SHORT).show();
            startActivity(bluetooth);
        }

        //setContentView(R.layout.activity_navigate_bin);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_navigate_bin, null, false);
        drawer.addView(contentView, 0);

        fab.setImageResource(R.drawable.floating_navigate_bin);
        fab.setVisibility(View.GONE);

        //call the widgets
        btnRight           = (Button)   findViewById(R.id.btnRight);
        btnLeft            = (Button)   findViewById(R.id.btnLeft);
        btnForward         = (Button)   findViewById(R.id.btnForward);
        btnDisconnect      = (Button)   findViewById(R.id.btnDisconnect);
        btnAutomationpause = (Button)  findViewById(R.id.btnAutomationPause);

        if(auto == "Pause Automation") {
            btnForward.setEnabled(false);
            btnLeft.setEnabled(false);
            btnRight.setEnabled(false);
        }
        else
        {
            btnForward.setEnabled(true);
            btnLeft.setEnabled(true);
            btnRight.setEnabled(true);
        }

        btnRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                turnRight(); // method to turn Right
                return true;
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnRight();;
            }
        });
        btnLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                turnLeft(); // method to turn Right
                return true;
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnLeft();;
            }
        });

        btnForward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                forward(); // method to Forward
                return true;
            }
        });
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward();
            }
        });
        btnAutomationpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auto == "Pause Automation") {
                    StopAutomate(); // method to Stop Automates
                    btnAutomationpause.setText("Automatic");
                    auto = "Automatic";
                    btnForward.setEnabled(true);
                    btnLeft.setEnabled(true);
                    btnRight.setEnabled(true);
                }
                else if(auto == "Automatic")
                {
                    Automation();
                    btnAutomationpause.setText("Pause Automation");
                    auto = "Pause Automation";
                    btnForward.setEnabled(false);
                    btnLeft.setEnabled(false);
                    btnRight.setEnabled(false);
                }
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Automation();
                Disconnect(); //close connection
            }
        });

        displayFloatingActionButton();
    }
    private void displayFloatingActionButton(){
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.floating_navigate_bin)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.bin_location_icon);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.deploy);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_action_register_bin);

        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.drawable.home_button);

        final SubActionButton sabLocateBin = itemBuilder
                .setBackgroundDrawable(ContextCompat.getDrawable(NavigateBin.this, R.drawable.bin_location_icon))
                .build();
        final SubActionButton sabDeployBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabRegisterBin = itemBuilder.setContentView(itemIcon3).build();
        final SubActionButton sabHome = itemBuilder.setContentView(itemIcon4).build();

        //attach the sub buttons to the main button
        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabLocateBin)
                .addSubActionView(sabDeployBin)
                .addSubActionView(sabRegisterBin)
                .addSubActionView(sabHome)
                .attachTo(actionButton)
                .build();

        sabLocateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        sabDeployBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
            }
        });
        sabRegisterBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterBin.class));
            }
        });

        sabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionButton.setVisibility(View.INVISIBLE);
                sabLocateBin.setVisibility(View.INVISIBLE);
                sabDeployBin.setVisibility(View.INVISIBLE);
                sabRegisterBin.setVisibility(View.INVISIBLE);
                sabHome.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionButton.setVisibility(View.VISIBLE);
                sabLocateBin.setVisibility(View.VISIBLE);
                sabDeployBin.setVisibility(View.VISIBLE);
                sabRegisterBin.setVisibility(View.VISIBLE);
                sabHome.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }//Display floating action button with circular animation

    private void Disconnect()
    {
        if (DeviceList.btSocket!=null) //If the btSocket is busy
        {
            try
            {
                DeviceList.btSocket.close(); //close connection
                DeviceList.btSocket = null;
                globalData.address = "";
                home = new Intent(this, HomeActivity.class);
                startActivity(home);
            }
            catch (IOException e)
            {
                globalData.msg("Error");
            }
        }
        finish(); //return to the first layout

    }

    private void forward()
    {
        if (DeviceList.btSocket!=null)
        {
            try
            {
                DeviceList.btSocket.getOutputStream().write("2".toString().getBytes());
            }
            catch (IOException e)
            {
                globalData.msg("Error");
            }
        }
    }

    public void turnLeft()
    {
        if (DeviceList.btSocket!=null)
        {
            try
            {
                DeviceList.btSocket.getOutputStream().write("3".toString().getBytes());
            }
            catch (IOException e)
            {
                globalData.msg("Error");
            }
        }
    }
    private void turnRight()
    {
        if (DeviceList.btSocket!=null)
        {
            try
            {
                DeviceList.btSocket.getOutputStream().write("4".toString().getBytes());
            }
            catch (IOException e)
            {
                globalData.msg("Error");
            }
        }
    }

    private void StopAutomate()
    {
        if (DeviceList.btSocket!=null)
        {
            try
            {
                DeviceList.btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e)
            {
                globalData.msg("Error");
            }
        }
    }
    public void Automation()
    {
        if (DeviceList.btSocket!=null)
        {
            try
            {
                DeviceList.btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e)
            {
                globalData.msg("Error");
            }
        }
    }

}