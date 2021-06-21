package com.bu.safeguard.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bu.safeguard.R;
import com.bu.safeguard.chatOperation.GroupListAdapter;
import com.bu.safeguard.chatOperation.chatPage;
import com.bu.safeguard.chatOperation.selectFriendGroupAdapter;
import com.bu.safeguard.models.friendModel;
import com.bu.safeguard.models.groupFriendModel;
import com.bu.safeguard.models.groupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GroupsFragment extends Fragment implements selectFriendGroupAdapter.ItemClickListener, GroupListAdapter.ItemClickListener {

    View view;
    FirebaseAuth auth;
    String uid;
    RecyclerView recyclerView;

    public GroupsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_group_list, container, false);
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        view.findViewById(R.id.fab)
                .setOnClickListener(v -> CreateAGroup());

        recyclerView = view.findViewById(R.id.groupList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        loadAGroup();

        return view;
    }

    private void loadAGroup() {
        List<groupModel> groupModelList = new ArrayList<>();
        DatabaseReference m = FirebaseDatabase.getInstance().getReference("profile").child(uid)
                .child("groupList");

        m.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot item : snapshot.getChildren()) {

                        groupModel model = item.getValue(groupModel.class);

                        groupModelList.add(model);


                    }
                    recyclerView.setAdapter(new GroupListAdapter(groupModelList, getContext(), GroupsFragment.this));
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void CreateAGroup() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fullscreen_create_dialogue);
        List<groupFriendModel> modelList = new ArrayList<>();
        List<String> userList = new ArrayList<>();
        Button acceptBTN = dialog.findViewById(R.id.create);
        EditText tvTitle = dialog.findViewById(R.id.nameEt);
        RecyclerView freenidLit = dialog.findViewById(R.id.friendList);

        freenidLit.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid).child("friendList");

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot item : snapshot.getChildren()) {

                        friendModel model = item.getValue(friendModel.class);

                        modelList.add(new groupFriendModel(model.getFriendUserID(), false));

                    }

                    freenidLit.setAdapter(new selectFriendGroupAdapter(modelList, getContext(), GroupsFragment.this::onItemClick));
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        acceptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = tvTitle.getText().toString();

                if (groupName.isEmpty()) {
                    Context context;
                    CharSequence text;
                    Toast.makeText(getContext(), "Error : Please Add A Group Name", Toast.LENGTH_SHORT).show();
                } else {

                    int counter = 0;

                    for (groupFriendModel item : modelList) {

                        if (item.isIdSelected()) {
                            counter++;
                            userList.add(item.getUserId());
                        }


                    }


                    if (counter == 0) {
                        Toast.makeText(getContext(), "You Cant OPen A Group WithOut User", Toast.LENGTH_SHORT).show();

                    } else {

                        userList.add(uid);

                        // create a group
                        DatabaseReference mreg = FirebaseDatabase.getInstance().getReference("groupList");
                        String key = mreg.push().getKey();
                        HashMap map = new HashMap<String, Object>();
                        map.put("name", groupName);
                        map.put("id", key);
                        map.put("userList", userList);

                        mreg.child(key).setValue(map);
                        DatabaseReference grop = FirebaseDatabase.getInstance().getReference("profile");
                        for (String item : userList) {

                            grop.child(item).child("groupList")
                                    .child(key)
                                    .child("id")
                                    .setValue(key);

                        }

                        dialog.dismiss();


                    }


                }


            }
        });


        dialog.show();


    }

    @Override
    public void onItemClick(groupFriendModel model) {

    }


    @Override
    public void onItemClick(String model) {

        Intent p = new Intent(getContext(), chatPage.class);
        p.putExtra("group", "yes");
        p.putExtra("groupID", model);
        startActivity(p);
    }
}
