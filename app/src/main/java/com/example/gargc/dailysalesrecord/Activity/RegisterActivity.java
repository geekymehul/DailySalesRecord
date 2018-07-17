package com.example.gargc.dailysalesrecord.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private Button btn_reg;
    private EditText edt_name,edt_pass,edt_email;
    private ProgressDialog mProgress;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        edt_email = (EditText)findViewById(R.id.reg_edt_email);
        edt_name = (EditText)findViewById(R.id.reg_edt_name);
        edt_pass = (EditText)findViewById(R.id.reg_edt_password);
        btn_reg = (Button)findViewById(R.id.reg_btn_reg);

        mProgress = new ProgressDialog(RegisterActivity.this);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String name = edt_name.getText().toString();
                final String email = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
                {
                    Toast.makeText(RegisterActivity.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgress.setTitle("Creating account...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                String userId = mAuth.getCurrentUser().getUid();

                                DatabaseReference user = userDatabase.child(userId);
                                user.child("name").setValue(name);
                                user.child("email").setValue(email);

                                mProgress.dismiss();

                                Intent mainIntent = new Intent(RegisterActivity.this , MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        }
                    });
                }
            }
        });

    }

}
