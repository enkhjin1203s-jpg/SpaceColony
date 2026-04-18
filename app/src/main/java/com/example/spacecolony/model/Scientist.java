package com.example.spacecolony.model;

public class Scientist extends CrewMember {
    public Scientist(int id, String name) { super(id, name, "Scientist", 8, 1, 17); }
    @Override public String getInfo() { return "[SCIENTIST] " + super.getInfo(); }
    @Override public int act(Threat threat) {
        return act() + ("science".equals(threat.getCategory()) ? 2 : 0);
    }
}