package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


import com.example.apaodevo.basura_juan.R;

public class NavigateBin extends NavigationDrawerActivity {

    Button                  btnRight,btnLeft,btnAutomationpause,btnForward,btnDisconnect;
    String                  address        = null;
    private ProgressDialog  progress;
    BluetoothAdapter        myBluetooth    = null;
    BluetoothSocket         btSocket       = null;
    private boolean         isBtConnected  = false;
    //SPP UUID. Look for it
    static final UUID myUUID               = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String auto ="Pause Automation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        if(newint != null) {
            address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device
        }
        else
        {
            Intent bluetooth = new Intent(NavigateBin.this, DeviceList.class);
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
        fab.setSize(100);

        //call the widgets
        btnRight           = (Button)   findViewById(R.id.btnRight);
        btnLeft            = (Button)   findViewById(R.id.btnLeft);
        btnForward         = (Button)   findViewById(R.id.btnForward);
        btnDisconnect      = (Button)   findViewById(R.id.btnDisconnect);
        btnAutomationpause = (Button)  findViewById(R.id.btnAutomationPause);

        new ConnectBT().execute(); //Call the class to connect

        btnRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                turnRight(); // method to turn Right
                return true;
            }
        });

        btnLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                turnLeft(); // method to turn Right
                return true;
            }
        });

        btnForward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                forward(); // method to Forward
                return true;
            }
        });

        btnAutomationpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auto == "Pause Automation") {
                    StopAutomate(); // method to Stop Automates
                    btnAutomationpause.setText("Automatic");
                    auto = "Automatic";
                }
                else if(auto == "Automatic")
                {
                    Automation();
                    btnAutomationpause.setText("Pause Automation");
                    auto = "Pause Automation";
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
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void forward()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("2".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnLeft()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("3".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnRight()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("4".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void StopAutomate()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void Automation()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("5".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(NavigateBin.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}