package com.example.zaimzamrii.psmmasjid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zaimzamrii.psmmasjid.sqlite.CustomAdapterExpList;
import com.example.zaimzamrii.psmmasjid.sqlite.NotesDBModel;
import com.example.zaimzamrii.psmmasjid.sqlite.editReminder;
import com.example.zaimzamrii.psmmasjid.sqlite.noteDB;

import java.util.ArrayList;


public class viewReminder extends AppCompatActivity {

    ActionBar toolbar;

    RecyclerView recyclerViewExpList;
    ArrayList<NotesDBModel> list;
    LinearLayout ll ;

    noteDB db;
    CustomAdapterExpList adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        toolbar = getSupportActionBar();
        toolbar.setTitle("All Notes");

        recyclerViewExpList  = findViewById(R.id.recyclerNotes);

        db = new noteDB(getApplicationContext());
        list = (ArrayList<NotesDBModel>)db.fnGetAllExpenses();
            adapter = new CustomAdapterExpList(db.fnGetAllExpenses());
            recyclerViewExpList.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewExpList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent i = new Intent(getApplication(),new addReminder().getClass());
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        list = (ArrayList<NotesDBModel>)db.fnGetAllExpenses();
        adapter = new CustomAdapterExpList(db.fnGetAllExpenses());
        recyclerViewExpList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
