package com.example.lab_00.appku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = username.getText().toString().toUpperCase();
                String p = password.getText().toString();

                if (u.equals("") || p.equals("")) {
                    if (u.equals("")) {
                        username.setError("Username harus diisi");
                        username.requestFocus();
                    }

                    if (p.equals("")) {
                        password.setError("Password harus diisi");
                        password.requestFocus();
                    }
                } else {
                    if (u.equals("J3C205003") && p.equals("123")) {
                        SharedPreferences sp = getSharedPreferences("statusLogin", MODE_PRIVATE);

                        SharedPreferences.Editor e = sp.edit();

                        e.putString("username", u);

                        e.commit();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Pengguna tidak terdaftar", Toast.LENGTH_LONG).show();

                        username.setText("");
                        password.setText("");
                        username.requestFocus();
                    }
                }
            }
        });
    }
}
