package com.bu.safeguard;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class viewholder extends RecyclerView.ViewHolder {

    public CardView Mapbtn ;
    public  CircleImageView prodileImageView ;


    View mview ;


    public viewholder(@NonNull View itemView) {
        super(itemView);
        mview = itemView ;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());

            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }

    public  void setView(Context context  ,  String  name  , String number , String  uid  , String useiMage , String time)
    {
        TextView title  = mview.findViewById(R.id.name);
        TextView phone = mview.findViewById(R.id.phone ) ;
        TextView textView = mview.findViewById(R.id.pghone);
        Mapbtn = mview.findViewById(R.id.mapbtn) ;
        prodileImageView = mview.findViewById(R.id.profile_image) ;
        textView.setText("Date: "+ time);
        Picasso.get().load(useiMage).error(R.drawable.user).placeholder(R.drawable.user).into(prodileImageView);



        title.setText(name);
        phone.setText(number);
    }

    private viewholder.ClickListener mClickListener;


    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(viewholder.ClickListener clickListener)
    {
        mClickListener = clickListener;
    }

}
