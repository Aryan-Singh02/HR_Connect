package com.example.connect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String userId = "";
    private static final int SPLASH_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setProgress(60);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
                String hrKey = sharedPreferences.getString("HR_Key", "");

                if(currentUser == null){
                    // No user signed in, redirect to Walkthrough_1 activity
                    startActivity(new Intent(SplashActivity.this, Walkthrough_1.class));
                    finish();
                } else {
                    String userId = currentUser.getUid();
                    DatabaseReference userRoleRef = databaseReference.child(userId).child("Role");

                    userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String role = snapshot.getValue(String.class);
                                if ("HR".equals(role)) {
                                    // User is HR, start DashboardHR activity
                                    startActivity(new Intent(SplashActivity.this, DashboardHR.class));
                                    finish();
                                } else {
                                    // User is not HR, start EmployeeDashBoard activity
                                    Intent intent = new Intent(SplashActivity.this, EmployeeDashBoard.class);
                                    startActivity(intent);
                                    intent.putExtra("key", hrKey);
                                    finish();
                                }
                            } else {
                                // Role data doesn't exist, assume user is an employee
                                Log.e("SplashActivity", "Role data doesn't exist");
                                Intent intent = new Intent(SplashActivity.this, EmployeeDashBoard.class);
                                startActivity(intent);
                                intent.putExtra("key", hrKey);
                                finish();
                            }
                            // Finish splash activity
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Error occurred while fetching data from database
                            Log.e("SplashActivity", "Database error: " + error.getMessage());
                        }
                    });
                }
            }
        }, SPLASH_DURATION);
    }
}