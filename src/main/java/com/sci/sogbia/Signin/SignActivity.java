package com.sci.sogbia.Signin;
import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sci.sogbia.Chat.Profail;
import com.sci.sogbia.MainActivity;
import com.sci.sogbia.R;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SignActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail, editTextPassword, editTextPasswordconf, username, tel;
    FirebaseAuth mAuth;
    Profail profail;
    Uri imguri;
    CircularImageView imageView;
    StorageTask uploadTask;
    DatabaseReference mDatabaseReference;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail");
        editTextEmail = findViewById(R.id.idemaildsing);
        editTextPassword = findViewById(R.id.idpasswordsing);
        editTextPasswordconf = findViewById(R.id.pconf);
        username = findViewById(R.id.iduser);
        tel = findViewById(R.id.idtelnum);
        imageView = findViewById(R.id.imgProfs);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        profail = new Profail();

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passworconf = editTextPasswordconf.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        if (!password.equals(passworconf)) {
            editTextPassword.setError("passord Incor");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    saveUserInformation();
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SignActivity.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("Registered", true);
                    editor.apply();
                     startActivity(new Intent(SignActivity.this, MainActivity.class));


                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:

                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(SignActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    Fileuploader();

//                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
//                    startActivity(intent);
                }
                break;


        }


    }

    public void upphoto(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
    }

    private void saveUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        profail.setNameprofil(username.getText().toString().trim());
        profail.setNumtel(tel.getText().toString().trim());
        profail.setNote("0");
        profail.setId_profail(user.getUid());
        mDatabaseReference.child(user.getUid()).setValue(profail);
        Toast.makeText(getApplicationContext(), "save info in data", Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_female:
                if (checked)
                    // Pirates are the best
                    profail.setGender("Female".trim());
                break;
            case R.id.radio_male:
                if (checked)
                    profail.setGender("Male".trim());
                break;
        }
    }

    public void onRadioButton(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.c:
                if (checked)

                    profail.setTyptV("C".trim());
                Toast.makeText(SignActivity.this, "Conducteur", Toast.LENGTH_SHORT).show();

                break;
            case R.id.v:
                if (checked)
                    Toast.makeText(SignActivity.this, "Voyageur", Toast.LENGTH_SHORT).show();
                profail.setTyptV("V".trim());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            imguri = data.getData();
            Bitmap bitmap = null;
            try {
                Toast.makeText(SignActivity.this, "..."+imguri, Toast.LENGTH_SHORT).show();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imguri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                Log.e("gg","Exp1");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("gg","Exp2");
                e.printStackTrace();
            }
        }
    }

    public String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    public void Fileuploader(){
        final StorageReference riversRef = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        Log.e("Tag",""+System.currentTimeMillis()+"."+getExtension(imguri));
        uploadTask= riversRef.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(SignActivity.this, "Up success", Toast.LENGTH_SHORT).show();
                    registerUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        riversRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Tag", "onSuccess: uri= "+uri.toString() );
                        profail.setImagID(uri.toString());
                    }
                });
            }
        });
    }



    //android:noHistory="true"




}
