package com.example.kopac.wimiplan.Plan;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {
    private List<Term> Terms;
    private RecyclerViewClickListener clickListener;

    public TermAdapter(List<Term> terms, RecyclerViewClickListener listener) {
        Terms = terms;
        clickListener = listener;
    }

    @NonNull
    @Override
    public TermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.terms_row, viewGroup, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TermName.setText(Terms.get(i).TermName);
    }

    @Override
    public int getItemCount() {
        return Terms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TermName;
        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            clickListener = listener;
            TermName = itemView.findViewById(R.id.term_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnRecyclerItemClick(v, getAdapterPosition());
        }
    }
}
