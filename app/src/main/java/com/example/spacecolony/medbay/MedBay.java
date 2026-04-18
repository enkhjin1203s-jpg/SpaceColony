package com.example.spacecolony.medbay;

import com.example.spacecolony.model.CrewMember;
import java.util.ArrayList;
import java.util.HashMap;

public class MedBay {
    private HashMap<Integer, Integer> recoveryTurns;
    private ArrayList<CrewMember> patients;
    private static final int RECOVERY_MISSIONS = 2;

    public MedBay() {
        recoveryTurns = new HashMap<>();
        patients = new ArrayList<>();
    }

    public void admitPatient(CrewMember cm) {
        if (!patients.contains(cm)) {
            patients.add(cm);
            recoveryTurns.put(cm.getId(), RECOVERY_MISSIONS);
            cm.resetToBase();
        }
    }

    public void tickRecovery() {
        for (CrewMember cm : patients) {
            int turns = recoveryTurns.getOrDefault(cm.getId(), 0);
            if (turns > 0) recoveryTurns.put(cm.getId(), turns - 1);
        }
    }

    public boolean isRecovered(CrewMember cm) {
        return recoveryTurns.getOrDefault(cm.getId(), 0) <= 0;
    }

    public int getTurnsRemaining(CrewMember cm) {
        return recoveryTurns.getOrDefault(cm.getId(), 0);
    }

    public void discharge(CrewMember cm) {
        patients.remove(cm);
        recoveryTurns.remove(cm.getId());
    }

    public ArrayList<CrewMember> getPatients() { return patients; }
    public int getPatientCount() { return patients.size(); }
}