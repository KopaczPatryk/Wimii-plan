package com.example.kopac.wimiplan.Plan;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kopac.wimiplan.Plan.Models.SchoolDaySchedule;
import com.example.kopac.wimiplan.Plan.Models.Subject;
import com.example.kopac.wimiplan.Plan.SchoolDayFragment.OnListFragmentInteractionListener;
import com.example.kopac.wimiplan.Plan.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SchoolDayRecyclerViewAdapter extends RecyclerView.Adapter<SchoolDayRecyclerViewAdapter.ViewHolder> {

    private final SchoolDaySchedule mValues;
    private final OnListFragmentInteractionListener mListener;

    public SchoolDayRecyclerViewAdapter(SchoolDaySchedule items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schoolday_subject, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Subject subject = mValues.Subjects.get(position);
        holder.SubjectType.setText(subject.Type);
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

        public final TextView SubjectType;
        public final TextView StartHour;
        public final TextView SubjectName;
        public final TextView SubjectTeacher;
        public final TextView SubjectRoom;

        public ViewHolder(View view) {
            super(view);
           //mView = view;
            SubjectType = (TextView) view.findViewById(R.id.subject_type);
            StartHour = (TextView) view.findViewById(R.id.subject_duration);
            SubjectName = (TextView) view.findViewById(R.id.group_name);
            SubjectTeacher = (TextView) view.findViewById(R.id.subject_teacher);
            SubjectRoom = (TextView) view.findViewById(R.id.subject_room);
        }
    }
}
