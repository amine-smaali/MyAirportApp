package com.example.airportapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private Button registerBtn;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> registerUser());
    }

    // Dans registerUser() method de RegisterActivity
    private void registerUser() {
        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Empêcher l'inscription avec 'admin'
        if (username.equalsIgnoreCase("admin")) {
            Toast.makeText(this, "Username 'admin' is reserved", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 3) {
            Toast.makeText(this, "Password must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addUser(username, password)) {
            Toast.makeText(this, "Registration successful! Please login", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }
}