package com.sci.sogbia.Porfail;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sci.sogbia.Chat.Profail;
import com.sci.sogbia.R;
import java.io.FileNotFoundException;
import java.io.IOException;
import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfail extends AppCompatActivity {
    EditText M_user,M_tel;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    CircularImageView imgModf;
    public Uri imguri ;
    StorageTask uploadTask;
    private StorageReference mStorageRef;
    Profail profail ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profail_activity);
        M_tel=findViewById(R.id.idtelnumM);
        M_user=findViewById(R.id.iduserM);
        imgModf=findViewById(R.id.imgProfMof);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Image");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail");
        profail = new Profail();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        FirebaseUser user      =mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail").child(user.getUid());
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_femaleM:
                if (checked)
                    // Pirates are the best
                mDatabaseReference.child("gender").setValue("Female");
                break;
            case R.id.radio_maleM:
                if (checked)
                mDatabaseReference.child("gender").setValue("Male");
                break;
        }
    }

    public void onRadioButton(View view) {
        // Is the button now checked?
        FirebaseUser   user  =mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail").child(user.getUid());
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.cM:
                if (checked)
                    mDatabaseReference.child("typtV").setValue("V");

                break;
            case R.id.vM:
                if (checked)
                    mDatabaseReference.child("typtV").setValue("C");
                break;
        }
    }


    public void Modification(View view) {
        FirebaseUser   user  =mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Profail").child(user.getUid());
        int X=0;
        if (M_tel.getText().toString().equals("")){
            Log.e("Tag","No Edit");
            Toast.makeText(EditProfail.this, " pas de Modification", Toast.LENGTH_LONG).show();

        }else {
            mDatabaseReference.child("numtel").setValue(M_tel.getText().toString());
            X=1;

        }
        if (M_user.getText().toString().equals("")){
            Log.e("Tag","No Edit");
            Toast.makeText(EditProfail.this, " pas de Modification", Toast.LENGTH_LONG).show();
        }else {
            mDatabaseReference.child("nameprofil").setValue(M_user.getText().toString());
            X=1;
        }
        if (X == 1){
            Intent i = new Intent(EditProfail.this, ProfeilActivity.class);
            startActivity(i);
        }

    }

    public void upphotoModf(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            imguri = data.getData();
            Bitmap bitmap = null;
            try {
                Toast.makeText(EditProfail.this, "..."+imguri, Toast.LENGTH_SHORT).show();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imguri);
                imgModf.setImageBitmap(bitmap);


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
        Fileuploader();
    }

    public String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    public void Fileuploader(){
        final FirebaseUser   user  =mAuth.getCurrentUser();
        final StorageReference riversRef = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        Log.e("Tag",""+System.currentTimeMillis()+"."+getExtension(imguri));

        uploadTask= riversRef.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(EditProfail.this, "Up success", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        Log.d("Tag", "onSuccess:.........." );
        riversRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Tag", "onSuccess: uri= "+uri.toString() );
                        //profail.setImagID(uri.toString());
                        mDatabaseReference.child(user.getUid()).child("imagID").setValue(uri.toString());
                    }
                });
            }
        });
    }

}
