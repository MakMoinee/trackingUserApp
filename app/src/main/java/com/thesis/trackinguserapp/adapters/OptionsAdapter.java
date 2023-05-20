package com.thesis.trackinguserapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thesis.trackinguserapp.R;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.models.Options;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    Context mContext;
    List<Options> optionsList;
    AdapterListener listener;

    public OptionsAdapter(Context mContext, List<Options> optionsList, AdapterListener listener) {
        this.mContext = mContext;
        this.optionsList = optionsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsAdapter.ViewHolder holder, int position) {
        Options options = optionsList.get(position);
        holder.txtTitle.setText(options.getTitle());
        holder.imgOption.setImageResource(options.getImageResourceID());
        holder.itemView.setOnClickListener(v -> listener.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        ImageView imgOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imgOption = itemView.findViewById(R.id.imgOption);
        }
    }
}
