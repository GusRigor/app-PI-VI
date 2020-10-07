package com.example.piantxjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etUsername, etPassowrd;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.et_username);
        etPassowrd = findViewById(R.id.et_password);
        btSubmit = findViewById(R.id.bt_submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassowrd.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),  "Login Sucessfully !", Toast.LENGTH_SHORT).show();
                    openNavActivity();
                } else {
                    Toast.makeText(getApplicationContext(),  "Invalid Username & Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void openNavActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id", etUsername.getText().toString());
        startActivity(intent);
    }
}