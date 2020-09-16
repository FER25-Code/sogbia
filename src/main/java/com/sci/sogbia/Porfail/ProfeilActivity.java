package com.sci.sogbia.Porfail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sci.sogbia.Acceuil.AcceuilActivity;
import com.sci.sogbia.R;
import com.sci.sogbia.SendEmail.SendEmailActivity;
import com.sci.sogbia.histoire.HistoireActivity;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;

public class ProfeilActivity extends AppCompatActivity {
    TextView nameP, emailp, telp, type, note;
    CircularImageView img;
    private String userID;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profeil_activtiy);
        nameP = findViewById(R.id.nameprof);
        emailp = findViewById(R.id.idemail);
        img = findViewById(R.id.imgProf);
        telp = findViewById(R.id.tel_num);
        type = findViewById(R.id.typevc);
        note = findViewById(R.id.idnote);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        emailp.setText(user.getEmail());
        mail =user.getEmail() ;
        userID = user.getUid();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Image");
        showProfil();
    }

    public void edit_profile(View view) {
        Intent Editprofail = new Intent(ProfeilActivity.this, EditProfail.class);
        startActivity(Editprofail);
    }

    public void signout(View view) {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        Intent Acceuil = new Intent(ProfeilActivity.this, AcceuilActivity.class);
        startActivity(Acceuil);
        // [END auth_sign_out]
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ProfeilActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Registered", false);
        editor.apply();
    }

    public void showProfil() {
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail").child(user.getUid());
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    String nameprofil = dataSnapshot.child("nameprofil").getValue().toString();
                    String numtel = dataSnapshot.child("numtel").getValue().toString();
                    String typtV = dataSnapshot.child("typtV").getValue().toString();
                    String imagID = dataSnapshot.child("imagID").getValue().toString();
                    String notep = dataSnapshot.child("note").getValue().toString();
                    nameP.setText(nameprofil);
                    telp.setText(numtel);
                    type.setText(typtV);
                    note.setText(notep + " Point");
                    Log.e("Tag", " Commit ...... " + imagID);
                    Log.e("Tag", " Commit ...................... " + nameprofil);
                    Log.e("Tag", " Commit ...................... " + numtel);
                    Uri i = Uri.parse(imagID);
                    Picasso.get().load(i).into(img);
                    //    Toast.makeText(ProfeilActivity.this, "C"+gender+"..."+nameprofil, Toast.LENGTH_LONG).show();


                    //     Toast.makeText(ProfeilActivity.this, "no data ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

            }
        });


    }

    public void Histoire(View view) {
        Intent histoire = new Intent(ProfeilActivity.this, HistoireActivity.class);
        startActivity(histoire);

        }

    public void SendEmail(View view) {

        Intent send = new Intent(ProfeilActivity.this, SendEmailActivity.class);  //when click on item pass to next ativity
        Bundle bundle = new Bundle();
        bundle.putString("email",mail );
        send.putExtras(bundle);
        ProfeilActivity.this.startActivity(send);

    }
}