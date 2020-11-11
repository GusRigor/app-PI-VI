package com.example.piantxjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
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

        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    EscondeTeclado(v);
                }
            }
        });

        etPassowrd.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    EscondeTeclado(v);
                }
            }
        });


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassowrd.getText().toString().equals("admin") && etUsername.getText().toString().equals("piv0069")){
                    Toast.makeText(getApplicationContext(),  "Login Sucessfully !", Toast.LENGTH_SHORT).show();
                    openNavActivity();
                } else {
                    Toast.makeText(getApplicationContext(),  "Invalid Username & Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void EscondeTeclado(View v){
        if(v != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void openNavActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id", etUsername.getText().toString());
        startActivity(intent);
    }
}