package com.example.spacecolony.model;

public class Pilot extends CrewMember {
    public Pilot(int id, String name) { super(id, name, "Pilot", 5, 4, 20); }
    @Override public String getInfo() { return "[PILOT] " + super.getInfo(); }
    @Override public int act(Threat threat) {
        return act() + ("navigation".equals(threat.getCategory()) ? 2 : 0);
    }
}