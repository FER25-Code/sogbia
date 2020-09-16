package com.sci.sogbia.Rechercher;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.sci.sogbia.Adapter.MainAdapterActivity;
import com.sci.sogbia.R;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RechercherActivity extends AppCompatActivity {
    private View parent_view;
    private static final int REQUEST_CODE_ORIGIN = 500;
    private static final int REQUEST_CODE_DEST = 600;

    String Line ,Line2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rechercher_activity);
        parent_view = findViewById(android.R.id.content);
        initComponent();
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
    }



    private void initComponent() {
        ( findViewById(R.id.tv_origin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(REQUEST_CODE_ORIGIN);
            }
        });

        ((TextView) findViewById(R.id.tv_destination)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(REQUEST_CODE_DEST);
            }
        });
    }

    private void openAutocompleteActivity(int request_code) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
        startActivityForResult(intent, request_code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORIGIN) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                ((TextView) findViewById(R.id.tv_origin)).setText(place.getName());
                Line=place.getName();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE_DEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                ((TextView) findViewById(R.id.tv_destination)).setText(place.getName());
                Line2=place.getName();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
              Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void listProfil (View view){

        if(Line==null||Line2==null){
            Toast.makeText(getApplicationContext(), " no  Line ", Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(RechercherActivity.this, MainAdapterActivity.class);  //when click on item pass to next ativity
            Bundle bundle = new Bundle();
            bundle.putString("bundle1", "bundle2");
            bundle.putString("line", Line); //get and past informations with bundle from main activity to next activity
            bundle.putString("line2",Line2 );
            i.putExtras(bundle);
            RechercherActivity.this.startActivity(i);
        }



    }




  }






