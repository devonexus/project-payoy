package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;

import java.io.IOException;


public class HomeActivity extends NavigationDrawerActivity{
    private Button btn_bin_location, btn_deploy_bin, btn_register_bin, btn_navigate_bin;
    private ProgressDialog pDialog;
    GlobalData globalData;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Put navigation drawer on this layout
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);
        globalData = (GlobalData) getApplicationContext();
//        //Go to bin location interface
//        btn_bin_location = (Button) findViewById(R.id.btn_de);
//        btn_bin_location.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//                    }
//                }
//        );
        //Go to bin location interface
        btn_bin_location = (Button) findViewById(R.id.btn_de);
        btn_bin_location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(getApplicationContext(), BinLocationActivity.class));
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    }
                }
        );

        //Go to deploy bin interface
        btn_deploy_bin = (Button) findViewById(R.id.btn_deploy_bin);
        btn_deploy_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
            }
        });
        //Go to register bin interface

        btn_register_bin    = (Button) findViewById(R.id.btn_register_bin);
        btn_register_bin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), RegisterBin.class));
                    }
                }
        );
        btn_navigate_bin    = (Button) findViewById(R.id.btn_navigate_bin);
        btn_navigate_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( globalData.address == "") {
                    startActivity(new Intent(getApplicationContext(), DeviceList.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), NavigateBin.class));
                }
            }
        });
        initializeProgressDialogState();


    }
    private void initializeProgressDialogState(){
        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Signing out, Please wait...");
        pDialog.setCancelable(false);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        signOut();
    }
    private void signOut(){
        showMessage("Logout","This is the last activity, going back will log you out of the application.");
    }
    public void showMessage(String title,String Message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_exit_to_app_white_48dp);
        builder.setMessage(Message);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                showpDialog();
                Thread thread = new Thread() {

                    @Override
                    public void run() {

                        // Block this thread for 4 seconds.al
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.putExtra("LOGIN_STATUS", false);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
                                startActivity(intent);
                                finish();
                                hidepDialog();
                                Disconnect();
                            }
                        });

                    }

                };
                thread.start();
            }
        });
        builder.show();
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }//Display progress dialog

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }//Dismiss progressDialog

    private void Disconnect()
    {
        if (NavigateBin.btSocket!=null) //If the btSocket is busy
        {
            try
            {
                NavigateBin.btSocket.close(); //close connection
            }
            catch (IOException e)
            { globalData.msg("Error");}
        }
        finish(); //return to the first layout
    }
}
//package com.example.apaodevo.basura_juan.Activities;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//
//import android.os.Bundle;
//
//import android.view.LayoutInflater;
//
//import android.view.View;
//import android.widget.Button;
//
//
//import com.example.apaodevo.basura_juan.R;
//
//
//
//public class HomeActivity extends NavigationDrawerActivity{
//    private Button btn_bin_location, btn_deploy_bin, btn_register_bin, btn_navigate_bin;
//    private ProgressDialog pDialog;
//    @Override
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //Put navigation drawer on this layout
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.activity_home, null, false);
//        drawer.addView(contentView, 0);
//        fab.setVisibility(View.INVISIBLE);
//
//        //Go to bin location interface
//        btn_bin_location = (Button) findViewById(R.id.btn_de);
//        btn_bin_location.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //startActivity(new Intent(getApplicationContext(), BinLocationActivity.class));
//                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//                    }
//                }
//        );
//
//        //Go to deploy bin interface
//        btn_deploy_bin = (Button) findViewById(R.id.btn_deploy_bin);
//        btn_deploy_bin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
//            }
//        });
//        //Go to register bin interface
//
//        btn_register_bin    = (Button) findViewById(R.id.btn_register_bin);
//        btn_register_bin.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getApplicationContext(), RegisterBin.class));
//                    }
//                }
//        );
//        btn_navigate_bin    = (Button) findViewById(R.id.btn_navigate_bin);
//        btn_navigate_bin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), DeviceList.class));
//            }
//        });
//        initializeProgressDialogState();
//
//
//    }
//    private void initializeProgressDialogState(){
//
//        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
//
//        pDialog = new ProgressDialog(this);
//
//        pDialog.setMessage("Signing out, Please wait...");
//        pDialog.setCancelable(false);
//    }
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        signOut();
//    }
//    private void signOut(){
//        showMessage("Logout","This is the last activity, going back will log you out of the application.");
//    }
//    public void showMessage(String title, final String Message){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//
//        builder.setIcon(R.drawable.ic_exit_to_app_white_48dp);
//        builder.setMessage(Message);
//        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//
//
//        builder.setMessage(Message);
//        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        dialog.dismiss();
//                    }
//                });
//                showpDialog();
//                Thread thread = new Thread() {
//
//                    @Override
//                    public void run() {
//
//                        // Block this thread for 4 seconds.al
//                        try {
//                            Thread.sleep(1500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                intent.putExtra("LOGIN_STATUS", false);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
//                                startActivity(intent);
//                                finish();
//                                hidepDialog();
//                            }
//                        });
//
//                    @Override
//                    public void run() {
//
//                        // Block this thread for 4 seconds.al
//                        try {
//                            Thread.sleep(1500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                intent.putExtra("LOGIN_STATUS", false);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
//                                startActivity(intent);
//                                finish();
//                                hidepDialog();
//                            }
//                        });
//                    }
//
//                };
//                thread.start();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
//    }
//    private void showpDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }//Display progress dialog
//
//    private void hidepDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }//Dismiss progressDialog
//}