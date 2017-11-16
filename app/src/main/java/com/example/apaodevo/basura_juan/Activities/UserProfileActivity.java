package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.PathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by apaodevo on 11/16/2017.
 */


public class UserProfileActivity extends NavigationDrawerActivity{
    private GlobalData globalData;
    private TextView etlname, etfname, etminitial, etuname, etpword, etemail;
    private Button btn_update;
    private ImageView imageView;
    private Uri uri;
    private static int RESULT_LOAD_IMAGE = 1;
    private String image_path, displayName;
    private TextInputLayout inputFname, inputLname, inputMinitial, inputUsername, inputPassword, inputEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_profile, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);
        //castObjects();

        globalData = (GlobalData) getApplicationContext();
        btn_update  = (Button) findViewById(R.id.btn_update);
        etlname     = (EditText) findViewById(R.id.etLname);
        etfname     = (EditText) findViewById(R.id.etFname);
        etuname     = (EditText) findViewById(R.id.etUname);
        etemail     = (EditText) findViewById(R.id.etEmail);
        etminitial  = (EditText) findViewById(R.id.etMinitial);
        etpword     = (EditText) findViewById(R.id.etPword);
        imageView   = (ImageView) findViewById(R.id.img_login_user);
        inputFname      = (TextInputLayout) findViewById(R.id.input_fname);
        inputLname      = (TextInputLayout) findViewById(R.id.input_lname);
        inputMinitial   = (TextInputLayout) findViewById(R.id.input_minitial);
        inputUsername   = (TextInputLayout) findViewById(R.id.input_uname);
        inputPassword   = (TextInputLayout) findViewById(R.id.input_pword);
        inputEmail      = (TextInputLayout) findViewById(R.id.input_email);
        GradientDrawable sd = (GradientDrawable) btn_update.getBackground();
        sd.setColor(Color.rgb(199, 0, 57));

        etlname.setText(globalData.getLastname());
        etfname.setText(globalData.getFirstname());
        etminitial.setText(globalData.getMinitial());
        etemail.setText(globalData.getEmailAddress());
        etuname.setText(globalData.getUsername());
        etpword.setText(globalData.getPassword());
        Picasso.with(this).load(globalData.getImageUrl()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

    }

    private void castObjects(){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {

                uri = data.getData();
                image_path = PathUtil.getPathFromURI(this, uri);
                File f = new File(uri.getPath());



                displayName = null;
                Picasso.with(this).load(uri).into(imageView);
                if (uri.toString().startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = this.getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uri.toString().startsWith("file://")) {
                    displayName = f.getName();
                }

            }

        }


    }

    private void getImageFromAlbum(){
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);

        }catch(Exception e){
            Log.i("Error", e.toString());
        }
    }
}
