package com.example.apaodevo.basura_juan.Services;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apaodevo.basura_juan.Models.NotificationModel;
import com.example.apaodevo.basura_juan.R;

import java.util.List;

/**
 * Created by apaodevo on 2/7/2018.
 */

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    private Context context;
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView notificationTitle, notificationMessage, notificationDateTime;
        public ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            notificationTitle = (TextView) view.findViewById(R.id.notifTitle);
            notificationMessage = (TextView) view.findViewById(R.id.notifContent);
            notificationDateTime = (TextView) view.findViewById(R.id.notifDateTime);
            imageView = (ImageView) view.findViewById(R.id.notification_marker);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, final int position) {
        final NotificationModel notificationModel = notificationModelList.get(position);
        holder.notificationTitle.setText(notificationModel.getNotificationTitle());
        holder.notificationMessage.setText(notificationModel.getNotificationMessage());
        holder.notificationDateTime.setText(notificationModel.getNotificationDate()+" "+notificationModel.getNotificationTime());
        if(notificationModel.getNotificationTitle().equals("Battery Status")){
            holder.imageView.setImageResource(R.drawable.low_battery);
        }else{
            holder.imageView.setImageResource(R.drawable.bin_capacity);
        }

    }

    @Override
    public int getItemCount() {

        return notificationModelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
   

    
}
