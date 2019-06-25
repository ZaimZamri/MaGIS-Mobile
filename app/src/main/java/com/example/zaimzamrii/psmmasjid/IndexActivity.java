package com.example.zaimzamrii.psmmasjid;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class IndexActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("My Masjid");
//        addFragment(new , false, "one" );

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("Mosque Nearest");
                   /* Intent intent1 = new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent1);*/
//                    fragment = new MapFragment();
//                    addFragment(fragment,false,"one");

                    break;
                case R.id.navigation_gifts:
                    toolbar.setTitle("Favourite Ustaz");
                 /*   fragment = new FavUstaz();
                    loadFragment(fragment);*/
                    break;
                case R.id.navigation_cart:
                    toolbar.setTitle("Account");
                  /*  Intent intent2 = new Intent(IndexActivity.this, com.example.zaimzamrii.psmmasjid.Account.class);
                    startActivity(intent2);*/
                    break;

            }
            return false;
        }
    };

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.frame_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }




}
