package com.example.apaodevo.basura_juan.Services;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apaodevo.basura_juan.Models.DeploymentModel;
import com.example.apaodevo.basura_juan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;



public class DeploymentHistoryAdapter extends RecyclerView.Adapter<DeploymentHistoryAdapter.MyViewHolder>{
    private Context context;
    private List<DeploymentModel> deployBinList;
    private Intent sendIntent;

    public DeploymentHistoryAdapter(Context context, List<DeploymentModel> deployBinList) {
        this.context = context;
        this.deployBinList = deployBinList;
    }

    public interface  DeploymentHistoryAdapterListener {
        void onBinSelected(DeploymentModel deploymentModel);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvBinName, tvdateStart, tvtimeStart, tvdateEnd, tvtimeEnd, tvLocation;
        public ImageView thumbNail;
        public RelativeLayout viewDeploymentLayout;
        public MyViewHolder(View view){
            super(view);
            tvBinName   = (TextView) view.findViewById(R.id.binName);
            tvtimeStart = (TextView) view.findViewById(R.id.deploy_time_start);
            tvdateStart = (TextView) view.findViewById(R.id.deployment_date_start);
            tvdateEnd   = (TextView) view.findViewById(R.id.deployment_date_end);
            tvtimeEnd   = (TextView) view.findViewById(R.id.deployment_time_end);
            tvLocation  = (TextView) view.findViewById(R.id.binLocation);
            thumbNail      = (ImageView) view.findViewById(R.id.deployment_bin_pic);
            viewDeploymentLayout = (RelativeLayout) view.findViewById(R.id.deployment_container);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deployment_history_item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(DeploymentHistoryAdapter.MyViewHolder holder, final int position) {
        final DeploymentModel deploymentModel = deployBinList.get(position);
        holder.tvBinName.setText("Bin: \t"+deploymentModel.getBinName());
        if(deploymentModel.getDateEnd().toString().equals("01/01/1970")){
            holder.tvdateEnd.setText("End Date: -");
        }else{
            holder.tvdateEnd.setText("End Date: \t"+deploymentModel.getDateEnd());
        } if(deploymentModel.getTimeEnd().toString().equals("12:00:00 AM")){
            holder.tvtimeEnd.setText("");
        }else{
            holder.tvtimeEnd.setText("at "+deploymentModel.getTimeEnd());
        }
        holder.tvdateStart.setText("Start Date: \t"+deploymentModel.getDateStart());

        holder.tvtimeStart.setText("at "+deploymentModel.getTimeStart());

        holder.tvLocation.setText("Location: \t"+deploymentModel.getLocation());
        Picasso.with(context)
                .load(R.drawable.bin_list_icon)
                .transform(new CropCircleTransformation())
                .into(holder.thumbNail);
    }

    @Override
    public int getItemCount() {

        return deployBinList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void removeItem(int position){
        deployBinList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(DeploymentModel deploymentModel, int position) {
        deployBinList.add(position, deploymentModel);
        // notify item added by position
        notifyItemInserted(position);
    }
    public void updateList(List<DeploymentModel> list){
        deployBinList = list;
        notifyDataSetChanged();
    }//This is used to update the list after triggering edit text

}
