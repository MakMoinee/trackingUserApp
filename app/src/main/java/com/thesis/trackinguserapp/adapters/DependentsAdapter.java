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
import com.thesis.trackinguserapp.models.Dependents;

import java.util.List;

public class DependentsAdapter extends RecyclerView.Adapter<DependentsAdapter.ViewHolder> {

    Context mContext;
    List<Dependents> dependentsList;
    AdapterListener listener;

    public DependentsAdapter(Context mContext, List<Dependents> dependentsList, AdapterListener listener) {
        this.mContext = mContext;
        this.dependentsList = dependentsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DependentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_dependents, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DependentsAdapter.ViewHolder holder, int position) {
        Dependents dependents = dependentsList.get(position);
        holder.txtName.setText(String.format("Name: %s", dependents.getDependentName()));
        holder.txtPhoneNumber.setText(String.format("Phone Number: %s", dependents.getDependentPhoneNumber()));
        holder.txtEmail.setText(String.format("Email: %s", dependents.getDependentEmail()));
        holder.itemView.setOnClickListener(v -> listener.onClick(holder.getAdapterPosition()));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return dependentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPhoneNumber, txtEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            txtEmail = itemView.findViewById(R.id.txtEmail);
        }
    }
}
