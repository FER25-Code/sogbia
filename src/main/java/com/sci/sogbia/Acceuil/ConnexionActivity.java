package com.sci.sogbia.Acceuil;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sci.sogbia.MainActivity;
import com.sci.sogbia.R;

public class ConnexionActivity extends AppCompatActivity {
    private EditText mEmail, mMotdepasse;
    private Button mLogin ;


    //progressbar to display while registering user
    ProgressDialog progressDialog ;
    ProgressDialog pd;


    // declare an instance of FirebaseAuth
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    // declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inscription ");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //init views
        mEmail =  findViewById(R.id.mail);
        mMotdepasse =  findViewById(R.id.password);
        mLogin = findViewById(R.id.connexion);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(" inscription en cours ...");

        pd = new ProgressDialog(this);
        pd.setMessage("Connexion en cours ...");



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inpute data
                String email = mEmail.getText().toString();
                String password = mMotdepasse.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email Invalide");
                    mEmail.setFocusable(true);

                } else {
                    loginUser(email, password);
                }

            }
        });
    }

    private void loginUser(String email, String password) {
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()){
                            pd.dismiss();
                            //sign in success , update
                            FirebaseUser user = mAuth.getCurrentUser();
                            //user is logged in so start login activity
                            startActivity(new Intent(ConnexionActivity.this , MainActivity.class));
                            finish();
                        }else {
                            pd.dismiss();
                            // if sign in fails , display a message to the user.
                            Toast.makeText(ConnexionActivity.this , "Authentification échoué" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ConnexionActivity.this , ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}