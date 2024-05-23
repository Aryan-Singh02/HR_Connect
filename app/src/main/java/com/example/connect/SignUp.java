package com.example.connect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import Models.HRManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Uri imageURI;
    String imageUri;
    EditText edUsername, edEmail, edPassword, key;
    Button signup;
    FirebaseStorage firebaseStorage;
    CircleImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        profile = findViewById(R.id.profilehr);
        edUsername = findViewById(R.id.edUsername);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        signup = findViewById(R.id.btnSignUp);
        key = findViewById(R.id.edHRKey);
        firebaseStorage = FirebaseStorage.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edUsername.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()) && !TextUtils.isEmpty(edEmail.getText().toString()) && !TextUtils.isEmpty(key.getText().toString())) {
                    // login
                    String enteredUsername = edUsername.getText().toString();
                    String email = edEmail.getText().toString();
                    String password = edPassword.getText().toString();
                    String hrKey = key.getText().toString();
                    String status = "Hey I am using this app";
                    createAccount(status,hrKey,enteredUsername,email,password);

                } else {
                    Toast.makeText(SignUp.this, "Please enter your username and password to continue..!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
                imageURI = data.getData();
                profile.setImageURI(imageURI);
            }
        }
    }

    private void createAccount(String status, String Key, String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this, "Register Success", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = mAuth.getCurrentUser();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

                    if(user != null){
                        String userID = user.getUid();
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        user.updateProfile(profileUpdate);

                        String userEmail = user.getEmail();
//                        String hrKey = generateHRKey();
                        String role = "HR";
                        StorageReference storageReference = firebaseStorage.getReference().child("upload").child(userID);
                        if(imageURI != null){
                            storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                imageUri = uri.toString();
                                                userRef.child(userID).child("Username").setValue(username);
                                                userRef.child(userID).child("Email").setValue(userEmail);
                                                userRef.child(userID).child("Password").setValue(password);
                                                userRef.child(userID).child("Role").setValue(role);
                                                userRef.child(userID).child("status").setValue(status);
                                                userRef.child(userID).child("HR_Key").setValue(Key).addOnCompleteListener(new OnCompleteListener<Void>() {

//                                                    HRManager hrManager = new HRManager(username,status);
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Intent intent = new Intent(SignUp.this, LogIn.class);
                                                            intent.putExtra("userid",userID);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else {
                                                            Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else {
                            imageUri = "https://firebasestorage.googleapis.com/v0/b/hr-connect-e2e1a.appspot.com/o/c.png?alt=media&token=d5d71680-9353-4ea7-8889-c678d1a415f5";
                            userRef.child(userID).child("Username").setValue(username);
                            userRef.child(userID).child("Email").setValue(userEmail);
                            userRef.child(userID).child("Password").setValue(password);
                            userRef.child(userID).child("Role").setValue(role);
                            userRef.child(userID).child("status").setValue(status);
                            userRef.child(userID).child("HR_Key").setValue(Key).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignUp.this, LogIn.class);
                                        intent.putExtra("userid",userID);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    else {
                        Log.e("TAG","user is null after successful signup");
                    }
                }
                else {
                    String errorMessage = "Signup failed " + task.getException().getMessage();
                    Log.e("TAG",errorMessage);
                    Toast.makeText(SignUp.this, "SignUp failed...!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String generateHRKey() {
        return UUID.randomUUID().toString();
    }
}