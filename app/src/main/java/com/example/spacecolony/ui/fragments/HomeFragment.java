package com.example.spacecolony.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.spacecolony.R;
import com.example.spacecolony.data.GameData;
import com.example.spacecolony.filemanager.FileManager;
import com.example.spacecolony.model.CrewMember;

import java.io.File;

public class HomeFragment extends Fragment {

    private TextView txtQuartersCount, txtSimulatorCount, txtMissionCount, txtMedBayCount;
    private TextView txtMissionsCompleted, txtColonySummary, txtTotalCrew;
    private LinearLayout layoutNewPlayerHint;
    private LinearLayout cardQuarters, cardSimulator, cardMission, cardMedBay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtQuartersCount     = view.findViewById(R.id.txtQuartersCount);
        txtSimulatorCount    = view.findViewById(R.id.txtSimulatorCount);
        txtMissionCount      = view.findViewById(R.id.txtMissionCount);
        txtMedBayCount       = view.findViewById(R.id.txtMedBayCount);
        txtMissionsCompleted = view.findViewById(R.id.txtMissionsCompleted);
        txtColonySummary     = view.findViewById(R.id.txtColonySummary);
        txtTotalCrew         = view.findViewById(R.id.txtTotalCrew);
        layoutNewPlayerHint  = view.findViewById(R.id.layoutNewPlayerHint);
        cardQuarters         = view.findViewById(R.id.cardQuarters);
        cardSimulator        = view.findViewById(R.id.cardSimulator);
        cardMission          = view.findViewById(R.id.cardMission);
        cardMedBay           = view.findViewById(R.id.cardMedBay);

        Button btnSave    = view.findViewById(R.id.btnSave);
        Button btnLoad    = view.findViewById(R.id.btnLoad);
        Button btnNewGame = view.findViewById(R.id.btnNewGame);

        refreshSummary();

        layoutNewPlayerHint.setOnClickListener(v -> navigate(new RecruitFragment()));

        cardQuarters.setOnClickListener(v ->  navigate(new QuartersFragment()));
        cardSimulator.setOnClickListener(v -> navigate(new SimulatorFragment()));
        cardMission.setOnClickListener(v ->   navigate(new MissionControlFragment()));
        cardMedBay.setOnClickListener(v ->    navigate(new MedBayFragment()));

        btnSave.setOnClickListener(v -> {
            boolean ok = FileManager.saveData(requireContext());
            Toast.makeText(getContext(), ok ? "Colony saved!" : "Save failed.", Toast.LENGTH_SHORT).show();
        });

        btnLoad.setOnClickListener(v -> {
            boolean ok = FileManager.loadData(requireContext());
            Toast.makeText(getContext(), ok ? "Colony loaded!" : "No save found.", Toast.LENGTH_SHORT).show();
            refreshSummary();
        });

        btnNewGame.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("New Game")
                    .setMessage("This will delete all crew and progress. Are you sure?")
                    .setPositiveButton("Yes, start over", (dialog, which) -> {
                        GameData.reset();
                        File f = new File(requireContext().getFilesDir(), "spacecolony_save.txt");
                        if (f.exists()) f.delete();
                        CrewMember.setNextId(1);
                        refreshSummary();
                        Toast.makeText(getContext(), "New game started!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    private void navigate(Fragment fragment) {
        ((com.example.spacecolony.ui.MainActivity) requireActivity()).loadFragment(fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSummary();
    }

    private void refreshSummary() {
        int quarters  = GameData.getQuarters().getCrewCount();
        int simulator = GameData.getSimulator().getCrewList().size();
        int mission   = GameData.getMissionControl().getCrewList().size();
        int medbay    = GameData.getMedBay().getPatientCount();
        int total     = GameData.getStorage().getCount();
        int missions  = GameData.getMissionControl().getCompletedMissions();

        txtQuartersCount.setText(String.valueOf(quarters));
        txtSimulatorCount.setText(String.valueOf(simulator));
        txtMissionCount.setText(String.valueOf(mission));
        txtMedBayCount.setText(String.valueOf(medbay));
        txtMissionsCompleted.setText("🎯 Missions: " + missions);
        txtTotalCrew.setText("👥 Crew: " + total);

        layoutNewPlayerHint.setVisibility(total == 0 ? View.VISIBLE : View.GONE);
        txtColonySummary.setText(String.valueOf(total));
    }
}