package com.example.spacecolony.statistics;

import com.example.spacecolony.model.CrewMember;
import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsManager {
    private HashMap<Integer, CrewStats> statsMap;
    private int totalMissions;
    private int totalWins;
    private static StatisticsManager instance;

    private StatisticsManager() {
        statsMap = new HashMap<>();
    }

    public static StatisticsManager getInstance() {
        if (instance == null) instance = new StatisticsManager();
        return instance;
    }

    public void ensureCrew(CrewMember cm) {
        if (!statsMap.containsKey(cm.getId()))
            statsMap.put(cm.getId(), new CrewStats(cm.getId(), cm.getName()));
    }

    public void recordMission(CrewMember a, boolean won) {
        ensureCrew(a);
        statsMap.get(a.getId()).recordMission(won);
        totalMissions++;
        if (won) totalWins++;
    }

    public void recordMission(CrewMember a, CrewMember b, boolean won) {
        ensureCrew(a); ensureCrew(b);
        statsMap.get(a.getId()).recordMission(won);
        statsMap.get(b.getId()).recordMission(won);
        totalMissions++;
        if (won) totalWins++;
    }

    public void recordMission(CrewMember a, CrewMember b, CrewMember c, boolean won) {
        recordMission(a, b, won);
        ensureCrew(c);
        statsMap.get(c.getId()).recordMission(won);
    }

    public void recordTraining(CrewMember cm) {
        ensureCrew(cm);
        statsMap.get(cm.getId()).recordTraining();
    }

    public void removeCrew(int id) { statsMap.remove(id); }

    public ArrayList<CrewStats> getAllStats() { return new ArrayList<>(statsMap.values()); }
    public CrewStats getCrewStats(int id) { return statsMap.get(id); }
    public int getTotalMissions() { return totalMissions; }
    public int getTotalWins() { return totalWins; }
    public void setTotalMissions(int v) { totalMissions = v; }
    public void setTotalWins(int v) { totalWins = v; }
    public HashMap<Integer, CrewStats> getStatsMap() { return statsMap; }
    public static void resetInstance() { instance = null; }
}