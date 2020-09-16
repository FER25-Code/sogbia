package com.sci.sogbia.histoire;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sci.sogbia.Adapter.MainAdapterActivity;
import com.sci.sogbia.Adapter.PublicationAdapter;
import com.sci.sogbia.Adapter.Trajet;
import com.sci.sogbia.Porfail.ProfeilActivity;
import com.sci.sogbia.R;

import java.util.ArrayList;

public class HistoireActivity extends AppCompatActivity {
    private ArrayList<Trajet> trajets = new ArrayList<Trajet>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PublicationAdapter adapter;
    Context contest=this;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histoire_activity);
        recyclerView = findViewById(R.id.recyclerViewhistoire);
        mAuth = FirebaseAuth.getInstance();
        adapter = new PublicationAdapter(contest,trajets);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(HistoireActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Histoire();
    }
    public void Histoire() {
        Log.i("Tag", " Start ..... ");
        final FirebaseUser user = mAuth.getCurrentUser();
        final ArrayList<Trajet> trjet = new ArrayList<Trajet>();
        Query query =  FirebaseDatabase.getInstance().getReference().child("Histoire").orderByChild("id_User").equalTo(user.getUid());
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
                Toast.makeText(HistoireActivity.this, "no data ", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
