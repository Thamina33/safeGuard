package com.bu.safeguard.chatOperation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bu.safeguard.R;
import com.bu.safeguard.models.GroupListModel;
import com.bu.safeguard.models.groupModel;
import com.bu.safeguard.models.modelForProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*** Created by Rahat Shovo on 6/21/2021 
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.viewholder> {

    private final Context context;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("groupList");
    private List<groupModel> groupFriendModelList = new ArrayList<>();
    private List<GroupListModel> mainGroupList = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public GroupListAdapter(List<groupModel> groupFriendModelList, Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.groupFriendModelList = groupFriendModelList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public @NotNull
    viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_for_chat_display, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        groupModel item = groupFriendModelList.get(position);

        mref.child(item.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    GroupListModel modelForProfile = snapshot.getValue(GroupListModel.class) ;

                    holder.nameTv.setText(modelForProfile.getName());
                    holder.lastMsg.setText(modelForProfile.getUserList().size()+ " Users");


                }

            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item.getId());
        });


    }


    @Override
    public int getItemCount() {
        if (groupFriendModelList == null) {
            return 0;
        }
        return groupFriendModelList.size();
    }

    public interface ItemClickListener {
        void onItemClick(String model);
    }

    class viewholder extends RecyclerView.ViewHolder {

        public TextView nameTv, lastMsg;
        public CircleImageView pp;

        viewholder(@NonNull View mView) {
            super(mView);

            nameTv = (TextView) mView.findViewById(R.id.display_name);
            pp = mView.findViewById(R.id.profile_image);
            lastMsg = (TextView) mView.findViewById(R.id.lastMsg);

        }


    }


}