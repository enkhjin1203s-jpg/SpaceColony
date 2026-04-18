package com.example.spacecolony.model;

import java.util.Random;

public class Threat {
    private String name;
    private int skill;
    private int resilience;
    private int maxEnergy;
    private int energy;
    private static final Random rng = new Random();

    private static final String[] THREAT_NAMES = {
            "Asteroid Storm", "Fuel Leak", "Alien Attack", "Solar Flare",
            "Hull Breach", "Reactor Meltdown", "Rogue AI", "Meteor Shower",
            "Oxygen Failure", "Cosmic Radiation"
    };

    public Threat(String name, int skill, int resilience, int energy) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = energy;
        this.energy = energy;
    }

    public static Threat generate(int completedMissions) {
        String name = THREAT_NAMES[rng.nextInt(THREAT_NAMES.length)];
        int skill = 4 + completedMissions;
        int resilience = 2;
        int energy = 25 + completedMissions * 2;
        return new Threat(name, skill, resilience, energy);
    }

    public int act() {
        double rand = 0.8 + rng.nextDouble() * 0.4; // 0.8–1.2x
        return (int) Math.round(skill * rand);
    }

    public void defend(int damage) {
        int finalDamage = Math.max(0, damage - resilience);
        energy = Math.max(0, energy - finalDamage);
    }

    public boolean isAlive() { return energy > 0; }
    public String getName() { return name; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getEnergy() { return energy; }

    public String getCategory() {
        switch (name) {
            case "Asteroid Storm":
            case "Meteor Shower":
                return "navigation";
            case "Hull Breach":
            case "Fuel Leak":
            case "Reactor Meltdown":
                return "technical";
            case "Oxygen Failure":
                return "medical";
            case "Solar Flare":
            case "Cosmic Radiation":
                return "science";
            case "Alien Attack":
            case "Rogue AI":
                return "combat";
            default:
                return "unknown";
        }
    }
}