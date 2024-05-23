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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.EmployeeDetail;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText empId, name, pass, confirmPass, email, jobPosition, department, contactDetail;
    Button submit;
    DatabaseReference databaseReference;
    CircleImageView userImage;
    String getStatus = "";
    String profilepic = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        empId = findViewById(R.id.employeeId);
        name = findViewById(R.id.firstName);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPassword);
        email = findViewById(R.id.emailId);
        getStatus = "Hey ! I am using this app..";
        jobPosition = findViewById(R.id.job_position);
        department = findViewById(R.id.department);
        contactDetail = findViewById(R.id.ph_no);
        userImage = findViewById(R.id.users);
        submit = findViewById(R.id.submit);


//        databaseReference = FirebaseDatabase.getInstance().getReference().child("employees");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String userID = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("employees");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeID = empId.getText().toString().trim();
                String firstName = name.getText().toString().trim();
//                String lastName = lName.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String confirmPassword = confirmPass.getText().toString().trim();
                String emailId = email.getText().toString().trim();
                String job = jobPosition.getText().toString().trim();
                String dept = department.getText().toString().trim();
                String phNo = contactDetail.getText().toString().trim();
                String btnSubmit = submit.getText().toString().trim();


                if(TextUtils.isEmpty(employeeID) || TextUtils.isEmpty(firstName)  || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(emailId) || TextUtils.isEmpty(btnSubmit) || TextUtils.isEmpty(dept) || TextUtils.isEmpty(phNo) | TextUtils.isEmpty(job)){
                    Toast.makeText(AddEmployeeActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }

                EmployeeDetail employeeDetail = new EmployeeDetail(employeeID,firstName,password,confirmPassword,emailId,getStatus,profilepic,job,dept,phNo,userID);

                String key = databaseReference.push().getKey();

                databaseReference.child(key).setValue(employeeDetail);

                Toast.makeText(AddEmployeeActivity.this, "Employee added successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddEmployeeActivity.this, DashboardHR.class);
                startActivity(intent);
                finish();
            }
        });

    }
}