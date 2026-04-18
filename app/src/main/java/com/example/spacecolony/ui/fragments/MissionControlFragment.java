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
import com.example.spacecolony.medbay.MedBay;
import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Threat;
import com.example.spacecolony.mission.*;
import com.example.spacecolony.statistics.StatisticsManager;

import java.util.ArrayList;
import java.util.List;

public class MissionControlFragment extends Fragment {

    private CrewMemberAdapter adapter;
    private ScrollView scrollMissionLog;

    private Threat activeThreat;
    private List<CrewMember> activeSquad;
    private int turnIndex = 0;
    private boolean missionRunning = false;

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateList(GameData.getMissionControl().getCrewList());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mission_control, container, false);

        RecyclerView recycler   = view.findViewById(R.id.recyclerMission);
        Button btnLaunch        = view.findViewById(R.id.btnLaunchMission);
        Button btnAttack        = view.findViewById(R.id.btnAttack);
        Button btnDefend        = view.findViewById(R.id.btnDefend);
        Button btnSpecial       = view.findViewById(R.id.btnSpecial);
        Button btnEndMission    = view.findViewById(R.id.btnEndMission);
        TextView txtLog         = view.findViewById(R.id.txtMissionLog);
        TextView txtThreatBar   = view.findViewById(R.id.txtThreatStatus);
        TextView txtEmpty       = view.findViewById(R.id.txtEmpty);
        scrollMissionLog        = view.findViewById(R.id.scrollMissionLog);

        adapter = new CrewMemberAdapter(GameData.getMissionControl().getCrewList(), true, null);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        refreshList(txtEmpty, recycler);
        setTacticalButtonsEnabled(false, btnAttack, btnDefend, btnSpecial);
        btnEndMission.setVisibility(View.GONE);

        btnLaunch.setOnClickListener(v -> {
            List<CrewMember> selected = adapter.getSelectedCrew();
            if (selected.size() < 2) {
                Toast.makeText(getContext(), "Select 2 or 3 crew members.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selected.size() > 3) {
                Toast.makeText(getContext(), "Maximum 3 crew members.", Toast.LENGTH_SHORT).show();
                return;
            }
            activeThreat = GameData.getMissionControl().generateThreat();
            activeSquad  = new ArrayList<>(selected);
            turnIndex    = 0;
            missionRunning = true;

            StringBuilder startLog = new StringBuilder("=== MISSION STARTED ===\nThreat: ")
                    .append(activeThreat.getName())
                    .append(" [").append(activeThreat.getCategory().toUpperCase()).append("]")
                    .append("\nSKL:").append(activeThreat.getSkill())
                    .append(" RES:").append(activeThreat.getResilience())
                    .append(" HP:").append(activeThreat.getMaxEnergy())
                    .append("\n\nSquad:");
            for (CrewMember cm : activeSquad) {
                startLog.append("\n  • ").append(cm.getName())
                        .append(" (").append(cm.getSpecialization()).append(")")
                        .append(" SKL:").append(cm.getSkill() + cm.getExperience())
                        .append(" RES:").append(cm.getResilience())
                        .append(" HP:").append(cm.getEnergy()).append("/").append(cm.getMaxEnergy());
            }
            startLog.append("\n\nYour turn — ").append(currentActor().getName()).append("!");
            txtLog.setText(startLog.toString());
            updateThreatBar(txtThreatBar);
            btnLaunch.setEnabled(false);
            setTacticalButtonsEnabled(true, btnAttack, btnDefend, btnSpecial);
            btnEndMission.setVisibility(View.VISIBLE);
            scrollLogToBottom();
        });

        btnAttack.setOnClickListener(v -> doAction(new AttackAction(), txtLog, txtThreatBar,
                btnLaunch, btnAttack, btnDefend, btnSpecial, btnEndMission, recycler, txtEmpty));
        btnDefend.setOnClickListener(v -> doAction(new DefendAction(), txtLog, txtThreatBar,
                btnLaunch, btnAttack, btnDefend, btnSpecial, btnEndMission, recycler, txtEmpty));
        btnSpecial.setOnClickListener(v -> doAction(new SpecialAction(), txtLog, txtThreatBar,
                btnLaunch, btnAttack, btnDefend, btnSpecial, btnEndMission, recycler, txtEmpty));

        btnEndMission.setOnClickListener(v -> {
            missionRunning = false;
            txtLog.append("\n[Mission abandoned]");
            scrollLogToBottom();
            setTacticalButtonsEnabled(false, btnAttack, btnDefend, btnSpecial);
            btnEndMission.setVisibility(View.GONE);
            btnLaunch.setEnabled(true);
            refreshList(txtEmpty, recycler);
        });

        return view;
    }

    private void scrollLogToBottom() {
        scrollMissionLog.post(() -> scrollMissionLog.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private CrewMember currentActor() {
        for (int i = 0; i < activeSquad.size(); i++) {
            int idx = (turnIndex + i) % activeSquad.size();
            if (activeSquad.get(idx).isAlive()) return activeSquad.get(idx);
        }
        return null;
    }

    private void advanceTurn() {
        int size = activeSquad.size();
        for (int i = 1; i <= size; i++) {
            int next = (turnIndex + i) % size;
            if (activeSquad.get(next).isAlive()) {
                turnIndex = next;
                return;
            }
        }
    }

    private void doAction(MissionAction action, TextView txtLog, TextView txtThreatBar,
                          Button btnLaunch, Button btnAttack, Button btnDefend,
                          Button btnSpecial, Button btnEndMission,
                          RecyclerView recycler, TextView txtEmpty) {
        if (!missionRunning) return;
        CrewMember actor = currentActor();
        if (actor == null) return;

        String result = GameData.getMissionControl().executeTurn(actor, activeThreat, action);
        txtLog.append("\n" + result);
        scrollLogToBottom();
        updateThreatBar(txtThreatBar);

        if (!activeThreat.isAlive()) {
            endMission(true, txtLog, txtThreatBar, btnLaunch, btnAttack, btnDefend, btnSpecial, btnEndMission, recycler, txtEmpty);
            return;
        }
        boolean anyAlive = false;
        for (CrewMember cm : activeSquad) if (cm.isAlive()) { anyAlive = true; break; }
        if (!anyAlive) {
            endMission(false, txtLog, txtThreatBar, btnLaunch, btnAttack, btnDefend, btnSpecial, btnEndMission, recycler, txtEmpty);
            return;
        }

        advanceTurn();
        CrewMember next = currentActor();
        if (next != null) {
            txtLog.append("\n→ " + next.getName() + "'s turn!");
            scrollLogToBottom();
        }
    }

    private void endMission(boolean won, TextView txtLog, TextView txtThreatBar,
                            Button btnLaunch, Button btnAttack, Button btnDefend,
                            Button btnSpecial, Button btnEndMission,
                            RecyclerView recycler, TextView txtEmpty) {
        missionRunning = false;
        setTacticalButtonsEnabled(false, btnAttack, btnDefend, btnSpecial);
        btnEndMission.setVisibility(View.GONE);
        btnLaunch.setEnabled(true);

        if (won) {
            txtLog.append("\n\n=== MISSION COMPLETE! ===");
            for (CrewMember cm : activeSquad) {
                if (cm.isAlive()) {
                    cm.gainExperience();
                    txtLog.append("\n" + cm.getName() + " +1 XP");
                }
            }
            GameData.getMissionControl().setCompletedMissions(
                    GameData.getMissionControl().getCompletedMissions() + 1);
        } else {
            txtLog.append("\n\n=== MISSION FAILED. All crew lost. ===");
        }

        if (activeSquad.size() == 3) {
            StatisticsManager.getInstance().recordMission(activeSquad.get(0), activeSquad.get(1), activeSquad.get(2), won);
        } else if (activeSquad.size() == 2) {
            StatisticsManager.getInstance().recordMission(activeSquad.get(0), activeSquad.get(1), won);
        } else if (activeSquad.size() == 1) {
            StatisticsManager.getInstance().recordMission(activeSquad.get(0), won);
        }

        txtLog.append("\n\n── CREW DEBRIEF ──");
        for (CrewMember cm : activeSquad) {
            GameData.getMissionControl().removeCrewMember(cm);
            if (cm.isAlive()) {
                GameData.getQuarters().addCrewMember(cm);
                txtLog.append("\n✅ " + cm.getName() + " returned to Quarters. (HP restored)");
            } else {
                GameData.getMedBay().admitPatient(cm);
                txtLog.append("\n🏥 " + cm.getName() + " was sent to MedBay. (Recovers in 2 missions)");
            }
        }

        scrollLogToBottom();

        MedBay mb = GameData.getMedBay();
        mb.tickRecovery();

        refreshList(txtEmpty, recycler);
    }

    private void updateThreatBar(TextView txt) {
        if (activeThreat == null) return;
        int hp = activeThreat.getEnergy(), max = activeThreat.getMaxEnergy();
        int bars = (int) Math.round(10.0 * hp / max);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) bar.append(i < bars ? "█" : "░");
        bar.append("] ").append(hp).append("/").append(max);
        txt.setText("Threat: " + activeThreat.getName() + "\n" + bar);
    }

    private void setTacticalButtonsEnabled(boolean enabled, Button... buttons) {
        for (Button b : buttons) b.setEnabled(enabled);
    }

    private void refreshList(TextView txtEmpty, RecyclerView recycler) {
        List<CrewMember> list = GameData.getMissionControl().getCrewList();
        adapter.updateList(list);
        txtEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }
}