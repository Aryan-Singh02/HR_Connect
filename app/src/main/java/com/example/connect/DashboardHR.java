package com.example.connect;

import com.example.connect.R;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.EmployeeDetailHorizontalAdapter;
import Adapters.ImageAdapter;
import Fragments.ChatFragment;
import Fragments.HomeFragment;
import Fragments.LeaveRequestFragment;
import Fragments.PayRollFragment;
import Models.EmployeeDetail;

public class DashboardHR extends AppCompatActivity {

    DrawerLayout drawerLayout;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    RecyclerView horizontalRecyclerView, imageRecyclerView;
    EmployeeDetailHorizontalAdapter employeeDetailHorizontalAdapter;
    ImageAdapter imageAdapter;
    List<EmployeeDetail> dataList;
//    DatabaseReference databaseReference;
    List<Integer> imageData;
    FirebaseAuth mAuth;
    Button addEmployee;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    FrameLayout frameLayout;
    HomeFragment homeFragment = new HomeFragment();
    PayRollFragment payRollFragment = new PayRollFragment();
    LeaveRequestFragment leaveRequestFragment = new LeaveRequestFragment();
    ChatFragment chatFragment = new ChatFragment();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_hr);

        drawerLayout = findViewById(R.id.drawer_layout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(DashboardHR.this,drawerLayout,toolbar,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.main_content);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_home) {
                    Log.d("Navigation", "Home Selected");
                    //getSupportFragmentManager().beginTransaction().replace(R.id.main_content,homeFragment).commit();
                    startActivity(new Intent(DashboardHR.this, DashboardHR.class));
                    finish();
//                    return true;
                }
                if(menuItem.getItemId() == R.id.navigation_payroll){
                    Log.d("Navigation", "Payroll Selected");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_content,payRollFragment).commit();
                    return true;
                }
                if(menuItem.getItemId() == R.id.navigation_leave){
                    Log.d("Navigation", "Leave Selected");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_content,leaveRequestFragment).commit();

                    return true;
                }
                if(menuItem.getItemId() == R.id.navigation_chat){
                    Log.d("Navigation", "Chat Selected");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_content,chatFragment).commit();
                    return true;
                }
                return false;
            }
        });

        addEmployee = findViewById(R.id.btnAddEmployee);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String username = user.getDisplayName();

            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView usernameTextView = headerView.findViewById(R.id.username_text_view);
            usernameTextView.setText(username);
        }
        imageRecyclerView = findViewById(R.id.holidays_horizontal_recycler_view);

        imageData = new ArrayList<>();

        imageData.add(R.drawable.holi);
        imageData.add(R.drawable.merry_christmas);
        imageData.add(R.drawable.happy_diwali);

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        imageAdapter = new ImageAdapter(this,imageData);
        imageRecyclerView.setAdapter(imageAdapter);

        horizontalRecyclerView = findViewById(R.id.employee_horizontal_recycler_view);

        dataList = new ArrayList<>();

        employeeDetailHorizontalAdapter = new EmployeeDetailHorizontalAdapter(dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        horizontalRecyclerView.setLayoutManager(layoutManager);
        horizontalRecyclerView.setAdapter(employeeDetailHorizontalAdapter);

        retrieveCurrentUserEmployee();

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardHR.this,AddEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveCurrentUserEmployee(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("employees");

        databaseReference.addValueEventListener(new ValueEventListener() {
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
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DashboardHR.this, LogIn.class);
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
}