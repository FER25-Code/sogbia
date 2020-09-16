package com.sci.sogbia.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sci.sogbia.Adapter.ItemClickListener;
import com.sci.sogbia.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder>implements Filterable { //creat adapter for setting data
    ArrayList<Profail> android,filterList;

    Context context;
    public ChatAdapter(Context context,ArrayList<Profail> android) {//constrector
        this.context=context;
        this.android = android;
        filterList=android;

    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder viewHolder, int i) {//set view and text to card view
        final Profail trajet = android.get(i);
        viewHolder.nameprofil.setText(trajet.getNameprofil());
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.e("","onclick item start ");
                Intent i = new Intent(context, ChatActivity.class);  //when click on item pass to next ativity
                Bundle bundle = new Bundle();    //get and past informations with bundle from main activity to next activity


                //bundle.putSerializable("obj", (Serializable) data);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // define components of item
        private TextView nameprofil;
        ItemClickListener itemClickListener;
        ImageView imageView ;
        public ViewHolder(View view) {
            super(view);

            nameprofil=itemView.findViewById(R.id.nameporfilchat);
            imageView = itemView.findViewById(R.id.idimgchat);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.e("","onclick item start ");
            this.itemClickListener.onItemClick(v,getLayoutPosition());

        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }}


}
