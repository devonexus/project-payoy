package com.example.apaodevo.basura_juan.Activities;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;


        import com.example.apaodevo.basura_juan.R;



public class HomeActivity extends NavigationDrawerActivity{
    private Button btn_bin_location, btn_deploy_bin, btn_register_bin, btn_navigate_bin;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Put navigation drawer on this layout
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);

        //Go to bin location interface
        btn_bin_location = (Button) findViewById(R.id.btn_de);
        btn_bin_location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), BinLocationActivity.class));
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
                startActivity(new Intent(getApplicationContext(), NavigateBin.class));
            }
        });


    }


}