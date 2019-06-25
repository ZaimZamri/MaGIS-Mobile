package com.example.zaimzamrii.psmmasjid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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


public class ListUstaz extends Fragment {

    private RecyclerView postList;

    private DatabaseReference dbUstaz,LikesRef;
    Boolean LikeChecker = false;


    private FirebaseAuth mAuth;
    private String currentUserid;
    private SearchView searchView;
    private TextView alllist;


    public ListUstaz() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_ustaz,null);

        postList = v.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        dbUstaz = FirebaseDatabase.getInstance().getReference().child("ustaz");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");


//        searchView = (SearchView)v.findViewById(R.id.search);
/*        alllist = (TextView)v.findViewById(R.id.alllist);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String searchBoxInput = searchView.getQuery().toString();
                searchagencyname(searchBoxInput);
                return false;
            }
        });*/

    /*    alllist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allpost();
            }
        });*/



        return v;
    }

//    public void searchagencyname(String searchBoxInput)
//    {
//        String query = searchBoxInput.toLowerCase();
//
//        Query SortAgentPost = dbUstaz.orderByChild("name").startAt(query).endAt(query + "\uf8ff");
//
//        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(SortAgentPost, Message.class).build();
//
//        FirebaseRecyclerAdapter<Message,PostsViewHolder> adapter = new FirebaseRecyclerAdapter<Message, PostsViewHolder>(options)
//        {
//            @Override
//            protected void onBindViewHolder(@NonNull PostsViewHolder holder, final int position, @NonNull Message model)
//            {
//
//                final String PostKey = getRef(position).getKey();
//
//                holder.productname.setText(model.getAgencyname());
//                holder.productprice.setText(model.getEmail());
//                holder.productdate.setText(model.getDate());
//                holder.productstatus.setText(model.getTags());
//                holder.productnumber.setText(model.getOfficenumber());
//                //Glide.with(MyAgencyPost.this).load(model.getPostImage()).into(holder.productimage);
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view)
//                    {
//                        //Untuk dpat id user
//                        //String PostKey = getSnapshots().get(position).getUid();
//
//                        // Untuk dpat Id dalam table post
//                        String PostKey = getSnapshots().getSnapshot(position).getKey();
//                        String Agencyname = getSnapshots().get(position).getAgencyname();
//
//
//                        Intent click_post = new Intent(Agency_post.this,Agency_Details.class);
//                        click_post.putExtra("PostKey", PostKey);
//                        //click_post.putExtra("Agencyname", Agencyname);
//                        startActivity(click_post);
//
//
//                    }
//                });
//
//                holder.setLikeButtonStatus(PostKey);
//                //holder.setDisLikeButtonStatus(PostKey);
//
//                holder.layout_likes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view)
//                    {
//                        LikeChecker = true;
//
//                        LikesRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                            {
//                                if(LikeChecker.equals(true))
//                                {
//                                    if(dataSnapshot.child(PostKey).hasChild(currentUserid))
//                                    {
//                                        LikesRef.child(PostKey).child(currentUserid).removeValue();
//                                        LikeChecker = false;
//
//                                    }
//                                    else {
//
//                                        LikesRef.child(PostKey).child(currentUserid).setValue(true);
//                                        DisLikesRef.child(PostKey).child(currentUserid).removeValue();
//                                        LikeChecker = false;
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError)
//                            {
//
//                            }
//                        });
//                    }
//                });
//
//
//            }
//
//            @NonNull
//            @Override
//            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//            {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_post_layout_agent2, viewGroup, false);
//                PostsViewHolder viewHolder = new PostsViewHolder(view);
//
//                return viewHolder;
//            }
//        };
//
//        postList.setAdapter(adapter);
//        adapter.startListening();
//
//    }

    @Override
    public void onStart()
    {
        super.onStart();

        allpost();
    }


    private void allpost() {

        //Query SortAgentPost = Postsref.orderByChild("uid").startAt(currentUserid).endAt(currentUserid + "\uf8ff");
        Query SortAgentPost = dbUstaz.orderByChild("name");

        FirebaseRecyclerOptions<UstazJB> options = new FirebaseRecyclerOptions.Builder<UstazJB>().setQuery(SortAgentPost, UstazJB.class).build();

        FirebaseRecyclerAdapter<UstazJB,PostsViewHolder> adapter = new FirebaseRecyclerAdapter<UstazJB, PostsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, final int position, @NonNull UstazJB model)
            {

                final String PostKey = getRef(position).getKey();

                holder.from.setText(model.getName());
                holder.message.setText(model.getKelulusan());


                if (!TextUtils.isEmpty(model.getImage())) {
                    Glide.with(getActivity()).load(model.getImage())
                            .thumbnail(0.5f)
                            .crossFade()
                            .transform(new CircleTransform(getActivity()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgProfile);
                    holder.imgProfile.setColorFilter(null);
                    holder.iconText.setVisibility(View.GONE);
                } else {
                    holder.imgProfile.setImageResource(R.drawable.bg_circle);

                    holder.iconText.setVisibility(View.VISIBLE);
                }

                //Glide.with(MyAgencyPost.this).load(model.getPostImage()).into(holder.productimage);

                holder.vl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),UstazProfile.class);
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
                                if(LikeChecker.equals(true))
                                {
                                    if(dataSnapshot.child(currentUserid).child(PostKey).hasChild(currentUserid))
                                    {
                                        LikesRef.child(currentUserid).child(PostKey).child(currentUserid).removeValue();
                                        LikeChecker = false;
                                        Toast.makeText(getActivity(),"Dislike",Toast.LENGTH_SHORT).show();

                                    }
                                    else {

                                        LikesRef.child(currentUserid).child(PostKey).child(currentUserid).setValue(true);

                                        LikeChecker = false;
                                        Toast.makeText(getActivity(),"Like",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });


                    }
                });



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
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView from, subject, message, iconText;
        LinearLayout vl;
        Integer countLikes;
        String currentUserid;
        DatabaseReference LikesRef,DisLikesRef;
        public ImageView iconImp, imgProfile;


        public PostsViewHolder(View itemView)
        {
            super(itemView);

           from = itemView.findViewById(R.id.from);

            message = (TextView) itemView.findViewById(R.id.txt_secondary);
            iconText = (TextView) itemView.findViewById(R.id.icon_text);

            iconImp = (ImageView) itemView.findViewById(R.id.icon_star);
            imgProfile = (ImageView) itemView.findViewById(R.id.icon_profile);
            vl = (LinearLayout) itemView.findViewById(R.id.message_container);



//            button_likes = (ImageView) itemView.findViewById(R.id.button_likes);
//            button_dislikes = (ImageView) itemView.findViewById(R.id.button_dislikes);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
//            DisLikesRef = FirebaseDatabase.getInstance().getReference().child("DisLikes");
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
