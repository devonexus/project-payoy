package com.example.apaodevo.basura_juan.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class DeviceList extends NavigationDrawerActivity
{
    //widgets
    Button btnPaired;
    ListView devicelist,getDevicelist;
    //Bluetooth
    private BluetoothAdapter myBluetooth,myBlue;
    private Set<BluetoothDevice> pairedDevices;

    public ProgressDialog        progress;
    public static BluetoothSocket btSocket ;
    private boolean               isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID        = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    GlobalData globalData;
    Intent navigate,deploy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_device_list);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_device_list, null, false);
        drawer.addView(contentView, 0);

        fab.setImageResource(R.drawable.floating_navigate_bin);
        fab.setVisibility(View.GONE);

        globalData = (GlobalData) getApplicationContext();
        //Calling widgets
        btnPaired     = (Button) findViewById(R.id.button);
        devicelist    = (ListView) findViewById(R.id.listView);
        getDevicelist = (ListView) findViewById(R.id.listView1);
        //if the device has bluetooth
        myBluetooth   = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //Show a message that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device is not turn on", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        } else if (!myBluetooth.isEnabled()) {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }

    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt: pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            globalData.address = address;
            globalData.name = info;
            //globalData.msg(info);
            if(btSocket != null)
            {
                try {
                    btSocket.connect();//start connection
                }
                catch (Exception io)
                {
                    globalData.msg(io.toString());
                }
            }
            else{
                new ConnectBT().execute(); //Call the class to connect
            }
        }
    };

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected
        @Override
        protected void onPreExecute()
        {
            progress = new ProgressDialog(DeviceList.this, R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Connecting...");
            progress.setMessage("Please wait!!!");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBlue = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice connect = myBlue.getRemoteDevice(globalData.address);//connects to the device's address and checks if it's available
                    btSocket = connect.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
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
                globalData.msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                btSocket = null;
            }
            else
            {
                isBtConnected = true;
                // Make an intent to start next activity.
                if(DeployBinActivity.deploy == "deploy")
                {
                    if (btSocket != null) {
                        try
                        {
                            btSocket.getOutputStream().write("0".toString().getBytes());
                            btSocket.getOutputStream().write("6".toString().getBytes());
                            DeployBinActivity.deploy = "";
                            deploy = new Intent(getApplicationContext(), DeployBinActivity.class);
                            startActivity(deploy);

                        } catch (IOException e) {
                            globalData.msg("Bluetooth Disconnected");
                        }
                    }
                }
                else {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("6".toString().getBytes());
                            navigate = new Intent(DeviceList.this, NavigateBin.class);
                            startActivity(navigate);
                        } catch (IOException e) {
                            globalData.msg("Bluetooth Disconnected");
                        }
                    }
                }
            }
            progress.dismiss();
        }
    }
}
