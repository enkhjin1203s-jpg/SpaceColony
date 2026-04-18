package com.example.spacecolony.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.spacecolony.R;
import com.example.spacecolony.ui.MainActivity;

public class MoreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        Button btnStatistics = view.findViewById(R.id.btnGoStatistics);
        Button btnMedBay     = view.findViewById(R.id.btnGoMedBay);

        btnStatistics.setOnClickListener(v ->
                ((MainActivity) requireActivity()).loadFragment(new StatisticsFragment()));
        btnMedBay.setOnClickListener(v ->
                ((MainActivity) requireActivity()).loadFragment(new MedBayFragment()));

        return view;
    }
}