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
import com.thesis.trackinguserapp.models.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context mContext;
    List<History> historyList;
    AdapterListener listener;

    public HistoryAdapter(Context mContext, List<History> historyList, AdapterListener listener) {
        this.mContext = mContext;
        this.historyList = historyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.txtDeviceID.setText(String.format("Device ID: %s", history.getSerialNumber()));
        holder.txtLastCommunication.setText(String.format("Last Communication: %s", history.getLastCommunication()));
        holder.txtLatLng.setText(String.format("Latitude: %s, Longitude: %s", history.getLatitude(), history.getLongitude()));
        holder.txtStatus.setText(String.format("Status: %s", history.getStatus()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDeviceID, txtLastCommunication, txtLatLng, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeviceID = itemView.findViewById(R.id.txtDeviceID);
            txtLastCommunication = itemView.findViewById(R.id.txtLastCommunication);
            txtLatLng = itemView.findViewById(R.id.txtLatLng);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
