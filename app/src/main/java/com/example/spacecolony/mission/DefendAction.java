package com.example.spacecolony.mission;

import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Threat;

public class DefendAction implements MissionAction {
    @Override public String getName() { return "Defend"; }

    @Override
    public String execute(CrewMember actor, Threat threat) {
        int reduction = actor.getResilience() + 2;
        int threatDmg = Math.max(0, threat.act() - reduction);
        actor.defend(threatDmg);
        return actor.getName() + " defends! Threat dealt only " + threatDmg + " dmg. "
                + actor.getName() + " HP: " + actor.getEnergy() + "/" + actor.getMaxEnergy();
    }
}