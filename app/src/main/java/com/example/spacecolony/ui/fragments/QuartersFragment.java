package com.example.spacecolony.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacecolony.R;
import com.example.spacecolony.adapter.CrewMemberAdapter;
import com.example.spacecolony.data.GameData;
import com.example.spacecolony.model.CrewMember;

import java.util.ArrayList;
import java.util.List;

public class QuartersFragment extends Fragment {

    private CrewMemberAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateList(GameData.getQuarters().getCrewList());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quarters, container, false);

        RecyclerView recycler   = view.findViewById(R.id.recyclerQuarters);
        Button btnToSimulator   = view.findViewById(R.id.btnMoveToSimulator);
        Button btnToMission     = view.findViewById(R.id.btnMoveToMission);
        Button btnClear         = view.findViewById(R.id.btnClearSelection);
        TextView txtEmpty       = view.findViewById(R.id.txtEmpty);
        TextView txtSelCount    = view.findViewById(R.id.txtSelectionCount);

        adapter = new CrewMemberAdapter(GameData.getQuarters().getCrewList(), true, null, txtSelCount);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        refreshList(txtEmpty, recycler);

        btnClear.setOnClickListener(v -> {
            adapter.clearSelections();
            txtSelCount.setText("None selected");
        });

        btnToSimulator.setOnClickListener(v -> {
            List<CrewMember> selected = new ArrayList<>(adapter.getSelectedCrew());
            if (selected.isEmpty()) { Toast.makeText(getContext(), "Select crew first.", Toast.LENGTH_SHORT).show(); return; }
            for (CrewMember cm : selected) {
                GameData.getQuarters().removeCrewMember(cm);
                GameData.getSimulator().addCrewMember(cm);
            }
            Toast.makeText(getContext(), selected.size() + " crew moved to Simulator.", Toast.LENGTH_SHORT).show();
            refreshList(txtEmpty, recycler);
            txtSelCount.setText("None selected");
        });

        btnToMission.setOnClickListener(v -> {
            List<CrewMember> selected = new ArrayList<>(adapter.getSelectedCrew());
            if (selected.isEmpty()) { Toast.makeText(getContext(), "Select crew first.", Toast.LENGTH_SHORT).show(); return; }
            for (CrewMember cm : selected) {
                GameData.getQuarters().removeCrewMember(cm);
                GameData.getMissionControl().addCrewMember(cm);
            }
            Toast.makeText(getContext(), selected.size() + " crew moved to Mission Control.", Toast.LENGTH_SHORT).show();
            refreshList(txtEmpty, recycler);
            txtSelCount.setText("None selected");
        });
        return view;
    }

    private void refreshList(TextView txtEmpty, RecyclerView recycler) {
        List<CrewMember> list = GameData.getQuarters().getCrewList();
        adapter.updateList(list);
        txtEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }
}