package com.example.spacecolony.storage;

import com.example.spacecolony.model.CrewMember;
import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    private HashMap<Integer, CrewMember> crewMap;

    public Storage() { crewMap = new HashMap<>(); }

    public void addCrewMember(CrewMember cm) { crewMap.put(cm.getId(), cm); }
    public CrewMember getCrewMember(int id) { return crewMap.get(id); }
    public void removeCrewMember(int id) { crewMap.remove(id); }
    public ArrayList<CrewMember> getAllCrewMembers() { return new ArrayList<>(crewMap.values()); }
    public int getCount() { return crewMap.size(); }
}