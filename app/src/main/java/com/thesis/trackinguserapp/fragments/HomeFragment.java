package com.thesis.trackinguserapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.thesis.trackinguserapp.R;
import com.thesis.trackinguserapp.adapters.OptionsAdapter;
import com.thesis.trackinguserapp.databinding.FragmentHomeBinding;
import com.thesis.trackinguserapp.interfaces.FragmentListener;
import com.thesis.trackinguserapp.models.Options;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Context mContext;
    FragmentHomeBinding binding;

    FragmentListener fragmentListener;

    List<Options> optionsList;

    OptionsAdapter adapter;

    final int DEVICES = 0;
    final int HISTORY = 1;
    final int DEPENDENTS = 2;
    final int TRACKING = 3;


    public HomeFragment(Context mContext, FragmentListener fragmentListener) {
        this.mContext = mContext;
        this.fragmentListener = fragmentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(mContext), container, false);
        createList();
        return binding.getRoot();
    }

    private void createList() {
        optionsList = new ArrayList<>();
        Options options = new Options();
        options.setTitle("Devices");
        options.setImageResourceID(R.drawable.device);
        optionsList.add(options);

        options = new Options();
        options.setTitle("History");
        options.setImageResourceID(R.drawable.history);
        optionsList.add(options);

        options = new Options();
        options.setTitle("Dependents");
        options.setImageResourceID(R.drawable.dependent);
        optionsList.add(options);

        options = new Options();
        options.setTitle("Tracking");
        options.setImageResourceID(R.drawable.tracking);
        optionsList.add(options);

        adapter = new OptionsAdapter(mContext, optionsList, position -> {
            switch (position) {
                case DEVICES:
                    fragmentListener.openDevices();
                    break;
                case HISTORY:
                    fragmentListener.openHistory();
                    break;
                case DEPENDENTS:
                    fragmentListener.openDependents();
                    break;
                case TRACKING:
                    fragmentListener.openTracking();
                    break;

            }
        });

        binding.recycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        binding.recycler.setAdapter(adapter);
    }
}
