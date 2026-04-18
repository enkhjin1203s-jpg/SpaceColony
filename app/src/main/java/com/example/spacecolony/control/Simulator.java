package com.example.spacecolony.control;

import com.example.spacecolony.model.CrewMember;
import java.util.ArrayList;

public class Simulator {
    private ArrayList<CrewMember> crewList;

    public Simulator() { crewList = new ArrayList<>(); }

    public void addCrewMember(CrewMember cm) { if (!crewList.contains(cm)) crewList.add(cm); }
    public void removeCrewMember(CrewMember cm) { crewList.remove(cm); }
    public void trainCrewMember(CrewMember cm) { cm.gainExperience(); }
    public ArrayList<CrewMember> getCrewList() { return crewList; }

    public CrewMember getCrewById(int id) {
        for (CrewMember c : crewList) if (c.getId() == id) return c;
        return null;
    }
}