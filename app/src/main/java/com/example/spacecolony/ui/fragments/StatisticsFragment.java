package com.example.spacecolony.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.spacecolony.R;
import com.example.spacecolony.data.GameData;
import com.example.spacecolony.statistics.CrewStats;
import com.example.spacecolony.statistics.StatisticsManager;

import java.util.List;

public class StatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        StatisticsManager sm = StatisticsManager.getInstance();
        int totalMissions = sm.getTotalMissions();
        int totalWins     = sm.getTotalWins();
        int totalCrew     = GameData.getStorage().getCount();
        int rate          = winRate(totalWins, totalMissions);

        // Metric cards
        ((TextView) view.findViewById(R.id.txtStatTotalMissions)).setText(String.valueOf(totalMissions));
        ((TextView) view.findViewById(R.id.txtStatWins)).setText(String.valueOf(totalWins));
        ((TextView) view.findViewById(R.id.txtStatLosses)).setText(String.valueOf(totalMissions - totalWins));
        ((TextView) view.findViewById(R.id.txtStatCrew)).setText(String.valueOf(totalCrew));

        // Win rate bar
        TextView txtWinRatePct = view.findViewById(R.id.txtWinRatePct);
        View viewWinBar        = view.findViewById(R.id.viewWinBar);
        txtWinRatePct.setText(rate + "%");

        // Set bar width proportionally after layout
        viewWinBar.post(() -> {
            int parentWidth = ((View) viewWinBar.getParent()).getWidth();
            int barWidth = (int) (parentWidth * rate / 100.0);
            ViewGroup.LayoutParams params = viewWinBar.getLayoutParams();
            params.width = barWidth;
            viewWinBar.setLayoutParams(params);

            // Color the bar based on win rate
            int color;
            if (rate >= 70)      color = 0xFF4ADE80; // green
            else if (rate >= 40) color = 0xFFFCD34D; // yellow
            else                 color = 0xFFF87171; // red
            viewWinBar.setBackgroundColor(color);
            txtWinRatePct.setTextColor(color);
        });

        // Per-crew rows
        LinearLayout layoutCrew = view.findViewById(R.id.layoutCrewStats);
        TextView txtNoStats     = view.findViewById(R.id.txtNoStats);
        List<CrewStats> allStats = sm.getAllStats();

        if (allStats.isEmpty()) {
            txtNoStats.setVisibility(View.VISIBLE);
        } else {
            txtNoStats.setVisibility(View.GONE);
            for (CrewStats s : allStats) {
                View row = inflater.inflate(R.layout.item_crew_stat_row, layoutCrew, false);

                ((TextView) row.findViewById(R.id.txtCrewStatName)).setText(s.getCrewName());
                ((TextView) row.findViewById(R.id.txtCrewStatMissions)).setText(String.valueOf(s.getMissionsCompleted()));
                ((TextView) row.findViewById(R.id.txtCrewStatWins)).setText(String.valueOf(s.getMissionsWon()));
                ((TextView) row.findViewById(R.id.txtCrewStatLosses)).setText(String.valueOf(s.getMissionsLost()));
                ((TextView) row.findViewById(R.id.txtCrewStatTraining)).setText(String.valueOf(s.getTrainingSessions()));

                int crewRate = winRate(s.getMissionsWon(), s.getMissionsCompleted());
                View crewBar = row.findViewById(R.id.viewCrewWinBar);
                TextView txtCrewRate = row.findViewById(R.id.txtCrewWinRate);
                txtCrewRate.setText(crewRate + "%");

                crewBar.post(() -> {
                    int parentWidth = ((View) crewBar.getParent()).getWidth();
                    int barWidth = (int) (parentWidth * crewRate / 100.0);
                    ViewGroup.LayoutParams p = crewBar.getLayoutParams();
                    p.width = Math.max(barWidth, 4);
                    crewBar.setLayoutParams(p);
                });

                layoutCrew.addView(row);
            }
        }

        return view;
    }

    private int winRate(int wins, int total) {
        if (total == 0) return 0;
        return (int) Math.round(100.0 * wins / total);
    }
}