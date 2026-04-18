package com.example.spacecolony.model;

public class Medic extends CrewMember {
    public Medic(int id, String name) { super(id, name, "Medic", 7, 2, 18); }
    @Override public String getInfo() { return "[MEDIC] " + super.getInfo(); }
    @Override public int act(Threat threat) {
        return act() + ("medical".equals(threat.getCategory()) ? 2 : 0);
    }
}