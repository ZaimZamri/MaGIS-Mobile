package com.example.zaimzamrii.psmmasjid.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zaimzamrii.psmmasjid.ChangePassword;
import com.example.zaimzamrii.psmmasjid.FavUstaz;
import com.example.zaimzamrii.psmmasjid.LoginActivity;
import com.example.zaimzamrii.psmmasjid.ManageProfile;
import com.example.zaimzamrii.psmmasjid.R;
import com.example.zaimzamrii.psmmasjid.viewReminder;
import com.google.firebase.auth.FirebaseAuth;


public class setting extends Fragment {

    Button btnFav,btnProfile,btnChangePass,btnSignOut,btnReminder;


    public setting() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting,null);

        btnFav = (Button) view.findViewById(R.id.btnFav);
        btnProfile = (Button) view.findViewById(R.id.btnProfile);
        btnChangePass = (Button) view.findViewById(R.id.btnChangePassword);
        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);
        btnReminder = (Button) view.findViewById(R.id.btnReminder);

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), new FavUstaz().getClass());
                startActivity(i);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),new ManageProfile().getClass());
                startActivity(intent);
            }
        });


        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(),new ChangePassword().getClass() );
                startActivity(in);
            }
        });

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(getActivity(),new viewReminder().getClass());
                startActivity(inte);
            }
        });



        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("Are you sure you want to logout?");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(),LoginActivity.class));

                    }
                });

                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertBuilder.show();

            }
        });





        return view;
    }




}
