package com.thesis.trackinguserapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.thesis.trackinguserapp.adapters.HistoryAdapter;
import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.databinding.ActivityHistoryBinding;
import com.thesis.trackinguserapp.dialogs.CustomProgress;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.History;
import com.thesis.trackinguserapp.services.HistoryRequest;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding binding;
    CustomProgress customProgress;
    AlertDialog pbLoad;
    HistoryRequest historyRequest;
    List<History> historyList = new ArrayList<>();
    HistoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        customProgress = new CustomProgress(HistoryActivity.this);
        customProgress.setDialog();
        pbLoad = customProgress.getDialog();
        historyRequest = new HistoryRequest();
        loadHistory();
        setListeners();
    }

    private void setListeners() {
        binding.refresh.setOnRefreshListener(() -> {
            binding.recycler.setAdapter(null);
            loadHistory();
            binding.refresh.setRefreshing(false);
        });
    }

    private void loadHistory() {
        try {
            if (!Common.currentDeviceID.equals("")) {
                historyRequest.getHistory(Common.currentDeviceID, new FirebaseListener() {

                    @Override
                    public <T> void onSuccessAny(T any) {
                        if (any instanceof List<?>) {
                            List<?> tmpList = (List<?>) any;
                            if (tmpList.size() > 0) {
                                historyList = (List<History>) tmpList;
                                adapter = new HistoryAdapter(HistoryActivity.this, historyList, new AdapterListener() {
                                    @Override
                                    public void onClick(int position) {

                                    }
                                });
                                binding.recycler.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                                binding.recycler.setAdapter(adapter);

                            }
                        }
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(HistoryActivity.this, "There are no available history as of the moment", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(HistoryActivity.this, "There are no available history as of the moment", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(HistoryActivity.this);
            DialogInterface.OnClickListener dListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    default:
                        dialog.dismiss();
                        break;
                }
            };
            mBuilder.setMessage("There are 10 history records are being kept and it keeps being updated with the last 10 updates happening on the device location")
                    .setNegativeButton("OK", dListener)
                    .setCancelable(true)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
