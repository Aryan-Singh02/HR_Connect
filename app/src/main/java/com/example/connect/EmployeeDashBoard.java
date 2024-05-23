package com.example.connect;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.EmployeeDetailHorizontalAdapter;
import Adapters.ImageAdapter;
import Models.EmployeeDetail;

public class EmployeeDashBoard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    Button editEmployee;
    ImageView chatBtn;
    NavigationView navigationView;
    RecyclerView imageRecyclerView, horizontalRecyclerView;
    List<EmployeeDetail> dataList;
    EmployeeDetailHorizontalAdapter employeeDetailHorizontalAdapter;
    ImageAdapter imageAdapter;
    List<Integer> imageData;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String hrId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        chatBtn = findViewById(R.id.img_chat);

        drawerLayout = findViewById(R.id.drawer_layout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.nav_logOut){
                    showLogoutDialog();
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        imageRecyclerView = findViewById(R.id.holidays_horizontal_recycler_view);

        imageData = new ArrayList<>();

        imageData.add(R.drawable.holi);
        imageData.add(R.drawable.merry_christmas);
        imageData.add(R.drawable.happy_diwali);

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        imageAdapter = new ImageAdapter(this,imageData);
        imageRecyclerView.setAdapter(imageAdapter);

//        horizontalRecyclerView = findViewById(R.id.employee_horizontal_recycler_view);
//
//        dataList = new ArrayList<>();
//
//        employeeDetailHorizontalAdapter = new EmployeeDetailHorizontalAdapter(dataList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        horizontalRecyclerView.setLayoutManager(layoutManager);
//        horizontalRecyclerView.setAdapter(employeeDetailHorizontalAdapter);

//        retrieveCurrentUserEmployee();
//        loadHRKey();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(EmployeeDashBoard.this,drawerLayout,toolbar,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String username = user.getDisplayName();

            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView usernameTextView = headerView.findViewById(R.id.username_text_view);
            usernameTextView.setText(username);

        }

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashBoard.this, EmployeeChats.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
    }
    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences preferences = getSharedPreferences("appPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("is_logged_in", false);
                editor.apply();

                Intent intent = new Intent(EmployeeDashBoard.this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*private void retrieveCurrentUserEmployee(String userID){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("employees");
        DatabaseReference employeeReference = databaseReference.child(hrId).child("employees");
        employeeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EmployeeDetail employeeDetail = dataSnapshot.getValue(EmployeeDetail.class);
                    dataList.add(employeeDetail);
                }
                employeeDetailHorizontalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("HrDashboardActivity", "Error reading data form database: " + error.getMessage());
            }
        });
    }*/

    /*private void loadHRKey() {
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        if (key == null || key.isEmpty()) {
            // If the key is not passed via intent, retrieve it from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
            key = sharedPreferences.getString("HR_Key", "");
        }
//        if (key == null || key.isEmpty()) {
//            Log.e("EmployeeChats", "HR key is missing in intent");
//            return;
//        }
        Query query = databaseReference.orderByChild("HR_Key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        hrId = dataSnapshot.getKey(); // Get the HR ID from the snapshot
                        if (hrId != null) {
//                            loadUserData(hrId);
//                            loadHRDetails(hrId);
                            retrieveCurrentUserEmployee(hrId);
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
    }*/
}