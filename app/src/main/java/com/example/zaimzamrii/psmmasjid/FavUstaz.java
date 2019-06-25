package com.example.zaimzamrii.psmmasjid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zaimzamrii.psmmasjid.JB.UstazJB;
import com.example.zaimzamrii.psmmasjid.helper.CircleTransform;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FavUstaz extends AppCompatActivity {

    private ActionBar toolbar;
    private RecyclerView postList;
    private DatabaseReference dbUstaz,LikesRef;
    Boolean LikeChecker = false;


    private FirebaseAuth mAuth;
    private String currentUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_ustaz);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Favourite Ustaz");

        postList = findViewById(R.id.recycler_fav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        dbUstaz = FirebaseDatabase.getInstance().getReference().child("ustaz");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes").child(currentUserid);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        allpost();
    }

    private void allpost() {

        //Query SortAgentPost = Postsref.orderByChild("uid").startAt(currentUserid).endAt(currentUserid + "\uf8ff");
        final Query SortAgentPost = LikesRef.orderByChild(currentUserid);
        SortAgentPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    FirebaseRecyclerOptions<UstazJB> options = new FirebaseRecyclerOptions.Builder<UstazJB>().setQuery(SortAgentPost, UstazJB.class).build();



                    FirebaseRecyclerAdapter<UstazJB,PostsViewHolder> adapter = new FirebaseRecyclerAdapter<UstazJB, PostsViewHolder>(options)
                    {
                        @Override
                        protected void onBindViewHolder(@NonNull final PostsViewHolder holder, final int position, @NonNull UstazJB model)
                        {

                            final String PostKey = getRef(position).getKey();
                            dbUstaz.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dbUstaz.child(PostKey).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()){
                                                String name = dataSnapshot.child("name").getValue().toString();
                                                String kelulusan = dataSnapshot.child("kelulusan").getValue().toString();
                                                String image = dataSnapshot.child("image").getValue().toString();

                                                holder.from.setText(name);
                                                holder.message.setText(kelulusan);


                                                //set image circle
                                                if (!TextUtils.isEmpty(image)) {
                                                    Glide.with(getApplicationContext()).load(image)
                                                            .thumbnail(0.5f)
                                                            .crossFade()
                                                            .transform(new CircleTransform(getApplicationContext()))
                                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                            .into(holder.imgProfile);
                                                    holder.imgProfile.setColorFilter(null);
                                                    holder.iconText.setVisibility(View.GONE);
                                                } else {
                                                    holder.imgProfile.setImageResource(R.drawable.bg_circle);

                                                    holder.iconText.setVisibility(View.VISIBLE);
                                                }

                                                holder.vl.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getApplicationContext(),UstazProfile.class);
                                                        intent.putExtra("ustazKey",PostKey);
                                                        startActivity(intent);
                                                    }
                                                });


                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view)
                                                    {
                                                        //Untuk dpat id user
                                                        //String PostKey = getSnapshots().get(position).getUid();

                                                        // Untuk dpat Id dalam table post
                                                        String PostKey = getSnapshots().getSnapshot(position).getKey();
                                                        String ustazName = getSnapshots().get(position).getName();


                      /*  Intent click_post = new Intent(getActivity(),Agency_Details.class);
                        click_post.putExtra("PostKey", PostKey);
                        //click_post.putExtra("Agencyname", Agencyname);
                        startActivity(click_post);*/


                                                    }
                                                });

                                                holder.setLikeButtonStatus(PostKey);
                                                //holder.setDisLikeButtonStatus(PostKey);

                                                holder.iconImp.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view)
                                                    {
                                                        LikeChecker = true;

                                                        LikesRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                            {
                                                                Log.e("tengok",LikeChecker.toString()+"  -->  "+PostKey);
                                                                if(LikeChecker.equals(true))
                                                                {
                                                                    String b = dataSnapshot.child(PostKey).child(currentUserid).getValue().toString();
                                                                    Log.e("tengok11",b);

                                                                    LikesRef.child(PostKey).child(currentUserid).removeValue();
                                                                    LikeChecker = false;



                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError)
                                                            {

                                                            }
                                                        });
                                                    }
                                                });
                                            }else{

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



                            //Glide.with(MyAgencyPost.this).load(model.getPostImage()).into(holder.productimage);





                        }

                        @NonNull
                        @Override
                        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                        {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_row, viewGroup, false);
                            PostsViewHolder viewHolder = new PostsViewHolder(view);

                            return viewHolder;
                        }
                    };

                    postList.setAdapter(adapter);
                    adapter.startListening();

                }else {
                    Toast.makeText(getApplicationContext(),"No Favourite Ustaz Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }



    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView from, subject, message, iconText, timestamp;
        Integer countLikes;
        LinearLayout vl ;
        String currentUserid;
        DatabaseReference LikesRef,DisLikesRef;
        public ImageView iconImp, imgProfile;



        public PostsViewHolder(View itemView) {
            super(itemView);

            from = itemView.findViewById(R.id.from);

            message = (TextView) itemView.findViewById(R.id.txt_secondary);
            iconText = (TextView) itemView.findViewById(R.id.icon_text);
            vl = (LinearLayout) itemView.findViewById(R.id.message_container);

            iconImp = (ImageView) itemView.findViewById(R.id.icon_star);
            imgProfile = (ImageView) itemView.findViewById(R.id.icon_profile);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            DisLikesRef = FirebaseDatabase.getInstance().getReference().child("DisLikes");
            currentUserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setLikeButtonStatus(final String PostKey)
        {
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(currentUserid).hasChild(PostKey))
                    {
                        countLikes = (int) dataSnapshot.child(PostKey).child(currentUserid).getChildrenCount();
                        iconImp.setImageResource(R.drawable.wishlist3);


                    }
                    else {

                        countLikes = (int) dataSnapshot.child(PostKey).child(currentUserid).getChildrenCount();
                        iconImp.setImageResource(R.drawable.wishlist2);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }






}
