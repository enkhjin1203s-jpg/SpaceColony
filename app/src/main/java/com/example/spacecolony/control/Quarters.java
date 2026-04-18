package com.example.spacecolony.control;

import com.example.spacecolony.model.CrewMember;
import java.util.ArrayList;

public class Quarters {
    private ArrayList<CrewMember> crewList;

    public Quarters() { crewList = new ArrayList<>(); }

    public void addCrewMember(CrewMember cm) {
        if (!crewList.contains(cm)) {
            cm.restoreEnergy();
            crewList.add(cm);
        }
    }

    public void addCrewMemberNoRestore(CrewMember cm) {
        if (!crewList.contains(cm)) crewList.add(cm);
    }

    public void removeCrewMember(CrewMember cm) { crewList.remove(cm); }
    public ArrayList<CrewMember> getCrewList() { return crewList; }
    public int getCrewCount() { return crewList.size(); }
    public void clearAll() { crewList.clear(); }

    public CrewMember getCrewById(int id) {
        for (CrewMember c : crewList) if (c.getId() == id) return c;
        return null;
    }
}