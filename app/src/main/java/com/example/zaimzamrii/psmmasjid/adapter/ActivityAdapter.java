package com.example.zaimzamrii.psmmasjid.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zaimzamrii.psmmasjid.JB.ActivityJB;
import com.example.zaimzamrii.psmmasjid.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {

    private List<ActivityJB> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tfname, tfplace, tfdate;

        public MyViewHolder(View view) {
            super(view);
            tfname = (TextView) view.findViewById(R.id.name);
            tfplace = (TextView) view.findViewById(R.id.place);
            tfdate = (TextView) view.findViewById(R.id.date);
        }
    }

    public ActivityAdapter(List<ActivityJB> listActivity) {
        this.list = listActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        ActivityJB movie = list.get(i);
        holder.tfname.setText(movie.getActivityName());
        holder.tfplace.setText(movie.getActivityPlace());
        holder.tfdate.setText(movie.getActivityDate());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}
