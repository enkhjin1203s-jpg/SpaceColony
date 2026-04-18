package com.example.spacecolony.model;

public class Engineer extends CrewMember {
    public Engineer(int id, String name) { super(id, name, "Engineer", 6, 3, 19); }
    @Override public String getInfo() { return "[ENGINEER] " + super.getInfo(); }
    @Override public int act(Threat threat) {
        return act() + ("technical".equals(threat.getCategory()) ? 2 : 0);
    }
}