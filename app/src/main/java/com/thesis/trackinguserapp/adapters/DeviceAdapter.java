package com.thesis.trackinguserapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thesis.trackinguserapp.R;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.models.Devices;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    Context mContext;
    List<Devices> devicesList;
    AdapterListener listener;

    public DeviceAdapter(Context mContext, List<Devices> devicesList, AdapterListener listener) {
        this.mContext = mContext;
        this.devicesList = devicesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_devices, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        Devices devices = devicesList.get(position);
        holder.txtDeviceID.setText(String.format("Device ID - %s", devices.getDeviceID()));
        holder.txtDeviceUserID.setText(String.format("User ID - %s", devices.getDeviceUserID()));
        holder.itemView.setOnClickListener(v -> listener.onClick(holder.getAdapterPosition()));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDeviceID, txtDeviceUserID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeviceID = itemView.findViewById(R.id.txtDeviceID);
            txtDeviceUserID = itemView.findViewById(R.id.txtDeviceUserID);
        }
    }
}
