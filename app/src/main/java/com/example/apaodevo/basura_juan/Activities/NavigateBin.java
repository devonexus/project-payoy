package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;

import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.FrameLayout;
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

    private Button  btnRight,btnLeft,btnAutomationpause,btnForward,btnDisconnect,btnStopDeployment;
    GlobalData      globalData;
    private String  auto = "Pause Automation";
    Intent          bluetooth,home;
    TextView        binConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalData = (GlobalData) getApplicationContext();

        //Intent newint = getIntent();
        //globalData.address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        if(globalData.address == null) {
            bluetooth = new Intent(NavigateBin.this, DeviceList.class);
            Toast.makeText(getApplicationContext(), "Please Connect First to a SPP bluetooth", Toast.LENGTH_SHORT).show();
            startActivity(bluetooth);
        }
        if(DeviceList.btSocket == null)
        {
            home = new Intent(NavigateBin.this, HomeActivity.class);
            startActivity(home);
        }

        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_navigate_bin, null, false);
        drawer.addView(contentView, 0);

        fab.setImageResource(R.drawable.floating_navigate_bin);
        fab.setVisibility(View.GONE);

        //call the widgets
        btnRight           = (Button)   findViewById(R.id.btnRight);
        btnLeft            = (Button)   findViewById(R.id.btnLeft);
        btnForward         = (Button)   findViewById(R.id.btnForward);
        btnDisconnect      = (Button)   findViewById(R.id.btnDisconnect);
        btnAutomationpause = (Button)   findViewById(R.id.btnAutomationPause);
        binConnected       = (TextView) findViewById(R.id.txtBinConnected);
        btnStopDeployment  = (Button)   findViewById(R.id.btnStopDeployment);

        binConnected.setText(globalData.name);
        if(auto == "Pause Automation") {
            btnForward.setEnabled(false);
            btnLeft.setEnabled(false);
            btnRight.setEnabled(false);
        }
        else {
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
                turnRight();
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
        btnStopDeployment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopDeployment();
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
                    btnRight.getBackground().setAlpha(255);
                    btnForward.getBackground().setAlpha(255);
                    btnLeft.getBackground().setAlpha(255);
                }
                else if(auto == "Automatic") {
                    Automation();
                    btnAutomationpause.setText("Pause Automation");
                    auto = "Pause Automation";
                    btnForward.setEnabled(false);
                    btnLeft.setEnabled(false);
                    btnRight.setEnabled(false);
                    btnRight.getBackground().setAlpha(100);
                    btnForward.getBackground().setAlpha(100);
                    btnLeft.getBackground().setAlpha(100);

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
        int blueSubActionButtonSize;
        blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        FrameLayout.LayoutParams ContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        itemBuilder.setLayoutParams(ContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams conParams = new FrameLayout.LayoutParams(blueSubActionButtonSize,blueSubActionButtonSize);
        itemBuilder.setLayoutParams(conParams);
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.bin_location_icon);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.deploy);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_action_register_bin);

        final SubActionButton sabLocateBin = itemBuilder
                .setBackgroundDrawable(ContextCompat.getDrawable(NavigateBin.this, R.drawable.bin_location_icon))
                .build();
        final SubActionButton sabDeployBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabRegisterBin = itemBuilder.setContentView(itemIcon3).build();

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabLocateBin)
                .addSubActionView(sabDeployBin)
                .addSubActionView(sabRegisterBin)
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
                if(DeviceList.btSocket != null)
                {
                    globalData.msg("Please disconnect currently connected bin");
                }
                else {
                    startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
                }
            }
        });
        sabRegisterBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterBin.class));
            }
        });
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionButton.setVisibility(View.INVISIBLE);
                sabLocateBin.setVisibility(View.INVISIBLE);
                sabDeployBin.setVisibility(View.INVISIBLE);
                sabRegisterBin.setVisibility(View.INVISIBLE);
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
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }//Display floating action button with circular animation

    private void Disconnect() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("7".toString().getBytes());
                DeviceList.btSocket.getOutputStream().write("0".toString().getBytes());
                globalData.msg(globalData.name + "Disconnected");
                DeviceList.btSocket.close(); //close connection
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";

                home = new Intent(NavigateBin.this, HomeActivity.class);
                startActivity(home);
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
        finish(); //return to the first layout
    }

    private void forward() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("2".toString().getBytes());
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
    }

    private void StopDeployment() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("8".toString().getBytes());
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
    }

    public void turnLeft() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("3".toString().getBytes());
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
    }
    private void turnRight() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("4".toString().getBytes());
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
    }

    private void StopAutomate() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
    }
    public void Automation() {
        if (DeviceList.btSocket!=null) {
            try {
                DeviceList.btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e) {
                globalData.msg("Bin Disconnected");
                DeviceList.btSocket = null;
                globalData.address = "";
                globalData.name = "";
            }
        }
    }

}