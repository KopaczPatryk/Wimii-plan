package com.example.kopac.wimiplan.Plan.Subjects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kopac.wimiplan.Plan.Models.Subject;
import com.example.kopac.wimiplan.Plan.R;
import com.example.kopac.wimiplan.Plan.RecyclerViewClickListener;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private List<Subject> Subjects;
    private RecyclerViewClickListener clickListener;

    public SubjectAdapter(List<Subject> subjects, RecyclerViewClickListener listener) {
        Subjects = subjects;
        clickListener = listener;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.subject_row, viewGroup, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder viewHolder, int i) {
        viewHolder.SubjectName.setText(Subjects.get(i).SubjectName);
    }

    @Override
    public int getItemCount() {
        return Subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView SubjectName;
        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            clickListener = listener;
            SubjectName = itemView.findViewById(R.id.subject_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnRecyclerItemClick(v, getAdapterPosition());
        }
    }
}
