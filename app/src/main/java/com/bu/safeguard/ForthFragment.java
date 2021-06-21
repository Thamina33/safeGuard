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

public class ForthFragment extends Fragment {


    public ForthFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.first_intro, container, false);
        TextView  title , description ;
        ImageView image ;
        title = view.findViewById(R.id.title) ;
        description = view.findViewById(R.id.tt) ;
        image= view.findViewById(R.id.imageView) ;

        description.setText("ভবিষ্যৎ সন্তানের জন্য মানবিক, আদর্শ সমাজ গঠনে " +
                " ‘সেইফগার্ড’ \n আপনার সাথে যুক্ত হয়ে কাজ করবে");
        return view;


    }
}
