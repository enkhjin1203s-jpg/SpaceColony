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
import com.example.spacecolony.statistics.StatisticsManager;

import java.util.List;

public class SimulatorFragment extends Fragment {

    private CrewMemberAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateList(GameData.getSimulator().getCrewList());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulator, container, false);
        RecyclerView recycler  = view.findViewById(R.id.recyclerSimulator);
        Button btnTrain        = view.findViewById(R.id.btnTrain);
        Button btnToQuarters   = view.findViewById(R.id.btnMoveToQuarters);
        Button btnClear        = view.findViewById(R.id.btnClearSelection);
        TextView txtEmpty      = view.findViewById(R.id.txtEmpty);
        TextView txtResult     = view.findViewById(R.id.txtTrainResult);
        TextView txtSelCount   = view.findViewById(R.id.txtSelectionCount);

        adapter = new CrewMemberAdapter(GameData.getSimulator().getCrewList(), true, null, txtSelCount);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        refreshList(txtEmpty, recycler);

        btnClear.setOnClickListener(v -> {
            adapter.clearSelections();
            txtSelCount.setText("None selected");
        });

        btnTrain.setOnClickListener(v -> {
            List<CrewMember> selected = adapter.getSelectedCrew();
            if (selected.isEmpty()) { Toast.makeText(getContext(), "Select crew to train.", Toast.LENGTH_SHORT).show(); return; }
            StringBuilder sb = new StringBuilder("Training results:\n");
            for (CrewMember cm : selected) {
                GameData.getSimulator().trainCrewMember(cm);
                StatisticsManager.getInstance().recordTraining(cm);
                sb.append(cm.getName()).append(" → XP: ").append(cm.getExperience()).append("\n");
            }
            txtResult.setText(sb.toString());
            refreshList(txtEmpty, recycler);
            txtSelCount.setText("None selected");
        });

        btnToQuarters.setOnClickListener(v -> {
            List<CrewMember> selected = adapter.getSelectedCrew();
            if (selected.isEmpty()) { Toast.makeText(getContext(), "Select crew first.", Toast.LENGTH_SHORT).show(); return; }
            for (CrewMember cm : selected) {
                GameData.getSimulator().removeCrewMember(cm);
                GameData.getQuarters().addCrewMember(cm);
            }
            txtResult.setText(selected.size() + " crew returned to Quarters.");
            refreshList(txtEmpty, recycler);
            txtSelCount.setText("None selected");
        });

        return view;
    }

    private void refreshList(TextView txtEmpty, RecyclerView recycler) {
        List<CrewMember> list = GameData.getSimulator().getCrewList();
        adapter.updateList(list);
        txtEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }
}