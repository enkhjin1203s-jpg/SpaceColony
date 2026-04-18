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

public class MedBayFragment extends Fragment {

    private CrewMemberAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateList(GameData.getMedBay().getPatients());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medbay, container, false);

        RecyclerView recycler   = view.findViewById(R.id.recyclerMedBay);
        Button btnDischarge     = view.findViewById(R.id.btnDischarge);
        TextView txtEmpty       = view.findViewById(R.id.txtEmpty);
        TextView txtStatus      = view.findViewById(R.id.txtMedBayStatus);

        adapter = new CrewMemberAdapter(new ArrayList<>(), true, null);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        refreshList(txtEmpty, recycler, txtStatus);

        btnDischarge.setOnClickListener(v -> {
            List<CrewMember> selected = adapter.getSelectedCrew();
            int discharged = 0;
            for (CrewMember cm : selected) {
                if (GameData.getMedBay().isRecovered(cm)) {
                    GameData.getMedBay().discharge(cm);
                    GameData.getQuarters().addCrewMember(cm);
                    discharged++;
                } else {
                    int turns = GameData.getMedBay().getTurnsRemaining(cm);
                    Toast.makeText(getContext(),
                            cm.getName() + " needs " + turns + " more colony mission" + (turns == 1 ? "" : "s") + " to recover.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            if (discharged > 0)
                Toast.makeText(getContext(), discharged + " crew discharged to Quarters.", Toast.LENGTH_SHORT).show();
            refreshList(txtEmpty, recycler, txtStatus);
        });

        return view;
    }

    private void refreshList(TextView txtEmpty, RecyclerView recycler, TextView txtStatus) {
        List<CrewMember> patients = GameData.getMedBay().getPatients();
        adapter.updateList(patients);
        txtEmpty.setVisibility(patients.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(patients.isEmpty() ? View.GONE : View.VISIBLE);

        if (!patients.isEmpty()) {
            StringBuilder sb = new StringBuilder("Recovery Status:\n");
            for (CrewMember cm : patients) {
                int turns = GameData.getMedBay().getTurnsRemaining(cm);
                sb.append(cm.getName()).append(": ")
                        .append(turns > 0
                                ? "Recovers after " + turns + " more colony mission" + (turns == 1 ? "" : "s")
                                : "✅ READY — tap to discharge")
                        .append("\n");
            }
            txtStatus.setText(sb.toString());
        } else {
            txtStatus.setText("MedBay is empty.");
        }
    }
}