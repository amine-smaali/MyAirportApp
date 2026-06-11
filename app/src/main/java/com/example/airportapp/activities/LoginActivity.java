package com.example.airportapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private TextView registerLink;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser la base de données
        db = new DatabaseHelper(this);

        // Vérifier que la base existe et a des données
        try {
            // Test simple pour vérifier la base
            db.getReadableDatabase();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur base de données: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);

        if (usernameEt == null || passwordEt == null || loginBtn == null) {
            Toast.makeText(this, "Erreur: vue non trouvée", Toast.LENGTH_LONG).show();
            return;
        }

        loginBtn.setOnClickListener(v -> loginUser());
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer nom d'utilisateur et mot de passe", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            boolean isValid = db.checkUser(username, password);

            if (isValid) {
                // Vérifier le rôle de l'utilisateur
                String role = db.getUserRole(username);

                if ("admin".equals(role)) {
                    // Rediriger vers le dashboard admin
                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
                    Toast.makeText(this, "Bienvenue Admin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Rediriger vers l'app normale
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
                    Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show();
                }
                finish(); // Fermer l'activité de login
            } else {
                Toast.makeText(this, "Nom d'utilisateur ou mot de passe invalide", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}