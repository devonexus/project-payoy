package com.example.apaodevo.basura_juan.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apaodevo.basura_juan.R;

/**
 * Created by Brylle on 2/11/2018.
 */

public class DeployedBinFragment extends Fragment{
    public DeployedBinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_bin_list, container, false);
    }
}
