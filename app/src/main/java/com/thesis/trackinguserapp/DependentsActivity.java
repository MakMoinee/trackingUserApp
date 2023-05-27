package com.thesis.trackinguserapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thesis.trackinguserapp.adapters.DependentsAdapter;
import com.thesis.trackinguserapp.databinding.ActivityDependentsBinding;
import com.thesis.trackinguserapp.databinding.DialogAddDependentsBinding;
import com.thesis.trackinguserapp.dialogs.CustomProgress;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Dependents;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.DependentsRequest;

import java.util.ArrayList;
import java.util.List;

public class DependentsActivity extends AppCompatActivity {

    ActivityDependentsBinding binding;
    DependentsRequest request;
    List<Dependents> dependentsList = new ArrayList<>();
    DependentsAdapter adapter;
    DialogAddDependentsBinding addDependentsBinding;
    AlertDialog dialogAddDependents, pdLoading;
    CustomProgress customProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDependentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setTitle("Dependents");
        customProgress = new CustomProgress(DependentsActivity.this);
        customProgress.setDialog();
        pdLoading = customProgress.getDialog();
        setListeners();
        loadData();
    }

    private void loadData() {
        Users users = new MyUserPref(DependentsActivity.this).getUsers();
        request = new DependentsRequest();
        request.getDependents(users.getDocID(), new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {
                if (any instanceof List<?>) {
                    List<?> tmpList = (List<?>) any;
                    if (tmpList.size() > 0) {
                        dependentsList = (List<Dependents>) tmpList;
                        adapter = new DependentsAdapter(DependentsActivity.this, dependentsList, new AdapterListener() {
                            @Override
                            public void onClick(int position) {

                            }

                            @Override
                            public void onLongClick(int position) {
                                Dependents d = dependentsList.get(position);
                                showDeleteDialog(d);
                            }
                        });
                        binding.recycler.setLayoutManager(new LinearLayoutManager(DependentsActivity.this));
                        binding.recycler.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(DependentsActivity.this, "There are no dependents added yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteDialog(Dependents d) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DependentsActivity.this);
        DialogInterface.OnClickListener dListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    pdLoading.show();
                    request.deleteDependent(d.getDocID(), new FirebaseListener() {

                        @Override
                        public <T> void onSuccessAny(T any) {
                            pdLoading.dismiss();
                            Toast.makeText(DependentsActivity.this, "Successfully Deleted Dependent", Toast.LENGTH_SHORT).show();
                            binding.recycler.setAdapter(null);
                            loadData();
                        }

                        @Override
                        public void onError() {
                            pdLoading.dismiss();
                            Toast.makeText(DependentsActivity.this, "Failed to delete dependent, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                default:
                    dialog.dismiss();
                    break;
            }
        };
        mBuilder.setMessage("Are You Want To Delete This Dependent?")
                .setNegativeButton("Yes, Proceed", dListener)
                .setPositiveButton("No", dListener)
                .setCancelable(false)
                .show();
    }

    private void setListeners() {
        binding.btnAddDependents.setOnClickListener(v -> showAddDependentDialog());
    }

    private void showAddDependentDialog() {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(DependentsActivity.this);
        addDependentsBinding = DialogAddDependentsBinding.inflate(getLayoutInflater(), null, false);
        setAddDependentsListeners();
        aBuilder.setView(addDependentsBinding.getRoot());
        dialogAddDependents = aBuilder.create();
        dialogAddDependents.show();
    }

    private void setAddDependentsListeners() {
        addDependentsBinding.btnSave.setOnClickListener(v -> {
            String dependentName = addDependentsBinding.editDependentName.getText().toString();
            String dependentPhoneNumber = addDependentsBinding.editDependentPhoneNumber.getText().toString();
            String dependentEmail = addDependentsBinding.editDependentEmail.getText().toString();
            if (dependentName.equals("") || dependentEmail.equals("") || dependentPhoneNumber.equals("")) {
                Toast.makeText(DependentsActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                pdLoading.show();
                Users users = new MyUserPref(DependentsActivity.this).getUsers();
                Dependents dependents = new Dependents.DependentBuilder()
                        .setDependentName(dependentName)
                        .setDependentEmail(dependentEmail)
                        .setDependentPhoneNumber(dependentPhoneNumber)
                        .setUserID(users.getDocID())
                        .build();

                request.insertDependent(dependents, new FirebaseListener() {

                    @Override
                    public <T> void onSuccessAny(T any) {
                        pdLoading.dismiss();
                        Toast.makeText(DependentsActivity.this, "Successfully Added Dependent", Toast.LENGTH_SHORT).show();
                        dialogAddDependents.dismiss();
                        binding.recycler.setAdapter(null);
                        loadData();
                    }

                    @Override
                    public void onError() {
                        pdLoading.dismiss();
                        Toast.makeText(DependentsActivity.this, "Failed to add dependent, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
