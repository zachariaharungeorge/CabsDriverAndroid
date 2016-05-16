package com.rdxcabs.UIActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.rdxcabs.Beans.DriverBean;
import com.rdxcabs.Constants.Constants;
import com.rdxcabs.R;
import com.rdxcabs.Services.LocationService;

import java.util.HashMap;
import java.util.Map;

import static com.firebase.client.Firebase.CompletionListener;
import static com.firebase.client.Firebase.setAndroidContext;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidContext(this);
        setContentView(R.layout.activity_sign_up_activity);

        final Button signUp = (Button) findViewById(R.id.signUp);
        final DriverBean driverBean = new DriverBean();

        final Spinner spinner = (Spinner) findViewById(R.id.cabType);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUpActivity.this, R.array.cabTypes, R.layout.dropdownlayout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                driverBean.setCabType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog=ProgressDialog.show(SignUpActivity.this, "Loading", "Saving Data", false,false);

                TextView fullName = (TextView) findViewById(R.id.fullName);
                driverBean.setFullName(fullName.getText().toString());

                TextView phoneNumber = (TextView) findViewById(R.id.phone);
                driverBean.setPhone(phoneNumber.getText().toString());

                TextView email = (TextView) findViewById(R.id.email);
                driverBean.setEmail(email.getText().toString());

                TextView username = (TextView) findViewById(R.id.username);
                driverBean.setUsername(username.getText().toString());

                TextView password = (TextView) findViewById(R.id.password);
                driverBean.setPassword(password.getText().toString());

                TextView license = (TextView) findViewById(R.id.license);
                driverBean.setLicenseNo(license.getText().toString());

                TextView regNo = (TextView) findViewById(R.id.regNo);
                driverBean.setRegistrationNo(regNo.getText().toString());

                if(fullName.getText() == null || phoneNumber.getText() == null || email.getText() == null || username.getText()==null || password.getText() == null){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
                    alertDialog.setTitle("Sign Up");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Please enter all the Details");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }

                Firebase myFirebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP + Constants.DRIVER + Constants.URL_SEP + username.getText().toString());

                myFirebaseRef.child(username.getText().toString());
                myFirebaseRef.setValue(driverBean, new CompletionListener() {

                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
                        alertDialog.setTitle("Sign Up");
                        alertDialog.setCancelable(false);
                        if (firebaseError == null) {
                            SharedPreferences sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
                            //SharedPreferences.Editor editor = sharedPreferences.edit();
                            //editor.putString("username", user.get("username"));
                            //editor.commit();
                            alertDialog.setMessage("Sign Up Successful");
                            alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startService(new Intent(SignUpActivity.this, LocationService.class));
                                    Intent intent = new Intent(SignUpActivity.this, TripListActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            alertDialog.setTitle("Sign Up");
                            alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                        }
                        progressDialog.dismiss();
                        alertDialog.show();
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
