package com.example.zaimzamrii.psmmasjid.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zaimzamrii.psmmasjid.R;
import com.example.zaimzamrii.psmmasjid.helper.CircleTransform;
import com.example.zaimzamrii.psmmasjid.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Message> messages;
    private MessageAdapterListener listener;
    private SparseBooleanArray selectedItems;
    private List<String> listFav = new ArrayList();

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    //firebase
    private DatabaseReference dbuser;
    private String userID;
    private FirebaseAuth mAuth;

    String array;


    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView from, subject, message, iconText, timestamp;
        public ImageView iconImp, imgProfile;
        public LinearLayout messageContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.from);

            message = (TextView) view.findViewById(R.id.txt_secondary);
            iconText = (TextView) view.findViewById(R.id.icon_text);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconImp = (ImageView) view.findViewById(R.id.icon_star);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);

        }

    }


    public MessagesAdapter(Context mContext, List<Message> messages, MessageAdapterListener listener) {
        this.mContext = mContext;
        this.messages = messages;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Message message = messages.get(position);

        // displaying text view data

        //check exist fav
        holder.from.setText(message.getFrom());
        holder.message.setText(message.getMessage());

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        dbuser = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        /*dbuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.e("orang",dataSnapshot.child("profiledescription").getValue().toString());
                 String array = dataSnapshot.child("profiledescription").getValue().toString();


//                jadiImportant(holder,message,array);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        Log.e("orangSie",String.valueOf(listFav.size()));





        // displaying the first letter of From in icon text
        holder.iconText.setText(message.getFrom().substring(0, 1));

        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        // change the font style depending on message read status
        applyReadStatus(holder, message);

        // handle message star
        applyImportant(holder, message);



        // display profile image
        applyProfilePicture(holder, message);

        // apply click events
        applyClickEvents(holder, position);


    }

    private void applyClickEvents(MyViewHolder holder, final int position) {


        holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });

        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });


    }



    private void applyProfilePicture(MyViewHolder holder, Message message) {
        if (!TextUtils.isEmpty(message.getPicture())) {
            Glide.with(mContext).load(message.getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(message.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }

    private void jadiImportant(MyViewHolder holder,Message message,String array){


            //try untuk default yellow

                if (array.contains(message.getKey())){
                    Log.e("orange","masuk");
                    message.setImportant(true);
                    applyImportant(holder,message);
                }




    }




    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0


    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    private void applyImportant(MyViewHolder holder, Message message) {
        if (message.isImportant()) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));


            if (!listFav.contains(message.getKey())){
                //tiada dalam list

                listFav.add(message.getKey());
                Log.e("orang1",String.valueOf(listFav.size()));
            }else{
                //dah ada dalamm list fav

            }


        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));

            if (listFav.contains(message.getKey())){
                listFav.remove(message.getKey());
                Log.e("orangElse",String.valueOf(listFav.size()));
            }

        }



        DatabaseReference updateData = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        updateData.child("profiledescription").setValue(listFav);
    }

    private void applyReadStatus(MyViewHolder holder, Message message) {
        if (message.isRead()) {
            holder.from.setTypeface(null, Typeface.NORMAL);
            holder.subject.setTypeface(null, Typeface.NORMAL);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
        } else {
            holder.from.setTypeface(null, Typeface.BOLD);
            holder.subject.setTypeface(null, Typeface.BOLD);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }





    public interface MessageAdapterListener {


        void onIconImportantClicked(int position);
        void onMessageRowClicked(int position);




    }


}
