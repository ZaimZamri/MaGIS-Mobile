package com.example.zaimzamrii.psmmasjid.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zaimzamrii.psmmasjid.R;
import com.example.zaimzamrii.psmmasjid.UstazProfile;
import com.example.zaimzamrii.psmmasjid.adapter.MessagesAdapter;
import com.example.zaimzamrii.psmmasjid.helper.DividerItemDecoration;
import com.example.zaimzamrii.psmmasjid.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ustaz extends Fragment implements SwipeRefreshLayout.OnRefreshListener,MessagesAdapter.MessageAdapterListener {



    private List<Message> list = new ArrayList<>();
    private ArrayList listFav = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    private DatabaseReference dbUstaz;
    private FirebaseAuth mAuth;
    private String userID;

    private ActionMode actionMode;



    public ustaz() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_ustaz,null);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new MessagesAdapter(getActivity(), list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);



        dbUstaz = FirebaseDatabase.getInstance().getReference("ustaz");
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        // show loader and fetch messages



        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );


        return v;
    }

    private void getInbox() {

        swipeRefreshLayout.setRefreshing(true);

       dbUstaz.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               list.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Message jb = new Message();
                    jb.setKey(ds.getKey());
                    jb.setFrom(ds.child("name").getValue().toString());
                    jb.setPicture(ds.child("image").getValue().toString());
                    jb.setMessage(ds.child("kelulusan").getValue().toString());

                    list.add(jb);
                    Log.i("tengok sizelist",String.valueOf(list.size()));
                }
               mAdapter.notifyDataSetChanged();
               swipeRefreshLayout.setRefreshing(false);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(getActivity(), "Check Internet Your Connection: ", Toast.LENGTH_LONG).show();
               swipeRefreshLayout.setRefreshing(false);

           }
       });

    }


    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        getInbox();

    }


    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        Message message = list.get(position);
        message.setImportant(!message.isImportant());
        list.set(position, message);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Read: "+message.getKey(), Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        Message message = list.get(position);
        message.setRead(false);
        list.set(position, message);
        mAdapter.notifyDataSetChanged();

        Intent intent = new Intent(getActivity(),UstazProfile.class);
        intent.putExtra("ustazKey",message.getKey());
        startActivity(intent);


        Toast.makeText(getActivity(), "Read: "+message.getKey(), Toast.LENGTH_SHORT).show();



    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search){
            Toast.makeText(getActivity(), "Search...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }





}
