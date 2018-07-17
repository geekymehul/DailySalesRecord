package com.example.gargc.dailysalesrecord.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{

    Button btn_login,btn_reg;
    EditText edt_email,edt_password;
    FirebaseAuth mAuth;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edt_email = (EditText)findViewById(R.id.login_edt_email);
        edt_password = (EditText)findViewById(R.id.login_edt_password);
        btn_reg = (Button)findViewById(R.id.login_btn_reg);
        btn_login = (Button)findViewById(R.id.login_btn_login);
        mProgress = new ProgressDialog(LoginActivity.this);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(LoginActivity.this , RegisterActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String username = edt_email.getText().toString();
                String password = edt_password.getText().toString();

                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(username))
                {
                    Toast.makeText(LoginActivity.this, "enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgress.setTitle("logging you in...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Could not sign in...", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                            else
                            {
                                mProgress.dismiss();

                                Intent loginIntent = new Intent(LoginActivity.this , MainActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);

                            }
                        }
                    });
                }
            }
        });
    }

}
