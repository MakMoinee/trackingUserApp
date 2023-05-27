package com.thesis.trackinguserapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.thesis.trackinguserapp.R;
import com.thesis.trackinguserapp.adapters.OptionsAdapter;
import com.thesis.trackinguserapp.common.Constants;
import com.thesis.trackinguserapp.databinding.DialogTermsBinding;
import com.thesis.trackinguserapp.databinding.FragmentSettingsBinding;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.interfaces.FragmentListener;
import com.thesis.trackinguserapp.models.Options;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    Context mContext;
    FragmentListener fragmentListener;

    FragmentSettingsBinding binding;

    List<Options> optionsList;
    OptionsAdapter adapter;

    DialogTermsBinding termsBinding;
    AlertDialog termsDialog;

    final int TERMS_AND_CONDITION = 0;
    final int LOG_OUT = 1;

    public SettingsFragment(Context mContext, FragmentListener fragmentListener) {
        this.mContext = mContext;
        this.fragmentListener = fragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(LayoutInflater.from(mContext), container, false);
        setOptions();
        return binding.getRoot();
    }

    private void setOptions() {
        optionsList = new ArrayList<>();
        Options options = new Options();
        options.setTitle("Terms And Conditions");
        options.setImageResourceID(R.drawable.terms);
        optionsList.add(options);

        options = new Options();
        options.setTitle("Log Out");
        options.setImageResourceID(R.drawable.ic_out);
        optionsList.add(options);

        adapter = new OptionsAdapter(mContext, optionsList, position -> {
            switch (position) {
                case TERMS_AND_CONDITION:
                    showTerms();
                    break;
                case LOG_OUT:
                    showLogout();
                    break;
            }
        });
        binding.recycler.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recycler.setAdapter(adapter);
    }

    private void showLogout() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        DialogInterface.OnClickListener dListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:

                    fragmentListener.exitApp();
                    break;
                default:
                    dialog.dismiss();
                    break;
            }
        };

        mBuilder.setMessage("Are You Sure You Want To Log Out?")
                .setNegativeButton("Yes, Proceed", dListener)
                .setPositiveButton("No", dListener)
                .setCancelable(false)
                .show();
    }

    private void showTerms() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        termsBinding = DialogTermsBinding.inflate(LayoutInflater.from(mContext), null, false);
        WebSettings ws = termsBinding.webView.getSettings();
        ws.setJavaScriptEnabled(true);
        termsBinding.webView.loadUrl(Constants.termsAndConditionURL);
        mBuilder.setView(termsBinding.getRoot());
        termsDialog = mBuilder.create();
        termsDialog.show();
    }
}
