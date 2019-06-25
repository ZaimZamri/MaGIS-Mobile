package com.example.zaimzamrii.psmmasjid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageProfile extends AppCompatActivity {

    private ActionBar toolbar;
    private DatabaseReference dbUser;
    private FirebaseAuth mAuth;
    private  String uid;
    ImageView iv;
    EditText tfName,tfPhone;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        getSupportActionBar().setTitle("Manage Profile");

        iv = (ImageView) findViewById(R.id.imageUser);
        tfName = (EditText) findViewById(R.id.editUserName);
        tfPhone = (EditText) findViewById(R.id.editUserPhone) ;
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();


        loadDataUser(uid);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    dbUser.child("name").setValue(tfName.getText().toString());
                    dbUser.child("phone").setValue(tfPhone.getText().toString());

                    Toast.makeText(getApplicationContext(),"Save Success !",Toast.LENGTH_SHORT);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void loadDataUser(String uid){
        dbUser = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                downloadImage(dataSnapshot.child("profileimage2").getValue().toString());


                tfName.setText(dataSnapshot.child("name").getValue().toString());
                tfPhone.setText(dataSnapshot.child("phone").getValue().toString());



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
}
