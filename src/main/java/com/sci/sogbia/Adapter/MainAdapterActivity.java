package com.sci.sogbia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sci.sogbia.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapterActivity extends AppCompatActivity {
    private ArrayList<Trajet> trajets = new ArrayList<Trajet>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PublicationAdapter adapter;
    Context contest=this;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    FirebaseUser  user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Trajet");
        user =mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerViewoneAdapter);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
           if (bundle.getString("bundle1").equals("bundle1")){
                    String Line = bundle.getString("line");
                    Log.e("", "..............." + Line);
                    Trajet trj = new Trajet();
                    trj.setDate(bundle.getString("date"));
                    trj.setDate2(bundle.getString("date2"));
                    trj.setTime(bundle.getString("time"));
                    trj.setTime2(bundle.getString("time2"));
                    trj.setLine(bundle.getString("line"));
                    trj.setLine2(bundle.getString("line2"));
                    trj.setNump(bundle.getString("nump"));
                    trj.setPrix(Integer.parseInt(bundle.getString("prix")));
                    trj.setMarque(bundle.getString("marque"));
                    trajets.add(trj);

                }else {
                    String L1 = bundle.getString("line");
                    String L2 = bundle.getString("line2");
                    Log.e("Line","..."+L2);

                    RechercherTrajet(L1,L2);
        }
        adapter = new PublicationAdapter(contest,trajets);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(MainAdapterActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
//
    public void imgclick(View view){
        Intent listtrajet = new Intent(MainAdapterActivity.this, InfoPublisherActivity.class);
        startActivity(listtrajet);
    }

    public void RechercherTrajet(String L ,String L2){
        final FirebaseUser user =mAuth.getCurrentUser();
        if (user != null) {
            Log.i(".........","user");
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Trajet").child(user.getUid());

        }else {
            Log.i(".........","no user");

        }
//        final Trajet trj = new Trajet();
        final ArrayList<Trajet> trjet = new ArrayList<Trajet>();
       Query query =  FirebaseDatabase.getInstance().getReference().child("Trajet").orderByChild("line_Line2").equalTo(L+"_"+L2);
       Log.e("","start query");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot da:dataSnapshot.getChildren()) {
                   Trajet trj =da.getValue(Trajet.class) ;
                    trjet.add(trj);
                     adapter = new PublicationAdapter(contest, trjet);
                     recyclerView.setAdapter(adapter);

                }

                }


            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                Toast.makeText(MainAdapterActivity.this, "no data ", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
