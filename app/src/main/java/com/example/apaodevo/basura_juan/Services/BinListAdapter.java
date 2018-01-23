package com.example.apaodevo.basura_juan.Services;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apaodevo.basura_juan.Activities.BinListActivity;
import com.example.apaodevo.basura_juan.Activities.RegisterBin;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by apaodevo on 12/5/2017.
 */

public class BinListAdapter extends RecyclerView.Adapter<BinListAdapter.MyViewHolder>{
    private Context context;
    private List<BinModel> binList;
    private Intent sendIntent;

    public BinListAdapter(Context context, List<BinModel> binList) {
        this.context = context;
        this.binList = binList;
    }

    public interface BinListAdapterListener {
        void onBinSelected(BinModel binModel);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewBinName, textViewBinIpAddress, textViewBinId;
        public ImageView thumbNail;
        public RelativeLayout viewBackground, viewForeground;
        public Button btnEditBin;
        public MyViewHolder(View view){
            super(view);
            textViewBinName = (TextView) view.findViewById(R.id.binlist_bin_name);
            textViewBinId = (TextView) view.findViewById(R.id.binlist_bin_id);
            textViewBinIpAddress = (TextView) view.findViewById(R.id.binlist_ip_address);
            thumbNail      = (ImageView) view.findViewById(R.id.thumbnail);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            btnEditBin     = (Button) view.findViewById(R.id.edit_bin);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bin_list_item, parent, false);


        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(BinListAdapter.MyViewHolder holder, final int position) {
        final BinModel binModel = binList.get(position);
        holder.textViewBinName.setText(binModel.getBinName());
        holder.textViewBinId.setVisibility(View.GONE);
        holder.textViewBinIpAddress.setText(binModel.getBinIpAddress());
        holder.textViewBinId.setText(binModel.getBinId());
        holder.btnEditBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent = new Intent(new Intent(context, RegisterBin.class));
                sendIntent.putExtra(Keys.TAG_BIN_UPDATE, "Update");
                sendIntent.putExtra(Keys.TAG_IP_ADDRESS, binModel.getBinIpAddress());
                sendIntent.putExtra(Keys.TAG_BIN_NAME, binModel.getBinName());
                sendIntent.putExtra(Keys.TAG_BIN_ID, binModel.getBinId());
                context.startActivity(sendIntent);
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
