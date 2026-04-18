package com.example.spacecolony.control;

import com.example.spacecolony.mission.AttackAction;
import com.example.spacecolony.mission.MissionAction;
import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Threat;
import java.util.ArrayList;

public class MissionControl {
    private ArrayList<CrewMember> crewList;
    private int completedMissions;

    public MissionControl() { crewList = new ArrayList<>(); completedMissions = 0; }

    public void addCrewMember(CrewMember cm) { if (!crewList.contains(cm)) crewList.add(cm); }
    public void removeCrewMember(CrewMember cm) { crewList.remove(cm); }
    public ArrayList<CrewMember> getCrewList() { return crewList; }
    public int getCompletedMissions() { return completedMissions; }
    public void setCompletedMissions(int v) { completedMissions = v; }

    public CrewMember getCrewById(int id) {
        for (CrewMember c : crewList) if (c.getId() == id) return c;
        return null;
    }

    public Threat generateThreat() {
        return Threat.generate(completedMissions);
    }

    public String launchMission(CrewMember a, CrewMember b) {
        return launchMission(a, b, null);
    }

    public String launchMission(CrewMember a, CrewMember b, CrewMember c) {
        StringBuilder log = new StringBuilder();
        Threat threat = generateThreat();
        log.append("=== MISSION: ").append(threat.getName()).append(" ===\n");
        log.append("Threat  SKL:").append(threat.getSkill())
                .append(" RES:").append(threat.getResilience())
                .append(" HP:").append(threat.getMaxEnergy()).append("\n\n");

        AttackAction attack = new AttackAction();
        ArrayList<CrewMember> squad = new ArrayList<>();
        squad.add(a); squad.add(b);
        if (c != null) squad.add(c);

        int round = 1;
        while (threat.isAlive()) {
            boolean anyAlive = false;
            for (CrewMember cm : squad) if (cm.isAlive()) { anyAlive = true; break; }
            if (!anyAlive) break;

            log.append("--- Round ").append(round).append(" ---\n");
            for (CrewMember cm : squad) {
                if (!cm.isAlive()) continue;
                log.append(attack.execute(cm, threat)).append("\n");
                if (!threat.isAlive()) break;
                int dmg = threat.act();
                int before = cm.getEnergy();
                cm.defend(dmg);
                log.append(threat.getName()).append(" hits ").append(cm.getName())
                        .append(" for ").append(before - cm.getEnergy())
                        .append(" dmg. HP: ").append(cm.getEnergy()).append("/").append(cm.getMaxEnergy()).append("\n");
            }
            round++;
        }

        boolean won = !threat.isAlive();
        if (won) {
            log.append("\n=== MISSION COMPLETE! Threat neutralized! ===\n");
            for (CrewMember cm : squad) {
                if (cm.isAlive()) {
                    cm.gainExperience();
                    log.append(cm.getName()).append(" +1 XP (total: ").append(cm.getExperience()).append(")\n");
                }
            }
            completedMissions++;
        } else {
            log.append("\n=== MISSION FAILED. All crew lost. ===\n");
        }
        return log.toString();
    }

    public String executeTurn(CrewMember actor, Threat threat, MissionAction action) {
        StringBuilder sb = new StringBuilder();
        sb.append(action.execute(actor, threat)).append("\n");
        if (!(action instanceof com.example.spacecolony.mission.DefendAction)
                && threat.isAlive() && actor.isAlive()) {
            int dmg = threat.act();
            int before = actor.getEnergy();
            actor.defend(dmg);
            sb.append(threat.getName()).append(" retaliates! ")
                    .append(before - actor.getEnergy()).append(" dmg. ")
                    .append(actor.getName()).append(" HP: ")
                    .append(actor.getEnergy()).append("/").append(actor.getMaxEnergy()).append("\n");
        }
        return sb.toString();
    }
}