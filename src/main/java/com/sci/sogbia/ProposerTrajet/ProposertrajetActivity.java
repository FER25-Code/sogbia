package com.sci.sogbia.ProposerTrajet;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sci.sogbia.Adapter.MainAdapterActivity;
import com.sci.sogbia.Adapter.Trajet;
import com.sci.sogbia.Chat.Tools;
import com.sci.sogbia.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Arrays;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
public class ProposertrajetActivity extends AppCompatActivity {
    private View parent_view;
    private static final int REQUEST_CODE_ORIGIN = 500;
    private static final int REQUEST_CODE_DEST = 600;
    EditText Editeprix,EditeNumP,EditMarque;
    Button timeS,timeE,dateS,dataE;
    String  T1,T2 ,DataS,DataE ,LS ,LE;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference,mDatabaseReferenceH;
    FirebaseUser  user;
    Trajet trajet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposertrajet_activity);
        parent_view = findViewById(android.R.id.content);
        initComponent();
        EditMarque =findViewById(R.id.marque);
        Editeprix =findViewById(R.id.prixe);
        EditeNumP =findViewById(R.id.numplace);
        timeS =findViewById(R.id.bt_pickTimeS);
        timeE =findViewById(R.id.bt_pickTimeE);
        dataE =findViewById(R.id.bt_pickdataE);
        dateS =findViewById(R.id.bt_pickdataS);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        initComponentTime();
        initComponentDate();
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Trajet");
        mDatabaseReferenceH= FirebaseDatabase.getInstance().getReference().child("Histoire");
        user =mAuth.getCurrentUser();
         trajet = new Trajet();
    }
    private void initComponent() {
        (findViewById(R.id.tv_originP)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(REQUEST_CODE_ORIGIN);
            }
        });

        (findViewById(R.id.tv_destinationP)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(REQUEST_CODE_DEST);
            }
        });
    }
    private void initComponentTime() {
        timeS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTimeSPickerLight((Button)  v);
            }
        });

        timeE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTimeEPickerLight((Button)v);
            }
        });
    }
    private void initComponentDate() {
        dataE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDateEPickerLight((Button)  v);
            }
        });

        dateS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDateSPickerLight((Button)  v);
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
              Place placeFromIntent = Autocomplete.getPlaceFromIntent(data);
                ((TextView) findViewById(R.id.tv_originP)).setText(placeFromIntent.getName());
                LS=placeFromIntent.getName();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE_DEST) {
            if (resultCode == RESULT_OK) {
                Place  placeFromIntent = Autocomplete.getPlaceFromIntent(data);
                ((TextView) findViewById(R.id.tv_destinationP)).setText(placeFromIntent.getName());
                LE=placeFromIntent.getName();
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
    private void dialogDateSPickerLight(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                   //     calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                   //   ((TextView) findViewById(R.id.result)).setText(Tools.getFormattedDateSimple(date_ship_millis));
                        DataS=String.valueOf(Tools.getFormattedDateSimple(date_ship_millis));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }
    private void dialogDateEPickerLight(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                    //   calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        //   ((TextView) findViewById(R.id.result)).setText(Tools.getFormattedDateSimple(date_ship_millis));
                       DataE=String.valueOf(Tools.getFormattedDateSimple(date_ship_millis));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }
    private void dialogTimeEPickerLight(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        T2=String.valueOf(hourOfDay + " : " + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }
    private void dialogTimeSPickerLight(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                T1=String.valueOf(hourOfDay + " : " + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }
    private void saveInformationTrajet() {

        trajet.setLine(LS.trim());
        trajet.setLine2(LE.trim());
        trajet.setTime(T1.trim());
        trajet.setTime2(T2.trim());
        trajet.setDate(DataE.trim());
        trajet.setDate2(DataS.trim());
        trajet.setNump(EditeNumP.getText().toString());
        trajet.setPrix(Integer.valueOf(String.valueOf(Editeprix.getText())));
        trajet.setMarque(EditMarque.getText().toString());
        trajet.setLine_Line2(LS.trim()+"_"+LE.trim());
        trajet.setId_User(user.getUid());
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabaseReference.child(user.getUid()).setValue(trajet);
        mDatabaseReferenceH.push().setValue(trajet);
        Toast.makeText(getApplicationContext(), "save info in data", Toast.LENGTH_SHORT).show();
        showInfoTrajet();

    }
    public void Publier(View view) {
      if(LS==null||LE==null||T1==null||T2==null||DataS==null||DataE==null||Editeprix.equals("")||EditeNumP.equals("")||EditMarque.equals("")){
    Toast.makeText(getApplicationContext(), "remplir tout les chime ", Toast.LENGTH_SHORT).show();
      }else {
    saveInformationTrajet();
      }
    }
    public void showInfoTrajet(){
        Log.i("Tag"," Start ..... ");
        final FirebaseUser user =mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Trajet").child(user.getUid());
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String date = dataSnapshot.child("date").getValue().toString();
                    String date2 = dataSnapshot.child("date2").getValue().toString();
                    String line = dataSnapshot.child("line").getValue().toString();
                    String line2 = dataSnapshot.child("line2").getValue().toString();
                    String marque = dataSnapshot.child("marque").getValue().toString();
                    String nump = dataSnapshot.child("nump").getValue().toString();
                    String prix = dataSnapshot.child("prix").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();
                    String time2 = dataSnapshot.child("time2").getValue().toString();
                    String iduser = dataSnapshot.child("id_User").getValue().toString();
                 //   String idimg = dataSnapshot.child("imagID").getValue().toString();
                 //   String name = dataSnapshot.child("nameprofil").getValue().toString();
                    Log.e("Tag"," Commit ....... "+date);
                    Log.e("Tag"," Commit ....... "+date2);
                    Log.e("Tag"," Commit ....... "+line);
                    Log.e("Tag"," Commit ....... "+line2);
                    Toast.makeText(ProposertrajetActivity.this, "C"+prix, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ProposertrajetActivity.this, MainAdapterActivity.class);  //when click on item pass to next ativity
                    Bundle bundle = new Bundle();
                    bundle.putString("bundle1", "bundle1"); //get and past informations with bundle from main activity to next activity
                    bundle.putString("date",date );
                    bundle.putString("date2",date2 );
                    bundle.putString("line",line );
                    bundle.putString("line2",line2 );
                    bundle.putString("marque",marque );
                    bundle.putString("nump",nump );
                    bundle.putString("prix",prix );
                    bundle.putString("time",time );
                    bundle.putString("time2",time2 );
                    bundle.putString("id_User",iduser );
                //    bundle.putString("nameprofil",name );
                 //   bundle.putString("imagID",idimg );
                    //bundle.putSerializable("obj", (Serializable) data);
                    i.putExtras(bundle);
                    ProposertrajetActivity.this.startActivity(i);
                }else {
                    Toast.makeText(ProposertrajetActivity.this, "start data  ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                Toast.makeText(ProposertrajetActivity.this, "no data ", Toast.LENGTH_SHORT).show();
            }
        });


    }
     }

