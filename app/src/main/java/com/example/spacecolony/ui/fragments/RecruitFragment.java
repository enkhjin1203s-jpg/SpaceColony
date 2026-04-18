package com.example.spacecolony.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.spacecolony.R;
import com.example.spacecolony.data.GameData;
import com.example.spacecolony.model.*;
import com.example.spacecolony.statistics.StatisticsManager;

public class RecruitFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruit, container, false);

        EditText edtName     = view.findViewById(R.id.edtName);
        RadioGroup radioSpec = view.findViewById(R.id.radioGroupSpecialization);
        Button btnCreate     = view.findViewById(R.id.btnCreate);
        Button btnClear      = view.findViewById(R.id.btnClear);
        TextView txtResult   = view.findViewById(R.id.txtResult);

        // Stat preview card views
        LinearLayout cardSpecPreview = view.findViewById(R.id.cardSpecPreview);
        TextView txtSpecName        = view.findViewById(R.id.txtSpecName);
        TextView txtSpecDescription = view.findViewById(R.id.txtSpecDescription);
        TextView txtSpecSkill       = view.findViewById(R.id.txtSpecSkill);
        TextView txtSpecRes         = view.findViewById(R.id.txtSpecRes);
        TextView txtSpecHp          = view.findViewById(R.id.txtSpecHp);
        TextView txtSpecTip         = view.findViewById(R.id.txtSpecTip);

        // Show stat preview when user selects a specialization
        radioSpec.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                cardSpecPreview.setVisibility(View.GONE);
                return;
            }
            cardSpecPreview.setVisibility(View.VISIBLE);
            if (checkedId == R.id.radioPilot) {
                txtSpecName.setText("🔵 Pilot");
                txtSpecDescription.setText("A versatile crew member with balanced stats. High resilience means they take less damage per hit — great for surviving long missions.");
                txtSpecSkill.setText("5");
                txtSpecRes.setText("4");
                txtSpecHp.setText("20");
                txtSpecTip.setText("💡 Best for: tanking hits and keeping the squad alive.");
            } else if (checkedId == R.id.radioEngineer) {
                txtSpecName.setText("🟡 Engineer");
                txtSpecDescription.setText("A solid all-rounder. Decent skill and good resilience make Engineers reliable in most situations without excelling or struggling anywhere.");
                txtSpecSkill.setText("6");
                txtSpecRes.setText("3");
                txtSpecHp.setText("19");
                txtSpecTip.setText("💡 Best for: consistent damage with decent survivability.");
            } else if (checkedId == R.id.radioMedic) {
                txtSpecName.setText("🟢 Medic");
                txtSpecDescription.setText("High skill but lower resilience. Medics hit hard but absorb less damage from threats. Keep them in the fight and they'll carry missions.");
                txtSpecSkill.setText("7");
                txtSpecRes.setText("2");
                txtSpecHp.setText("18");
                txtSpecTip.setText("💡 Best for: dealing damage quickly before taking too many hits.");
            } else if (checkedId == R.id.radioScientist) {
                txtSpecName.setText("🟣 Scientist");
                txtSpecDescription.setText("The highest skill in the colony but almost no resilience. Scientists dish out massive damage but are very fragile — one bad turn can knock them out.");
                txtSpecSkill.setText("8");
                txtSpecRes.setText("1");
                txtSpecHp.setText("17");
                txtSpecTip.setText("💡 Best for: burst damage. Pair with a Pilot to protect them.");
            } else if (checkedId == R.id.radioSoldier) {
                txtSpecName.setText("🔴 Soldier");
                txtSpecDescription.setText("Maximum skill, zero resilience. Soldiers deal the most raw damage of any class but take every point of threat damage directly — extremely high risk, high reward.");
                txtSpecSkill.setText("9");
                txtSpecRes.setText("0");
                txtSpecHp.setText("16");
                txtSpecTip.setText("💡 Best for: finishing threats fast. Train them first to maximise XP bonus.");
            }
        });

        btnCreate.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Enter a name!", Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedId = radioSpec.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getContext(), "Choose a specialization!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean nameExists = false;
            for (CrewMember existing : GameData.getStorage().getAllCrewMembers()) {
                if (existing.getName().equalsIgnoreCase(name)) {
                    nameExists = true;
                    break;
                }
            }
            if (nameExists) {
                Toast.makeText(getContext(), "A crew member named \"" + name + "\" already exists!", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = CrewMember.generateId();
            CrewMember cm = null;

            if      (selectedId == R.id.radioPilot)     cm = new Pilot(id, name);
            else if (selectedId == R.id.radioEngineer)  cm = new Engineer(id, name);
            else if (selectedId == R.id.radioMedic)     cm = new Medic(id, name);
            else if (selectedId == R.id.radioScientist) cm = new Scientist(id, name);
            else if (selectedId == R.id.radioSoldier)   cm = new Soldier(id, name);

            if (cm != null) {
                GameData.getStorage().addCrewMember(cm);
                GameData.getQuarters().addCrewMember(cm);
                StatisticsManager.getInstance().ensureCrew(cm);
                txtResult.setText("✅ Recruited " + cm.getName() + " the " + cm.getSpecialization() + "!\nSent to Quarters.");
                edtName.setText("");
                radioSpec.clearCheck();
                cardSpecPreview.setVisibility(View.GONE);
            }
        });

        btnClear.setOnClickListener(v -> {
            edtName.setText("");
            radioSpec.clearCheck();
            txtResult.setText("");
            cardSpecPreview.setVisibility(View.GONE);
        });

        return view;
    }
}