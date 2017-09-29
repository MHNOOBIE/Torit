package com.example.mahdi.sparenfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private TextView tvWelcome;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mydb;
    private User user;
    private Intent intent;
    private ProgressBar pbar;
    private RelativeLayout rl;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rl = (RelativeLayout) findViewById(R.id.rl);
        ll = (LinearLayout) findViewById(R.id.ll);
        rl.setVisibility(View.VISIBLE);
        ll.setVisibility(View.INVISIBLE);

        pbar = (ProgressBar) findViewById(R.id.pbarProfile);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        String uid = mAuth.getCurrentUser().getUid();
        mydb = database.getReference().child("Users").child(uid);

        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = dataSnapshot.getValue(User.class);
                Show(user);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Profile.this, "Error Occured ! Check your internet Connection . Returning to Home .", Toast.LENGTH_SHORT).show();
                intent = new Intent(Profile.this,Home.class);
                startActivity(intent);
                finish();
            }
        });


        Button button = (Button) findViewById(R.id.btnLog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Profile.this,Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Show(User user) {
        tvWelcome.setText("Welcome " + user.name);
        pbar.setVisibility(View.INVISIBLE);
        ll.setVisibility(View.VISIBLE);
    }
}
