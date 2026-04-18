package com.example.spacecolony.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.spacecolony.R;
import com.example.spacecolony.filemanager.FileManager;
import com.example.spacecolony.ui.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FileManager.hasSaveFile(this)) {
            boolean loaded = FileManager.loadData(this);
            if (loaded) Toast.makeText(this, "Colony data loaded.", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if      (id == R.id.nav_home)      fragment = new HomeFragment();
            else if (id == R.id.nav_recruit)   fragment = new RecruitFragment();
            else if (id == R.id.nav_quarters)  fragment = new QuartersFragment();
            else if (id == R.id.nav_simulator) fragment = new SimulatorFragment();
            else if (id == R.id.nav_mission)   fragment = new MissionControlFragment();
            else if (id == R.id.nav_more)      fragment = new MoreFragment();
            if (fragment != null) loadFragment(fragment);
            return true;
        });

        // Default fragment
        if (savedInstanceState == null) loadFragment(new HomeFragment());
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}