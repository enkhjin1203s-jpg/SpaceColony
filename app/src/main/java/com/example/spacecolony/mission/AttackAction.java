package com.example.spacecolony.mission;

import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Threat;
import java.util.Random;

public class AttackAction implements MissionAction {
    private static final Random rng = new Random();

    @Override public String getName() { return "Attack"; }

    @Override
    public String execute(CrewMember actor, Threat threat) {
        double rand = 0.8 + rng.nextDouble() * 0.4;
        int base = actor.act(threat);
        int damage = (int) Math.round(base * rand);
        threat.defend(damage);
        boolean hasBonus = base > actor.act();
        String bonusNote = hasBonus ? " ⚡+2 spec bonus!" : "";
        return actor.getName() + " attacks for " + damage + " dmg!" + bonusNote + " "
                + "Threat HP: " + threat.getEnergy() + "/" + threat.getMaxEnergy();
    }
}