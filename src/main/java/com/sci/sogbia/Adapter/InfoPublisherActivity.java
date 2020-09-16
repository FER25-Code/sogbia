package com.sci.sogbia.Adapter;

import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sci.sogbia.Chat.ChatActivity;
import com.sci.sogbia.MainActivity;
import com.sci.sogbia.R;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class InfoPublisherActivity extends AppCompatActivity {
    CircularImageView imgInfo ;
    TextView name , tel , marque, note ;
    DatabaseReference mDatabaseReference;

    public String getNumP() {
        return numP;
    }

    public void setNumP(String numP) {
        this.numP = numP;
    }

    FirebaseAuth mAuth;
    String numP,nameprofil,uriImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_publisher_activity);
        name   = findViewById(R.id.nameInfo);
        tel= findViewById(R.id.telInfo);
        marque = findViewById(R.id.MInfo);
        imgInfo = findViewById(R.id.imageInfo);
        note = findViewById(R.id.idnote);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        String marques = bundle.getString("marque");
        String tels = bundle.getString("tel");
        String nameS = bundle.getString("name");
        String uri = bundle.getString("imagID");
        String notes = bundle.getString("note");
        Uri iu= Uri.parse(uri);
        Picasso.get().load(iu).into(imgInfo);
        name.setText(nameS);
        tel.setText(tels);
        note.setText(notes);
        marque.setText(marques);
        mAuth = FirebaseAuth.getInstance();
        nameprofil =nameS;
        uriImg =uri;
        Log.e("Tag","oncrate"+uri);
        Log.e("Tag","oncrate"+uriImg);
    }

    public void chatOnline(View view) {
        Toast.makeText(InfoPublisherActivity.this, "Ok Chat ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(InfoPublisherActivity.this, ChatActivity.class);
        intent.putExtra("Name", nameprofil);
        intent.putExtra("URI", uriImg);
        Log.e("Tag","chatonline "+uriImg);
        startActivity(intent);
    }

    public void Conf(View view) {
        NumPlace();
        Toast.makeText(InfoPublisherActivity.this, "Ok Confo  ", Toast.LENGTH_LONG).show();
        Log.i("Tag", " Start ..... ");

    }

     public void NumPlace(){
         FirebaseUser user = mAuth.getCurrentUser();
         SharedPreferences sp = getSharedPreferences("your_prefs", InfoPublisherActivity.MODE_PRIVATE);
         final SharedPreferences.Editor editor = sp.edit();
         mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Trajet").child(user.getUid());
         mDatabaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                     String nump = dataSnapshot.child("nump").getValue().toString();
                     Log.e("",""+nump);
                     editor.putString("your_int_key", nump);
                     editor.commit();

                 } else {


                 }

             }

             @Override
             public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

             }

         });
         ModNumPlace ();

     }
      public void ModNumPlace (){
          SharedPreferences sp = getSharedPreferences("your_prefs", InfoPublisherActivity.MODE_PRIVATE);
         String myIntValue = sp.getString("your_int_key", "");

          Log.e("","..........ccc........."+ myIntValue);
          switch(myIntValue) {
              case "3":
                  // code block
                  mDatabaseReference.child("nump").setValue("2" );
                  Intent ir = new Intent(InfoPublisherActivity.this, MainActivity.class);
                  startActivity(ir);
                  break;
              case "2":
                  mDatabaseReference.child("nump").setValue("1" );
                  Intent ie = new Intent(InfoPublisherActivity.this, MainActivity.class);
                  startActivity(ie);
                  // code block
                  break;
              case "1":
                  mDatabaseReference.child("nump").setValue("0" );
                  FirebaseUser user = mAuth.getCurrentUser();
                  mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Trajet").child(user.getUid());
                  mDatabaseReference.removeValue();
                  Intent iv = new Intent(InfoPublisherActivity.this, MainActivity.class);
                  startActivity(iv);
                  // code block
                  break;
              default:
                  // code block
          }

      }
}
