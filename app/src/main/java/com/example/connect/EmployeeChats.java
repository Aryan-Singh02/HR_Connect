package com.example.connect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import Adapters.UserAdapter;
import Models.EmployeeDetail;
import Models.HRManager;

public class EmployeeChats extends AppCompatActivity {

    FirebaseAuth mAuth;
    RecyclerView employeeRecyclerView;
    UserAdapter employeeList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<EmployeeDetail> employees;
    String userID;
    String hrId;
    ArrayList<String> hrDetails;
    EmployeeDetail hrDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_chats);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }else {
            Log.e("ChatFragment","user not authenticate");
            return;
        }
        employeeRecyclerView = findViewById(R.id.chats_recycler);
        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        employees = new ArrayList<>();
        hrDetails = new ArrayList<>();
        employeeList = new UserAdapter(employees);

        employeeRecyclerView.setAdapter(employeeList);

        loadHRKey();
    }
    private void loadHRKey() {
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        if (key == null || key.isEmpty()) {
            // If the key is not passed via intent, retrieve it from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
            key = sharedPreferences.getString("HR_Key", "");
        }
        if (key == null || key.isEmpty()) {
            Log.e("EmployeeChats", "HR key is missing in intent");
            return;
        }
        Query query = databaseReference.orderByChild("HR_Key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        hrId = dataSnapshot.getKey(); // Get the HR ID from the snapshot
                        if (hrId != null) {
                            loadUserData(hrId);
                            loadHRDetails(hrId);
                        } else {
                            Log.e("EmployeeChats", "HR ID is null");
                        }
                        break; // Assuming you only need the first matching HR ID
                    }
                } else {
                    Log.e("EmployeeChats", "No HR found for the provided key");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeChats", "Database error: " + error.getMessage());
            }
        });
    }

    private void loadHRDetails(String hrId){
        DatabaseReference hrReference = databaseReference.child(hrId);
        hrReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hrDetail = snapshot.getValue(EmployeeDetail.class);
                    if (hrDetail != null) {

                        String hrName = snapshot.child("name").getValue(String.class);
                        String hrStatus = snapshot.child("status").getValue(String.class);

                        hrDetail.setUserID(hrId);
                        hrDetail.getName();// Set the HR ID
                        employees.add(0, hrDetail); // Add HR to the top of the list
                        runOnUiThread(() -> employeeList.notifyDataSetChanged());
                    }
                } else {
                    Log.e("EmployeeChats", "No HR details found for HR ID: " + hrId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeChats", "Database error: " + error.getMessage());
            }
        });

    }

    private void loadUserData(String hrId) {
        DatabaseReference employeeReference = databaseReference.child(hrId).child("employees");
        employeeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        EmployeeDetail employeeDetail = dataSnapshot.getValue(EmployeeDetail.class);
                        if (employeeDetail != null) {
                            employees.add(employeeDetail);
                        }
                    }
                    runOnUiThread(() -> employeeList.notifyDataSetChanged()); // Notify the adapter on the main thread
                } else {
                    Log.e("EmployeeChats", "No employees found for HR ID: " + hrId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeChats", "Database error: " + error.getMessage());
            }
        });
    }
}