package com.bu.safeguard.chatOperation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bu.safeguard.R;
import com.bu.safeguard.models.groupFriendModel;
import com.bu.safeguard.models.modelForProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*** Created by Rahat Shovo on 6/21/2021 
 */
public class selectFriendGroupAdapter extends RecyclerView.Adapter<selectFriendGroupAdapter.viewholder> {

    private final Context context;
    private List<groupFriendModel> groupFriendModelList;
    private ItemClickListener itemClickListener;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile");
    public selectFriendGroupAdapter( List<groupFriendModel> groupFriendModelList, Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.groupFriendModelList = groupFriendModelList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public @NotNull
    viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_create_group_row, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        groupFriendModel item = groupFriendModelList.get(position);

        mref.child(item.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    modelForProfile modelForProfile = snapshot.getValue(modelForProfile.class) ;

                    holder.textView.setText(modelForProfile.getNickName());
                    Glide.with(context)
                            .load(modelForProfile.getPpLink())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(holder.circleImageView) ;


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                groupFriendModelList.get(position).setIdSelected(true);
            } else {
                groupFriendModelList.get(position).setIdSelected(false);
            }

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
        void onItemClick(groupFriendModel model);
    }

    class viewholder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView textView;
        CheckBox checkBox;

        viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            checkBox = itemView.findViewById(R.id.isDone);
            circleImageView = itemView.findViewById(R.id.image);


        }


    }


}