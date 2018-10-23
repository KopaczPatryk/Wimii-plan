package com.example.kopac.wimiplan.Plan.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kopac.wimiplan.Plan.Models.SchoolDaySchedule;
import com.example.kopac.wimiplan.Plan.Models.Subject;
import com.example.kopac.wimiplan.Plan.R;
import com.example.kopac.wimiplan.Plan.Core.Schedule.Day.SchoolDayFragment.OnListFragmentInteractionListener;

public class SchoolDaysAdapter extends RecyclerView.Adapter<SchoolDaysAdapter.ViewHolder> {

    private final SchoolDaySchedule mValues;
    private final OnListFragmentInteractionListener mListener;

    public SchoolDaysAdapter(SchoolDaySchedule items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schoolday_subject_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Subject subject = mValues.Subjects.get(position);
        if (subject.Type != null) {
            switch (subject.Type) {
                case Laboratory:
                    holder.SubjectType.setText("Labka");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"));
                    break;
                case Exercise:
                    holder.SubjectType.setText("Ćw");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"));
                    break;
                case Lecture:
                    holder.SubjectType.setText("Wykład");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#b1dda6"));
                    break;
                case Freiheit:
                    holder.SubjectType.setText("");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"));
                    break;
                case Gap:
                    holder.SubjectType.setText("Okienko");
                    holder.SubjectName.setText("Przykro mi");
                    break;
            }
        }
        holder.StartHour.setText(subject.HourStart);
        holder.SubjectName.setText(subject.SubjectName);
        holder.SubjectTeacher.setText(subject.Teacher);
        holder.SubjectRoom.setText(subject.Room);
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.Subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;

        public final LinearLayout SubjectLayout;
        public final TextView SubjectType;
        public final TextView StartHour;
        public final TextView SubjectName;
        public final TextView SubjectTeacher;
        public final TextView SubjectRoom;

        public ViewHolder(View view) {
            super(view);
           //mView = view;
            SubjectLayout = view.findViewById(R.id.row_container);
            SubjectType = view.findViewById(R.id.subject_type);
            StartHour = view.findViewById(R.id.subject_duration);
            SubjectName = view.findViewById(R.id.group_name);
            SubjectTeacher = view.findViewById(R.id.subject_teacher);
            SubjectRoom = view.findViewById(R.id.subject_room);
        }
    }
}
