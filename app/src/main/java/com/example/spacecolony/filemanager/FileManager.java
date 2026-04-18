package com.example.spacecolony.filemanager;

import android.content.Context;
import com.example.spacecolony.data.GameData;
import com.example.spacecolony.model.CrewMember;
import com.example.spacecolony.model.Engineer;
import com.example.spacecolony.model.Medic;
import com.example.spacecolony.model.Pilot;
import com.example.spacecolony.model.Scientist;
import com.example.spacecolony.model.Soldier;
import com.example.spacecolony.statistics.CrewStats;
import com.example.spacecolony.statistics.StatisticsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {
    private static final String FILE_NAME = "spacecolony_save.txt";

    public static boolean saveData(Context ctx) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(ctx.getFilesDir(), FILE_NAME)))) {
            ArrayList<CrewMember> all = GameData.getStorage().getAllCrewMembers();
            pw.println("CREW_COUNT:" + all.size());
            for (CrewMember cm : all) {
                String loc = "MEDBAY";
                if (GameData.getQuarters().getCrewById(cm.getId()) != null)
                    loc = "QUARTERS";
                else if (GameData.getSimulator().getCrewById(cm.getId()) != null)
                    loc = "SIMULATOR";
                else if (GameData.getMissionControl().getCrewById(cm.getId()) != null)
                    loc = "MISSIONCONTROL";
                pw.println(
                        cm.getId() + "," +
                                cm.getName() + "," +
                                cm.getSpecialization() + "," +
                                cm.getSkill() + "," +
                                cm.getResilience() + "," +
                                cm.getMaxEnergy() + "," +
                                cm.getEnergy() + "," +
                                cm.getExperience() + "," +
                                loc
                );
            }
            pw.println("TOTAL_MISSIONS:" + StatisticsManager.getInstance().getTotalMissions());
            pw.println("TOTAL_WINS:" + StatisticsManager.getInstance().getTotalWins());
            pw.println("COMPLETED_MISSIONS:" + GameData.getMissionControl().getCompletedMissions());
            ArrayList<CrewStats> stats = StatisticsManager.getInstance().getAllStats();
            pw.println("STATS_COUNT:" + stats.size());
            for (CrewStats s : stats) {
                pw.println(
                        s.getCrewId() + "," +
                                s.getCrewName() + "," +
                                s.getMissionsCompleted() + "," +
                                s.getMissionsWon() + "," +
                                s.getTrainingSessions()
                );
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loadData(Context ctx) {
        File f = new File(ctx.getFilesDir(), FILE_NAME);
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            GameData.reset();
            int crewCount = Integer.parseInt(br.readLine().split(":")[1]);
            int maxId = 0;
            for (int i = 0; i < crewCount; i++) {
                String[] p = br.readLine().split(",");
                int id = Integer.parseInt(p[0]);
                if (id > maxId) maxId = id;
                String name = p[1];
                String spec = p[2];
                int en  = Integer.parseInt(p[6]);
                int xp  = Integer.parseInt(p[7]);
                String loc = p[8];
                CrewMember cm = createFromSpec(id, name, spec);
                cm.setEnergy(en);
                cm.setExperience(xp);
                GameData.getStorage().addCrewMember(cm);
                switch (loc) {
                    case "QUARTERS":
                        GameData.getQuarters().addCrewMemberNoRestore(cm);
                        break;
                    case "SIMULATOR":
                        GameData.getSimulator().addCrewMember(cm);
                        break;
                    case "MISSIONCONTROL":
                        GameData.getMissionControl().addCrewMember(cm);
                        break;
                    default:
                        GameData.getMedBay().admitPatient(cm);
                        break;
                }
            }
            CrewMember.setNextId(maxId + 1);

            String line;

            line = br.readLine();
            int totalM = Integer.parseInt(line.split(":")[1]);

            line = br.readLine();
            int totalW = Integer.parseInt(line.split(":")[1]);

            line = br.readLine();
            int completed = Integer.parseInt(line.split(":")[1]);

            StatisticsManager.getInstance().setTotalMissions(totalM);
            StatisticsManager.getInstance().setTotalWins(totalW);
            GameData.getMissionControl().setCompletedMissions(completed);

            line = br.readLine();
            int statsCount = Integer.parseInt(line.split(":")[1]);

            for (int i = 0; i < statsCount; i++) {
                String[] p = br.readLine().split(",");
                CrewStats cs = new CrewStats(Integer.parseInt(p[0]), p[1]);
                cs.setMissionsCompleted(Integer.parseInt(p[2]));
                cs.setMissionsWon(Integer.parseInt(p[3]));
                cs.setTrainingSessions(Integer.parseInt(p[4]));
                StatisticsManager.getInstance().getStatsMap().put(cs.getCrewId(), cs);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static CrewMember createFromSpec(int id, String name, String spec) {
        switch (spec) {
            case "Pilot":     return new Pilot(id, name);
            case "Engineer":  return new Engineer(id, name);
            case "Medic":     return new Medic(id, name);
            case "Scientist": return new Scientist(id, name);
            case "Soldier":   return new Soldier(id, name);
            default:          return new Pilot(id, name);
        }
    }

    public static boolean hasSaveFile(Context ctx) {
        return new File(ctx.getFilesDir(), FILE_NAME).exists();
    }
}