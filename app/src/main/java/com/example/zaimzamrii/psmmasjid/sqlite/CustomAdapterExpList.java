package com.example.zaimzamrii.psmmasjid.sqlite;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.zaimzamrii.psmmasjid.R;

import java.util.List;

public class CustomAdapterExpList extends RecyclerView.Adapter<CustomAdapterExpList.ViewHolder> {

    List<NotesDBModel> list;
    double sum = 0;
    TextView lblSum;
    NotesDBModel jb;

    public CustomAdapterExpList(List<NotesDBModel> expenseList){
        this.list=expenseList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notes_recycler,parent,false);
        View lay = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_view_reminder,parent,false);

        return new ViewHolder(view,lay);
    }


    @Override
    public void onBindViewHolder(@NonNull final CustomAdapterExpList.ViewHolder holder, int position) {
         jb = list.get(position);

        holder.txtVwExpName.setText(jb.getTitle());
        holder.txtVwExpPrice.setText(String.valueOf(jb.getDesc()));
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, editReminder.class);
                intent.putExtra("id",jb.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtVwExpName,txtVwExpPrice;
        LinearLayout ll;
        public ViewHolder(@NonNull View itemView,View lay) {
            super(itemView);

            txtVwExpName = itemView.findViewById(R.id.lblTitle);
            txtVwExpPrice = itemView.findViewById(R.id.lblDesc);
            ll = itemView.findViewById(R.id.llNotes);
        }
    }





}

