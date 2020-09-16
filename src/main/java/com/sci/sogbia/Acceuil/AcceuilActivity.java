package com.sci.sogbia.Acceuil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sci.sogbia.MainActivity;
import com.sci.sogbia.R;
import com.sci.sogbia.Signin.SignActivity;

public class AcceuilActivity extends AppCompatActivity {
    private Button mConnexion , mInscription ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        //init views

        mConnexion = findViewById(R.id.connexion);
        mInscription = findViewById(R.id.inscription);
        Boolean Registered;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Registered = sharedPref.getBoolean("Registered", false);
        if (!Registered)
        {
            //startActivity(new Intent(this,SignActivity.class));

        }
        else {
            startActivity(new Intent(this, MainActivity.class));
        }
        //  startService(new Intent(AcceuilActivity.this, onAppKilled.class));

        //handle register button on clik
        mInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start conducteur login activity
                Intent intent = new Intent(AcceuilActivity.this, SignActivity.class);
                startActivity(intent);


            }
        });

        mConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start voyageur login activity
                Intent intent = new Intent(AcceuilActivity.this, ConnexionActivity.class);
                startActivity(intent);




            }
        });


    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
