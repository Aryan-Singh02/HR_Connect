package com.example.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Select_HR_Employee extends AppCompatActivity {

    Button selectHR, selectEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hr_employe);
        selectHR = findViewById(R.id.select_HR);
        selectEmployee = findViewById(R.id.select_Employee);

        selectHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_HR_Employee.this,LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        selectEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_HR_Employee.this, EmployeeLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}