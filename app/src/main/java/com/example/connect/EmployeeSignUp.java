package com.example.connect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Models.EmployeeDetail;

public class EmployeeSignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText edUsername, edEmail, edPassword, edKey;
    Button signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_sign_up);

        edUsername = findViewById(R.id.edUsername);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edKey = findViewById(R.id.edHRKey);
        signup = findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edUsername.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()) && !TextUtils.isEmpty(edEmail.getText().toString())) {

                    String enteredUsername = edUsername.getText().toString();
                    String email = edEmail.getText().toString();
                    String password = edPassword.getText().toString();
                    String key = edKey.getText().toString();
                    createAccount(key,enteredUsername,email,password);

                } else {
                    Toast.makeText(EmployeeSignUp.this, "Please enter your username and password to continue..!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createAccount(String key, String username, String email, String password) {

        // In your login activity or wherever the HR key is first retrieved
        SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("HR_Key", key); // Replace hrKey with the actual HR key value
        editor.apply();

        Query query = databaseReference.orderByChild("HR_Key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(EmployeeSignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    String userID = user.getUid();

                                    // Get the HR ID from the first child of the snapshot
                                    String hrID = snapshot.getChildren().iterator().next().getKey();

                                    EmployeeDetail employeeDetail = new EmployeeDetail("",username,password,password,email,"hey! I am using this app", "","","","",userID);
                                    // Check if "employees" node exists under the HR node
                                    DatabaseReference hrEmployeesRef = databaseReference.child(hrID).child("employees");
                                    hrEmployeesRef.child(userID).setValue(employeeDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
//                                            SharedPreferences preferences = getSharedPreferences("appPrefs",MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = preferences.edit();
//                                            editor.putBoolean("is_logged_in", true);
//                                            editor.apply();
                                            Toast.makeText(EmployeeSignUp.this, "Data stored with respective HR", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EmployeeSignUp.this, "Data not stored with respective HR", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                    user.updateProfile(profileUpdate);
                                    Intent intent = new Intent(EmployeeSignUp.this, EmployeeLogin.class);
                                    intent.putExtra("userid", userID);
                                    intent.putExtra("key", key);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(EmployeeSignUp.this, "SignUp failed...!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(EmployeeSignUp.this, "Key doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeSignUp.this, "Database error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}