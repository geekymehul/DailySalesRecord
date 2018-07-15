package com.example.gargc.dailysalesrecord.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.R;

public class AddCustomer extends AppCompatActivity {

    EditText name,phoneNumber,customerEmail,amountPaid,notes;

    Button button;
    private ProgressBar progressBar;

    private String customerName,customerEmailId,customerAmountPaid,customerNotes,customerPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        name=(EditText) findViewById(R.id.customerName);
        phoneNumber=(EditText) findViewById(R.id.customerphoneNumber);
        customerEmail =(EditText) findViewById(R.id.customerEmailId);
        amountPaid=(EditText) findViewById(R.id.customerAmountPaid);
        notes=(EditText) findViewById(R.id.customerNotes);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        button=(Button) findViewById(R.id.btSignIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName=name.getText().toString().trim();
                customerEmailId=customerEmail.getText().toString().trim().toLowerCase();
                customerAmountPaid=amountPaid.getText().toString().trim();
                customerNotes=notes.getText().toString().trim();
                customerPhoneNumber=phoneNumber.getText().toString();

                if (customerName.length() < 2) {
                    Toast.makeText(AddCustomer.this, "Name must be at least 2 characters", Toast.LENGTH_LONG).show();
                } else if (!customerName.matches("^[^<>'\"/;`%&!@#$*()+=:?.,~|{}]*$")) {
                    Toast.makeText(AddCustomer.this, "Name cannot contain special characters", Toast.LENGTH_LONG).show();
                    name.requestFocus();
                } else if (!customerEmailId.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                    Toast.makeText(AddCustomer.this, "Please enter a valid email id", Toast.LENGTH_LONG).show();
                    customerEmail.requestFocus();
                } else if (!(customerPhoneNumber.length() == 10 && (customerPhoneNumber.startsWith("9") || customerPhoneNumber.startsWith("8") || customerPhoneNumber.startsWith("7")))) {
                    Toast.makeText(AddCustomer.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    phoneNumber.requestFocus();
                }
                else {
                    toggleViews(false);
                    button.setEnabled(false);
                    addCustomer();
                    return;
                }
                toggleViews(true);
                button.setEnabled(true);


            }
        });


    }

    private void addCustomer() {

    }

    private void toggleViews(boolean enabled) {
        progressBar.setVisibility((enabled) ? View.GONE : View.VISIBLE);
        button.setVisibility((enabled) ? View.VISIBLE : View.INVISIBLE);

        name.setEnabled(enabled);
        customerEmail.setEnabled(enabled);
        phoneNumber.setEnabled(enabled);
        amountPaid.setEnabled(enabled);
        notes.setEnabled(enabled);

    }
}
