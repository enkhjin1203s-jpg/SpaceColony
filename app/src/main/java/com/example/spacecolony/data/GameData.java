package com.example.spacecolony.data;

import com.example.spacecolony.control.MissionControl;
import com.example.spacecolony.control.Quarters;
import com.example.spacecolony.control.Simulator;
import com.example.spacecolony.medbay.MedBay;
import com.example.spacecolony.statistics.StatisticsManager;
import com.example.spacecolony.storage.Storage;

public class GameData {
    private static Storage storage = new Storage();
    private static Quarters quarters = new Quarters();
    private static Simulator simulator = new Simulator();
    private static MissionControl missionControl = new MissionControl();
    private static MedBay medBay = new MedBay();

    public static Storage getStorage() { return storage; }
    public static Quarters getQuarters() { return quarters; }
    public static Simulator getSimulator() { return simulator; }
    public static MissionControl getMissionControl() { return missionControl; }
    public static MedBay getMedBay() { return medBay; }

    public static void reset() {
        storage = new Storage();
        quarters = new Quarters();
        simulator = new Simulator();
        missionControl = new MissionControl();
        medBay = new MedBay();
        StatisticsManager.resetInstance();
    }
}