package com.example.zaimzamrii.psmmasjid;

import android.Manifest;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zaimzamrii.psmmasjid.JB.ActivityJB;
import com.example.zaimzamrii.psmmasjid.JB.MarkerJB;
import com.example.zaimzamrii.psmmasjid.JB.MosqueJB;
import com.example.zaimzamrii.psmmasjid.fragment.setting;
import com.example.zaimzamrii.psmmasjid.maps.GetNearbyPlaces;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private DatabaseReference dbActivities,dbLike,dbact,dbUstaz,dbMosque;
    private FirebaseAuth mAuth;
    private String userID;
    private ActionBar toolbar;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private double latitude, longitude;


    private int ProximityRadius = 10000;
    SupportMapFragment mapFragment;
    FrameLayout fl;

    private String PostAddress;


    //t


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        dbActivities = FirebaseDatabase.getInstance().getReference("activity");

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        dbLike = FirebaseDatabase.getInstance().getReference("Likes").child(userID);
        dbact = FirebaseDatabase.getInstance().getReference("activity");
        dbUstaz = FirebaseDatabase.getInstance().getReference("ustaz");
        dbMosque = FirebaseDatabase.getInstance().getReference("mosque");

        //t
        toolbar = getSupportActionBar();
        toolbar.setTitle("Nearest Mosque");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLike();


    }



    private String getURL(double latitude,double longitude, String nearbyPlace)
    {

        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");


        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();

            mMap.setPadding(0,0,0,90);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setMyLocationEnabled(true);

        }

    }

    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location)
    {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;

        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        //Old user current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(4));
        mMap.getUiSettings().setMapToolbarEnabled(true);


        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();


        String url = getURL(latitude,longitude,"mosque");
        transferData[0] = mMap;
        transferData[1] = url;
        Log.e("GPSTEST",url);

        getNearbyPlaces.execute(transferData);

        dbActivities.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList listNama = new ArrayList();
                final ArrayList<MarkerJB> listMarker = new ArrayList<>();

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                final String formattedDate = df.format(c);


                for(final DataSnapshot ds: dataSnapshot.getChildren()){
                    MarkerJB jb = new MarkerJB();
                    String a = ds.toString();
//                            Toast.makeText(MapsActivity.this, a, Toast.LENGTH_SHORT).show();



                    Log.i("tarikh",formattedDate+"->"+ds.child("startDate").getValue().toString());
                    if(formattedDate.contains(ds.child("startDate").getValue().toString())){
                        String namaMasjid = ds.child("mosque").getValue().toString().toUpperCase();
                        jb.setTitle(namaMasjid);
                        Log.i("Tarikh3","Masuk set snippet");
                        if (ds.child("ustaz").exists()){
                            jb.setSnippet("Today's Activities"+"\n"+"Title: "+ds.child("title").getValue().toString()+"\n"+"Speaker: "+ds.child("ustaz").getValue().toString()+"\n"+"Time: "+ds.child("startTime").getValue().toString()+" - "+ds.child("endTime").getValue().toString());
                        }else{
                            jb.setSnippet("Today's Activities"+"\n"+"Title: "+ds.child("title").getValue().toString()+"\n"+"Time: "+ds.child("startTime").getValue().toString()+" - "+ds.child("endTime").getValue().toString());
                        }

                        jb.setStartDate(ds.child("startDate").getValue().toString());

                        if (listNama.contains(namaMasjid)){

                        }else{
                            listNama.add(namaMasjid);

                        }

                    }

//                    listNama.add(namaMasjid);

                    listMarker.add(jb);



                    String postKey = ds.getKey();



                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {


                            Context mContext = getApplicationContext();
                            LinearLayout info = new LinearLayout(mContext);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(mContext);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(mContext);
                            snippet.setTextColor(Color.GRAY);

                            Log.i("Size1 ",String.valueOf(listMarker.size()));
                            if (listNama.contains(marker.getTitle().toUpperCase())){
                                for(MarkerJB jb: listMarker){
                                    if (marker.getTitle().toUpperCase().equalsIgnoreCase(jb.getTitle())){
                                        Log.i("tarikh1",marker.getTitle()+"->"+jb.getSnippet());
                                        if (formattedDate.contains(jb.getStartDate())){
                                            Log.i("tarikhu","ada");
                                            snippet.setText("Rating: "+marker.getSnippet()+" Star"+"\n"+jb.getSnippet());
                                        }


                                    }
                                }


                            }else{
                                snippet.setText("Rating: "+marker.getSnippet()+"\n"+"No Activity Found");

                            }

                            info.addView(title);
                            info.addView(snippet);


                            return info;




                        }
                    });

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            mMap.getUiSettings().setMapToolbarEnabled(true);
                            return false;
                        }
                    });

                    final List<MosqueJB> listMasjid = new ArrayList<>();
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(final Marker marker) {


                            dbMosque.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    MosqueJB jb = new MosqueJB();
                                    listMasjid.clear();

                                    for(final DataSnapshot ds: dataSnapshot.getChildren()){
                                        jb = new MosqueJB();
                                        jb.setKey(ds.getKey());
                                        jb.setName(ds.child("name").getValue().toString());
                                        jb.setAddress(ds.child("address").getValue().toString());
                                        jb.setPhone(ds.child("phone").getValue().toString());
                                        jb.setImage(ds.child("image").getValue().toString());
                                        listMasjid.add(jb);

                                    }

                                    if(listMasjid.size()>0){
                                        for (MosqueJB j: listMasjid){
                                            if (marker.getTitle().equalsIgnoreCase(j.getName())){
                                                Intent intent = new Intent(getApplicationContext(),view_comittee.class);
                                                intent.putExtra("key",j.getKey());
                                                startActivity(intent);

                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }
                    });




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Toast.makeText(this, "Searching for nearby mosque", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Showing Nearby mosque", Toast.LENGTH_SHORT).show();



        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //t
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentManager fm = getFragmentManager();
            android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
            if (mapFragment.isAdded())
                sFm.beginTransaction().hide(mapFragment).commit();

            switch (item.getItemId()) {



                case R.id.navigation_shop:
                    toolbar.setTitle("Nearest Mosque");
                   /* Intent intent1 = new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent1);*/
//                    fragment = new MapFragment();
//                    addFragment(fragment,false,"one");

                    if (!mapFragment.isAdded())
                        sFm.beginTransaction().add(R.id.frame_container, mapFragment).commit();
                    else
                        fl = (FrameLayout)findViewById(R.id.frame_container);
                    fl.setVisibility(View.INVISIBLE);

                    sFm.beginTransaction().show(mapFragment).commit();

                    break;
                case R.id.navigation_gifts:
                    toolbar.setTitle("All Ustaz");
                    fl = (FrameLayout)findViewById(R.id.frame_container);
                    fl.setVisibility(View.VISIBLE);
//                    addFragment(new ustaz(),true,"1");
                    addFragment(new ListUstaz(),true,"1");
                    break;
                case R.id.navigation_cart:
                    toolbar.setTitle("My Account");
                    fl = (FrameLayout)findViewById(R.id.frame_container);
                    fl.setVisibility(View.VISIBLE);
                    addFragment(new setting(),true,"1");
//                    fm.beginTransaction().replace(R.id.frame_container, new ImportFragment()).commit();
                  /*  Intent intent2 = new Intent(IndexActivity.this, com.example.zaimzamrii.psmmasjid.Account.class);
                    startActivity(intent2);*/
                    break;

            }
            return false;
        }
    };

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.frame_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    private  void getLike(){
        final List<String> ListFavUstaz = new ArrayList<>();
        final List<String> ListNamaFav = new ArrayList<>();
        final List<ActivityJB> keyActivity = new ArrayList<>();


        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final String formattedDate = df.format(c);

        dbLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListFavUstaz.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String requests = postSnapshot.getKey();

                    ListFavUstaz.add(requests);
                }

                Log.e("jadi",String.valueOf(ListFavUstaz.size()));

                dbUstaz.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ListNamaFav.clear();
                        for (DataSnapshot dd : dataSnapshot.getChildren()){

                            if (ListFavUstaz.contains(dd.getKey())){

                                ListNamaFav.add(dd.child("name").getValue().toString());


                            }
                        }
                        Log.e("jadi2",ListNamaFav.toString());

                        dbact.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                keyActivity.clear();
                                ActivityJB jb = new ActivityJB();
                                for (DataSnapshot da : dataSnapshot.getChildren()) {
                                    jb = new ActivityJB();

                                    if (ListNamaFav.contains(da.child("ustaz").getValue().toString())){
                                        if (formattedDate.contains(da.child("startDate").getValue().toString())){
                                            Log.e("jadi4",da.getKey());
                                            jb.setKey(da.getKey());
                                            jb.setName(da.child("ustaz").getValue().toString());
                                            jb.setActivityDate(da.child("startDate").getValue().toString());
                                            jb.setTime(da.child("startTime").getValue().toString());
                                            jb.setLocation(da.child("place").getValue().toString()+" - "+da.child("mosque").getValue().toString());
                                            jb.setActivityName(da.child("title").getValue().toString());
                                            keyActivity.add(jb);

                                            //Calendar now = Calendar.getInstance();
                                            //now.add(Calendar.MINUTE, 30);
                                            // 24 hours format
                                            //SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                                            //else
                                            //get current Time
                                         //   long currentTime = System.currentTimeMillis();
                                            //now add half an hour, 1 800 000 miliseconds = 30 minutes
                                           // long halfAnHourLater = currentTime + 1800000;
                                            //System.currentTimeMillis()+(30*60*1000);

                                            //get current time + 30minit = jb.getTime

                                            if (keyActivity.size()> 0){

//                                                LocalTime now = LocalTime.now();
//                                                LocalTime limit = LocalTime.parse( jb.getTime() );
//                                                Boolean isLate = now.isAfter( limit );

                                                setAlarm(jb);
                                            }
                                        }
                                    }
                                }




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void setAlarm(ActivityJB jb){






        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm");

           String a = jb.getActivityDate()+jb.getTime();

           String day = a.substring(0,2);
            String month=a.substring(3,5);
            String year=a.substring(6,10);
            String hour=a.substring(10,12);
            String minit=a.substring(13,15);
            Log.e("jam",year+"-"+month+"-"+day+"-"+hour+"-"+minit);


            Calendar myAlarmDate = Calendar.getInstance();
            myAlarmDate.setTimeInMillis(System.currentTimeMillis());
//            myAlarmDate.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minit), 0);
//            myAlarmDate.set(2019, 05, 28, 04, 20, 0);
        myAlarmDate.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
        myAlarmDate.set(Calendar.MINUTE,Integer.parseInt(minit));
        myAlarmDate.set(Calendar.SECOND,0);




        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

//         jb.setName(da.child("ustaz").getValue().toString());
//                                            jb.setActivityDate(da.child("startDate").getValue().toString());
//                                            jb.setTime(da.child("startTime").getValue().toString());
//                                            jb.setLocation(da.child("place").getValue().toString()+" - "+da.child("mosque").toString());
//                                            jb.setActivityName(da.child("title").getValue().toString());
            Intent _myIntent = new Intent(this, ItemBroadcastReceiver.class);
            _myIntent.putExtra("ActivityName",jb.getActivityName());
            _myIntent.putExtra("Location",jb.getLocation());
            _myIntent.putExtra("time",jb.getTime());
            _myIntent.putExtra("date",jb.getActivityDate());
        _myIntent.putExtra("ustaz",jb.getName());

            PendingIntent _myPendingIntent = PendingIntent.getBroadcast(this, 1, _myIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(),_myPendingIntent);




    }

    public static Date convertDisplayDateStrtoSQLDateObj(String dt) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date tempDate = new Date();

        try {
            tempDate = df.parse(dt);
        } catch (Exception e) {
        }

        return tempDate;

    }

    private Date convertTimeToDate(String time){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date d = new Date();
        try{
             d = dateFormat.parse(time);
        }catch (Exception e){
            e.printStackTrace();
        }
       return d;

    }



}
