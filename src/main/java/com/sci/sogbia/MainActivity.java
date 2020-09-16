package com.sci.sogbia;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sci.sogbia.Acceuil.AcceuilActivity;
import com.sci.sogbia.Adapter.InfoPublisherActivity;
import com.sci.sogbia.Chat.ChatListActivity;
import com.sci.sogbia.Evaluation.ScannerActivity;
import com.sci.sogbia.Porfail.ProfeilActivity;
import com.sci.sogbia.ProposerTrajet.ProposertrajetActivity;
import com.sci.sogbia.Rechercher.RechercherActivity;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mAuth = FirebaseAuth.getInstance();
        initComponent();
    }

    private void initComponent() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
//                        Intent intentc = new Intent(MainAdapterActivity.this, BusActivity.class);
//                        startActivity(intentc);
                        break;
                    case R.id.search:
                        Intent search = new Intent(MainActivity.this, RechercherActivity.class);
                        startActivity(search);

                        break;
                    case R.id.add:
                        Intent sing = new Intent(MainActivity.this, ProposertrajetActivity.class);
                        startActivity(sing);
                        break;
                    case R.id.profil:
                        Intent profail = new Intent(MainActivity.this, ProfeilActivity.class);
                        startActivity(profail);
                        break;
                    case R.id.chat:
//                        Intent intentchat = new Intent(MainActivity.this, ScannerActivity.class);
//                        startActivity(intentchat);
                        new IntentIntegrator(MainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();

                }
                return false;
            }
        });
    }

    public void Proposertrajet(View view) {
        Intent Proposertrajet = new Intent(MainActivity.this, ProposertrajetActivity.class);
        startActivity(Proposertrajet);

    }

    @Override
    public void onBackPressed() {
        //   finish();

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            finish();
            Toast.makeText(getBaseContext(), "Click two times to close an activity", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }


    public void rechercher(View view) {
        Intent rechercher = new Intent(MainActivity.this, RechercherActivity.class);
        startActivity(rechercher);

    }

    //Qr code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //show dialogue with result
                Toast.makeText(this, "Scan start", Toast.LENGTH_LONG).show();
                Log.e("",""+result.getContents());
                Evaluation(result.getContents());
                // Intent intent = new Intent(MainActivity.this, BusActivity.class);
                //  startActivity(intent);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //Qr code
    //method to construct dialogue with scan results
    public void Evaluation(final String result) {
        Log.e("","start result ;;;;"+result);
        final String[] s = new String[1];
        Query query =  FirebaseDatabase.getInstance().getReference().child("Profail").orderByChild("id_profail").equalTo(result);
        Log.e("","start query");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String  note = snapshot.child("note").getValue().toString();
                        Log.e("tag", "ondatachange" + note);
                        final SharedPreferences sp = getSharedPreferences("Evaluation", MainActivity.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sp.edit();
                        editor.putString("your_int_key", "0");
                        editor.apply();
                        String myIntValue = sp.getString("your_int_key", "0");
                        Log.e("","........."+myIntValue);
                        editor.putString("your_int_key", note);
                        editor.apply();
                    }
                    SharedPreferences sp = getSharedPreferences("Evaluation", InfoPublisherActivity.MODE_PRIVATE);
                    String myIntValue = sp.getString("your_int_key", "0");
                AddNote(result,myIntValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void AddNote(String result, String note) {
        Log.e("", "................." + result);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail").child(result);
        int noteNew = Integer.parseInt(note)+5;
        String noteAdd = String.valueOf(noteNew);
        Log.e("", "..................noteNew :" + noteNew);
        Log.e("tag", "...........noteAdd : " + noteAdd);
        mDatabaseReference.child("note").setValue(noteAdd);
        Dialog ();

    }
     public void Dialog (){
         AlertDialog.Builder builder= new AlertDialog.Builder(this);
         builder.setTitle("Evaluation ");
         builder.setMessage("vous lui donnez 5 points");
         AlertDialog dialog= builder.create();
         dialog.show();
     }
    private void checkUserStatus (){
        // get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            // user exist stay here
        }else {
            //user not signe in go to main activity !! doit retourner pour choisir login ou registration
            startActivity(new Intent(MainActivity.this , AcceuilActivity.class));
            finish();

        }
    }

    @Override
    protected void onStart() {

        // check on Start of app
        checkUserStatus();

        super.onStart();
    }
}
