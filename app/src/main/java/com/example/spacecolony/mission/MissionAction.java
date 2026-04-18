package com.example.spacecolony.mission;

import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Threat;

public interface MissionAction {
    String getName();
    String execute(CrewMember actor, Threat threat);
}