package com.thesis.trackinguserapp.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.thesis.trackinguserapp.databinding.ProgressLoadingBinding;

public class CustomProgress {
    Context mContext;
    AlertDialog dialog;
    ProgressLoadingBinding binding;

    public CustomProgress(Context mContext) {
        this.mContext = mContext;
    }

    public void setDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        binding = ProgressLoadingBinding.inflate(LayoutInflater.from(mContext), null, false);

        mBuilder.setView(binding.getRoot());
        dialog = mBuilder.create();
        dialog.setCancelable(false);
    }

    public AlertDialog getDialog() {
        return this.dialog;
    }
}
