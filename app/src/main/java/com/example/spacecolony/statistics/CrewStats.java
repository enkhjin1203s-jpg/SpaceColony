package com.example.spacecolony.statistics;

public class CrewStats {
    private int crewId;
    private String crewName;
    private int missionsCompleted;
    private int missionsWon;
    private int missionsLost;
    private int trainingSessions;

    public CrewStats(int crewId, String crewName) {
        this.crewId = crewId;
        this.crewName = crewName;
    }

    public void recordMission(boolean won) {
        missionsCompleted++;
        if (won) missionsWon++;
        else missionsLost++;
    }

    public void recordTraining() { trainingSessions++; }

    public int getCrewId() { return crewId; }
    public String getCrewName() { return crewName; }
    public int getMissionsCompleted() { return missionsCompleted; }
    public int getMissionsWon() { return missionsWon; }
    public int getMissionsLost() { return missionsLost; }
    public int getTrainingSessions() { return trainingSessions; }
    public void setCrewName(String name) { this.crewName = name; }
    public void setMissionsCompleted(int v) { this.missionsCompleted = v; }
    public void setMissionsWon(int v) { this.missionsWon = v; }
    public void setMissionsLost(int v) { this.missionsLost = v; }
    public void setTrainingSessions(int v) { this.trainingSessions = v; }
}