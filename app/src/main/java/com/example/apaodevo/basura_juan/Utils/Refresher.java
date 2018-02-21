package com.example.apaodevo.basura_juan.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * Created by apaodevo on 2/19/2018.
 */


public class Refresher {
    @SuppressLint("NewApi")
    public static void recreateActivityCompat(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
            Log.d("recreateActivityCompat", "Recreate");
        } else {
            Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
            Log.d("recreateActivityCompat", "Not recreate");
        }
    }
}

