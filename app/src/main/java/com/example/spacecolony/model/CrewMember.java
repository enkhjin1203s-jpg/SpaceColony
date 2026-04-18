package com.example.spacecolony.model;

public class CrewMember {
    private int id;
    private String name;
    private String specialization;
    private int skill;
    private int resilience;
    private int maxEnergy;
    private int energy;
    private int experience;
    private static int idCounter = 1;

    public CrewMember(int id, String name, String specialization, int skill, int resilience, int maxEnergy) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
    }

    public int act() { return skill + experience; }

    // Subclasses override this to apply specialization bonuses
    public int act(Threat threat) { return act(); }

    public void defend(int damage) {
        int finalDamage = Math.max(0, damage - resilience);
        energy = Math.max(0, energy - finalDamage);
    }

    public boolean isAlive() { return energy > 0; }
    public void gainExperience() { experience++; }
    public void restoreEnergy() { energy = maxEnergy; }
    public void resetToBase() { experience = 0; energy = maxEnergy; }

    public String getInfo() {
        return specialization + " | SKL:" + (skill + experience) + " RES:" + resilience
                + " HP:" + energy + "/" + maxEnergy + " XP:" + experience;
    }

    public static int generateId() { return idCounter++; }
    public static void setNextId(int next) { idCounter = next; }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getEnergy() { return energy; }
    public int getExperience() { return experience; }
    public void setName(String name) { this.name = name; }
    public void setEnergy(int energy) { this.energy = energy; }
    public void setExperience(int experience) { this.experience = experience; }
}