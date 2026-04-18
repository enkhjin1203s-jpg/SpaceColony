package com.example.spacecolony.mission;

import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Threat;
import java.util.Random;

public class SpecialAction implements MissionAction {
    private static final Random rng = new Random();

    @Override public String getName() { return "Special"; }

    @Override
    public String execute(CrewMember actor, Threat threat) {
        int damage = (int) Math.round(actor.act(threat) * (1.5 + rng.nextDouble() * 0.3));
        threat.defend(damage);
        actor.defend(2);
        return "★ SPECIAL! " + actor.getName() + " hits for " + damage + " dmg (costs 2 HP). "
                + "Threat HP: " + threat.getEnergy() + "/" + threat.getMaxEnergy()
                + " | " + actor.getName() + " HP: " + actor.getEnergy() + "/" + actor.getMaxEnergy();
    }
}