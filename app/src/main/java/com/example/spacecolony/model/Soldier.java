package com.example.spacecolony.model;

public class Soldier extends CrewMember {
    public Soldier(int id, String name) { super(id, name, "Soldier", 9, 0, 16); }
    @Override public String getInfo() { return "[SOLDIER] " + super.getInfo(); }
    @Override public int act(Threat threat) {
        return act() + ("combat".equals(threat.getCategory()) ? 2 : 0);
    }
}