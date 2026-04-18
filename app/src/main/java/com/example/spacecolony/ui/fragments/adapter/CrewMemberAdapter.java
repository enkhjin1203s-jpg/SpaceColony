package com.example.spacecolony.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacecolony.R;
import com.example.spacecolony.model.CrewMember;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrewMemberAdapter extends RecyclerView.Adapter<CrewMemberAdapter.ViewHolder> {

    public interface OnCrewClickListener {
        void onCrewClick(CrewMember cm);
    }

    private List<CrewMember> crewList;
    private Set<Integer> selectedIds = new HashSet<>();
    private OnCrewClickListener listener;
    private boolean selectable;
    private TextView selectionCountView;

    public CrewMemberAdapter(List<CrewMember> crewList, boolean selectable, OnCrewClickListener listener) {
        this.crewList = new ArrayList<>(crewList);
        this.selectable = selectable;
        this.listener = listener;
    }

    public CrewMemberAdapter(List<CrewMember> crewList, boolean selectable, OnCrewClickListener listener, TextView selectionCountView) {
        this(crewList, selectable, listener);
        this.selectionCountView = selectionCountView;
    }

    private void updateSelectionCount() {
        if (selectionCountView != null) {
            int count = selectedIds.size();
            selectionCountView.setText(count == 0 ? "None selected" : count + " selected");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CrewMember cm = crewList.get(position);
        holder.txtName.setText(cm.getName());
        holder.txtInfo.setText(cm.getInfo());
        holder.txtEnergy.setText("HP: " + cm.getEnergy() + "/" + cm.getMaxEnergy());
        holder.imgCrew.setImageResource(getCrewImage(cm.getSpecialization()));

        if (selectable) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(selectedIds.contains(cm.getId()));
            holder.checkBox.setOnCheckedChangeListener((b, checked) -> {
                if (checked) selectedIds.add(cm.getId());
                else selectedIds.remove(cm.getId());
                updateSelectionCount();
            });
            holder.itemView.setOnClickListener(v -> holder.checkBox.toggle());
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onCrewClick(cm);
            });
        }
    }

    private int getCrewImage(String specialization) {
        switch (specialization) {
            case "Pilot":     return R.drawable.crew_pilot;
            case "Engineer":  return R.drawable.crew_engineer;
            case "Medic":     return R.drawable.crew_medic;
            case "Scientist": return R.drawable.crew_scientist;
            case "Soldier":   return R.drawable.crew_soldier;
            default:          return R.drawable.crew_pilot;
        }
    }

    @Override
    public int getItemCount() { return crewList.size(); }

    public void updateList(List<CrewMember> newList) {
        crewList = new ArrayList<>(newList);
        selectedIds.clear();
        notifyDataSetChanged();
    }

    public List<CrewMember> getSelectedCrew() {
        List<CrewMember> selected = new ArrayList<>();
        for (CrewMember cm : crewList) {
            if (selectedIds.contains(cm.getId())) selected.add(cm);
        }
        return selected;
    }

    public void clearSelections() {
        selectedIds.clear();
        updateSelectionCount();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtInfo, txtEnergy;
        CheckBox checkBox;
        ImageView imgCrew;

        ViewHolder(View v) {
            super(v);
            txtName   = v.findViewById(R.id.txtCrewName);
            txtInfo   = v.findViewById(R.id.txtCrewInfo);
            txtEnergy = v.findViewById(R.id.txtCrewEnergy);
            checkBox  = v.findViewById(R.id.checkBoxCrew);
            imgCrew   = v.findViewById(R.id.imgCrew);
        }
    }
}