package com.sci.sogbia.Adapter;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sci.sogbia.Chat.Profail;
import com.sci.sogbia.Porfail.ProfeilActivity;
import com.sci.sogbia.R;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.ViewHolder>implements Filterable { //creat adapter for setting data
    ArrayList<Trajet> android,filterList;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    Context context;
    public PublicationAdapter(Context context, ArrayList<Trajet> android) {//constrector
        this.context=context;
        this.android = android;
        filterList=android;

    }

    @Override
    public PublicationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PublicationAdapter.ViewHolder viewHolder, int i) {//set view and text to card view
        final Trajet trajet = android.get(i);
        viewHolder.Line.setText(trajet.getLine());
        viewHolder.Line2.setText(trajet.getLine2());
        viewHolder.time.setText(trajet.getTime());
        viewHolder.time2.setText(trajet.getTime2());
        viewHolder.date.setText(trajet.getDate());
        viewHolder.date2.setText(trajet.getDate2());
        viewHolder.nump2.setText(String.valueOf(trajet.getNump()));
        viewHolder.prix.setText(String.valueOf(trajet.getPrix()+"Dz"));
      //  viewHolder.nameprofil.setText(trajet.getDate());
       // Uri uri= Uri.parse(trajet.getIdimg());
    //    Picasso.get().load(uri).into( viewHolder.imageView);
        ;
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                FirebaseUser user =mAuth.getCurrentUser();
                Query query =  FirebaseDatabase.getInstance().getReference().child("Trajet").orderByChild("id_User").equalTo(trajet.getId_User());
Log.e("Tag","Id user "+trajet.getId_User());
                final Profail profail = new Profail();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail").child(trajet.getId_User());



                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Log.e("Tag","Id user astrt");
                String nameprofil = dataSnapshot.child("nameprofil").getValue().toString();
                String numtel = dataSnapshot.child("numtel").getValue().toString();
                String typtV = dataSnapshot.child("typtV").getValue().toString();
                String imagID = dataSnapshot.child("imagID").getValue().toString();
                String note = dataSnapshot.child("note").getValue().toString();
                Log.i("Tag"," Commit ...... "+typtV);
                Log.e("Tag"," Commit ...................... "+nameprofil);
                Log.e("Tag"," Commit ...................... "+numtel);
                profail.setNumtel(numtel);
                profail.setNameprofil(nameprofil);
                profail.setTyptV(typtV);
                profail.setImagID(imagID);
                profail.setNote(note);

                Log.e("","onclick item start ");
                Intent i = new Intent(context, InfoPublisherActivity.class);  //when click on item pass to next ativity
                Bundle bundle = new Bundle();    //get and past informations with bundle from main activity to next activity
                bundle.putString("marque", trajet.getMarque());
                bundle.putString("tel", profail.getNumtel());
                bundle.putString("name", profail.getNameprofil());
                bundle.putString("typev", profail.getTyptV());
                bundle.putString("imagID",profail.getImagID());
                bundle.putString("note",profail.getNote());

                i.putExtras(bundle);
                context.startActivity(i);

                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                    }
                });
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot da:dataSnapshot.getChildren()) {

                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return android.size();
    }


    @Override
    public android.widget.Filter getFilter() {
  return null;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // define components of item
        private TextView Line ,Line2 ,time,time2,date,date2,nump2,prix;
        ItemClickListener itemClickListener;

        public ViewHolder(View view) {
            super(view);
            Line =  itemView.findViewById(R.id.line1);
            Line2 =  itemView.findViewById(R.id.line2);
            time=itemView.findViewById(R.id.time1);
            time2=itemView.findViewById(R.id.time2);
            date=itemView.findViewById(R.id.date1);
            date2=itemView.findViewById(R.id.date2);
            nump2=itemView.findViewById(R.id.nubp2);
            prix=itemView.findViewById(R.id.prix);
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
