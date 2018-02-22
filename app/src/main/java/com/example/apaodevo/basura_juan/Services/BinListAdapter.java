package com.example.apaodevo.basura_juan.Services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Activities.BinListActivity;
import com.example.apaodevo.basura_juan.Activities.DeviceList;
import com.example.apaodevo.basura_juan.Activities.HomeActivity;
import com.example.apaodevo.basura_juan.Activities.NavigateBin;
import com.example.apaodevo.basura_juan.Activities.NotificationActivity;
import com.example.apaodevo.basura_juan.Activities.RegisterBin;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Fragment.DeployedBinFragment;
import com.example.apaodevo.basura_juan.Fragment.UndeployedBinFragment;
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.Models.DeploymentModel;
import com.example.apaodevo.basura_juan.Models.NotificationModel;
import com.example.apaodevo.basura_juan.Models.UserModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Utils.Refresher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by apaodevo on 12/5/2017.
 */

public class BinListAdapter extends RecyclerView.Adapter<BinListAdapter.MyViewHolder>{
    private Context context;
    private List<BinModel> binList;
    private Intent sendIntent,home;
    GlobalData globalData;

    public BinListAdapter(Context context, List<BinModel> binList) {
        this.context = context;
        this.binList = binList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewBinName, textViewBinId;
        public ImageView thumbNail;
        public RelativeLayout viewBackground, viewForeground;
        public Button btnEditBin;
        public MyViewHolder(View view){
            super(view);
            textViewBinName = (TextView) view.findViewById(R.id.binlist_bin_name);
            textViewBinId = (TextView) view.findViewById(R.id.binlist_bin_id);
            thumbNail      = (ImageView) view.findViewById(R.id.thumbnail);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            btnEditBin     = (Button) view.findViewById(R.id.edit_bin);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bin_list_items, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final BinListAdapter.MyViewHolder holder, final int position) {
        final BinModel binModel = binList.get(position);
        holder.textViewBinName.setText(binModel.getBinName());
        holder.textViewBinId.setVisibility(View.GONE);
        holder.textViewBinId.setText(binModel.getBinId());

        if(binModel.getBinStatus().toString().equals("Deployed")){
            holder.btnEditBin.setText("Stop deployment");
        }else{
            holder.btnEditBin.setText("Edit Bin");
        }
        holder.btnEditBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btnEditBin.getText().toString().equals("Stop deployment")){
                    try {
                        //stopBinDeployment(Integer.parseInt(String.valueOf(binModel.getBinId())));
                        /*Display alert dialog for stopping deployment*/
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                        builder.setCancelable(true);
                        builder.setTitle("Stop deployment session");
                        builder.setMessage("Stopping deployment for " + binModel.getBinName() + "?");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               /* if (DeviceList.btSocket!=null) {
                                    try {
                                        DeviceList.btSocket.getOutputStream().write("5".toString().getBytes());
                                        DeviceList.btSocket = null;
                                        removeItem(holder.getAdapterPosition());
                                        stopBinDeployment(Integer.parseInt(binModel.getBinId()));
                                        notifyItemRangeRemoved(position, binList.size());
                                        Toast.makeText(context, "Deploying chu chu", Toast.LENGTH_SHORT).show();
                                    }
                                    catch (IOException e) {
                                        Toast.makeText(context,"Bluetooth Disconnected, Please bluetooth", Toast.LENGTH_LONG).show();
                                        DeviceList.btSocket = null;
                                    }
                                }*/
                                //removeItem(holder.getAdapterPosition());
                                //notifyItemRangeRemoved(position, binList.size());
                                stopBinDeployment(Integer.parseInt(binModel.getBinId()));
                                sendIntent = new Intent(context, BinListActivity.class);
                                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                ((BinListActivity)context).finish();
                                ((BinListActivity)context).overridePendingTransition(0, 0);
                                context.startActivity(sendIntent);
                                ((BinListActivity) context).overridePendingTransition(0, 0);


                            }
                        });
                        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        builder.show();
                    }
                    catch (Exception io)
                    {

                    }
                }else{
                    sendIntent = new Intent(new Intent(context, RegisterBin.class));
                    sendIntent.putExtra(Keys.TAG_BIN_UPDATE, "Update");
                    sendIntent.putExtra(Keys.TAG_BIN_NAME, binModel.getBinName());
                    sendIntent.putExtra(Keys.TAG_BIN_ID, binModel.getBinId());
                    context.startActivity(sendIntent);
                }

            }
        });
        Picasso.with(context)
                .load(R.drawable.bin_list_icon)
                .transform(new CropCircleTransformation())
                .into(holder.thumbNail);
    }

    @Override
    public int getItemCount() {
        return binList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void removeItem(int position){
        binList.remove(position);
        notifyItemRemoved(position);
    }
    private void stopBinDeployment(final int binId){
        CustomJSONRequest stopDeploymentRequest  = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.DEPLOYMENT_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        MDToast mdToast = MDToast.makeText(context, "Bin status changed to Undeployed", MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
                        mdToast.show();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_BIN_ID, String.valueOf(binId));
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(stopDeploymentRequest);
    }
    public void restoreItem(BinModel binModel, int position) {
        binList.add(position, binModel);
        // notify item added by position
        notifyItemInserted(position);
    }
    public void updateList(List<BinModel> list){
        binList = list;
        notifyDataSetChanged();
    }//This is used to update the list after triggering edit text

}
