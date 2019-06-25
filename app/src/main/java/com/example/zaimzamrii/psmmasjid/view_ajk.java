package com.example.zaimzamrii.psmmasjid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.zaimzamrii.psmmasjid.JB.ActivityJB;
import com.example.zaimzamrii.psmmasjid.adapter.ActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class view_ajk extends AppCompatActivity {

    TextView lblComittee;
    private RecyclerView recyclerView;
    private ActivityAdapter mAdapter;

    private DatabaseReference dbMosque;
    private List<ActivityJB> listMosque = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ajk);

        lblComittee = (TextView) findViewById(R.id.lblComittee);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_ajk);

        mAdapter = new ActivityAdapter(listMosque);

        String name = getIntent().getExtras().get("name").toString();
        lblComittee.setText("Comittee for "+name);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getAjk(name);
    }

    public void getAjk(final String name){
        dbMosque = FirebaseDatabase.getInstance().getReference("staff");
        dbMosque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMosque.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ActivityJB jb = new ActivityJB();
                    if (name.equalsIgnoreCase(ds.child("mosque").getValue().toString())){
                        jb.setActivityName(ds.child("name").getValue().toString());
                        jb.setActivityPlace(ds.child("role").getValue().toString());
                        listMosque.add(jb);

                    }
                }
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
