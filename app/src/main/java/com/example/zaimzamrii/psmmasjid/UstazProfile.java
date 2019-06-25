package com.example.zaimzamrii.psmmasjid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.zaimzamrii.psmmasjid.JB.ActivityJB;
import com.example.zaimzamrii.psmmasjid.Util.CommonUtil;
import com.example.zaimzamrii.psmmasjid.adapter.ActivityAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UstazProfile extends AppCompatActivity {

    String getUstazKey,name;
    TextView tfName,et;
    ImageView iv;


    private List<ActivityJB> listAct = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivityAdapter mAdapter;

    private DatabaseReference dbUstaz;
    private DatabaseReference dbActivity;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustaz_profile);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_activity);
        tfName = findViewById(R.id.tfUstazName);
        iv = findViewById(R.id.ustazImage);
        et = findViewById(R.id.kelulusan);

        mAdapter = new ActivityAdapter(listAct);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




        getUstazKey = getIntent().getExtras().get("ustazKey").toString();


        getActivity(getUstazKey);
        getEvent();
    }

    private void getActivity(String ustazUid){
        dbUstaz = FirebaseDatabase.getInstance().getReference("ustaz").child(getUstazKey);
        mAuth = FirebaseAuth.getInstance();

        dbUstaz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("ustaz",dataSnapshot.getKey().toString()+"->"+getUstazKey);
                tfName.setText(dataSnapshot.child("name").getValue().toString());
                downloadImage(dataSnapshot.child("image").getValue().toString());
                et.setText(dataSnapshot.child("kelulusan").getValue().toString());

                name = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void downloadImage(String link){
        Glide.with(this).load(link).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public void getEvent(){
        dbActivity = FirebaseDatabase.getInstance().getReference("activity");

        dbActivity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAct.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ActivityJB jb = new ActivityJB();

                    Log.e("ustaz",ds.child("ustaz").getValue().toString()+" -> "+name);
                    if (ds.child("ustaz").getValue().toString().equalsIgnoreCase(name)){

                        CommonUtil c = new CommonUtil();
                        String date = c.changeDateFormat(ds.child("startDate").getValue().toString(),"dd/MM/yyyy","yyyy/MM/dd");
                        Log.e("jam3",date);
                        if (validateDate(date)){
                            jb.setActivityName(ds.child("title").getValue().toString());
                            jb.setActivityDate(ds.child("startDate").getValue().toString()+"  "+ds.child("startTime").getValue().toString()+" - "+ds.child("endTime").getValue().toString());
                            jb.setActivityPlace(ds.child("place").getValue().toString()+", "+ds.child("mosque").getValue().toString());
                            jb.setName(ds.child("ustaz").getValue().toString());
                            listAct.add(jb);
                        }

                    }

                }
                mAdapter.notifyDataSetChanged();
                Log.e("ustaz2",String.valueOf(listAct.size()));
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
