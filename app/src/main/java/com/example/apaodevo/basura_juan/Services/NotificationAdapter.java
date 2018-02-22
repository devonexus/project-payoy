package com.example.apaodevo.basura_juan.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Activities.BinListActivity;
import com.example.apaodevo.basura_juan.Activities.DeviceList;
import com.example.apaodevo.basura_juan.Activities.NavigationDrawerActivity;
import com.example.apaodevo.basura_juan.Activities.NotificationActivity;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.Models.NotificationModel;
import com.example.apaodevo.basura_juan.Models.UserModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Utils.Refresher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apaodevo on 2/7/2018.
 */

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    public Context context;
    private List<NotificationModel> notificationModelList;
    public static UserModel userModel;
    private int notificationId, userId, notificationCount;
    private Intent sendIntent;
    public static NotificationModel notifModel = new NotificationModel();
    private NotificationActivity notificationActivity;
    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView notificationTitle, notificationMessage, notificationDateTime;
        public ImageView imageView;
        public Button btnMarkAsRead;
        public MyViewHolder(View view){
            super(view);
            notificationTitle = (TextView) view.findViewById(R.id.notifTitle);
            notificationMessage = (TextView) view.findViewById(R.id.notifContent);
            notificationDateTime = (TextView) view.findViewById(R.id.notifDateTime);
            imageView       = (ImageView) view.findViewById(R.id.notification_marker);
            btnMarkAsRead   = (Button) view.findViewById(R.id.btnNotifRead);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);
        userModel = UserModel.getInstance();
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, final int position) {
        final NotificationModel notificationModel = notificationModelList.get(position);
        holder.notificationTitle.setText(notificationModel.getNotificationTitle());
        holder.notificationMessage.setText(notificationModel.getNotificationMessage());
        holder.notificationDateTime.setText(notificationModel.getNotificationDate()+" at "+notificationModel.getNotificationTime());
        if(notificationModel.getNotificationTitle().equals("Battery Status")){
            holder.imageView.setImageResource(R.drawable.low_battery);
        }else if(notificationModel.getNotificationTitle().equals("Bin Capacity")){
            holder.imageView.setImageResource(R.drawable.bin_full);
        }

        holder.notificationDateTime.setText(notificationModel.getNotificationDate()+" "+notificationModel.getNotificationTime());
        if(notificationModel.getNotificationTitle().equals("Battery Status")){
            holder.imageView.setImageResource(R.drawable.low_battery);
        }else{
            holder.imageView.setImageResource(R.drawable.bin_capacity);
        }
        notificationId = notificationModel.getNotificationId();
        userId = userModel.getUserId();
        holder.btnMarkAsRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sendIntent = new Intent(context, NotificationActivity.class);

                //updateNotificationStatusToRead(notificationId, userId);

                updateNotificationStatusToRead(notificationId, userId);
                Refresher.recreateActivityCompat(((NotificationActivity)context));
            }
        });
    }

    @Override
    public int getItemCount() {

        return notificationModelList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void removeItem(int position){
        notificationModelList.remove(position);
        notifyItemRemoved(position);
    }
    private void updateNotificationStatusToRead(final int notificationId, final int userId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceUrl.NOTIFICATION_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_NOTIFICATION_ID, String.valueOf(notificationId));
                params.put(Keys.TAG_USER_ID, String.valueOf(userId));
                return params;
            }
        };
//       VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
