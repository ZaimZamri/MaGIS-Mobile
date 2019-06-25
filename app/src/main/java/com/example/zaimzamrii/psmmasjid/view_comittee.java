package com.example.zaimzamrii.psmmasjid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zaimzamrii.psmmasjid.JB.ActivityJB;
import com.example.zaimzamrii.psmmasjid.Util.CommonUtil;
import com.example.zaimzamrii.psmmasjid.adapter.ActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class view_comittee extends AppCompatActivity {

    String key="";
    TextView lblAddress,lblPhone;
    ImageView img;
    Button btnajk;
    private RecyclerView recyclerView;
    private ActivityAdapter mAdapter;

    private DatabaseReference dbMosque,dbact;
    private List<ActivityJB> listAct = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comittee);

        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblPhone = (TextView) findViewById(R.id.lblPhone);
        img = (ImageView) findViewById(R.id.ustazImage);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_mosque);
        btnajk = (Button) findViewById(R.id.buttonAjk);

        mAdapter = new ActivityAdapter(listAct);




        key = getIntent().getExtras().get("key").toString();
        dbMosque = FirebaseDatabase.getInstance().getReference("mosque").child(key);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        getMosqueDetails();





    }

    public void getMosqueDetails(){
        dbMosque.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                lblAddress.setText("Address : "+dataSnapshot.child("address").getValue().toString());
                lblPhone.setText("Phone Number : "+dataSnapshot.child("phone").getValue().toString());
                Glide.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(img);
                getEventDetails(dataSnapshot.child("name").getValue().toString());

                btnajk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),view_ajk.class);
                        intent.putExtra("name",dataSnapshot.child("name").getValue().toString());
                        startActivity(intent);


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getEventDetails(final String name){
        dbact = FirebaseDatabase.getInstance().getReference("activity");

        dbact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot da: dataSnapshot.getChildren()){
                    ActivityJB jb = new ActivityJB();
                    if (name.equalsIgnoreCase(da.child("mosque").getValue().toString())){
                        CommonUtil c = new CommonUtil();
                        String date = c.changeDateFormat(da.child("startDate").getValue().toString(),"dd/MM/yyyy","yyyy/MM/dd");
                        Log.e("jam3",date);
                        if (validateDate(date)){

                            jb.setName(da.child("ustaz").getValue().toString());

                            jb.setActivityDate(da.child("startDate").getValue().toString()+"  "+da.child("startTime").getValue().toString()+" - "+da.child("endTime").getValue().toString());
                            jb.setActivityPlace(da.child("place").getValue().toString()+" , "+da.child("mosque").getValue().toString());
                            jb.setActivityName(da.child("title").getValue().toString());
                            listAct.add(jb);
                        }


                    }


                }

                mAdapter.notifyDataSetChanged();
                Log.e("jam4",String.valueOf(listAct.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public boolean validateDate(String date){
        boolean isAfter = false;

        Date enteredDate=null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            enteredDate = sdf.parse(date);
        }catch (Exception ex)
        {
            // enteredDate will be null if date="287686";
        }
        Date currentDate = new Date();
        if(enteredDate.after(currentDate)){
            isAfter = true;
        }else{
            isAfter = false;
        }
           return  isAfter;
    }


}
