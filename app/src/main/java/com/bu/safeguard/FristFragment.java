package com.bu.safeguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FristFragment extends Fragment {
    public FristFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View  view = inflater.inflate(R.layout.first_intro, container, false);

        TextView title , description ;
        ImageView image ;
        title = view.findViewById(R.id.title) ;
        description = view.findViewById(R.id.tt) ;
        image= view.findViewById(R.id.imageView) ;

        description.setText("বাংলাদেশের নাগরিকদের\n" +
                "নিরাপত্তা নিশ্চিত করতে \n" +
                "জনগণের নিরাপত্তা প্রহরী\n‘সেইফগার্ড’");
        return view;





    }
}
