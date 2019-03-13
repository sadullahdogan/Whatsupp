package com.example.sadul.whatsupp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadul.whatsupp.Activity.ResimActivity;
import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.Util.ChangeFragment;
import com.example.sadul.whatsupp.models.mesajModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MesajAdapter extends RecyclerView.Adapter<MesajAdapter.viewHolder> {

    Context context;
    String userName;
    List<mesajModel> list;
    Activity activity;
    boolean state,state2,state3;
   int view_send=1,view_recieved=2,view_imageSend=3,view_imageRec=4;

    public MesajAdapter(Context context, List<mesajModel> list, Activity activity, String userName) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName=userName;
        state=false;
        state2=false;
        state3=false;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view ;
       if(viewType==view_send){
         view= LayoutInflater.from(context).inflate(R.layout.sent_layout,parent,false);
           return new viewHolder(view);
       }
       else if(viewType==view_recieved){
           view = LayoutInflater.from(context).inflate(R.layout.recieved_layout,parent,false);
           return new viewHolder(view);
       }
       else if(viewType==view_imageSend){
           view = LayoutInflater.from(context).inflate(R.layout.send_layout_image,parent,false);
           return  new viewHolder(view);
       }
       else {
           view = LayoutInflater.from(context).inflate(R.layout.recieved_layout_image,parent,false);
           return  new viewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        if(state3) {
            holder.textView.setText(list.get(position).getText().toString());
        }
        else {
            holder.resim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context,ResimActivity.class);
                    intent.putExtra("imageLink",list.get(position).getText());
                    activity.startActivity(intent);

                }
            });
            Picasso.get().load(list.get(position).getText()).into(holder.resim);

        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView resim;


        public viewHolder(View itemView) {
            super(itemView);
            if(state3) {
                if (state) {

                    textView = itemView.findViewById(R.id.sendText);
                } else {

                    textView = itemView.findViewById(R.id.recievedText);
                }
            }
            else{
                if(state2){
                    resim=itemView.findViewById(R.id.sendImage);
                }
                else {
                    resim=itemView.findViewById(R.id.recievedImage);

                }
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getType().equals("resim")){
            state3=false;
            if (list.get(position).getFrom().equals(userName)) {
                state2 = true;
                return view_imageSend;
            } else {
                state2 = false;
                return view_imageRec;
            }
        }
        else {
            state3=true;
            if (list.get(position).getFrom().equals(userName)) {
                state = true;
                return view_send;
            } else {
                state = false;
                return view_recieved;
            }
        }
    }


}
