package com.example.connect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmployeeLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText edRegEmail, edRegPassword;
    Button btnLogin, createAccount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createAccount = findViewById(R.id.createAccount);
        edRegEmail = findViewById(R.id.edRegEmail);
        edRegPassword = findViewById(R.id.edRegPassword);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeLogin.this, EmployeeSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edRegEmail.getText().toString()) && !TextUtils.isEmpty(edRegPassword.getText().toString())) {
                    // login
                    String email = edRegEmail.getText().toString();
                    String password = edRegPassword.getText().toString();
                    loginAccount(email,password);

                } else {
                    Toast.makeText(EmployeeLogin.this, "Please enter your username and password to continue..!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loginAccount(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(EmployeeLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // TODO: rest of the operation
                    Toast.makeText(EmployeeLogin.this, "SignIn success", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null) {
                        Intent intent = new Intent(EmployeeLogin.this, EmployeeDashBoard.class);
                        String key = null;
                        Bundle extras = getIntent().getExtras();
                        String userid = null;
                        if (extras != null) {
                            userid = extras.getString("userid");
                            key = extras.getString("key");
                        }
                        intent.putExtra("userid", userid);
                        intent.putExtra("key", key);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(EmployeeLogin.this, "SignIn failed...!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}