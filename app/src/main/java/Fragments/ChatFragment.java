package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.connect.DashboardHR;
import com.example.connect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Adapters.UserAdapter;
import Models.EmployeeDetail;

public class ChatFragment extends Fragment {

    FirebaseAuth mAuth;
    RecyclerView userRecyclerView;
    UserAdapter userAdapter;
    FirebaseDatabase database;
    ArrayList<EmployeeDetail> employeeList;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        } else {
            // Handle the case where the user is not authenticated
            Log.e("ChatFragment", "User not authenticated");
            return view;
        }

        // Initialize RecyclerView and Adapter
        userRecyclerView = view.findViewById(R.id.chats_recycler);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        employeeList = new ArrayList<>();
        userAdapter = new UserAdapter( employeeList);
        userRecyclerView.setAdapter(userAdapter);

        // Load data from Firebase
        loadUserData();
        return view;
    }

    private void loadUserData() {
        Log.d("ChatFragment", "loadUserData() called");
        DatabaseReference reference = database.getReference().child("users").child(userID).child("employees");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeDetail employeeDetail = dataSnapshot.getValue(EmployeeDetail.class);
                    if (employeeDetail != null) {
                        employeeList.add(employeeDetail);
                    }
                }

                if(employeeList.isEmpty()){
                    Log.d("ChatFragment","No employees found for current user");
                }
                else {
                    Log.d("ChatFragment", "Employees found: " + employeeList.size());
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatFragment", "Database error: " + error.getMessage());
            }
        });
    }
}