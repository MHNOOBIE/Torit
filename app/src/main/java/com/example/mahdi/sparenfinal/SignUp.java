package com.example.mahdi.sparenfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Button btnReg;
    private FirebaseAuth firebaseAuth;
    private EditText etName,etEmail,etPass,etLocation,etFamily;
    private Spinner spinner;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private DatabaseReference mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //initializing all the views
        etName = (EditText) findViewById(R.id.etName);
        etPass = (EditText) findViewById(R.id.etPass1);
        etEmail = (EditText) findViewById(R.id.etEmail1);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etFamily = (EditText) findViewById(R.id.etFamily);
        btnReg = (Button) findViewById(R.id.btnReg);
        progressBar = (ProgressBar) findViewById(R.id.pbarReg);
        spinner = (Spinner) findViewById(R.id.spinner);
        //progressDialog = new ProgressDialog(this);

        //attaching listener to button sign up
        btnReg.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        registerUser();
    }

    private void registerUser() {

        //getting all the inputs from the user
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPass.getText().toString().trim();
        final String location = etLocation.getText().toString().trim();
        final String family = etFamily.getText().toString().trim();
        final String type = spinner.getSelectedItem().toString();
        //validation
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(location)){
            Toast.makeText(this, "Please enter location!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(family)){
            Toast.makeText(this, "Please enter No. of family members!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.matches("Select a Type")){
            Toast.makeText(this, "Please select house type!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() <= 5){
            Toast.makeText(this, "Password must be more than 5 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //progressDialog.setMessage("Registering, Please Wait...");
        //progressDialog.show();
        btnReg.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        //now we can create the user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mydb = database.getReference();
                            User user = new User(name,location,family,type);
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            mydb.child("Users").child(uid).setValue(user);
                            Toast.makeText(SignUp.this, "Successfully Registered :) .Login to continue.", Toast.LENGTH_SHORT).show();
                            //progressDialog.hide();
                            btnReg.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(SignUp.this,Home.class);
                            startActivity(intent);
                            finish();

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUp.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(SignUp.this, "Something went wrong! Check your internet access and try again .", Toast.LENGTH_SHORT).show();
                            //progressDialog.hide();
                            btnReg.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });



    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
