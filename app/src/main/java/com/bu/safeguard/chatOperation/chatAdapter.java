package com.bu.safeguard.chatOperation;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bu.safeguard.R;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.firebase.auth.FirebaseAuth;
import com.joooonho.SelectableRoundedImageView;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.myHolder> {

    List <chatMsgModel> chatList ;
  private   Context contextt ;
    private  static  final  int MSG_TYPE_RIGHT = 1 ;
    private  static  final  int MSG_TYPE_LEFT = 0 ;
    private  static  final  int IMAGE_MSG_TYPE_RIGHT = 5 ;
    private  static  final  int IMAGE_MSG_TYPE_LEFT = 6 ;
    String uid = FirebaseAuth.getInstance().getUid();


    public  chatAdapter (Context context , List <chatMsgModel> chatList  )
    {
        this.contextt = context ;
        this.chatList =  chatList ;

    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = null  ;

        if(viewType == MSG_TYPE_LEFT)
        {
             view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_left_chat ,  parent, false);
        }
        else if ( viewType == MSG_TYPE_RIGHT)
        {
             view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_right_chat ,  parent, false);
        }

        else if (viewType == IMAGE_MSG_TYPE_LEFT)
        {
            view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_image_left ,  parent, false);
        }
        else if (viewType == IMAGE_MSG_TYPE_RIGHT)
        {
            view  = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_image_right ,  parent, false);
        }






        return  new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final  int position) {

        String msg = chatList.get(position).getMsg() ;
        holder.msgView.setText(msg);

        if(!chatList.get(position).getContent_type().equals("null") && !chatList.get(position).getContent_link().equals("null") )
        {
          // holder.msgView.setText(msg);

          //  holder.setImageView(chatList.get(position).getContent_link() , contextt);

//            holder.imageView.setShapeAppearanceModel(holder.imageView.getShapeAppearanceModel()
//                    .toBuilder()
//                    .setTopLeftCorner(CornerFamily.ROUNDED,15)
//                    .setTopRightCorner(CornerFamily.ROUNDED,15)
//                    .setBottomLeftCorner(CornerFamily.ROUNDED,15)
//                    .build());




          Glide.with(contextt).load(chatList.get(position).getContent_link()).into(holder.imageView) ;
         //  Picasso.get().load(chatList.get(position).getContent_link()).into(holder.imageView) ;


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText( contextt, chatList.get(position).getContent_link()+"", Toast.LENGTH_LONG).show();
                }
            });

        }




    }

    @Override
    public int getItemViewType(int position) {



       if( chatList.get(position).getUid().equals(uid)  && chatList.get(position).getContent_type().equals("null"))
       {

           return MSG_TYPE_RIGHT ;

       }
       else if( chatList.get(position).getUid().equals(uid)  && !chatList.get(position).getContent_type().equals("null"))
        {

            return IMAGE_MSG_TYPE_RIGHT ;

        }

       else if(!chatList.get(position).getUid().equals(uid) && chatList.get(position).getContent_type().equals("null"))
       {
           return  MSG_TYPE_LEFT ;
       }

       else
      {
               return  IMAGE_MSG_TYPE_LEFT ;
       }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }





    class myHolder extends RecyclerView.ViewHolder
    {

      public  TextView msgView , date  ; SelectableRoundedImageView imageView ;








        public myHolder(@NonNull View itemView) {
            super(itemView);


            msgView = itemView.findViewById(R.id.msgTv);
            date = itemView.findViewById(R.id.dateview) ;
            imageView = itemView.findViewById(R.id.image) ;







        }

        private  void setImageView( String url , Context context)
        {
            imageView = itemView.findViewById(R.id.image) ;
            Glide.with(context).load(url).into(imageView) ;
        }



    }



}





